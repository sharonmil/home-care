import Vue from 'vue';
import { Component, Inject } from 'vue-property-decorator';
import { email, helpers, maxLength, minLength, required, sameAs } from 'vuelidate/lib/validators';
import LoginService from '@/account/login.service';
import RegisterService from '@/account/register/register.service';
import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from '@/constants';
import {Familiar, IFamiliar} from "@/shared/model/familiar.model";
import {Parentesco} from "@/shared/model/enumerations/parentesco.model";
import UserService from "@/entities/user/user.service";
import {IPaciente, Paciente} from "@/shared/model/paciente.model";



const loginPattern = helpers.regex('alpha', /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/);
const validations: any = {
  registerAccount: {
    login: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(50),
      pattern: loginPattern,
    },
    firstName: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(50),
    },
    lastName: {
      required,
      minLength: minLength(1),
      maxLength: maxLength(50),
    },
    email: {
      required,
      minLength: minLength(5),
      maxLength: maxLength(254),
      email,
    },
    password: {
      required,
      minLength: minLength(4),
      maxLength: maxLength(254),
    },
    userType: {
      required,
    }
  },
  confirmPassword: {
    required,
    minLength: minLength(4),
    maxLength: maxLength(50),
    // prettier-ignore
    sameAsPassword: sameAs(function() {
      return this.registerAccount.password;
    }),
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
};

@Component({
  validations,
})
export default class Register extends Vue {
  @Inject('registerService') private registerService: () => RegisterService;
  @Inject('loginService') private loginService: () => LoginService;
  @Inject('userService') public userService: () => UserService;

  public registerAccount: any = {
    login: undefined,
    email: undefined,
    password: undefined,
    userType: undefined,
    firstName: undefined,
    lastName: undefined,
    telefono: undefined
  };

  public familiar: IFamiliar = new Familiar();

  public paciente: IPaciente = new Paciente();

  public parentescoValues: string[] = Object.keys(Parentesco);
  public currentLanguage = '';

  public users: Array<any> = [];
  public confirmPassword: any = null;
  public error = '';
  public errorEmailExists = '';
  public errorUserExists = '';
  public success = false;

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public register(): void {
    this.error = null;
    this.errorUserExists = null;
    this.errorEmailExists = null;
    this.registerAccount.langKey = this.$store.getters.currentLanguage;
    if (this.registerAccount.userType == 'Paciente') {
      this.registerAccount.paciente = this.paciente;
    }
    else {
      this.registerAccount.familiar = this.familiar;
      this.registerAccount.emailPaciente = this.familiar.emailPaciente;
    }
    this.registerService()
      .processRegistration(this.registerAccount)
      .then(() => {
        this.success = true;
      })
      .catch(error => {
        this.success = null;
        if (error.response.status === 400 && error.response.data.type === LOGIN_ALREADY_USED_TYPE) {
          this.errorUserExists = 'ERROR';
        } else if (error.response.status === 400 && error.response.data.type === EMAIL_ALREADY_USED_TYPE) {
          this.errorEmailExists = 'ERROR';
        } else {
          this.error = 'ERROR';
        }
      });
  }

  public openLogin(): void {
    this.loginService().openLogin((<any>this).$root);
  }

  public formIsValid() {
    if (this.registerAccount.userType === 'Paciente') {
        return !this.$v.registerAccount.$invalid && !this.$v.paciente.$invalid;
    } else if (this.registerAccount.userType === 'Familiar') {
        return !this.$v.registerAccount.$invalid && !this.$v.familiar.$invalid;
    }
  }

  public initRelationships(): void {
    this.userService()
      .retrieve()
      .then(res => {
        this.users = res.data;
      });
  }
}
