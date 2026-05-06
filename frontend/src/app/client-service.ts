import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Cliente } from './models/cliente.model';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  private readonly apiUrl = 'https://localhost:8080/api/usuarios/clientes';

  constructor(private readonly http: HttpClient) { }

  getClientes(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.apiUrl);
  }


}
