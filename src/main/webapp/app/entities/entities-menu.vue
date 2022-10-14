<template>
  <div>
    <b-dropdown-item to="/paciente" >
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.paciente')">Paciente</span>
    </b-dropdown-item>
    <b-dropdown-item to="/familiar" v-if="hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_FAMILIAR')">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.familiar')">Familiar</span>
    </b-dropdown-item>
    <b-dropdown-item to="/medico" v-if="hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_MEDICO')">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.medico')">Médico</span>
    </b-dropdown-item>
    <b-dropdown-item to="/signos-vitales">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.signosVitales')">Signos Vitales</span>
    </b-dropdown-item>
    <b-dropdown-item to="/historia-clinica">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.historiaClinica')">Historia Clinica</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
