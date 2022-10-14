import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IFamiliar } from '@/shared/model/familiar.model';

import FamiliarService from './familiar.service';
import AlertService from '@/shared/alert/alert.service';
import AccountService from "@/account/account.service";

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Familiar extends Vue {
  @Inject('familiarService') private familiarService: () => FamiliarService;
  @Inject('alertService') private alertService: () => AlertService;
  @Inject('accountService') private accountService: () => AccountService;

  private removeId: number = null;

  public familiars: IFamiliar[] = [];

  public isFetching = false;

  private hasAnyAuthorityValues = {};

  public mounted(): void {
    this.retrieveAllFamiliars();
  }

  public clear(): void {
    this.retrieveAllFamiliars();
  }

  public retrieveAllFamiliars(): void {
    this.isFetching = true;
    this.familiarService()
      .retrieve()
      .then(
        res => {
          this.familiars = res.data;
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

  public prepareRemove(instance: IFamiliar): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeFamiliar(): void {
    this.familiarService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('homecareApp.familiar.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllFamiliars();
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
