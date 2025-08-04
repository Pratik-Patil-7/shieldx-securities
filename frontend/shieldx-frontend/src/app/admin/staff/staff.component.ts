import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AdminService } from '../../core/services/admin.service';
import { JobApplicationResponse } from '../../dto/job-application-response';
import { BouncerRequest } from '../../dto/bouncer-request';
import { ToastrService } from 'ngx-toastr';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-staff',
  templateUrl: './staff.component.html',
  styleUrls: ['./staff.component.css']
})
export class StaffComponent implements OnInit {
  applications: JobApplicationResponse[] = [];
  isLoading = false;
  currentTab = 'applications';
  selectedApplication: JobApplicationResponse | null = null;
  isProcessing = false;
  bouncerForm: FormGroup;

  constructor(
    private adminService: AdminService,
    private modalService: NgbModal,
    private toastr: ToastrService,
    private fb: FormBuilder
  ) {
    this.bouncerForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      gender: ['', Validators.required],
      age: ['', [Validators.required, Validators.min(18), Validators.max(65)]],
      email: ['', [Validators.required, Validators.email]],
      mobile: ['', [Validators.required, Validators.pattern(/^[0-9]{10,15}$/)]],
      address: ['', Validators.required],
      qualification: ['', Validators.required],
      experience: ['', Validators.required],
      isArmed: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadApplications();
  }

  loadApplications(): void {
    this.isLoading = true;
    this.adminService.getAllApplications().subscribe({
      next: (applications) => {
        this.applications = applications;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  async approveApplication(applicationId: number): Promise<void> {
    if (this.isProcessing) return;
    const confirmed = await this.showConfirmation('Approve Application', 'Are you sure you want to approve this application?');
    if (!confirmed) return;

    this.isProcessing = true;
    this.adminService.approveApplication(applicationId).subscribe({
      next: () => {
        this.toastr.success('Application approved successfully');
        this.loadApplications();
        this.modalService.dismissAll();
        this.isProcessing = false;
      },
      error: () => {
        this.isProcessing = false;
      }
    });
  }

  async rejectApplication(applicationId: number): Promise<void> {
    if (this.isProcessing) return;
    const confirmed = await this.showConfirmation('Reject Application', 'Are you sure you want to reject this application?');
    if (!confirmed) return;

    this.isProcessing = true;
    this.adminService.rejectApplication(applicationId).subscribe({
      next: () => {
        this.toastr.success('Application rejected successfully');
        this.loadApplications();
        this.modalService.dismissAll();
        this.isProcessing = false;
      },
      error: () => {
        this.isProcessing = false;
      }
    });
  }

addBouncer(): void {
    if (this.bouncerForm.invalid || this.isProcessing) return;

    this.isProcessing = true;
    const request: BouncerRequest = this.bouncerForm.value;
    console.log('Sending request:', request); // Debug log
    this.adminService.addBouncer(request).subscribe({
      next: () => {
        this.toastr.success('Bouncer added successfully');
        this.bouncerForm.reset({ isArmed: 'no' });
        this.isProcessing = false;
      },
      error: (err) => {
        console.error('addBouncer error:', err); // Debug log
        this.isProcessing = false;
      }
    });
  }

  openApplicationDetails(content: any, application: JobApplicationResponse): void {
    this.selectedApplication = application;
    this.modalService.open(content, {
      centered: true,
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });
  }

  private async showConfirmation(title: string, message: string): Promise<boolean> {
    return confirm(message);
  }

  getStatusBadgeClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'APPROVED': return 'bg-success bg-opacity-10 text-success';
      case 'PENDING': return 'bg-warning bg-opacity-10 text-warning';
      case 'REJECTED': return 'bg-danger bg-opacity-10 text-danger';
      default: return 'bg-secondary bg-opacity-10 text-secondary';
    }
  }

  formatDate(dateString: string): string {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }
}
