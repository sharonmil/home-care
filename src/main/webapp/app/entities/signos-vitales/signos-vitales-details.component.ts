import { Component, Vue, Inject } from 'vue-property-decorator';

import { ISignosVitales } from '@/shared/model/signos-vitales.model';
import SignosVitalesService from './signos-vitales.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from "@/account/account.service";

@Component
export default class SignosVitalesDetails extends Vue {
  @Inject('signosVitalesService') private signosVitalesService: () => SignosVitalesService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;

  public signosVitales: ISignosVitales = {};

  private hasAnyAuthorityValues = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.signosVitalesId) {
        vm.retrieveSignosVitales(to.params.signosVitalesId);
      }
    });
  }

  public retrieveSignosVitales(signosVitalesId) {
    this.signosVitalesService()
      .find(signosVitalesId)
      .then(res => {
        this.signosVitales = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }
  public get username(): string {
    return this.$store.getters.account?.login ?? '';
  }

  public previousState() {
    this.$router.go(-1);
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
