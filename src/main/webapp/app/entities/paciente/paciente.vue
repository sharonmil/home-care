<template>
  <div>
    <h2 id="page-heading" data-cy="PacienteHeading">
      <span v-text="$t('homecareApp.paciente.home.title')" id="paciente-heading">Pacientes</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('homecareApp.paciente.home.refreshListLabel')">Refresh List</span>
        </button>
        <div v-if="hasAnyAuthority('ROLE_ADMIN')">
        </div>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && pacientes && pacientes.length === 0">
      <span v-text="$t('homecareApp.paciente.home.notFound')">No pacientes found</span>
    </div>

    <div class="table-responsive" v-if="pacientes && pacientes.length > 0">
      <table class="table table-striped" aria-describedby="pacientes">
        <thead>
          <tr>
            <th scope="row"><span>Login</span></th>
            <th scope="row"><span v-text="$t('homecareApp.paciente.cedula')">Cedula</span></th>
            <th scope="row"><span v-text="$t('homecareApp.paciente.nombres')">Nombres</span></th>
            <th scope="row"><span v-text="$t('homecareApp.paciente.ciudad')">Ciudad</span></th>
            <th scope="row"><span>Dirección</span></th>
            <th scope="row"><span v-text="$t('homecareApp.paciente.telefono')">Telefono</span></th>
            <th scope="row"><span v-text="$t('homecareApp.paciente.fechaNacimiento')">Fecha Nacimiento</span></th>
            <th scope="row"><span v-text="$t('homecareApp.paciente.familiar')">Familiar</span></th>
            <th scope="row"><span v-text="$t('homecareApp.paciente.medico')">Medico</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="paciente in pacientes" :key="paciente.id" data-cy="entityTable"
              v-if="hasAnyAuthority('ROLE_ADMIN') ||
              (paciente.username && paciente.username.login == username) ||
               (paciente.familiar && paciente.familiar.username && paciente.familiar.username.login == username) ||
                (paciente.medico && paciente.medico.username && paciente.medico.username.login == username) "
          >
            <td>
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: paciente.id } }">{{ paciente.username.login }}</router-link>
            </td>
            <td>{{ paciente.cedula }}</td>
            <td v-if="paciente.username">{{ paciente.username.firstName }} {{ paciente.username.lastName }}</td> <td v-else></td>
            <td>{{ paciente.ciudad }}</td>
            <td>{{ paciente.direccion }}</td>
            <td>{{ paciente.telefono }}</td>
            <td>{{ paciente.fechaNacimiento }}</td>
            <td>
              <div v-if="paciente.familiar">
                <router-link :to="{ name: 'FamiliarView', params: { familiarId: paciente.familiar.id } }">{{
                  paciente.familiar.username.firstName + ' ' + paciente.familiar.username.lastName
                }}</router-link>
              </div>
            </td>

            <td>
              <div v-if="paciente.medico">
                <router-link :to="{ name: 'MedicoView', params: { medicoId: paciente.medico.id } }">{{ paciente.medico.username.firstName }} {{ paciente.medico.username.lastName }}</router-link>
              </div>
              <div v-else="paciente.medico">
                <router-link v-if="hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_AUXILIAR')"
                             :to="{ name: 'PacienteEdit', params: { pacienteId: paciente.id } }"> Asignar Profesional en Salud</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: paciente.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link v-if="hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_AUXILIAR')"
                  :to="{ name: 'PacienteEdit', params: { pacienteId: paciente.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button v-if="hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_AUXILIAR')"
                  v-on:click="prepareRemove(paciente)"
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
        ><span id="homecareApp.paciente.delete.question" data-cy="pacienteDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-paciente-heading" v-text="$t('homecareApp.paciente.delete.question', { id: removeId })">
          Are you sure you want to delete this Paciente?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-paciente"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removePaciente()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./paciente.component.ts"></script>
