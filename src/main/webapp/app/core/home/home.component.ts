import Component from 'vue-class-component';
import { Inject, Vue } from 'vue-property-decorator';
import LoginService from '@/account/login.service';

@Component
export default class Home extends Vue {
  @Inject('loginService')
  private loginService: () => LoginService;

  public openLogin(): void {
    this.loginService().openLogin((<any>this).$root);
  }

  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }

  public get username(): string {
    return this.$store.getters.account?.login ?? '';
  }

  public get firstName(): string {
    return this.$store.getters.account?.firstName ?? '';
  }

  public get lastName(): string {
    return this.$store.getters.account?.lastName ?? '';
  }

  public get email(): string {
    return this.$store.getters.account?.email ?? '';
  }
}