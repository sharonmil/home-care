import { Component, Vue, Inject } from 'vue-property-decorator';

import { IHistoriaClinica } from '@/shared/model/historia-clinica.model';
import HistoriaClinicaService from './historia-clinica.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class HistoriaClinicaDetails extends Vue {
  @Inject('historiaClinicaService') private historiaClinicaService: () => HistoriaClinicaService;
  @Inject('alertService') private alertService: () => AlertService;

  public historiaClinica: IHistoriaClinica = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.historiaClinicaId) {
        vm.retrieveHistoriaClinica(to.params.historiaClinicaId);
      }
    });
  }

  public retrieveHistoriaClinica(historiaClinicaId) {
    this.historiaClinicaService()
      .find(historiaClinicaId)
      .then(res => {
        this.historiaClinica = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
