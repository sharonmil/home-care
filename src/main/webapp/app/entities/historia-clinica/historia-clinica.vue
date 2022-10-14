<template>
  <div>
    <h2 id="page-heading" data-cy="HistoriaClinicaHeading">
      <span v-text="$t('homecareApp.historiaClinica.home.title')" id="historia-clinica-heading">Historia Clinicas</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('homecareApp.historiaClinica.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link v-if="hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_MEDICO')"
          :to="{ name: 'HistoriaClinicaCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-historia-clinica"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('homecareApp.historiaClinica.home.createLabel')"> Create a new Historia Clinica </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && historiaClinicas && historiaClinicas.length === 0">
      <span>No Historias Clinicas Encontradas</span>
    </div>
    <div class="table-responsive" v-if="historiaClinicas && historiaClinicas.length > 0">
      <table class="table table-striped" aria-describedby="historiaClinicas">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('homecareApp.historiaClinica.paciente')">Paciente</span></th>
            <th scope="row"><span v-text="$t('homecareApp.historiaClinica.familiar')">Familiar</span></th>
            <th scope="row"><span v-text="$t('homecareApp.historiaClinica.medico')">Medico</span></th>
            <th scope="row"><span v-text="$t('homecareApp.historiaClinica.sugerencia')">Sugerencia</span></th>
            <th scope="row"><span v-text="$t('homecareApp.historiaClinica.diagnostico')">Diagnostico</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="hasAnyAuthority('ROLE_ADMIN') ||
                    (historiaClinica.paciente && historiaClinica.paciente.username &&
                    historiaClinica.paciente.username.login == username) ||
                    (historiaClinica.paciente &&
                     historiaClinica.paciente.medico &&
                     historiaClinica.paciente.medico.username &&
                     historiaClinica.paciente.medico.username.login == username) ||
                    (historiaClinica.paciente &&
                     historiaClinica.paciente.familiar &&
                     historiaClinica.paciente.familiar.username &&
                     historiaClinica.paciente.familiar.username.login == username)"
             v-for="historiaClinica in historiaClinicas" :key="historiaClinica.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'HistoriaClinicaView', params: { historiaClinicaId: historiaClinica.id } }">{{
                historiaClinica.id
              }}</router-link>
            </td>
            <td>
              <div v-if="historiaClinica.paciente && historiaClinica.paciente.username">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: historiaClinica.paciente.id } }">{{
                    historiaClinica.paciente.username.firstName + ' ' + historiaClinica.paciente.username.lastName
                  }}</router-link>
              </div>

            </td>
            <td>
              {{ historiaClinica.paciente && historiaClinica.paciente.familiar && historiaClinica.paciente.familiar.username? historiaClinica.paciente.familiar.username.firstName + ' ' + historiaClinica.paciente.familiar.username.lastName : '' }}
            </td>
            <td>
              {{ historiaClinica.paciente && historiaClinica.paciente.medico && historiaClinica.paciente.medico.username ? historiaClinica.paciente.medico.username.firstName + ' ' + historiaClinica.paciente.medico.username.lastName : '' }}
            </td>

            <td>{{ historiaClinica.sugerencia }}</td>
            <td>{{ historiaClinica.diagnostico }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'HistoriaClinicaView', params: { historiaClinicaId: historiaClinica.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link v-if="hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_MEDICO')"
                  :to="{ name: 'HistoriaClinicaEdit', params: { historiaClinicaId: historiaClinica.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button v-if="hasAnyAuthority('ROLE_ADMIN')"
                  v-on:click="prepareRemove(historiaClinica)"
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
        ><span
          id="homecareApp.historiaClinica.delete.question"
          data-cy="historiaClinicaDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-historiaClinica-heading" v-text="$t('homecareApp.historiaClinica.delete.question', { id: removeId })">
          Are you sure you want to delete this Historia Clinica?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-historiaClinica"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeHistoriaClinica()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./historia-clinica.component.ts"></script>
