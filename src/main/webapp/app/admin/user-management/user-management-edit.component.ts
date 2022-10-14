import { email, maxLength, minLength, required } from 'vuelidate/lib/validators';
import { Component, Inject, Vue } from 'vue-property-decorator';
import UserManagementService from './user-management.service';
import { IUser, User } from '@/shared/model/user.model';
import AlertService from '@/shared/alert/alert.service';
import {IPaciente, Paciente} from "@/shared/model/paciente.model";
import {Familiar, IFamiliar} from "@/shared/model/familiar.model";
import {Parentesco} from "@/shared/model/enumerations/parentesco.model";
import {IMedico, Medico} from "@/shared/model/medico.model";
import PacienteService from "@/entities/paciente/paciente.service";
import FamiliarService from "@/entities/familiar/familiar.service";
import MedicoService from "@/entities/medico/medico.service";

const loginValidator = (value: string) => {
  if (!value) {
    return true;
  }
  return /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/.test(value);
};

const validations: any = {
  userAccount: {
    login: {
      required,
      maxLength: maxLength(254),
      pattern: loginValidator,
    },
    firstName: {
      maxLength: maxLength(50),
    },
    lastName: {
      maxLength: maxLength(50),
    },
    email: {
      required,
      email,
      minLength: minLength(5),
      maxLength: maxLength(50),
    },
  },

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
  familiar: {
    emailPaciente: {
      required,
      minLength: minLength(5),
      maxLength: maxLength(254),
      email,
    },
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
      minLength: minLength(2),
    },
  },
};

@Component({
  validations,
})
export default class JhiUserManagementEdit extends Vue {
  @Inject('userManagementService') private userManagementService: () => UserManagementService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('pacienteService') private pacienteService: () => PacienteService;
  @Inject('familiarService') private familiarService: () => FamiliarService;
  @Inject('medicoService') private medicoService: () => MedicoService;

  public medico: IMedico = new Medico();
  public pacientes: IPaciente[] = [];

  public userAccount: IUser;
  public isSaving = false;
  public authorities: any[] = [];
  public authoritiesOptions: any[] = [];
  public languages: any = this.$store.getters.languages;
  public paciente: IPaciente = new Paciente();
  public familiar: IFamiliar = new Familiar();
  public currentLanguage = '';
  public parentescoValues: string[] = Object.keys(Parentesco);

  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.initAuthorities();
      if (to.params.userId) {
        vm.initWhenEditing(to.params.userId);
      }
    });
  }

  public constructor() {
    super();
    this.userAccount = new User();
    this.userAccount.authorities = [];
    this.authoritiesOptions = [];
    this.userAccount.activated = true;
  }

  public initAuthorities() {
    this.userManagementService()
      .retrieveAuthorities()
      .then(_res => {
        this.authorities = _res.data;
        this.initAuthoritiesOptions();
      });
  }

  public initWhenEditing(userId: number): void {
    this.userManagementService()
      .get(userId)
      .then(res => {
        this.userAccount = res.data;
      });
    this.pacienteService()
      .retrieve()
      .then(res => {
        this.paciente = res.data.find(obj => {
          return obj.username.login === ''+userId
        });
      });
    this.familiarService()
      .retrieve()
      .then(res => {
          this.familiar = res.data.find(obj => {
            return obj.username.login === ''+userId
          })
        this.familiar.emailPaciente = this.familiar.paciente.username.email;
      });
    this.medicoService()
      .retrieve()
      .then(res => {
        this.medico = res.data.find(obj => {
          return obj.username.login === ''+userId
        })
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

  public previousState(): void {
    (<any>this).$router.go(-1);
  }

  public save(): void {
    this.isSaving = true;
    this.userAccount.langKey = 'es';

    if (this.userAccount.id) {
      this.userManagementService()
        .update(this.userAccount)
        .then(res => {
          this.returnToList();
          (this.$root as any).$bvToast.toast(this.getMessageFromHeader(res).toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = true;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      if (this.userAccount.authorities.includes('ROLE_PACIENTE')) {
        this.userAccount.paciente = this.paciente;
      }
      else if (this.userAccount.authorities.includes('ROLE_FAMILIAR')) {
        this.userAccount.familiar = this.familiar;
        this.userAccount.emailPaciente = this.familiar.emailPaciente;
      }
      else if (this.userAccount.authorities.includes('ROLE_MEDICO')) {
        this.userAccount.medico = this.medico;
      }
      console.log(this.userAccount)
      this.userManagementService()
        .create(this.userAccount)
        .then(res => {
          this.returnToList();
          (this.$root as any).$bvToast.toast(this.getMessageFromHeader(res).toString(), {
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

  private returnToList(): void {
    this.isSaving = false;
    (<any>this).$router.go(-1);
  }

  private getMessageFromHeader(res: any): any {
    return this.$t(res.headers['x-homecareapp-alert'], {
      param: decodeURIComponent(res.headers['x-homecareapp-params'].replace(/\+/g, ' ')),
    });
  }

  public authoritySelect($event) {
    this.userAccount.authorities = [];
    if ($event.target.value == 'ROLE_PACIENTE') {
      this.userAccount.authorities[0] = 'ROLE_USER';
      this.userAccount.authorities[1] = 'ROLE_PACIENTE';
    }

    if ($event.target.value == 'ROLE_FAMILIAR') {
      this.userAccount.authorities[0] = 'ROLE_USER';
      this.userAccount.authorities[1] = 'ROLE_FAMILIAR';
    }

    if ($event.target.value == 'ROLE_MEDICO') {
      this.userAccount.authorities[0] = 'ROLE_USER';
      this.userAccount.authorities[1] = 'ROLE_MEDICO';
    }

    if ($event.target.value == 'ROLE_ENFERMERO') {
      this.userAccount.authorities[0] = 'ROLE_USER';
      this.userAccount.authorities[1] = 'ROLE_MEDICO';
      this.userAccount.authorities[2] = 'ROLE_ENFERMERO';
    }

    if ($event.target.value == 'ROLE_AUXILIAR') {
      this.userAccount.authorities[0] = 'ROLE_USER';
      this.userAccount.authorities[1] = 'ROLE_ADMIN';
      this.userAccount.authorities[2] = 'ROLE_AUXILIAR';
    }
  }

  public initAuthoritiesOptions(): any {
    this.authoritiesOptions = [];
    for (let i = 0;  i<  this.authorities.length; i++) {
      if (this.authorities[i] === 'ROLE_ADMIN' || this.authorities[i] === 'ROLE_USER'){
        this.authoritiesOptions[i] = {label: this.authorities[i], value: this.authorities[i], disabled: true};
      } else {
        this.authoritiesOptions[i] = {label: this.authorities[i], value: this.authorities[i]};
      }
    }
  }

  public formIsValid() {
    if (this.userAccount.authorities.includes('ROLE_PACIENTE')) {
      return !this.$v.userAccount.$invalid && !this.$v.paciente.$invalid;
    } else if (this.userAccount.authorities.includes('ROLE_FAMILIAR')) {
      return !this.$v.userAccount.$invalid && !this.$v.familiar.$invalid;
    } else if (this.userAccount.authorities.includes('ROLE_MEDICO')) {
      return !this.$v.userAccount.$invalid && !this.$v.medico.$invalid;
    }
  }
}
