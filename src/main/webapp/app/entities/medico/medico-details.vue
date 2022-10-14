<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="medico">
        <h2 class="jh-entity-heading" data-cy="medicoDetailsHeading">
          <span v-text="$t('homecareApp.medico.detail.title')">Medico</span> {{ medico.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('homecareApp.medico.cedula')">Cedula</span>
          </dt>
          <dd>
            <span>{{ medico.cedula }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.medico.ciudad')">Ciudad</span>
          </dt>
          <dd>
            <span>{{ medico.ciudad }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.medico.direccion')">Direccion</span>
          </dt>
          <dd>
            <span>{{ medico.direccion }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.medico.telefono')">Telefono</span>
          </dt>
          <dd>
            <span>{{ medico.telefono }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.medico.fechaNacimiento')">Fecha Nacimiento</span>
          </dt>
          <dd>
            <span>{{ medico.fechaNacimiento }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.medico.especialidad')">Especialidad</span>
          </dt>
          <dd>
            <span v-if="medico.especialidad">{{ medico.especialidad }}</span><span v-else> ENFERMERO </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./medico-details.component.ts"></script>
