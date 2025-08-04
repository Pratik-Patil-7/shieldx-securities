import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { JobService, JobApplication, JobApplicationSubmission } from 'src/app/core/services/job.service';

@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrls: ['./jobs.component.css']
})
export class JobsComponent implements OnInit {
  isLoading: boolean = true;
  isSubmitting: boolean = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  showForm: boolean = false;
  maxDate: string;
  applications: JobApplication[] = [];
  selectedApplication: JobApplication | null = null;

  @ViewChild('jobForm') jobForm!: NgForm;

  application: JobApplicationSubmission = {
    name: '',
    email: '',
    mobile: '',
    dob: '',
    gender: '',
    address: '',
    qualification: '',
    experience: ''
  };

  constructor(
    private authService: AuthService,
    private jobService: JobService,
    public router: Router
  ) {
    // Set max date to today (11:01 PM IST, July 11, 2025)
    const today = new Date();
    this.maxDate = today.toISOString().split('T')[0];
  }

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    this.loadPendingApplications();
  }

  loadPendingApplications(): void {
    this.isLoading = true;
    this.jobService.getPendingApplications().subscribe({
      next: (applications) => {
        this.applications = applications;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Failed to load pending applications:', err);
        this.errorMessage = 'Failed to load applications. Please try again.';
        this.isLoading = false;
      }
    });
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }

  viewApplication(id: number): void {
    this.isLoading = true;
    this.jobService.getApplicationById(id).subscribe({
      next: (application) => {
        this.selectedApplication = application;
        this.isLoading = false;
        this.openModal();
      },
      error: (err) => {
        console.error('Failed to load application details:', err);
        this.errorMessage = 'Failed to load application details. Please try again.';
        this.isLoading = false;
      }
    });
  }

  openModal(): void {
    const modalElement = document.getElementById('viewApplicationModal');
    if (modalElement) {
      const modal = new (window as any).bootstrap.Modal(modalElement);
      modal.show();
    } else {
      console.error('Modal element not found');
    }
  }

  closeModal(): void {
    this.selectedApplication = null;
    const modalElement = document.getElementById('viewApplicationModal');
    if (modalElement) {
      const modal = (window as any).bootstrap.Modal.getInstance(modalElement);
      if (modal) {
        modal.hide();
      }
    }
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  logout(): void {
    this.authService.logout();
  }

  toggleForm(): void {
    this.showForm = !this.showForm;
    if (this.showForm) {
      this.successMessage = null;
      this.errorMessage = null;
      this.resetForm();
    }
  }

  onSubmit(form: NgForm): void {
    if (form.invalid) {
      return;
    }

    this.isSubmitting = true;
    this.isLoading = true;
    this.successMessage = null;
    this.errorMessage = null;

    const submissionData: JobApplicationSubmission = { ...this.application };

    this.jobService.applyForJob(submissionData).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        this.isLoading = false;
        this.successMessage = 'Application submitted successfully!';
        this.loadPendingApplications();
        this.toggleForm();
      },
      error: (err) => {
        this.isSubmitting = false;
        this.isLoading = false;
        this.errorMessage = 'Failed to submit application. Please try again.';
        console.error('Application error:', err);
      }
    });
  }

  resetForm(form?: NgForm): void {
    if (form) {
      form.resetForm();
    }
    this.application = {
      name: '',
      email: '',
      mobile: '',
      dob: '',
      gender: '',
      address: '',
      qualification: '',
      experience: ''
    };
    this.successMessage = null;
    this.errorMessage = null;
  }

  formatDate(date: string | null): string {
    if (!date) return 'N/A';
    const [year, month, day] = date.split('-');
    return `${month}/${day}/${year}`;
  }

  getStatusBadgeClass(status: string): string {
    switch (status.toUpperCase()) {
      case 'PENDING': return 'bg-warning bg-opacity-10 text-warning';
      case 'APPROVED': return 'bg-success bg-opacity-10 text-success';
      case 'REJECTED': return 'bg-danger bg-opacity-10 text-danger';
      default: return 'bg-secondary bg-opacity-10 text-secondary';
    }
  }

  getStatusIcon(status: string): string {
    switch (status.toUpperCase()) {
      case 'PENDING': return 'bi-clock';
      case 'APPROVED': return 'bi-check-circle';
      case 'REJECTED': return 'bi-x-circle';
      default: return 'bi-question-circle';
    }
  }
}
