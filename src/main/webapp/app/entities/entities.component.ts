import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import PacienteService from './paciente/paciente.service';
import FamiliarService from './familiar/familiar.service';
import MedicoService from './medico/medico.service';
import SignosVitalesService from './signos-vitales/signos-vitales.service';
import HistoriaClinicaService from './historia-clinica/historia-clinica.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('pacienteService') private pacienteService = () => new PacienteService();
  @Provide('familiarService') private familiarService = () => new FamiliarService();
  @Provide('medicoService') private medicoService = () => new MedicoService();
  @Provide('signosVitalesService') private signosVitalesService = () => new SignosVitalesService();
  @Provide('historiaClinicaService') private historiaClinicaService = () => new HistoriaClinicaService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
