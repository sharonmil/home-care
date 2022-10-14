<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="signosVitales">
        <h2 class="jh-entity-heading" data-cy="signosVitalesDetailsHeading">
          <span v-text="$t('homecareApp.signosVitales.detail.title')">SignosVitales</span> {{ signosVitales.id }}
        </h2>
        <dl class="row jh-entity-details">

          <dt v-if="signosVitales.paciente && hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_AUXILIAR')">
            <span v-text="$t('homecareApp.signosVitales.paciente')">Paciente</span>
          </dt>
          <dd v-if="signosVitales.paciente && hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_AUXILIAR')">
            <div v-if="signosVitales.paciente">
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: signosVitales.paciente.id } }">{{
                  signosVitales.paciente.username.firstName + ' ' + signosVitales.paciente.username.lastName
                }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.signosVitales.familiar')">Familiar</span>
          </dt>
          <dd>
            {{ signosVitales.paciente.familiar ? signosVitales.paciente.familiar.username.firstName + ' ' + signosVitales.paciente.familiar.username.lastName : '' }}
          </dd>
          <dt>
            <span v-text="$t('homecareApp.signosVitales.medico')">Medico</span>
          </dt>
          <dd>
            {{ signosVitales.paciente && signosVitales.paciente.medico &&  signosVitales.paciente.medico.username ? signosVitales.paciente.medico.username.firstName + ' ' + signosVitales.paciente.medico.username.lastName : '' }}
          </dd>
          <dt>
            <span v-text="$t('homecareApp.signosVitales.oximetria')">Oximetria</span>
          </dt>
          <dd>
            <span>{{ signosVitales.oximetria }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.signosVitales.frecuenciaRespiratoria')">Frecuencia Respiratoria</span>
          </dt>
          <dd>
            <span>{{ signosVitales.frecuenciaRespiratoria }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.signosVitales.frecuenciaCardiaca')">Frecuencia Cardiaca</span>
          </dt>
          <dd>
            <span>{{ signosVitales.frecuenciaCardiaca }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.signosVitales.presion')">Presion</span>
          </dt>
          <dd>
            <span>{{ signosVitales.presion }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.signosVitales.glicemia')">Glicemia</span>
          </dt>
          <dd>
            <span>{{ signosVitales.glicemia }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.signosVitales.createdDate')">Created Date</span>
          </dt>
          <dd>
            <span>{{ signosVitales.createdDate }}</span>
          </dd>

        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./signos-vitales-details.component.ts"></script>
