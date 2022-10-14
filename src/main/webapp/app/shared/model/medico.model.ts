import { IUser } from '@/shared/model/user.model';
import { IPaciente } from '@/shared/model/paciente.model';

export interface IMedico {
  id?: number;
  cedula?: string;
  ciudad?: string;
  direccion?: string;
  telefono?: string;
  fechaNacimiento?: Date;
  especialidad?: string | null;
  username?: IUser | null;
  pacientes?: IPaciente[] | null;
}

export class Medico implements IMedico {
  constructor(
    public id?: number,
    public cedula?: string,
    public ciudad?: string,
    public direccion?: string,
    public telefono?: string,
    public fechaNacimiento?: Date,
    public especialidad?: string,
    public username?: IUser | null,
    public pacientes?: IPaciente[] | null
  ) {}
}
