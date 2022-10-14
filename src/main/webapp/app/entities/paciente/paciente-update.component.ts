import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, minLength, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';

import FamiliarService from '@/entities/familiar/familiar.service';
import { IFamiliar } from '@/shared/model/familiar.model';

import UserManagementService from "@/admin/user-management/user-management.service";

import MedicoService from '@/entities/medico/medico.service';
import { IMedico } from '@/shared/model/medico.model';

import HistoriaClinicaService from '@/entities/historia-clinica/historia-clinica.service';
import { IHistoriaClinica } from '@/shared/model/historia-clinica.model';

import SignosVitalesService from '@/entities/signos-vitales/signos-vitales.service';
import { ISignosVitales } from '@/shared/model/signos-vitales.model';

import { IPaciente, Paciente } from '@/shared/model/paciente.model';
import PacienteService from './paciente.service';
import AccountService from "@/account/account.service";


const validations: any = {
  paciente: {
    cedula: {
      required,
      minLength: minLength(5),
      maxLength: maxLength(20),
    },
    ciudad: {
      required,
      minLength: minLength(5),
      maxLength: maxLength(20),
    },
    direccion: {
      required,
      minLength: minLength(5),
    },
    telefono: {
      required,
      minLength: minLength(7),
    },
    fechaNacimiento: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class PacienteUpdate extends Vue {
  @Inject('pacienteService') private pacienteService: () => PacienteService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;

  public paciente: IPaciente = new Paciente();

  @Inject('userService') private userService: () => UserService;

  @Inject('userManagementService') private userManagementService: () => UserManagementService;

  public users: Array<any> = [];

  @Inject('familiarService') private familiarService: () => FamiliarService;

  public familiars: IFamiliar[] = [];

  @Inject('medicoService') private medicoService: () => MedicoService;

  public medicos: IMedico[] = [];

  @Inject('historiaClinicaService') private historiaClinicaService: () => HistoriaClinicaService;

  public historiaClinicas: IHistoriaClinica[] = [];

  @Inject('signosVitalesService') private signosVitalesService: () => SignosVitalesService;

  public signosVitales: ISignosVitales[] = [];
  public isSaving = false;
  public currentLanguage = '';
  private hasAnyAuthorityValues = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.pacienteId) {
        vm.retrievePaciente(to.params.pacienteId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );

  }

  public save(): void {
    this.isSaving = true;
    if (this.paciente.id) {
      this.pacienteService()
        .update(this.paciente)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('homecareApp.paciente.updated', { param: param.id });
          // @ts-ignore
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.pacienteService()
        .create(this.paciente)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('homecareApp.paciente.created', { param: param.id });
          // @ts-ignore
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrievePaciente(pacienteId): void {
    this.pacienteService()
      .find(pacienteId)
      .then(res => {
        this.paciente = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.userManagementService()
      .retrieve()
      .then(res => {
        this.users = res.data;
      });
    this.familiarService()
      .retrieve()
      .then(res => {
        this.familiars = res.data;
      });
    this.medicoService()
      .retrieve()
      .then(res => {
        this.medicos = res.data;
      });
    this.historiaClinicaService()
      .retrieve()
      .then(res => {
        this.historiaClinicas = res.data;
      });
    this.signosVitalesService()
      .retrieve()
      .then(res => {
        this.signosVitales = res.data;
      });
  }

  public hasAnyAuthority(authorities: any): boolean {
    this.accountService()
      .hasAnyAuthorityAndCheckAuth(authorities)
      .then(value => {
        if (this.hasAnyAuthorityValues[authorities] !== value) {
          this.hasAnyAuthorityValues = { ...this.hasAnyAuthorityValues, [authorities]: value };
        }
      });
    return this.hasAnyAuthorityValues[authorities] ?? false;
  }
}
