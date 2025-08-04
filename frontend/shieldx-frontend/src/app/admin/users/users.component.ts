import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { AdminService } from '../../core/services/admin.service';
import { UserResponse } from '../../dto/user-response';
import { HttpErrorResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  users: UserResponse[] = [];
  isLoading: boolean = true;
  currentPage: number = 1;
  itemsPerPage: number = 10;
  searchTerm: string = '';
  processingUserId: number | null = null;

  constructor(
    private adminService: AdminService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

loadUsers(): void {
  this.isLoading = true;
  this.adminService.getAllUsers().subscribe({
    next: (users) => {
      this.users = users.map(user => ({
        userId: user.userId,
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        mobile: user.mobile,
        address: user.address,
        status: (user.status?.toLowerCase() === 'active' ? 'active' : 'deactivate') as 'active' | 'deactivate'
      }));
      this.isLoading = false;
      console.log('Users loaded:', this.users); // Debug log
      this.cdr.markForCheck();
    },
    error: (err: HttpErrorResponse) => {
      console.error('Failed to load users:', err);
      this.toastr.error(`Failed to load users: ${err.status === 403 ? 'Access denied (403 Forbidden)' : err.error?.message || 'Unknown error'}`, 'Error');
      this.isLoading = false;
      this.cdr.markForCheck();
    }
  });
}

  toggleUserStatus(user: UserResponse): void {
    if (this.processingUserId) return;

    const newStatus = user.status === 'active' ? 'deactivate' : 'active';
    const confirmMessage = `Are you sure you want to ${newStatus} this user?`;

    if (confirm(confirmMessage)) {
      this.processingUserId = user.userId;
      this.adminService.toggleUserStatus(user.userId).subscribe({
        next: () => {
          // Fetch the updated user details from the database
          this.adminService.getUserDetails(user.userId).subscribe({
            next: (updatedUser) => {
              const updatedUsers = [...this.users];
              const userIndex = updatedUsers.findIndex(u => u.userId === user.userId);
              if (userIndex !== -1) {
                updatedUsers[userIndex] = {
                  ...updatedUsers[userIndex],
                  ...updatedUser,
                  status: updatedUser.status // Use raw status from response
                };
                this.users = updatedUsers; // Reassign the array with updated data
              }
              console.log('Updated user from database:', updatedUsers[userIndex]); // Debug log
              this.toastr.success(`User ${updatedUser.status === 'active' ? 'activated' : 'deactivated'} successfully`);
              this.processingUserId = null;
              this.cdr.markForCheck(); // Mark for check
            },
            error: (err: HttpErrorResponse) => {
              console.error('Failed to fetch updated user:', err);
              this.toastr.error(err.error?.message || `Failed to fetch updated user details`, 'Error');
              this.processingUserId = null;
              this.cdr.markForCheck(); // Mark for check
            }
          });
        },
        error: (err: HttpErrorResponse) => {
          console.error('Failed to toggle user status:', err);
          this.toastr.error(err.error?.message || `Failed to ${newStatus} user`, 'Error');
          this.processingUserId = null;
          this.cdr.markForCheck(); // Mark for check
        }
      });
    }
  }

  get filteredUsers(): UserResponse[] {
    if (!this.searchTerm) return this.users;

    return this.users.filter(user =>
      (user.firstName?.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
       user.lastName?.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
       user.email?.toLowerCase().includes(this.searchTerm.toLowerCase()))
    );
  }

  get paginatedUsers(): UserResponse[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredUsers.slice(startIndex, startIndex + this.itemsPerPage);
  }

  onSearch(): void {
    this.currentPage = 1;
    this.cdr.markForCheck(); // Mark for check
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.cdr.markForCheck(); // Mark for check
  }

  trackByUser(index: number, user: UserResponse): number {
    return user.userId; // Unique identifier for tracking
  }
}
