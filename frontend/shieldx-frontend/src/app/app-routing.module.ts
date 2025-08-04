import { VipModule } from './user/vip/vip.module';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { HomeComponent } from './home/home/home.component';
import { RegisterComponent } from './auth/register/register.component';
import { DashboardComponent } from './user/dashboard/dashboard.component';
import { ProfileComponent } from './user/profile/profile.component';
import { MybookingsComponent } from './user/mybookings/mybookings.component';
import { VipListComponent } from './user/vip/vip-list/vip-list.component';
import { JobsComponent } from './user/jobs/jobs.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'user-dashboard', component: DashboardComponent },
  { path: 'auth', loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule) },
  { path: 'user', loadChildren: () => import('./user/user.module').then(m => m.UserModule) },
  { path: 'admin', loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule) },
  { path: "profile", component: ProfileComponent },
  { path: "mybookings", component: MybookingsComponent },
  { path: "jobapplication", component: JobsComponent },
  { path: 'vip', loadChildren: () => import('./user/vip/vip.module').then(m => m.VipModule) },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
