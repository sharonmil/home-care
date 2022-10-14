import { IUser } from '@/shared/model/user.model';
import { IFamiliar } from '@/shared/model/familiar.model';
import { IMedico } from '@/shared/model/medico.model';
import { IHistoriaClinica } from '@/shared/model/historia-clinica.model';
import { ISignosVitales } from '@/shared/model/signos-vitales.model';

export interface IPaciente {
  id?: number;
  cedula?: string;
  ciudad?: string;
  direccion?: string;
  telefono?: string;
  fechaNacimiento?: Date;
  username?: IUser | null;
  familiar?: IFamiliar | null;
  medico?: IMedico | null;
  historias?: IHistoriaClinica[] | null;
  historiaSignos?: ISignosVitales[] | null;
}

export class Paciente implements IPaciente {
  constructor(
    public id?: number,
    public cedula?: string,
    public ciudad?: string,
    public direccion?: string,
    public telefono?: string,
    public fechaNacimiento?: Date,
    public username?: IUser | null,
    public familiar?: IFamiliar | null,
    public medico?: IMedico | null,
    public historias?: IHistoriaClinica[] | null,
    public historiaSignos?: ISignosVitales[] | null
  ) {}
}
