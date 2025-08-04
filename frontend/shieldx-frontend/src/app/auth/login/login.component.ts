// import { Component, OnDestroy, OnInit } from '@angular/core';
// import { NgForm } from '@angular/forms';
// import { Router } from '@angular/router';
// import { AuthService } from 'src/app/core/services/auth.service';
// import { interval, Subscription } from 'rxjs';

// @Component({
//   selector: 'app-login',
//   templateUrl: './login.component.html',
//   styleUrls: ['./login.component.css']
// })
// export class LoginComponent implements OnInit, OnDestroy {
//   // Form fields
//   email = '';
//   password = '';
//   otpCode = '';
//   resetEmail = '';
//   newPassword = '';
//   confirmPassword = '';

//   // UI state
//   errorMessage = '';
//   isLoading = false;
//   showOtpForm = false;
//   showResetForm = false;
//   showNewPasswordForm = false;

//   // OTP management
//   otpTimer = 600; // 10 minutes in seconds
//   isResendingOtp = false;
//   resendDisabled = false;
//   resendCountdown = 120; // 2 minutes in seconds

//   private timerSub?: Subscription;
//   private resendSub?: Subscription;

//   constructor(private auth: AuthService, private router: Router) { }

//   ngOnInit(): void {
//     // Restore state on page refresh
//     const otpData = window.sessionStorage.getItem('otpVerificationData');
//     if (otpData) {
//       const data = JSON.parse(otpData);
//       this.email = data.email;
//       this.showOtpForm = true;
//       this.otpTimer = data.otpTimer || 600;
//       this.resendCountdown = data.resendCountdown || 0;
//       this.resendDisabled = this.resendCountdown > 0;
//       // Restore temp auth data in AuthService
//       this.auth.restoreTempAuthData(data.email, data.tempToken);
//       console.log('Restored temp auth data:', this.auth.getTempAuthData()); // Debug
//       if (!this.auth.getTempAuthData()) {
//         console.warn('No valid temp auth data restored, resetting to login');
//         this.backToLogin();
//       } else {
//         this.startOtpTimer();
//         if (this.resendDisabled) {
//           this.startResendCountdown();
//         }
//       }
//     }
//   }

//   onLogin(form: NgForm): void {
//     if (form.valid) {
//       this.isLoading = true;
//       this.errorMessage = '';

//       this.auth.login(this.email, this.password).subscribe({
//         next: () => {
//           console.log('Login successful, temp auth data:', this.auth.getTempAuthData()); // Debug
//           this.sendOtpAfterLogin();
//         },
//         error: (err) => {
//           console.error('Login error:', err);
//           this.errorMessage = err.error?.message || 'Invalid credentials';
//           this.isLoading = false;
//         }
//       });
//     }
//   }

//   private sendOtpAfterLogin(): void {
//     this.auth.sendOtp(this.email).subscribe({
//       next: () => {
//         this.handleOtpSuccess();
//       },
//       error: (err) => {
//         if (err.status === 200 && err.error instanceof ProgressEvent) {
//           this.handleOtpSuccess();
//         } else {
//           console.error('OTP send error:', err);
//           this.errorMessage = 'Failed to send OTP';
//           this.isLoading = false;
//         }
//       }
//     });
//   }

//   private handleOtpSuccess(): void {
//     // Save state to sessionStorage
//     const tempAuthData = this.auth.getTempAuthData();
//     console.log('Saving OTP state, temp auth data:', tempAuthData); // Debug
//     if (!tempAuthData) {
//       console.warn('No temp auth data after OTP send, resetting to login');
//       this.backToLogin();
//       return;
//     }
//     window.sessionStorage.setItem('otpVerificationData', JSON.stringify({
//       email: this.email,
//       otpTimer: this.otpTimer,
//       resendCountdown: this.resendCountdown,
//       tempToken: tempAuthData.token
//     }));

//     this.showOtpForm = true;
//     this.startOtpTimer();
//     this.isLoading = false;
//     this.errorMessage = '';
//   }

//   onVerifyOtp(form: NgForm): void {
//     if (form.valid && this.otpTimer > 0) {
//       this.isLoading = true;
//       this.errorMessage = '';

//       const trimmedOtp = this.otpCode.trim();
//       console.log('Verifying OTP:', trimmedOtp, 'with temp auth data:', this.auth.getTempAuthData()); // Debug

//       const tempAuthData = this.auth.getTempAuthData();
//       if (!tempAuthData) {
//         console.warn('No temp auth data for OTP verification, resetting to login');
//         this.errorMessage = 'Authentication session expired. Please try logging in again.';
//         this.isLoading = false;
//         this.backToLogin();
//         return;
//       }

//       this.auth.verifyOtp(trimmedOtp).subscribe({
//         next: () => {
//           console.log('OTP verification successful');
//           window.sessionStorage.removeItem('otpVerificationData');
//           this.isLoading = false;
//           this.router.navigate(['/']);
//         },
//         error: (err) => {
//           console.error('OTP verification error:', err);
//           this.errorMessage = err.error?.message || 'Invalid OTP or authentication session expired. Please try logging in again.';
//           this.isLoading = false;
//           this.otpCode = ''; // Clear OTP input for next attempt
//           if (err.error?.message === 'No pending authentication' || err.error?.message === 'Invalid temporary token') {
//             this.backToLogin(); // Reset to login form if session is invalid
//           }
//         },
//         complete: () => {
//           this.isLoading = false;
//         }
//       });
//     } else if (this.otpTimer === 0) {
//       this.errorMessage = 'OTP has expired. Please request a new one.';
//     } else {
//       this.errorMessage = 'Please enter a valid OTP.';
//     }
//   }

//   onForgotPassword(): void {
//     this.showResetForm = true;
//     this.errorMessage = '';
//   }

//   onSendResetOtp(form: NgForm): void {
//     if (form.valid) {
//       this.isLoading = true;

//       this.auth.forgotPassword(this.resetEmail).subscribe({
//         next: () => {
//           console.log('Password reset OTP sent');
//           this.showResetForm = false;
//           this.showNewPasswordForm = true;
//           this.startOtpTimer();
//           this.isLoading = false;
//         },
//         error: (err) => {
//           console.error('Password reset OTP error:', err);
//           this.errorMessage = err.error?.message || 'Failed to send OTP';
//           this.isLoading = false;
//         }
//       });
//     }
//   }

//   onResendOtp(): void {
//     if (this.resendDisabled) return;

//     this.isResendingOtp = true;
//     this.errorMessage = '';

//     // Re-authenticate by calling login again to refresh tempAuthData
//     this.auth.login(this.email, this.password).subscribe({
//       next: () => {
//         console.log('Re-authentication successful for OTP resend, temp auth data:', this.auth.getTempAuthData()); // Debug
//         this.auth.sendOtp(this.email).subscribe({
//           next: () => {
//             console.log('OTP resent successfully');
//             this.isResendingOtp = false;
//             this.resendDisabled = true;
//             this.resendCountdown = 120;
//             this.otpTimer = 600;

//             // Update sessionStorage
//             const tempAuthData = this.auth.getTempAuthData();
//             console.log('Resend OTP, temp auth data:', tempAuthData); // Debug
//             if (!tempAuthData) {
//               console.warn('No temp auth data after OTP resend, resetting to login');
//               this.backToLogin();
//               return;
//             }
//             window.sessionStorage.setItem('otpVerificationData', JSON.stringify({
//               email: this.email,
//               otpTimer: this.otpTimer,
//               resendCountdown: this.resendCountdown,
//               tempToken: tempAuthData.token
//             }));

//             this.startOtpTimer();
//             this.startResendCountdown();
//           },
//           error: (err) => {
//             console.error('OTP resend error:', err);
//             this.errorMessage = err.error?.message || 'Failed to resend OTP';
//             this.isResendingOtp = false;
//           }
//         });
//       },
//       error: (err) => {
//         console.error('Re-authentication error for OTP resend:', err);
//         this.errorMessage = err.error?.message || 'Failed to re-authenticate for OTP resend';
//         this.isResendingOtp = false;
//         this.backToLogin();
//       }
//     });
//   }

//   onResetPassword(form: NgForm): void {
//     if (form.valid && this.newPassword === this.confirmPassword) {
//       this.isLoading = true;

//       const trimmedOtp = this.otpCode.trim();
//       this.auth.resetPassword(
//         this.resetEmail,
//         trimmedOtp,
//         this.newPassword
//       ).subscribe({
//         next: () => {
//           console.log('Password reset successful');
//           this.showNewPasswordForm = false;
//           this.errorMessage = 'Password reset successfully!';
//           this.isLoading = false;
//           window.sessionStorage.removeItem('otpVerificationData');
//         },
//         error: (err) => {
//           console.error('Password reset error:', err);
//           this.errorMessage = err.error?.message || 'Password reset failed';
//           this.isLoading = false;
//           this.otpCode = '';
//         }
//       });
//     } else if (this.newPassword !== this.confirmPassword) {
//       this.errorMessage = 'Passwords do not match';
//     }
//   }

//   private startOtpTimer(): void {
//     this.timerSub?.unsubscribe();
//     this.timerSub = interval(1000).subscribe(() => {
//       if (this.otpTimer > 0) {
//         this.otpTimer--;
//         const tempAuthData = this.auth.getTempAuthData();
//         window.sessionStorage.setItem('otpVerificationData', JSON.stringify({
//           email: this.email,
//           otpTimer: this.otpTimer,
//           resendCountdown: this.resendCountdown,
//           tempToken: tempAuthData?.token || ''
//         }));
//       } else {
//         this.timerSub?.unsubscribe();
//         this.errorMessage = 'OTP has expired. Please request a new one.';
//       }
//     });
//   }

//   private startResendCountdown(): void {
//     this.resendSub?.unsubscribe();
//     this.resendSub = interval(1000).subscribe(() => {
//       if (this.resendCountdown > 0) {
//         this.resendCountdown--;
//         const tempAuthData = this.auth.getTempAuthData();
//         window.sessionStorage.setItem('otpVerificationData', JSON.stringify({
//           email: this.email,
//           otpTimer: this.otpTimer,
//           resendCountdown: this.resendCountdown,
//           tempToken: tempAuthData?.token || ''
//         }));
//       } else {
//         this.resendDisabled = false;
//         this.resendSub?.unsubscribe();
//       }
//     });
//   }

//   backToLogin(): void {
//     this.showOtpForm = false;
//     window.sessionStorage.removeItem('otpVerificationData');
//     this.auth.clearTempAuthData();
//   }

//   ngOnDestroy(): void {
//     this.timerSub?.unsubscribe();
//     this.resendSub?.unsubscribe();
//   }
// }


import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { interval, Subscription } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  // Form fields
  email = '';
  password = '';
  otpCode = '';
  resetEmail = '';
  newPassword = '';
  confirmPassword = '';

  // UI state
  errorMessage = '';
  isLoading = false;
  showOtpForm = false;
  showResetForm = false;
  showNewPasswordForm = false;

  // OTP management
  otpTimer = 600; // 10 minutes in seconds
  isResendingOtp = false;
  resendDisabled = false;
  resendCountdown = 120; // 2 minutes in seconds

  private timerSub?: Subscription;
  private resendSub?: Subscription;

  constructor(private auth: AuthService, private router: Router) {}

  ngOnInit(): void {
    // Restore state on page refresh
    const otpData = window.sessionStorage.getItem('otpVerificationData');
    if (otpData) {
      const data = JSON.parse(otpData);
      this.email = data.email;
      this.showOtpForm = true;
      this.otpTimer = data.otpTimer || 600;
      this.resendCountdown = data.resendCountdown || 0;
      this.resendDisabled = this.resendCountdown > 0;
      // Restore temp auth data in AuthService
      this.auth.restoreTempAuthData(data.email, data.tempToken);
      console.log('Restored temp auth data:', this.auth.getTempAuthData()); // Debug
      if (!this.auth.getTempAuthData()) {
        console.warn('No valid temp auth data restored, resetting to login');
        this.backToLogin();
      } else {
        this.startOtpTimer();
        if (this.resendDisabled) {
          this.startResendCountdown();
        }
      }
    }
  }

  onLogin(form: NgForm): void {
    if (form.valid) {
      this.isLoading = true;
      this.errorMessage = '';

      this.auth.login(this.email, this.password).subscribe({
        next: (response) => {
          console.log('Login successful, temp auth data:', this.auth.getTempAuthData()); // Debug
          const tempAuthData = this.auth.getTempAuthData();
          if (tempAuthData) {
            // Decode the token to check the role
            const decoded: any = this.decodeToken(tempAuthData.token);
            const role = decoded?.role;

            if (role === 'ADMIN') {
              // Admin bypasses OTP and goes directly to root
              this.auth.storeAuthData(tempAuthData.token); // Store auth data for admin
              this.auth.clearTempAuthData();
              window.sessionStorage.removeItem('otpVerificationData');
              this.router.navigate(['/']);
              this.isLoading = false;
            } else {
              // Non-admin proceeds to OTP
              this.sendOtpAfterLogin();
            }
          } else {
            console.warn('No temp auth data after login, resetting to login');
            this.errorMessage = 'Authentication session failed. Please try again.';
            this.isLoading = false;
          }
        },
        error: (err) => {
          console.error('Login error:', err);
          this.errorMessage = err.error?.message || 'Invalid credentials';
          this.isLoading = false;
        }
      });
    }
  }

  private decodeToken(token: string): any {
    try {
      return this.auth.isTokenValid(token) ? jwtDecode(token) : null;
    } catch (error) {
      console.error('Token decoding error:', error);
      return null;
    }
  }

  private sendOtpAfterLogin(): void {
    this.auth.sendOtp(this.email).subscribe({
      next: () => {
        this.handleOtpSuccess();
      },
      error: (err) => {
        if (err.status === 200 && err.error instanceof ProgressEvent) {
          this.handleOtpSuccess();
        } else {
          console.error('OTP send error:', err);
          this.errorMessage = 'Failed to send OTP';
          this.isLoading = false;
        }
      }
    });
  }

  private handleOtpSuccess(): void {
    // Save state to sessionStorage
    const tempAuthData = this.auth.getTempAuthData();
    console.log('Saving OTP state, temp auth data:', tempAuthData); // Debug
    if (!tempAuthData) {
      console.warn('No temp auth data after OTP send, resetting to login');
      this.backToLogin();
      return;
    }
    window.sessionStorage.setItem('otpVerificationData', JSON.stringify({
      email: this.email,
      otpTimer: this.otpTimer,
      resendCountdown: this.resendCountdown,
      tempToken: tempAuthData.token
    }));

    this.showOtpForm = true;
    this.startOtpTimer();
    this.isLoading = false;
    this.errorMessage = '';
  }

  onVerifyOtp(form: NgForm): void {
    if (form.valid && this.otpTimer > 0) {
      this.isLoading = true;
      this.errorMessage = '';

      const trimmedOtp = this.otpCode.trim();
      console.log('Verifying OTP:', trimmedOtp, 'with temp auth data:', this.auth.getTempAuthData()); // Debug

      const tempAuthData = this.auth.getTempAuthData();
      if (!tempAuthData) {
        console.warn('No temp auth data for OTP verification, resetting to login');
        this.errorMessage = 'Authentication session expired. Please try logging in again.';
        this.isLoading = false;
        this.backToLogin();
        return;
      }

      this.auth.verifyOtp(trimmedOtp).subscribe({
        next: () => {
          console.log('OTP verification successful');
          window.sessionStorage.removeItem('otpVerificationData');
          this.isLoading = false;
          this.router.navigate(['/']);
        },
        error: (err) => {
          console.error('OTP verification error:', err);
          this.errorMessage = err.error?.message || 'Invalid OTP or authentication session expired. Please try logging in again.';
          this.isLoading = false;
          this.otpCode = ''; // Clear OTP input for next attempt
          if (err.error?.message === 'No pending authentication' || err.error?.message === 'Invalid temporary token') {
            this.backToLogin(); // Reset to login form if session is invalid
          }
        },
        complete: () => {
          this.isLoading = false;
        }
      });
    } else if (this.otpTimer === 0) {
      this.errorMessage = 'OTP has expired. Please request a new one.';
    } else {
      this.errorMessage = 'Please enter a valid OTP.';
    }
  }

  onForgotPassword(): void {
    this.showResetForm = true;
    this.errorMessage = '';
  }

  onSendResetOtp(form: NgForm): void {
    if (form.valid) {
      this.isLoading = true;

      this.auth.forgotPassword(this.resetEmail).subscribe({
        next: () => {
          console.log('Password reset OTP sent');
          this.showResetForm = false;
          this.showNewPasswordForm = true;
          this.startOtpTimer();
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Password reset OTP error:', err);
          this.errorMessage = err.error?.message || 'Failed to send OTP';
          this.isLoading = false;
        }
      });
    }
  }

  onResendOtp(): void {
    if (this.resendDisabled) return;

    this.isResendingOtp = true;
    this.errorMessage = '';

    // Re-authenticate by calling login again to refresh tempAuthData
    this.auth.login(this.email, this.password).subscribe({
      next: () => {
        console.log('Re-authentication successful for OTP resend, temp auth data:', this.auth.getTempAuthData()); // Debug
        this.auth.sendOtp(this.email).subscribe({
          next: () => {
            console.log('OTP resent successfully');
            this.isResendingOtp = false;
            this.resendDisabled = true;
            this.resendCountdown = 120;
            this.otpTimer = 600;

            // Update sessionStorage
            const tempAuthData = this.auth.getTempAuthData();
            console.log('Resend OTP, temp auth data:', tempAuthData); // Debug
            if (!tempAuthData) {
              console.warn('No temp auth data after OTP resend, resetting to login');
              this.backToLogin();
              return;
            }
            window.sessionStorage.setItem('otpVerificationData', JSON.stringify({
              email: this.email,
              otpTimer: this.otpTimer,
              resendCountdown: this.resendCountdown,
              tempToken: tempAuthData.token
            }));

            this.startOtpTimer();
            this.startResendCountdown();
          },
          error: (err) => {
            console.error('OTP resend error:', err);
            this.errorMessage = err.error?.message || 'Failed to resend OTP';
            this.isResendingOtp = false;
          }
        });
      },
      error: (err) => {
        console.error('Re-authentication error for OTP resend:', err);
        this.errorMessage = err.error?.message || 'Failed to re-authenticate for OTP resend';
        this.isResendingOtp = false;
        this.backToLogin();
      }
    });
  }

  onResetPassword(form: NgForm): void {
    if (form.valid && this.newPassword === this.confirmPassword) {
      this.isLoading = true;

      const trimmedOtp = this.otpCode.trim();
      this.auth.resetPassword(
        this.resetEmail,
        trimmedOtp,
        this.newPassword
      ).subscribe({
        next: () => {
          console.log('Password reset successful');
          this.showNewPasswordForm = false;
          this.errorMessage = 'Password reset successfully!';
          this.isLoading = false;
          window.sessionStorage.removeItem('otpVerificationData');
        },
        error: (err) => {
          console.error('Password reset error:', err);
          this.errorMessage = err.error?.message || 'Password reset failed';
          this.isLoading = false;
          this.otpCode = '';
        }
      });
    } else if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match';
    }
  }

  private startOtpTimer(): void {
    this.timerSub?.unsubscribe();
    this.timerSub = interval(1000).subscribe(() => {
      if (this.otpTimer > 0) {
        this.otpTimer--;
        const tempAuthData = this.auth.getTempAuthData();
        window.sessionStorage.setItem('otpVerificationData', JSON.stringify({
          email: this.email,
          otpTimer: this.otpTimer,
          resendCountdown: this.resendCountdown,
          tempToken: tempAuthData?.token || ''
        }));
      } else {
        this.timerSub?.unsubscribe();
        this.errorMessage = 'OTP has expired. Please request a new one.';
      }
    });
  }

  private startResendCountdown(): void {
    this.resendSub?.unsubscribe();
    this.resendSub = interval(1000).subscribe(() => {
      if (this.resendCountdown > 0) {
        this.resendCountdown--;
        const tempAuthData = this.auth.getTempAuthData();
        window.sessionStorage.setItem('otpVerificationData', JSON.stringify({
          email: this.email,
          otpTimer: this.otpTimer,
          resendCountdown: this.resendCountdown,
          tempToken: tempAuthData?.token || ''
        }));
      } else {
        this.resendDisabled = false;
        this.resendSub?.unsubscribe();
      }
    });
  }

  backToLogin(): void {
    this.showOtpForm = false;
    window.sessionStorage.removeItem('otpVerificationData');
    this.auth.clearTempAuthData();
  }

  ngOnDestroy(): void {
    this.timerSub?.unsubscribe();
    this.resendSub?.unsubscribe();
  }
}
