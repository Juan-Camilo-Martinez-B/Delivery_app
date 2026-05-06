import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClienteService } from '../../core/services/cliente.service';
import { Cliente } from '../../core/models/cliente.model';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './client-list.component.html',
  styleUrl: './client-list.component.css'
})
export class ClientListComponent implements OnInit {
  private clienteService = inject(ClienteService);
  clientes = signal<Cliente[]>([]);

  ngOnInit() {
    this.clienteService.getClientes().subscribe({
      next: (data) => {
        console.log('API Response (Clientes):', data);
        this.clientes.set(data);
      },
      error: (err) => {
        console.error('Error fetching clients:', err);
        alert('Error al conectar con el servidor. Revisa la consola (F12).');
      }
    });
  }
}
