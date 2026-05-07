import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Usuario } from '../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/auth';
  
  // Usamos signals para un estado reactivo moderno (Angular 17+)
  currentUser = signal<Usuario | null>(null);
  isAuthenticated = signal<boolean>(false);

  constructor() {
    this.checkSession();
  }

  login(credentials: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login`, credentials).pipe(
      tap(response => {
        this.saveSession(response);
      })
    );
  }

  register(userData: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/register`, userData);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUser.set(null);
    this.isAuthenticated.set(false);
  }

  private saveSession(response: any): void {
    const user: Usuario = {
      id: response.id,
      nombre: response.nombre || '',
      email: response.email,
      telefono: '',
      direccion: '',
      roles: response.roles
    };
    
    localStorage.setItem('token', response.accessToken);
    localStorage.setItem('user', JSON.stringify(user));
    
    this.currentUser.set(user);
    this.isAuthenticated.set(true);
  }

  private checkSession(): void {
    const token = localStorage.getItem('token');
    const userStr = localStorage.getItem('user');
    
    if (token && userStr) {
      this.currentUser.set(JSON.parse(userStr));
      this.isAuthenticated.set(true);
    }
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
}
