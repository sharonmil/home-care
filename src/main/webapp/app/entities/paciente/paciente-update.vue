<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="homecareApp.paciente.home.createOrEditLabel"
          data-cy="PacienteCreateUpdateHeading"
          v-text="$t('homecareApp.paciente.home.createOrEditLabel')"
        >
          Create or edit a Paciente
        </h2>
        <div>
          <div class="form-group" v-if="paciente.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="paciente.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.paciente.cedula')" for="paciente-cedula">Cedula</label>
            <input

              type="text"
              class="form-control"
              name="cedula"
              id="paciente-cedula"
              data-cy="cedula"
              :disabled ="!hasAnyAuthority('ROLE_ADMIN')"
              :class="{ valid: !$v.paciente.cedula.$invalid, invalid: $v.paciente.cedula.$invalid }"
              v-model="$v.paciente.cedula.$model"
              required
            />
            <div v-if="$v.paciente.cedula.$anyDirty && $v.paciente.cedula.$invalid">
              <small class="form-text text-danger" v-if="!$v.paciente.cedula.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.paciente.cedula.minLength"
                v-text="$t('entity.validation.minlength', { min: 5 })"
              >
                This field is required to be at least 5 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.paciente.cedula.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 20 })"
              >
                This field cannot be longer than 20 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.paciente.ciudad')" for="paciente-ciudad">Ciudad</label>
            <input
              type="text"
              class="form-control"
              name="ciudad"
              id="paciente-ciudad"
              data-cy="ciudad"
              :class="{ valid: !$v.paciente.ciudad.$invalid, invalid: $v.paciente.ciudad.$invalid }"
              v-model="$v.paciente.ciudad.$model"
              required
            />
            <div v-if="$v.paciente.ciudad.$anyDirty && $v.paciente.ciudad.$invalid">
              <small class="form-text text-danger" v-if="!$v.paciente.ciudad.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.paciente.ciudad.minLength"
                v-text="$t('entity.validation.minlength', { min: 5 })"
              >
                This field is required to be at least 5 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.paciente.ciudad.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 20 })"
              >
                This field cannot be longer than 20 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.paciente.direccion')" for="paciente-direccion">Direccion</label>
            <input
              type="text"
              class="form-control"
              name="direccion"
              id="paciente-direccion"
              data-cy="direccion"
              :class="{ valid: !$v.paciente.direccion.$invalid, invalid: $v.paciente.direccion.$invalid }"
              v-model="$v.paciente.direccion.$model"
              required
            />
            <div v-if="$v.paciente.direccion.$anyDirty && $v.paciente.direccion.$invalid">
              <small class="form-text text-danger" v-if="!$v.paciente.direccion.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.paciente.direccion.minLength"
                v-text="$t('entity.validation.minlength', { min: 5 })"
              >
                This field is required to be at least 5 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.paciente.telefono')" for="paciente-telefono">Telefono</label>
            <input
              type="text"
              class="form-control"
              name="telefono"
              id="paciente-telefono"
              data-cy="telefono"
              :class="{ valid: !$v.paciente.telefono.$invalid, invalid: $v.paciente.telefono.$invalid }"
              v-model="$v.paciente.telefono.$model"
              required
            />
            <div v-if="$v.paciente.telefono.$anyDirty && $v.paciente.telefono.$invalid">
              <small class="form-text text-danger" v-if="!$v.paciente.telefono.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.paciente.telefono.minLength"
                v-text="$t('entity.validation.minlength', { min: 7 })"
              >
                This field is required to be at least 7 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.paciente.fechaNacimiento')" for="paciente-fechaNacimiento"
              >Fecha Nacimiento</label
            >
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="paciente-fechaNacimiento"
                  v-model="$v.paciente.fechaNacimiento.$model"
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
                id="paciente-fechaNacimiento"
                data-cy="fechaNacimiento"
                type="text"
                class="form-control"
                name="fechaNacimiento"
                :class="{ valid: !$v.paciente.fechaNacimiento.$invalid, invalid: $v.paciente.fechaNacimiento.$invalid }"
                v-model="$v.paciente.fechaNacimiento.$model"
                required
              />
            </b-input-group>
            <div v-if="$v.paciente.fechaNacimiento.$anyDirty && $v.paciente.fechaNacimiento.$invalid">
              <small class="form-text text-danger" v-if="!$v.paciente.fechaNacimiento.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>

          <div class="form-group" v-if="hasAnyAuthority('ROLE_ADMIN')">
            <label class="form-control-label" v-text="$t('homecareApp.paciente.familiar')" for="paciente-familiar">Familiar</label>
            <select class="form-control" id="paciente-familiar" data-cy="familiar" name="familiar" v-model="paciente.familiar">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="paciente.familiar && familiarOption.id === paciente.familiar.id ? paciente.familiar : familiarOption"
                v-for="familiarOption in familiars"
                :key="familiarOption.id"
              >
                {{ familiarOption.username.firstName }} {{ familiarOption.username.lastName }}
              </option>
            </select>
          </div>

          <div class="form-group" v-if="hasAnyAuthority('ROLE_ADMIN')">
            <label class="form-control-label" v-text="$t('homecareApp.paciente.medico')" for="paciente-medico">Medico</label>
            <select class="form-control" id="paciente-medico" data-cy="medico" name="medico" v-model="paciente.medico">
              <option v-bind:value="null"></option>

              <option  v-for="medicoOption in medicos" v-if="medicoOption.username"
                v-bind:value="paciente.medico && medicoOption.id === paciente.medico.id ? paciente.medico : medicoOption"
                :key="medicoOption.id"
              >
                {{medicoOption.username.firstName  }} {{  medicoOption.username.lastName }}
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
            :disabled="$v.paciente.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./paciente-update.component.ts"></script>
