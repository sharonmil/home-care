<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="familiar">
        <h2 class="jh-entity-heading" data-cy="familiarDetailsHeading">
          <span v-text="$t('homecareApp.familiar.detail.title')">Familiar</span> {{ familiar.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('homecareApp.familiar.cedula')">Cedula</span>
          </dt>
          <dd>
            <span>{{ familiar.cedula }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.familiar.ciudad')">Ciudad</span>
          </dt>
          <dd>
            <span>{{ familiar.ciudad }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.familiar.direccion')">Direccion</span>
          </dt>
          <dd>
            <span>{{ familiar.direccion }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.familiar.telefono')">Telefono</span>
          </dt>
          <dd>
            <span>{{ familiar.telefono }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.familiar.fechaNacimiento')">Fecha Nacimiento</span>
          </dt>
          <dd>
            <span>{{ familiar.fechaNacimiento }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.familiar.parentesco')">Parentesco</span>
          </dt>
          <dd>
            <span v-text="$t('homecareApp.Parentesco.' + familiar.parentesco)">{{ familiar.parentesco }}</span>
          </dd>
          <dt>
            <span v-text="$t('homecareApp.familiar.paciente')">Paciente</span>
          </dt>
          <dd>
            {{ familiar.paciente ? familiar.paciente.username.firstName + ' ' + familiar.paciente.username.lastName  : '' }}
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>

      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./familiar-details.component.ts"></script>
