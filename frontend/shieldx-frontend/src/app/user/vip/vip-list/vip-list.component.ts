import { VipPerson, VipService } from './../../../core/services/vip.service';
import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
   standalone: true,
  selector: 'app-vip-list',
  templateUrl:'./vip-list.component.html',
  styleUrls: ['./vip-list.component.css'],
  imports: [CommonModule, FormsModule, RouterModule]
})
export class VipListComponent implements OnInit {
  vipPersons: VipPerson[] = [];
  isLoading = false;
  errorMessage = '';

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

  deleteVipPerson(id: number): void {
    if (confirm('Are you sure you want to delete this VIP person?')) {
      this.vipService.deleteVipPerson(id).subscribe({
        next: () => {
          this.loadVipPersons();
        },
        error: (err) => {
          this.errorMessage = 'Failed to delete VIP person';
        }
      });
    }
  }
}
