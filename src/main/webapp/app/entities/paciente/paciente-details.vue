<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="paciente">
        <h2 class="jh-entity-heading" data-cy="pacienteDetailsHeading">
          <span v-text="$t('homecareApp.paciente.detail.title')">Paciente</span> {{ paciente.username.login }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Nombres</span>
          </dt>
          <dd>
            <span>{{ paciente.username.firstName }} {{ paciente.username.lastName }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.paciente.cedula')">Cedula</span>
          </dt>
          <dd>
            <span>{{ paciente.cedula }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.paciente.ciudad')">Ciudad</span>
          </dt>
          <dd>
            <span>{{ paciente.ciudad }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.paciente.direccion')">Direccion</span>
          </dt>
          <dd>
            <span>{{ paciente.direccion }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.paciente.telefono')">Telefono</span>
          </dt>
          <dd>
            <span>{{ paciente.telefono }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.paciente.fechaNacimiento')">Fecha Nacimiento</span>
          </dt>
          <dd>
            <span>{{ paciente.fechaNacimiento }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.paciente.familiar')">Familiar</span>
          </dt>
          <dd>
            <div v-if="paciente.familiar">
              <router-link :to="{ name: 'FamiliarView', params: { familiarId: paciente.familiar.id } }">{{
                paciente.familiar.username.firstName + ' ' + paciente.familiar.username.lastName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.paciente.medico')">Medico</span>
          </dt>
          <dd>
            <div v-if="paciente.medico">
              <router-link :to="{ name: 'MedicoView', params: { medicoId: paciente.medico.id } }">{{ paciente.medico.username.firstName }} {{ paciente.medico.username.lastName }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./paciente-details.component.ts"></script>
