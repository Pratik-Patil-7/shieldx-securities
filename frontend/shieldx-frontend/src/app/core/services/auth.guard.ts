import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router, private authService: AuthService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const token = localStorage.getItem('token');
    if (token) {
      const role = this.authService.getRole();
      const requiredRoles = route.data['roles'] as string[] | undefined;

      if (!requiredRoles) {
        return true;
      }

      if (requiredRoles.includes(role || '')) {
        return true;
      }

      if (role === 'USER') {
        this.router.navigate(['/dashboard']);
      } else if (role === 'ADMIN') {
        this.router.navigate(['/admin/dashboard']);
      } else {
        this.router.navigate(['/login']);
      }
      return false;
    }
    this.router.navigate(['/login']);
    return false;
  }
}
