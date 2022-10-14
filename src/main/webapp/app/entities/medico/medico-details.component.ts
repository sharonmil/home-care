import { Component, Vue, Inject } from 'vue-property-decorator';

import { IMedico } from '@/shared/model/medico.model';
import MedicoService from './medico.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class MedicoDetails extends Vue {
  @Inject('medicoService') private medicoService: () => MedicoService;
  @Inject('alertService') private alertService: () => AlertService;

  public medico: IMedico = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.medicoId) {
        vm.retrieveMedico(to.params.medicoId);
      }
    });
  }

  public retrieveMedico(medicoId) {
    this.medicoService()
      .find(medicoId)
      .then(res => {
        this.medico = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
