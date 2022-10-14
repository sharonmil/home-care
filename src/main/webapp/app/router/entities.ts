import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const Paciente = () => import('@/entities/paciente/paciente.vue');
// prettier-ignore
const PacienteUpdate = () => import('@/entities/paciente/paciente-update.vue');
// prettier-ignore
const PacienteDetails = () => import('@/entities/paciente/paciente-details.vue');
// prettier-ignore
const Familiar = () => import('@/entities/familiar/familiar.vue');
// prettier-ignore
const FamiliarUpdate = () => import('@/entities/familiar/familiar-update.vue');
// prettier-ignore
const FamiliarDetails = () => import('@/entities/familiar/familiar-details.vue');
// prettier-ignore
const Medico = () => import('@/entities/medico/medico.vue');
// prettier-ignore
const MedicoUpdate = () => import('@/entities/medico/medico-update.vue');
// prettier-ignore
const MedicoDetails = () => import('@/entities/medico/medico-details.vue');
// prettier-ignore
const SignosVitales = () => import('@/entities/signos-vitales/signos-vitales.vue');
// prettier-ignore
const SignosVitalesUpdate = () => import('@/entities/signos-vitales/signos-vitales-update.vue');
// prettier-ignore
const SignosVitalesDetails = () => import('@/entities/signos-vitales/signos-vitales-details.vue');
// prettier-ignore
const HistoriaClinica = () => import('@/entities/historia-clinica/historia-clinica.vue');
// prettier-ignore
const HistoriaClinicaUpdate = () => import('@/entities/historia-clinica/historia-clinica-update.vue');
// prettier-ignore
const HistoriaClinicaDetails = () => import('@/entities/historia-clinica/historia-clinica-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'paciente',
      name: 'Paciente',
      component: Paciente,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente/new',
      name: 'PacienteCreate',
      component: PacienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente/:pacienteId/edit',
      name: 'PacienteEdit',
      component: PacienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'paciente/:pacienteId/view',
      name: 'PacienteView',
      component: PacienteDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'familiar',
      name: 'Familiar',
      component: Familiar,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'familiar/new',
      name: 'FamiliarCreate',
      component: FamiliarUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'familiar/:familiarId/edit',
      name: 'FamiliarEdit',
      component: FamiliarUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'familiar/:familiarId/view',
      name: 'FamiliarView',
      component: FamiliarDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medico',
      name: 'Medico',
      component: Medico,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medico/new',
      name: 'MedicoCreate',
      component: MedicoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medico/:medicoId/edit',
      name: 'MedicoEdit',
      component: MedicoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'medico/:medicoId/view',
      name: 'MedicoView',
      component: MedicoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'signos-vitales',
      name: 'SignosVitales',
      component: SignosVitales,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'signos-vitales/new',
      name: 'SignosVitalesCreate',
      component: SignosVitalesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'signos-vitales/:signosVitalesId/edit',
      name: 'SignosVitalesEdit',
      component: SignosVitalesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'signos-vitales/:signosVitalesId/view',
      name: 'SignosVitalesView',
      component: SignosVitalesDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historia-clinica',
      name: 'HistoriaClinica',
      component: HistoriaClinica,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historia-clinica/new',
      name: 'HistoriaClinicaCreate',
      component: HistoriaClinicaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historia-clinica/:historiaClinicaId/edit',
      name: 'HistoriaClinicaEdit',
      component: HistoriaClinicaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'historia-clinica/:historiaClinicaId/view',
      name: 'HistoriaClinicaView',
      component: HistoriaClinicaDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
