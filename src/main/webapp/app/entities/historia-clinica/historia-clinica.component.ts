import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IHistoriaClinica } from '@/shared/model/historia-clinica.model';

import HistoriaClinicaService from './historia-clinica.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from "@/account/account.service";

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class HistoriaClinica extends Vue {
  @Inject('historiaClinicaService') private historiaClinicaService: () => HistoriaClinicaService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;

  private removeId: number = null;

  public historiaClinicas: IHistoriaClinica[] = [];

  public isFetching = false;

  private hasAnyAuthorityValues = {};

  public mounted(): void {
    this.retrieveAllHistoriaClinicas();
  }

  public clear(): void {
    this.retrieveAllHistoriaClinicas();
  }

  public retrieveAllHistoriaClinicas(): void {
    this.isFetching = true;
    this.historiaClinicaService()
      .retrieve()
      .then(
        res => {
          this.historiaClinicas = res.data;
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

  public prepareRemove(instance: IHistoriaClinica): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeHistoriaClinica(): void {
    this.historiaClinicaService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('homecareApp.historiaClinica.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllHistoriaClinicas();
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
}
