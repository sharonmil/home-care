<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="homecareApp.historiaClinica.home.createOrEditLabel"
          data-cy="HistoriaClinicaCreateUpdateHeading"
          v-text="$t('homecareApp.historiaClinica.home.createOrEditLabel')"
        >
          Create or edit a HistoriaClinica
        </h2>
        <div>
          <div class="form-group" v-if="historiaClinica.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="historiaClinica.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.historiaClinica.sugerencia')" for="historia-clinica-sugerencia"
              >Sugerencia</label
            >
            <textarea
              type="text"
              class="form-control"
              name="sugerencia"
              id="historia-clinica-sugerencia"
              data-cy="sugerencia"
              :class="{ valid: !$v.historiaClinica.sugerencia.$invalid, invalid: $v.historiaClinica.sugerencia.$invalid }"
              v-model="$v.historiaClinica.sugerencia.$model"
              required
            />
            <div v-if="$v.historiaClinica.sugerencia.$anyDirty && $v.historiaClinica.sugerencia.$invalid">
              <small class="form-text text-danger" v-if="!$v.historiaClinica.sugerencia.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.historiaClinica.sugerencia.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 2000 })"
              >
                This field cannot be longer than 2000 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.historiaClinica.diagnostico')" for="historia-clinica-diagnostico"
              >Diagnostico</label
            >
            <textarea
              type="text"
              class="form-control"
              name="diagnostico"
              id="historia-clinica-diagnostico"
              data-cy="diagnostico"
              :class="{ valid: !$v.historiaClinica.diagnostico.$invalid, invalid: $v.historiaClinica.diagnostico.$invalid }"
              v-model="$v.historiaClinica.diagnostico.$model"
            />
            <div v-if="$v.historiaClinica.diagnostico.$anyDirty && $v.historiaClinica.diagnostico.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.historiaClinica.diagnostico.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 2000 })"
              >
                This field cannot be longer than 2000 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('homecareApp.historiaClinica.paciente')" for="historia-clinica-paciente"
              >Paciente</label
            >
            <select
              class="form-control"
              id="historia-clinica-paciente"
              data-cy="paciente"
              name="paciente"
              v-model="historiaClinica.paciente"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  historiaClinica.paciente && pacienteOption.id === historiaClinica.paciente.id ? historiaClinica.paciente : pacienteOption
                "
                v-for="pacienteOption in pacientes"
                :key="pacienteOption.id"
                v-if="pacienteOption.medico && pacienteOption.medico.username.login === username"
              >
                {{ 'Cedula: ' + pacienteOption.cedula + ' - Nombres: ' + pacienteOption.username.firstName + ' ' +  pacienteOption.username.lastName}}
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
            :disabled="$v.historiaClinica.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./historia-clinica-update.component.ts"></script>
