import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPaciente } from '@/shared/model/paciente.model';
import PacienteService from './paciente.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PacienteDetails extends Vue {
  @Inject('pacienteService') private pacienteService: () => PacienteService;
  @Inject('alertService') private alertService: () => AlertService;

  public paciente: IPaciente = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.pacienteId) {
        vm.retrievePaciente(to.params.pacienteId);
      }
    });
  }

  public retrievePaciente(pacienteId) {
    this.pacienteService()
      .find(pacienteId)
      .then(res => {
        this.paciente = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
