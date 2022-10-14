import { Component, Vue, Inject } from 'vue-property-decorator';

import { IFamiliar } from '@/shared/model/familiar.model';
import FamiliarService from './familiar.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class FamiliarDetails extends Vue {
  @Inject('familiarService') private familiarService: () => FamiliarService;
  @Inject('alertService') private alertService: () => AlertService;

  public familiar: IFamiliar = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.familiarId) {
        vm.retrieveFamiliar(to.params.familiarId);
      }
    });
  }

  public retrieveFamiliar(familiarId) {
    this.familiarService()
      .find(familiarId)
      .then(res => {
        this.familiar = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
