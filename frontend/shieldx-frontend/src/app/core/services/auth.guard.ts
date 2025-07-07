import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const token = localStorage.getItem('token');
    if (token) {
      const role = localStorage.getItem('role'); // Assume role is stored after login
      const requiredRoles = route.data['roles'];
      if (!requiredRoles || (requiredRoles && requiredRoles.includes(role))) {
        return true;
      }
      this.router.navigate(['/dashboard']);
      return false;
    }
    this.router.navigate(['/login']);
    return false;
  }
}
