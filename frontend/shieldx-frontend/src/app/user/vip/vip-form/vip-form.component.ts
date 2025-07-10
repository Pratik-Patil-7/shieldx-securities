import { Component, Input, OnInit } from '@angular/core';
import { VipPerson, VipService, AddVipPerson } from './../../../core/services/vip.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-vip-form',
  templateUrl: './vip-form.component.html',
  styleUrls: ['./vip-form.component.css'],
  imports: [CommonModule, FormsModule]
})
export class VipFormComponent implements OnInit {
  @Input() isSelf = false;
  @Input() existingVip: VipPerson | null = null;

  vipPerson: VipPerson = {
    vipId: 0,
    name: '',
    email: '',
    mobile: '',
    gender: '',
    dateOfBirth: '',
    address: '',
    profession: '',
    reasonForSecurity: '',
    userId: 0
  };

  isLoading = false;
  errorMessage = '';
  successMessage = '';
  currentUserId: number = 0;

  constructor(
    private vipService: VipService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {

    const storedUserId = localStorage.getItem('userId');
    this.currentUserId = storedUserId ? parseInt(storedUserId, 10) : 0;

    const user = this.authService.getCurrentUser();
    if (user) {
      this.currentUserId = user.userId;
      this.vipPerson.userId = this.currentUserId;

      if (this.isSelf) {
        this.vipPerson = {
          ...this.vipPerson,
          name: `${user.firstName} ${user.lastName}`,
          email: user.email || '',
          mobile: user.mobile || '',
          address: user.address || '',
          reasonForSecurity: 'Personal security'
        };
      }
    }

    if (this.existingVip) {
      this.vipPerson = { ...this.existingVip };
      this.vipPerson.userId = this.existingVip.userId || this.currentUserId;
    }
  }

  onSubmit(): void {

     const storedUserId = localStorage.getItem('userId');
    this.currentUserId = storedUserId ? parseInt(storedUserId, 10) : 0;


    if (this.currentUserId <= 0) {
      this.errorMessage = 'You must be logged in to perform this action';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.existingVip) {

      this.vipService.updateVipPerson(this.vipPerson.vipId, this.vipPerson).subscribe({
        next: () => {
          this.successMessage = 'VIP person updated successfully!';
          this.isLoading = false;
          setTimeout(() => this.router.navigate(['/vip']), 2000);
        },
        error: (err) => {
          this.errorMessage = 'Update failed. Please try again.';
          this.isLoading = false;
          console.error('Error:', err);
        }
      });
    } else {
      // Create case
      const newVipPerson: AddVipPerson = {
        ...this.vipPerson,
        userId: this.currentUserId
      };

      this.vipService.createVipPerson(newVipPerson).subscribe({
        next: () => {
          this.successMessage = 'VIP person created successfully!';
          this.isLoading = false;
          setTimeout(() => this.router.navigate(['/vip']), 2000);
        },
        error: (err) => {
          this.errorMessage = 'Creation failed. Please try again.';
          this.isLoading = false;
          console.error('Error:', err);
        }
      });
    }
  }
}
