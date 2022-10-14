import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, minLength, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';

import PacienteService from '@/entities/paciente/paciente.service';
import { IPaciente } from '@/shared/model/paciente.model';

import { IFamiliar, Familiar } from '@/shared/model/familiar.model';
import FamiliarService from './familiar.service';
import { Parentesco } from '@/shared/model/enumerations/parentesco.model';

const validations: any = {
  familiar: {
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
    parentesco: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class FamiliarUpdate extends Vue {
  @Inject('familiarService') private familiarService: () => FamiliarService;
  @Inject('alertService') private alertService: () => AlertService;

  public familiar: IFamiliar = new Familiar();

  @Inject('userService') private userService: () => UserService;

  public users: Array<any> = [];

  @Inject('pacienteService') private pacienteService: () => PacienteService;

  public pacientes: IPaciente[] = [];
  public parentescoValues: string[] = Object.keys(Parentesco);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.familiarId) {
        vm.retrieveFamiliar(to.params.familiarId);
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
    if (this.familiar.id) {
      this.familiarService()
        .update(this.familiar)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('homecareApp.familiar.updated', { param: param.id });
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
      this.familiarService()
        .create(this.familiar)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('homecareApp.familiar.created', { param: param.id });
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

  public retrieveFamiliar(familiarId): void {
    this.familiarService()
      .find(familiarId)
      .then(res => {
        this.familiar = res;
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
