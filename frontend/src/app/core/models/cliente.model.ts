import { Usuario } from './usuario.model';

export interface Cliente extends Usuario {
  puntosFidelidad: number;
  preferencias: string;
}
