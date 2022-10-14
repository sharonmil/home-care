<template>
  <div>
    <h2 id="page-heading" data-cy="MedicoHeading">
      <span v-text="$t('homecareApp.medico.home.title')" id="medico-heading">Medicos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('homecareApp.medico.home.refreshListLabel')">Refresh List</span>
        </button>

      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && medicos && medicos.length === 0">
      <span v-text="$t('homecareApp.medico.home.notFound')">No medicos found</span>
    </div>
    <div class="table-responsive" v-if="medicos && medicos.length > 0">
      <table class="table table-striped" aria-describedby="medicos">
        <thead>
          <tr>
            <th scope="row"><span>Login</span></th>
            <th scope="row"><span v-text="$t('homecareApp.medico.cedula')">Cedula</span></th>
            <th scope="row"><span v-text="$t('homecareApp.paciente.nombres')">Nombres</span></th>
            <th scope="row"><span v-text="$t('homecareApp.medico.ciudad')">Ciudad</span></th>
            <th scope="row"><span v-text="$t('homecareApp.medico.direccion')">Direccion</span></th>
            <th scope="row"><span v-text="$t('homecareApp.medico.telefono')">Telefono</span></th>
            <th scope="row"><span v-text="$t('homecareApp.medico.fechaNacimiento')">Fecha Nacimiento</span></th>
            <th scope="row"><span v-text="$t('homecareApp.medico.especialidad')">Especialidad</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="medico in medicos" :key="medico.id" data-cy="entityTable"
              v-if="hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_AUXILIAR') || (medico.username && medico.username.login == username)"
          >
            <td>
              <router-link :to="{ name: 'MedicoView', params: { medicoId: medico.id } }">{{ medico.username.login }}</router-link>
            </td>
            <td>{{ medico.cedula }}</td>
            <td v-if="medico.username">{{ medico.username.firstName }} {{ medico.username.lastName }}</td> <td v-else></td>
            <td>{{ medico.ciudad }}</td>
            <td>{{ medico.direccion }}</td>
            <td>{{ medico.telefono }}</td>
            <td>{{ medico.fechaNacimiento }}</td>
            <td v-if="medico.especialidad">{{ medico.especialidad }}</td><td v-else> ENFERMERO </td>

            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'MedicoView', params: { medicoId: medico.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MedicoEdit', params: { medicoId: medico.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button v-if="hasAnyAuthority('ROLE_ADMIN')"
                  v-on:click="prepareRemove(medico)"
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
        ><span id="homecareApp.medico.delete.question" data-cy="medicoDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-medico-heading" v-text="$t('homecareApp.medico.delete.question', { id: removeId })">
          Are you sure you want to delete this Medico?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button v-if="hasAnyAuthority('ROLE_ADMIN')"
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-medico"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeMedico()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./medico.component.ts"></script>
