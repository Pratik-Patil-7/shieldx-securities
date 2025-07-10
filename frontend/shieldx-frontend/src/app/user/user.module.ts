import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserRoutingModule } from './user-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
// import { BookingsComponent } from '../admin/bookings/bookings.component';
import { ProfileComponent } from './profile/profile.component';
import { PaymentsComponent } from './payments/payments.component';
import { JobsComponent } from './jobs/jobs.component';
import { MybookingsComponent } from './mybookings/mybookings.component';
import { VipListComponent } from './vip/vip-list/vip-list.component';
import { VipFormComponent } from './vip/vip-form/vip-form.component';


@NgModule({
  declarations: [
    DashboardComponent,
    ProfileComponent,
    PaymentsComponent,
    JobsComponent,
    MybookingsComponent,
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    FormsModule,
    ReactiveFormsModule

  ]
})
export class UserModule { }
