<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="homecareApp.medico.home.createOrEditLabel"
          data-cy="MedicoCreateUpdateHeading"
          v-text="$t('homecareApp.medico.home.createOrEditLabel')"
        >
          Create or edit a Medico
        </h2>
        <div>
          <div class="form-group" v-if="medico.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="medico.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.medico.cedula')" for="medico-cedula">Cedula</label>
            <input
              type="text"
              class="form-control"
              name="cedula"
              id="medico-cedula"
              data-cy="cedula"
              :class="{ valid: !$v.medico.cedula.$invalid, invalid: $v.medico.cedula.$invalid }"
              v-model="$v.medico.cedula.$model"
              required
            />
            <div v-if="$v.medico.cedula.$anyDirty && $v.medico.cedula.$invalid">
              <small class="form-text text-danger" v-if="!$v.medico.cedula.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.medico.cedula.minLength"
                v-text="$t('entity.validation.minlength', { min: 5 })"
              >
                This field is required to be at least 5 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.medico.cedula.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 20 })"
              >
                This field cannot be longer than 20 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.medico.ciudad')" for="medico-ciudad">Ciudad</label>
            <input
              type="text"
              class="form-control"
              name="ciudad"
              id="medico-ciudad"
              data-cy="ciudad"
              :class="{ valid: !$v.medico.ciudad.$invalid, invalid: $v.medico.ciudad.$invalid }"
              v-model="$v.medico.ciudad.$model"
              required
            />
            <div v-if="$v.medico.ciudad.$anyDirty && $v.medico.ciudad.$invalid">
              <small class="form-text text-danger" v-if="!$v.medico.ciudad.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.medico.ciudad.minLength"
                v-text="$t('entity.validation.minlength', { min: 5 })"
              >
                This field is required to be at least 5 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.medico.ciudad.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 20 })"
              >
                This field cannot be longer than 20 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.medico.direccion')" for="medico-direccion">Direccion</label>
            <input
              type="text"
              class="form-control"
              name="direccion"
              id="medico-direccion"
              data-cy="direccion"
              :class="{ valid: !$v.medico.direccion.$invalid, invalid: $v.medico.direccion.$invalid }"
              v-model="$v.medico.direccion.$model"
              required
            />
            <div v-if="$v.medico.direccion.$anyDirty && $v.medico.direccion.$invalid">
              <small class="form-text text-danger" v-if="!$v.medico.direccion.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.medico.direccion.minLength"
                v-text="$t('entity.validation.minlength', { min: 5 })"
              >
                This field is required to be at least 5 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.medico.telefono')" for="medico-telefono">Telefono</label>
            <input
              type="text"
              class="form-control"
              name="telefono"
              id="medico-telefono"
              data-cy="telefono"
              :class="{ valid: !$v.medico.telefono.$invalid, invalid: $v.medico.telefono.$invalid }"
              v-model="$v.medico.telefono.$model"
              required
            />
            <div v-if="$v.medico.telefono.$anyDirty && $v.medico.telefono.$invalid">
              <small class="form-text text-danger" v-if="!$v.medico.telefono.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.medico.telefono.minLength"
                v-text="$t('entity.validation.minlength', { min: 7 })"
              >
                This field is required to be at least 7 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.medico.fechaNacimiento')" for="medico-fechaNacimiento"
              >Fecha Nacimiento</label
            >
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="medico-fechaNacimiento"
                  v-model="$v.medico.fechaNacimiento.$model"
                  name="fechaNacimiento"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="medico-fechaNacimiento"
                data-cy="fechaNacimiento"
                type="text"
                class="form-control"
                name="fechaNacimiento"
                :class="{ valid: !$v.medico.fechaNacimiento.$invalid, invalid: $v.medico.fechaNacimiento.$invalid }"
                v-model="$v.medico.fechaNacimiento.$model"
                required
              />
            </b-input-group>
            <div v-if="$v.medico.fechaNacimiento.$anyDirty && $v.medico.fechaNacimiento.$invalid">
              <small class="form-text text-danger" v-if="!$v.medico.fechaNacimiento.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.medico.especialidad')" for="medico-especialidad">Especialidad</label>
            <input
              type="text"
              class="form-control"
              name="especialidad"
              id="medico-especialidad"
              data-cy="especialidad"
              :class="{ valid: !$v.medico.especialidad.$invalid, invalid: $v.medico.especialidad.$invalid }"
              v-model="$v.medico.especialidad.$model"
              required
            />
            <div v-if="$v.medico.especialidad.$anyDirty && $v.medico.especialidad.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.medico.especialidad.minLength"
                v-text="$t('entity.validation.minlength', { min: 2 })"
              >
                This field is required to be at least 2 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.medico.username')" for="medico-username">Username</label>
            <select class="form-control" id="medico-username" data-cy="username" name="username" v-model="medico.username">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="medico.username && userOption.id === medico.username.id ? medico.username : userOption"
                v-for="userOption in users"
                :key="userOption.id"
              >
                {{ userOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.medico.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./medico-update.component.ts"></script>
