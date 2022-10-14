<template>
  <div>
    <h2 id="page-heading" data-cy="SignosVitalesHeading">
      <span v-text="$t('homecareApp.signosVitales.home.title')" id="signos-vitales-heading">Signos Vitales</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('homecareApp.signosVitales.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link v-if="hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_PACIENTE') || hasAnyAuthority('ROLE_FAMILIAR')"
          :to="{ name: 'SignosVitalesCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-signos-vitales"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('homecareApp.signosVitales.home.createLabel')"> Create a new Signos Vitales </span>
          </button>
        </router-link>
      </div>
    </h2>
    <div id="date-search-input" class="form-group">
        <label class="form-control-label" style="width:30%;" for="input-fechadesde" >Fecha Desde</label>
        <b-input-group class="mb-3" style="width:30%;">
          <b-input-group-prepend>
            <b-form-datepicker
              aria-controls="input-fechadesde"
              v-model="modelFechaDesde"
              name="fechadesde"
              class="form-control"
              button-only
              today-button
              reset-button
              close-button
            >
            </b-form-datepicker>
          </b-input-group-prepend>
          <input
            id="input-fechadesde"
            type="text"
            class="form-control"
            name="fechadesde"
            required v-model="modelFechaDesde"/>
        </b-input-group>

      <label class="form-control-label" style="width:30%;" for="input-fechahast" >Fecha Hasta</label>
      <b-input-group class="mb-3" style="width:30%;">
        <b-input-group-prepend>
          <b-form-datepicker
            aria-controls="input-fechahasta"
            v-model="modelFechaHasta"
            name="fechahasta"
            class="form-control"
            button-only
            today-button
            reset-button
            close-button>
          </b-form-datepicker>
        </b-input-group-prepend>
        <input
          id="input-fechahasta"
          type="text"
          class="form-control"
          name="fechahasta"
          required v-model="modelFechaHasta"/>
      </b-input-group>

      <div class="btn-group" style="width:30%;">
        <router-link
          :to="{ name: 'SignosVitales', params: { fechaDesde: modelFechaDesde, fechaHasta: modelFechaHasta } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="search" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
            <font-awesome-icon icon="search"></font-awesome-icon>
            <span class="d-none d-md-inline">Consultar</span>
          </button>
        </router-link>
      </div>
    </div>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && signosVitales && signosVitales.length === 0">
      <span v-text="$t('homecareApp.signosVitales.home.notFound')">No signosVitales found</span>
    </div>
    <div class="table-responsive" v-if="signosVitales && signosVitales.length > 0">
      <table class="table table-striped" aria-describedby="signosVitales">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span>Paciente</span></th>
            <th scope="row"><span v-text="$t('homecareApp.signosVitales.oximetria')">Oximetria</span></th>
            <th scope="row"><span v-text="$t('homecareApp.signosVitales.frecuenciaRespiratoria')">Frecuencia Respiratoria</span></th>
            <th scope="row"><span v-text="$t('homecareApp.signosVitales.frecuenciaCardiaca')">Frecuencia Cardiaca</span></th>
            <th scope="row"><span v-text="$t('homecareApp.signosVitales.presion')">Presion</span></th>
            <th scope="row"><span v-text="$t('homecareApp.signosVitales.glicemia')">Glicemia</span></th>
            <th scope="row"><span v-text="$t('homecareApp.signosVitales.createdDate')">Created Date</span></th>
            <th scope="row"><span v-text="$t('homecareApp.signosVitales.familiar')">Familiar</span></th>
            <th scope="row"><span v-text="$t('homecareApp.signosVitales.medico')">Medico</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="signosVitales in signosVitales" :key="signosVitales.id" data-cy="entityTable"
              v-if="hasAnyAuthority('ROLE_ADMIN') ||
              (signosVitales.paciente && signosVitales.paciente.username && signosVitales.paciente.username.login == username) ||
              (signosVitales.paciente && signosVitales.paciente.familiar && signosVitales.paciente.familiar.username.login == username) ||
              (signosVitales.paciente && signosVitales.paciente.medico && signosVitales.paciente.medico.username.login == username)"
          >
            <td>
              <router-link :to="{ name: 'SignosVitalesView', params: { signosVitalesId: signosVitales.id } }">{{
                signosVitales.id
              }}</router-link>
            </td>
            <td>
              <div v-if="signosVitales.paciente && signosVitales.paciente.username">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: signosVitales.paciente.id } }">
                  {{ signosVitales.paciente.username.firstName }} {{ signosVitales.paciente.username.lastName }}
                  </router-link>
              </div>
            </td>
            <td>{{ signosVitales.oximetria }}</td>
            <td>{{ signosVitales.frecuenciaRespiratoria }}</td>
            <td>{{ signosVitales.frecuenciaCardiaca }}</td>
            <td>{{ signosVitales.presion }}</td>
            <td>{{ signosVitales.glicemia }}</td>
            <td>{{ signosVitales.createdDate }}</td>

            <td>
              <div v-if="signosVitales.paciente && signosVitales.paciente.familiar">
                <router-link :to="{ name: 'FamiliarView', params: { familiarId: signosVitales.paciente.familiar.id } }">
                  {{ signosVitales.paciente.familiar && signosVitales.paciente.familiar.username ? signosVitales.paciente.familiar.username.firstName + ' ' +  signosVitales.paciente.familiar.username.lastName: '' }}
                </router-link>
               </div>
            </td>

            <td>
              <div v-if="signosVitales.paciente">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: signosVitales.paciente.id } }">{{
                    signosVitales.paciente && signosVitales.paciente.medico && signosVitales.paciente.medico.username ? signosVitales.paciente.medico.username.firstName + ' ' + signosVitales.paciente.medico.username.lastName : ''
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SignosVitalesView', params: { signosVitalesId: signosVitales.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link v-if="hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_PACIENTE') || hasAnyAuthority('ROLE_FAMILIAR')"
                  :to="{ name: 'SignosVitalesEdit', params: { signosVitalesId: signosVitales.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button v-if="hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_AUXILIAR')"
                  v-on:click="prepareRemove(signosVitales)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="homecareApp.signosVitales.delete.question" data-cy="signosVitalesDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-signosVitales-heading" v-text="$t('homecareApp.signosVitales.delete.question', { id: removeId })">
          Are you sure you want to delete this Signos Vitales?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-signosVitales"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeSignosVitales()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./signos-vitales.component.ts"></script>
