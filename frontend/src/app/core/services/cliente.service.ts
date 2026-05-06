import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente.model';

@Injectable({
  providedIn: 'root'
})
export class ClienteService extends ApiService {
  private resource = 'usuarios/clientes';

  getClientes(): Observable<Cliente[]> {
    return this.get<Cliente[]>(this.resource);
  }

  getCliente(id: string): Observable<Cliente> {
    return this.get<Cliente>(`${this.resource}/${id}`);
  }

  crearCliente(cliente: Partial<Cliente>): Observable<Cliente> {
    return this.post<Cliente>(this.resource, cliente);
  }
}
