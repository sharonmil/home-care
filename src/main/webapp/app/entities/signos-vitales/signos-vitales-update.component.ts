import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, minValue, maxValue, minLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';
import AccountService from "@/account/account.service";
import PacienteService from '@/entities/paciente/paciente.service';
import { IPaciente } from '@/shared/model/paciente.model';

import UserService from '@/entities/user/user.service';

import { ISignosVitales, SignosVitales } from '@/shared/model/signos-vitales.model';
import SignosVitalesService from './signos-vitales.service';

const validations: any = {
  signosVitales: {
    oximetria: {
      numeric,
      min: minValue(1),
      max: maxValue(100),
    },
    frecuenciaRespiratoria: {
      numeric,
      min: minValue(1),
      max: maxValue(300),
    },
    frecuenciaCardiaca: {
      numeric,
      min: minValue(5),
      max: maxValue(300),
    },
    presion: {
      minLength: minLength(2),
    },
    glicemia: {
      numeric,
      min: minValue(1),
      max: maxValue(600),
    },
    createdDate: {},
  },
};

@Component({
  validations,
})
export default class SignosVitalesUpdate extends Vue {
  @Inject('signosVitalesService') private signosVitalesService: () => SignosVitalesService;
  @Inject('alertService') private alertService: () => AlertService;

  public signosVitales: ISignosVitales = new SignosVitales();
  @Inject('accountService') private accountService: () => AccountService;
  @Inject('pacienteService') private pacienteService: () => PacienteService;

  public pacientes: IPaciente[] = [];

  @Inject('userService') private userService: () => UserService;

  private hasAnyAuthorityValues = {};

  public users: Array<any> = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.signosVitalesId) {
        vm.retrieveSignosVitales(to.params.signosVitalesId);
      } else{
        vm.signosVitales.createdDate = new Date();
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
    this.initRelationships();
  }

  public save(): void {
    this.isSaving = true;
    if (this.signosVitales.id) {
      this.signosVitalesService()
        .update(this.signosVitales)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('homecareApp.signosVitales.updated', { param: param.id });
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
      this.signosVitalesService()
        .create(this.signosVitales)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('homecareApp.signosVitales.created', { param: param.id });
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

  public get username(): string {
    return this.$store.getters.account?.login ?? '';
  }

  public retrieveSignosVitales(signosVitalesId): void {
    this.signosVitalesService()
      .find(signosVitalesId)
      .then(res => {
        this.signosVitales = res;
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

        if (!this.signosVitales.paciente) {
          this.signosVitales.paciente = this.pacientes.find(obj => {
            return obj.username.login === this.username
          })
        }
        if (!this.signosVitales.paciente) {
          this.signosVitales.paciente = this.pacientes.find(obj => {
            return obj.familiar.username.login === this.username
          })
        }
      });

    this.userService()
      .retrieve()
      .then(res => {
        this.users = res.data;
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
