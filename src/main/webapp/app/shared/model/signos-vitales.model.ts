import { IPaciente } from '@/shared/model/paciente.model';
import { IUser } from '@/shared/model/user.model';

export interface ISignosVitales {
  id?: number;
  oximetria?: number | null;
  frecuenciaRespiratoria?: number | null;
  frecuenciaCardiaca?: number | null;
  presion?: string | null;
  glicemia?: number | null;
  createdDate?: Date | null;
  paciente?: IPaciente | null;
  familiar?: IUser | null;
  medico?: IUser | null;
}

export class SignosVitales implements ISignosVitales {
  constructor(
    public id?: number,
    public oximetria?: number | null,
    public frecuenciaRespiratoria?: number | null,
    public frecuenciaCardiaca?: number | null,
    public presion?: string | null,
    public glicemia?: number | null,
    public createdDate?: Date | null,
    public paciente?: IPaciente | null,
    public familiar?: IUser | null,
    public medico?: IUser | null
  ) {}
}
