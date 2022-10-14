import { IUser } from '@/shared/model/user.model';
import { IPaciente } from '@/shared/model/paciente.model';

import { Parentesco } from '@/shared/model/enumerations/parentesco.model';
export interface IFamiliar {
  id?: number;
  cedula?: string;
  ciudad?: string;
  direccion?: string;
  telefono?: string;
  fechaNacimiento?: Date;
  parentesco?: Parentesco;
  username?: IUser | null;
  paciente?: IPaciente | null;
  emailPaciente?: string | null;
}

export class Familiar implements IFamiliar {
  constructor(
    public id?: number,
    public cedula?: string,
    public ciudad?: string,
    public direccion?: string,
    public telefono?: string,
    public fechaNacimiento?: Date,
    public parentesco?: Parentesco,
    public username?: IUser | null,
    public paciente?: IPaciente | null,
    public emailPaciente ?: string | null
  ) {}
}
