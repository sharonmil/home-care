import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import PacienteService from '@/entities/paciente/paciente.service';
import { IPaciente } from '@/shared/model/paciente.model';

import UserService from '@/entities/user/user.service';

import { IHistoriaClinica, HistoriaClinica } from '@/shared/model/historia-clinica.model';
import HistoriaClinicaService from './historia-clinica.service';

const validations: any = {
  historiaClinica: {
    sugerencia: {
      required,
      maxLength: maxLength(2000),
    },
    diagnostico: {
      maxLength: maxLength(2000),
    },
  },
};

@Component({
  validations,
})
export default class HistoriaClinicaUpdate extends Vue {
  @Inject('historiaClinicaService') private historiaClinicaService: () => HistoriaClinicaService;
  @Inject('alertService') private alertService: () => AlertService;

  public historiaClinica: IHistoriaClinica = new HistoriaClinica();

  @Inject('pacienteService') private pacienteService: () => PacienteService;

  public pacientes: IPaciente[] = [];

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.historiaClinicaId) {
        vm.retrieveHistoriaClinica(to.params.historiaClinicaId);
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
    if (this.historiaClinica.id) {
      this.historiaClinicaService()
        .update(this.historiaClinica)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('homecareApp.historiaClinica.updated', { param: param.id });
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
      this.historiaClinicaService()
        .create(this.historiaClinica)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('homecareApp.historiaClinica.created', { param: param.id });
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

  public retrieveHistoriaClinica(historiaClinicaId): void {
    this.historiaClinicaService()
      .find(historiaClinicaId)
      .then(res => {
        this.historiaClinica = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.pacienteService()
      .retrieve()
      .then(res => {
        this.pacientes = res.data;
      });

    this.userService()
      .retrieve()
      .then(res => {
        this.users = res.data;
      });
  }
  public get username(): string {
    return this.$store.getters.account?.login ?? '';
  }
}
