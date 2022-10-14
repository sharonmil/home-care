import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, minLength, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';

import PacienteService from '@/entities/paciente/paciente.service';
import { IPaciente } from '@/shared/model/paciente.model';

import { IMedico, Medico } from '@/shared/model/medico.model';
import MedicoService from './medico.service';

const validations: any = {
  medico: {
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
    especialidad: {
      required,
      minLength: minLength(2),
    },
  },
};

@Component({
  validations,
})
export default class MedicoUpdate extends Vue {
  @Inject('medicoService') private medicoService: () => MedicoService;
  @Inject('alertService') private alertService: () => AlertService;

  public medico: IMedico = new Medico();

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];

  @Inject('pacienteService') private pacienteService: () => PacienteService;

  public pacientes: IPaciente[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.medicoId) {
        vm.retrieveMedico(to.params.medicoId);
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
    if (this.medico.id) {
      this.medicoService()
        .update(this.medico)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('homecareApp.medico.updated', { param: param.id });
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
      this.medicoService()
        .create(this.medico)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('homecareApp.medico.created', { param: param.id });
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

  public retrieveMedico(medicoId): void {
    this.medicoService()
      .find(medicoId)
      .then(res => {
        this.medico = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.userService()
      .retrieve()
      .then(res => {
        this.users = res.data;
      });
    this.pacienteService()
      .retrieve()
      .then(res => {
        this.pacientes = res.data;
      });
  }
}
