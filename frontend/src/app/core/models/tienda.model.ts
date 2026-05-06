import { Usuario } from './usuario.model';
import { Producto } from './producto.model';

export interface Tienda extends Usuario {
  horarioApertura: string;
  horarioCierre: string;
  productos?: Producto[];
}
