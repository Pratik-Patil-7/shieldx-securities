// profile.component.ts
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AuthService, UserProfile } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: UserProfile | null = null;
  isEditing = false;
  isChangingPassword = false;
  editProfileData: Partial<UserProfile> = {
    firstName: '',
    lastName: '',
    email: '',
    mobile: '',
    address: ''
  };
  passwordData = {
    currentPassword: '',
    newPassword: ''
  };
  errorMessage = '';
  isLoading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }

    this.loadUserProfile();
  }

  loadUserProfile(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.authService.getUserProfile().subscribe({
      next: (profile) => {
        this.currentUser = profile;
        this.editProfileData = { ...profile };
        this.isLoading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading = false;
        this.handleProfileError(error);
      }
    });
  }

  private handleProfileError(error: HttpErrorResponse): void {
    console.error('Profile error:', error);

    if (error.status === 403) {
      this.errorMessage = 'Session expired. Please login again.';
      this.authService.logout();
    } else {
      this.errorMessage = 'Failed to load profile. Please try again later.';
    }
  }

  editProfile(): void {
    this.isEditing = true;
    this.errorMessage = '';
  }

  cancelEdit(): void {
    this.isEditing = false;
    this.editProfileData = { ...this.currentUser! };
    this.errorMessage = '';
  }

  onUpdateProfile(form: NgForm): void {
    if (form.invalid) {
      this.errorMessage = 'Please fill all required fields correctly.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.updateProfile(this.editProfileData).subscribe({
      next: (updatedProfile) => {
        this.currentUser = updatedProfile;
        this.isEditing = false;
        this.errorMessage = 'Profile updated successfully!';
        this.isLoading = false;
        this.clearSuccessMessage();
        this.loadUserProfile();
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading = false;
        this.errorMessage = error.error?.message || 'Failed to update profile. Please try again.';
      }
    });
  }

  changePassword(): void {
    this.isChangingPassword = true;
    this.errorMessage = '';
    this.passwordData = { currentPassword: '', newPassword: '' };
  }

  cancelPasswordChange(): void {
    this.isChangingPassword = false;
    this.errorMessage = '';
  }

 // profile.component.ts
onChangePassword(form: NgForm): void {
  if (form.invalid) {
    this.errorMessage = 'Please provide valid current and new passwords (min 8 characters).';
    return;
  }

  this.isLoading = true;
  this.errorMessage = '';

  // this.authService.changePassword(
  //   this.passwordData.currentPassword,
  //   this.passwordData.newPassword
  // ).subscribe({
  //   next: () => {
  //     this.isChangingPassword = false;
  //     this.errorMessage = 'Password updated successfully!';
  //     this.isLoading = false;
  //     this.passwordData = { currentPassword: '', newPassword: '' };
  //     setTimeout(() => this.errorMessage = '', 3000);
  //   },
  //   error: (error: HttpErrorResponse) => {
  //     this.isLoading = false;
  //     if (error.status === 403) {
  //       this.errorMessage = 'Session expired or invalid current password. Please login again.';
  //       this.authService.logout();
  //     } else {
  //       this.errorMessage = error.error?.message || 'Failed to update password. Please try again.';
  //     }
  //   }
  // });
}

  private clearSuccessMessage(): void {
    setTimeout(() => {
      this.errorMessage = '';
    }, 3000);
  }
}
