import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

export interface VipPerson {
  vipId: number;
  name: string;
  email: string;
  mobile: string;
  gender: string;
  dateOfBirth: string;
  profession: string;
  address: string;
  reasonForSecurity: string;
  userId: number;
}

export interface AddVipPerson {
  name: string;
  email: string;
  mobile: string;
  gender: string;
  dateOfBirth: string;
  profession: string;
  address: string;
  reasonForSecurity: string;
  userId: number | null;
}

@Injectable({
  providedIn: 'root'
})
export class VipService {
  private apiUrl = 'http://localhost:8080/api/vip';

  constructor(private http: HttpClient, private authService: AuthService) { }

  private getHeaders() {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
      'Content-Type': 'application/json'
    });
  }

  getAllVipPersons(): Observable<VipPerson[]> {
    return this.http.get<VipPerson[]>(this.apiUrl, { headers: this.getHeaders() });
  }

  createVipPerson(vipPersonData: AddVipPerson): Observable<VipPerson> {
    return this.http.post<VipPerson>(this.apiUrl, vipPersonData, {
      headers: this.getHeaders()
    });
  }

  updateVipPerson(id: number, vipPerson: VipPerson): Observable<VipPerson> {
    return this.http.put<VipPerson>(`${this.apiUrl}/${id}`, vipPerson, {
      headers: this.getHeaders()
    });
  }

  deleteVipPerson(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }
}
