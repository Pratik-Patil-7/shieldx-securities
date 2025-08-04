// import { NgModule } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { AdminRoutingModule } from './admin-routing.module';
// import { DashboardComponent } from './dashboard/dashboard.component';
// import { UsersComponent } from './users/users.component';
// import { BookingsComponent } from './bookings/bookings.component';
// import { StaffComponent } from './staff/staff.component';
// import { SecurityTypeComponent } from './security-type/security-type.component';
// import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// import { AdminMenuComponent } from './admin-menu/admin-menu.component';
// import { NgbPagination } from "@ng-bootstrap/ng-bootstrap";


// @NgModule({
//   declarations: [
//     DashboardComponent,
//     UsersComponent,
//     BookingsComponent,
//     StaffComponent,
//     SecurityTypeComponent,
//     AdminMenuComponent
//   ],
//   imports: [
//     CommonModule,
//     AdminRoutingModule,
//     FormsModule,
//     ReactiveFormsModule,
//     NgbPagination
// ]
// })
// export class AdminModule {}


import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbPaginationModule, NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AdminRoutingModule } from './admin-routing.module';
import { ToastrModule } from 'ngx-toastr';
import { DashboardComponent } from './dashboard/dashboard.component';
import { BookingsComponent } from './bookings/bookings.component';
import { UsersComponent } from './users/users.component';
import { StaffComponent } from './staff/staff.component';
import { SecurityTypeComponent } from './security-type/security-type.component';
import { AdminMenuComponent } from './admin-menu/admin-menu.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    AdminMenuComponent,
    DashboardComponent,
    BookingsComponent,
    UsersComponent,
    StaffComponent,
    SecurityTypeComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    NgbPaginationModule,
    NgbDropdownModule,
    AdminRoutingModule,
    // NoopAnimationsModule,
    // BrowserAnimationsModule,
    // ToastrModule.forRoot()

  ]
})
export class AdminModule { }
