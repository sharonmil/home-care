<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="homecareApp.familiar.home.createOrEditLabel"
          data-cy="FamiliarCreateUpdateHeading"
          v-text="$t('homecareApp.familiar.home.createOrEditLabel')"
        >
          Create or edit a Familiar
        </h2>
        <div>
          <div class="form-group" v-if="familiar.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="familiar.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.familiar.cedula')" for="familiar-cedula">Cedula</label>
            <input
              type="text"
              class="form-control"
              name="cedula"
              id="familiar-cedula"
              data-cy="cedula"
              :class="{ valid: !$v.familiar.cedula.$invalid, invalid: $v.familiar.cedula.$invalid }"
              v-model="$v.familiar.cedula.$model"
              required
            />
            <div v-if="$v.familiar.cedula.$anyDirty && $v.familiar.cedula.$invalid">
              <small class="form-text text-danger" v-if="!$v.familiar.cedula.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.familiar.cedula.minLength"
                v-text="$t('entity.validation.minlength', { min: 5 })"
              >
                This field is required to be at least 5 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.familiar.cedula.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 20 })"
              >
                This field cannot be longer than 20 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.familiar.ciudad')" for="familiar-ciudad">Ciudad</label>
            <input
              type="text"
              class="form-control"
              name="ciudad"
              id="familiar-ciudad"
              data-cy="ciudad"
              :class="{ valid: !$v.familiar.ciudad.$invalid, invalid: $v.familiar.ciudad.$invalid }"
              v-model="$v.familiar.ciudad.$model"
              required
            />
            <div v-if="$v.familiar.ciudad.$anyDirty && $v.familiar.ciudad.$invalid">
              <small class="form-text text-danger" v-if="!$v.familiar.ciudad.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.familiar.ciudad.minLength"
                v-text="$t('entity.validation.minlength', { min: 5 })"
              >
                This field is required to be at least 5 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.familiar.ciudad.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 20 })"
              >
                This field cannot be longer than 20 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.familiar.direccion')" for="familiar-direccion">Direccion</label>
            <input
              type="text"
              class="form-control"
              name="direccion"
              id="familiar-direccion"
              data-cy="direccion"
              :class="{ valid: !$v.familiar.direccion.$invalid, invalid: $v.familiar.direccion.$invalid }"
              v-model="$v.familiar.direccion.$model"
              required
            />
            <div v-if="$v.familiar.direccion.$anyDirty && $v.familiar.direccion.$invalid">
              <small class="form-text text-danger" v-if="!$v.familiar.direccion.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.familiar.direccion.minLength"
                v-text="$t('entity.validation.minlength', { min: 5 })"
              >
                This field is required to be at least 5 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.familiar.telefono')" for="familiar-telefono">Telefono</label>
            <input
              type="text"
              class="form-control"
              name="telefono"
              id="familiar-telefono"
              data-cy="telefono"
              :class="{ valid: !$v.familiar.telefono.$invalid, invalid: $v.familiar.telefono.$invalid }"
              v-model="$v.familiar.telefono.$model"
              required
            />
            <div v-if="$v.familiar.telefono.$anyDirty && $v.familiar.telefono.$invalid">
              <small class="form-text text-danger" v-if="!$v.familiar.telefono.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.familiar.telefono.minLength"
                v-text="$t('entity.validation.minlength', { min: 7 })"
              >
                This field is required to be at least 7 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.familiar.fechaNacimiento')" for="familiar-fechaNacimiento"
              >Fecha Nacimiento</label
            >
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="familiar-fechaNacimiento"
                  v-model="$v.familiar.fechaNacimiento.$model"
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
                id="familiar-fechaNacimiento"
                data-cy="fechaNacimiento"
                type="text"
                class="form-control"
                name="fechaNacimiento"
                :class="{ valid: !$v.familiar.fechaNacimiento.$invalid, invalid: $v.familiar.fechaNacimiento.$invalid }"
                v-model="$v.familiar.fechaNacimiento.$model"
                required
              />
            </b-input-group>
            <div v-if="$v.familiar.fechaNacimiento.$anyDirty && $v.familiar.fechaNacimiento.$invalid">
              <small class="form-text text-danger" v-if="!$v.familiar.fechaNacimiento.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.familiar.parentesco')" for="familiar-parentesco">Parentesco</label>
            <select
              class="form-control"
              name="parentesco"
              :class="{ valid: !$v.familiar.parentesco.$invalid, invalid: $v.familiar.parentesco.$invalid }"
              v-model="$v.familiar.parentesco.$model"
              id="familiar-parentesco"
              data-cy="parentesco"
              required
            >
              <option
                v-for="parentesco in parentescoValues"
                :key="parentesco"
                v-bind:value="parentesco"
                v-bind:label="$t('homecareApp.Parentesco.' + parentesco)"
              >
                {{ parentesco }}
              </option>
            </select>
            <div v-if="$v.familiar.parentesco.$anyDirty && $v.familiar.parentesco.$invalid">
              <small class="form-text text-danger" v-if="!$v.familiar.parentesco.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
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
            :disabled="$v.familiar.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./familiar-update.component.ts"></script>
