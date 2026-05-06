import { Cliente } from './cliente.model';
import { Tienda } from './tienda.model';

export interface Pedido {
  id: string;
  fecha: string;
  estado: string;
  total: number;
  direccionEntrega: string;
  cliente?: Cliente;
  tienda?: Tienda;
}
