import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ISignosVitales } from '@/shared/model/signos-vitales.model';

import SignosVitalesService from './signos-vitales.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from "@/account/account.service";

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class SignosVitales extends Vue {
  @Inject('signosVitalesService') private signosVitalesService: () => SignosVitalesService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;

  private removeId: number = null;

  public signosVitales: ISignosVitales[] = [];

  public isFetching = false;

  private hasAnyAuthorityValues = {};

  private modelFechaDesde: string = '';
  private modelFechaHasta: string = '';

  public mounted(): void {
    this.retrieveAllSignosVitaless();
  }

  public clear(): void {
    this.retrieveAllSignosVitaless();
  }

  public retrieveAllSignosVitaless(): void {
    this.isFetching = true;
    this.signosVitalesService()
      .retrieve()
      .then(
        res => {
          this.signosVitales = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public retrieveRangeSignosVitales(): void {
    this.isFetching = true;
    this.signosVitalesService()
      .retrieveRange(this.modelFechaDesde, this.modelFechaHasta)
      .then(
        res => {
          this.signosVitales = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: ISignosVitales): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeSignosVitales(): void {
    this.signosVitalesService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('homecareApp.signosVitales.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllSignosVitaless();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public get username(): string {
    return this.$store.getters.account?.login ?? '';
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
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

  public search() : void {
     this.retrieveRangeSignosVitales();
  }
}
