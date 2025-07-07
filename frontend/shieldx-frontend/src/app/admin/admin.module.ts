import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UsersComponent } from './users/users.component';
import { BookingsComponent } from './bookings/bookings.component';
import { StaffComponent } from './staff/staff.component';
import { SecurityTypeComponent } from './security-type/security-type.component';


@NgModule({
  declarations: [
    DashboardComponent,
    UsersComponent,
    BookingsComponent,
    StaffComponent,
    SecurityTypeComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }
