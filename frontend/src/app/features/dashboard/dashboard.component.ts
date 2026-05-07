import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClienteService } from '../../core/services/cliente.service';
import { Cliente } from '../../core/models/cliente.model';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  private clienteService = inject(ClienteService);
  private authService = inject(AuthService);
  private router = inject(Router);
  
  totalClientes = signal(0);
  totalPedidos = signal(0); // Placeholder
  totalTiendas = signal(0); // Placeholder
  recentClientes = signal<Cliente[]>([]);

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  ngOnInit() {
    this.clienteService.getClientes().subscribe({
      next: (clientes) => {
        console.log('Dashboard Data Received:', clientes.length, 'clients');
        this.totalClientes.set(clientes.length);
        this.recentClientes.set(clientes.slice(0, 5));
      },
      error: (err) => console.error('Error fetching clients', err)
    });
  }
}
