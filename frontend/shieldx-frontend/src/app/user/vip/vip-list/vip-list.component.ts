import { Component, OnInit, ViewChild } from '@angular/core';
import { VipPerson, VipService } from './../../../core/services/vip.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms'; // Import Bootstrap's JS for modal control
import { Modal } from 'bootstrap';

@Component({
  standalone: true,
  selector: 'app-vip-list',
  templateUrl: './vip-list.component.html',
  styleUrls: ['./vip-list.component.css'],
  imports: [CommonModule, FormsModule, RouterModule]
})
export class VipListComponent implements OnInit {
  vipPersons: VipPerson[] = [];
  selectedVip: VipPerson | null = null;
  isLoading = false;
  errorMessage = '';
  @ViewChild('editForm') editForm!: NgForm;

  private modalInstance: Modal | null = null;

  constructor(
    private vipService: VipService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadVipPersons();
  }

  loadVipPersons(): void {
    this.isLoading = true;
    this.vipService.getAllVipPersons().subscribe({
      next: (data) => {
        this.vipPersons = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load VIP persons';
        this.isLoading = false;
      }
    });
  }

  openEditModal(vip: VipPerson): void {
    // Deep copy to avoid mutating the original object
    this.selectedVip = { ...vip };
    const modalElement = document.getElementById('editVipModal');
    if (modalElement) {
      this.modalInstance = new Modal(modalElement, {
        backdrop: 'static',
        keyboard: false
      });
      this.modalInstance.show();
    }
  }

  closeEditModal(): void {
    if (this.modalInstance) {
      this.modalInstance.hide();
      this.selectedVip = null;
    }
  }

  resetForm(): void {
    if (this.editForm) {
      this.editForm.resetForm();
      // Restore the original selectedVip data
      if (this.selectedVip) {
        const originalVip = this.vipPersons.find(v => v.vipId === this.selectedVip!.vipId);
        this.selectedVip = originalVip ? { ...originalVip } : null;
      }
    }
  }

  updateVipPerson(): void {
    if (this.selectedVip && this.editForm.valid) {
      this.isLoading = true;
      this.vipService.updateVipPerson(this.selectedVip.vipId, this.selectedVip).subscribe({
        next: (updatedVip) => {
          this.isLoading = false;
          this.errorMessage = '';
          // Update the local list
          const index = this.vipPersons.findIndex(v => v.vipId === updatedVip.vipId);
          if (index !== -1) {
            this.vipPersons[index] = updatedVip;
          }
          this.closeEditModal();
          this.loadVipPersons();
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = 'Failed to update VIP person';
        }
      });
    }
  }
}
