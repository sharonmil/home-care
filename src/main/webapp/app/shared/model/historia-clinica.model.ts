import { IPaciente } from '@/shared/model/paciente.model';
import { IUser } from '@/shared/model/user.model';

export interface IHistoriaClinica {
  id?: number;
  sugerencia?: string;
  diagnostico?: string | null;
  paciente?: IPaciente | null;
  familiar?: IUser | null;
  medico?: IUser | null;
}

export class HistoriaClinica implements IHistoriaClinica {
  constructor(
    public id?: number,
    public sugerencia?: string,
    public diagnostico?: string | null,
    public paciente?: IPaciente | null,
    public familiar?: IUser | null,
    public medico?: IUser | null
  ) {}
}
