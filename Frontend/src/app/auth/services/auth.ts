import { inject, Injectable, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  url: string = 'http://localhost:3000/ecommerce';
  private readonly http = inject(HttpClient);

  login(data: any): Observable<any> {
    return this.http.post(`${this.url}/login`, data);
  }
}
