<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="homecareApp.signosVitales.home.createOrEditLabel"
          data-cy="SignosVitalesCreateUpdateHeading"
          v-text="$t('homecareApp.signosVitales.home.createOrEditLabel')"
        >
          Create or edit a SignosVitales
        </h2>
        <div>
          <div class="form-group" v-if="signosVitales.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="signosVitales.id" readonly />
          </div>
          <div class="form-group" >
            <label class="form-control-label" v-text="$t('homecareApp.signosVitales.paciente')" for="signos-vitales-paciente"
            >Paciente</label
            >
            <select :disabled="!hasAnyAuthority('ROLE_ADMIN')" class="form-control" id="signos-vitales-paciente" data-cy="paciente" name="paciente"
                    v-model="signosVitales.paciente">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  signosVitales.paciente && pacienteOption.id === signosVitales.paciente.id ? signosVitales.paciente : pacienteOption
                "
                v-for="pacienteOption in pacientes"
                :key="pacienteOption.id"
              >
                {{ pacienteOption.username.firstName }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.signosVitales.oximetria')" for="signos-vitales-oximetria"
              >Oximetria</label
            >
            <input
              type="number"
              class="form-control"
              name="oximetria"
              id="signos-vitales-oximetria"
              data-cy="oximetria"
              :class="{ valid: !$v.signosVitales.oximetria.$invalid, invalid: $v.signosVitales.oximetria.$invalid }"
              v-model.number="$v.signosVitales.oximetria.$model"
            />
            <div v-if="$v.signosVitales.oximetria.$anyDirty && $v.signosVitales.oximetria.$invalid">
              <small class="form-text text-danger" v-if="!$v.signosVitales.oximetria.min" v-text="$t('entity.validation.min', { min: 1 })">
                This field should be at least 1.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.signosVitales.oximetria.max"
                v-text="$t('entity.validation.max', { max: 100 })"
              >
                This field cannot be longer than 100 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.signosVitales.oximetria.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('homecareApp.signosVitales.frecuenciaRespiratoria')"
              for="signos-vitales-frecuenciaRespiratoria"
              >Frecuencia Respiratoria</label
            >
            <input
              type="number"
              class="form-control"
              name="frecuenciaRespiratoria"
              id="signos-vitales-frecuenciaRespiratoria"
              data-cy="frecuenciaRespiratoria"
              :class="{
                valid: !$v.signosVitales.frecuenciaRespiratoria.$invalid,
                invalid: $v.signosVitales.frecuenciaRespiratoria.$invalid,
              }"
              v-model.number="$v.signosVitales.frecuenciaRespiratoria.$model"
            />
            <div v-if="$v.signosVitales.frecuenciaRespiratoria.$anyDirty && $v.signosVitales.frecuenciaRespiratoria.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.signosVitales.frecuenciaRespiratoria.min"
                v-text="$t('entity.validation.min', { min: 1 })"
              >
                This field should be at least 1.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.signosVitales.frecuenciaRespiratoria.max"
                v-text="$t('entity.validation.max', { max: 300 })"
              >
                This field cannot be longer than 300 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.signosVitales.frecuenciaRespiratoria.numeric"
                v-text="$t('entity.validation.number')"
              >
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('homecareApp.signosVitales.frecuenciaCardiaca')"
              for="signos-vitales-frecuenciaCardiaca"
              >Frecuencia Cardiaca</label
            >
            <input
              type="number"
              class="form-control"
              name="frecuenciaCardiaca"
              id="signos-vitales-frecuenciaCardiaca"
              data-cy="frecuenciaCardiaca"
              :class="{ valid: !$v.signosVitales.frecuenciaCardiaca.$invalid, invalid: $v.signosVitales.frecuenciaCardiaca.$invalid }"
              v-model.number="$v.signosVitales.frecuenciaCardiaca.$model"
            />
            <div v-if="$v.signosVitales.frecuenciaCardiaca.$anyDirty && $v.signosVitales.frecuenciaCardiaca.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.signosVitales.frecuenciaCardiaca.min"
                v-text="$t('entity.validation.min', { min: 5 })"
              >
                This field should be at least 5.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.signosVitales.frecuenciaCardiaca.max"
                v-text="$t('entity.validation.max', { max: 300 })"
              >
                This field cannot be longer than 300 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.signosVitales.frecuenciaCardiaca.numeric"
                v-text="$t('entity.validation.number')"
              >
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.signosVitales.presion')" for="signos-vitales-presion">Presion</label>
            <input
              type="text"
              class="form-control"
              name="presion"
              id="signos-vitales-presion"
              data-cy="presion"
              :class="{ valid: !$v.signosVitales.presion.$invalid, invalid: $v.signosVitales.presion.$invalid }"
              v-model="$v.signosVitales.presion.$model"
            />
            <div v-if="$v.signosVitales.presion.$anyDirty && $v.signosVitales.presion.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.signosVitales.presion.minLength"
                v-text="$t('entity.validation.minlength', { min: 2 })"
              >
                This field is required to be at least 2 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.signosVitales.glicemia')" for="signos-vitales-glicemia"
              >Glicemia</label
            >
            <input
              type="number"
              class="form-control"
              name="glicemia"
              id="signos-vitales-glicemia"
              data-cy="glicemia"
              :class="{ valid: !$v.signosVitales.glicemia.$invalid, invalid: $v.signosVitales.glicemia.$invalid }"
              v-model.number="$v.signosVitales.glicemia.$model"
            />
            <div v-if="$v.signosVitales.glicemia.$anyDirty && $v.signosVitales.glicemia.$invalid">
              <small class="form-text text-danger" v-if="!$v.signosVitales.glicemia.min" v-text="$t('entity.validation.min', { min: 1 })">
                This field should be at least 1.
              </small>
              <small class="form-text text-danger" v-if="!$v.signosVitales.glicemia.max" v-text="$t('entity.validation.max', { max: 600 })">
                This field cannot be longer than 600 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.signosVitales.glicemia.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>

          <div class="form-group" v-if="hasAnyAuthority('ROLE_ADMIN')">
            <label class="form-control-label" for="signos-vitales-createdDate">
              Fecha de Creación
            </label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="signos-vitales-createdDate"
                  v-model="$v.signosVitales.createdDate.$model"
                  name="createdDate"
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
                id="signos-vitales-createdDate"
                data-cy="createdDate"
                type="text"
                class="form-control"
                name="createdDate"
                :class="{ valid: !$v.signosVitales.createdDate.$invalid, invalid: $v.signosVitales.createdDate.$invalid }"
                v-model="$v.signosVitales.createdDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group" v-else>
            <dl class="row jh-entity-details">
              <dt>
                <span>Fecha de Creación</span>
              </dt>
              <dd>
                <span v-if="signosVitales.createdDate">{{ signosVitales.createdDate }}</span>
              </dd>
            </dl>
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
            :disabled="$v.signosVitales.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./signos-vitales-update.component.ts"></script>
