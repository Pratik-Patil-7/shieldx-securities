import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { AdminService } from '../../core/services/admin.service';
import { SecurityTypeRequest } from '../../dto/security-type-request';
import { SecurityTypeResponse } from '../../dto/security-type-response';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-security-type',
  templateUrl: './security-type.component.html',
  styleUrls: ['./security-type.component.css']
})
export class SecurityTypeComponent implements OnInit {
  securityTypes: SecurityTypeResponse[] = [];
  isLoading: boolean = true;
  newSecurityType: SecurityTypeRequest = {
    levelName: '',
    description: '',
    isArmed: 'No',
    pricePerDay: 0
  };
  editMode: boolean = false;
  currentEditId: number | null = null;
  isProcessing: boolean = false;

  @ViewChild('editModal') editModal!: TemplateRef<any>;

  constructor(
    private adminService: AdminService,
    private toastr: ToastrService,
    private modalService: NgbModal,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadSecurityTypes();
  }

  loadSecurityTypes(): void {
    this.isLoading = true;
    this.adminService.getAllSecurityTypes().subscribe({
      next: (types) => {
        this.securityTypes = types;
        this.isLoading = false;
      },
      error: (err: HttpErrorResponse) => {
        this.handleError('Failed to load security types', err);
        this.isLoading = false;
      }
    });
  }

  openAddForm(): void {
    this.editMode = false;
    this.resetForm();
    this.modalService.open(this.editModal, { centered: true, size: 'lg' });
  }

  editSecurityType(type: SecurityTypeResponse): void {
    this.editMode = true;
    this.currentEditId = type.stId;
    this.newSecurityType = {
      levelName: type.levelName,
      description: type.description,
      isArmed: type.isArmed,
      pricePerDay: type.pricePerDay
    };
    this.modalService.open(this.editModal, { centered: true, size: 'lg' });
  }

  addSecurityType(): void {
    if (this.isProcessing) return;
    this.isProcessing = true;

    this.adminService.addSecurityType(this.newSecurityType).subscribe({
      next: () => {
        this.toastr.success('Security type added successfully');
        this.loadSecurityTypes();
        this.modalService.dismissAll();
        this.isProcessing = false;
      },
      error: (err: HttpErrorResponse) => {
        this.handleError('Failed to add security type', err);
        this.isProcessing = false;
      }
    });
  }

  updateSecurityType(): void {
    if (this.isProcessing || !this.currentEditId) return;
    this.isProcessing = true;

    this.adminService.updateSecurityType(this.currentEditId, this.newSecurityType).subscribe({
      next: () => {
        this.toastr.success('Security type updated successfully');
        this.loadSecurityTypes();
        this.modalService.dismissAll();
        this.isProcessing = false;
      },
      error: (err: HttpErrorResponse) => {
        this.handleError('Failed to update security type', err);
        this.isProcessing = false;
      }
    });
  }

  confirmDelete(stId: number, levelName: string): void {
    if (confirm(`Are you sure you want to delete "${levelName}" security type?`)) {
      this.deleteSecurityType(stId);
    }
  }

  // In SecurityTypeComponent
deleteSecurityType(stId: number): void {
  if (confirm('Are you sure you want to delete this security type?')) {
    this.isProcessing = true;
    console.log('Deleting security type with ID:', stId); // Debug
    this.adminService.deleteSecurityType(stId).subscribe({
      next: () => {
        this.toastr.success('Security type deleted successfully');
        this.loadSecurityTypes();
        this.isProcessing = false;
      },
      error: (err: HttpErrorResponse) => {
        console.error('Delete error:', err); // Debug
        this.isProcessing = false;
      },
      complete: () => {
        this.isProcessing = false;
      }
    });
  }
}

  resetForm(): void {
    this.newSecurityType = {
      levelName: '',
      description: '',
      isArmed: 'No',
      pricePerDay: 0
    };
    this.editMode = false;
    this.currentEditId = null;
  }

  onSubmit(): void {
    if (this.editMode) {
      this.updateSecurityType();
    } else {
      this.addSecurityType();
    }
  }

  private handleError(message: string, error: HttpErrorResponse): void {
    console.error(message, error);
    let errorMessage = error.error?.message || error.message || 'Unknown error occurred';
    this.toastr.error(errorMessage, 'Error');
  }
}
