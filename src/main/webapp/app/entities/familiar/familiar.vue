<template>
  <div>
    <h2 id="page-heading" data-cy="FamiliarHeading">
      <span v-text="$t('homecareApp.familiar.home.title')" id="familiar-heading">Familiars</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('homecareApp.familiar.home.refreshListLabel')">Refresh List</span>
        </button>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && familiars && familiars.length === 0">
      <span v-text="$t('homecareApp.familiar.home.notFound')">No familiares found</span>
    </div>
    <div class="table-responsive" v-if="familiars && familiars.length > 0">
      <table class="table table-striped" aria-describedby="familiars">
        <thead>
          <tr>
            <th scope="row"><span>Login</span></th>
            <th scope="row"><span v-text="$t('homecareApp.familiar.cedula')">Cedula</span></th>
            <th scope="row"><span v-text="$t('homecareApp.paciente.nombres')">Nombres</span></th>
            <th scope="row"><span v-text="$t('homecareApp.familiar.ciudad')">Ciudad</span></th>
            <th scope="row"><span v-text="$t('homecareApp.familiar.direccion')">Direccion</span></th>
            <th scope="row"><span v-text="$t('homecareApp.familiar.telefono')">Telefono</span></th>
            <th scope="row"><span v-text="$t('homecareApp.familiar.fechaNacimiento')">Fecha Nacimiento</span></th>
            <th scope="row"><span v-text="$t('homecareApp.familiar.parentesco')">Parentesco</span></th>
            <th scope="row"><span v-text="$t('homecareApp.familiar.paciente')">Paciente</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="familiar in familiars" :key="familiar.id" data-cy="entityTable"
              v-if="hasAnyAuthority('ROLE_ADMIN') || hasAnyAuthority('ROLE_AUXILIAR') || (familiar.username && familiar.username.login == username)"
          >
            <td>
              <router-link :to="{ name: 'FamiliarView', params: { familiarId: familiar.id } }">{{ familiar.username.login }}</router-link>
            </td>
            <td>{{ familiar.cedula }}</td>
            <td v-if="familiar.username">{{ familiar.username.firstName }} {{ familiar.username.lastName }}</td> <td v-else></td>
            <td>{{ familiar.ciudad }}</td>
            <td>{{ familiar.direccion }}</td>
            <td>{{ familiar.telefono }}</td>
            <td>{{ familiar.fechaNacimiento }}</td>
            <td v-text="$t('homecareApp.Parentesco.' + familiar.parentesco)">{{ familiar.parentesco }}</td>
            <td>{{familiar.paciente.username.firstName}} {{familiar.paciente.username.lastName}}</td>

            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'FamiliarView', params: { familiarId: familiar.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'FamiliarEdit', params: { familiarId: familiar.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button v-if="hasAnyAuthority('ROLE_ADMIN')"
                  v-on:click="prepareRemove(familiar)"
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
        ><span id="homecareApp.familiar.delete.question" data-cy="familiarDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-familiar-heading" v-text="$t('homecareApp.familiar.delete.question', { id: removeId })">
          Are you sure you want to delete this Familiar?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-familiar"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeFamiliar()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./familiar.component.ts"></script>
