<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="historiaClinica">
        <h2 class="jh-entity-heading" data-cy="historiaClinicaDetailsHeading">
          <span v-text="$t('homecareApp.historiaClinica.detail.title')">HistoriaClinica</span> {{ historiaClinica.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('homecareApp.historiaClinica.sugerencia')">Sugerencia</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.sugerencia }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.historiaClinica.diagnostico')">Diagnostico</span>
          </dt>
          <dd>
            <span>{{ historiaClinica.diagnostico }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.historiaClinica.paciente')">Paciente</span>
          </dt>
          <dd>
            <div v-if="historiaClinica.paciente && historiaClinica.paciente.username">
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: historiaClinica.paciente.id } }">{{
                historiaClinica.paciente.username.firstName + ' ' +  historiaClinica.paciente.username.lastName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.historiaClinica.medico')">Medico</span>
          </dt>
          <dd>
            {{ historiaClinica.paciente && historiaClinica.paciente.medico ?
            historiaClinica.paciente.medico.username.firstName + ' ' + historiaClinica.paciente.medico.username.lastName   : '' }}
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./historia-clinica-details.component.ts"></script>
