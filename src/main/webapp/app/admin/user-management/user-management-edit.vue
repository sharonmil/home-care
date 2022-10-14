<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" v-if="userAccount">
        <h2 id="myUserLabel" v-text="$t('userManagement.home.createOrEditLabel')">Create or edit a User</h2>
        <div>
          <div class="form-group" :hidden="!userAccount.id">
            <label v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" name="id" v-model="userAccount.id" readonly />
          </div>

          <div class="form-group">
            <label class="form-control-label" v-text="$t('userManagement.login')">Login</label>
            <input
              type="text"
              class="form-control"
              name="login"
              :class="{ valid: !$v.userAccount.login.$invalid, invalid: $v.userAccount.login.$invalid }"
              v-model="$v.userAccount.login.$model"
            />

            <div v-if="$v.userAccount.login.$anyDirty && $v.userAccount.login.$invalid">
              <small class="form-text text-danger" v-if="!$v.userAccount.login.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>

              <small
                class="form-text text-danger"
                v-if="!$v.userAccount.login.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>

              <small class="form-text text-danger" v-if="!$v.userAccount.login.pattern" v-text="$t('entity.validation.patternLogin')">
                This field can only contain letters, digits and e-mail addresses.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="firstName" v-text="$t('userManagement.firstName')">First Name</label>
            <input
              type="text"
              class="form-control"
              id="firstName"
              name="firstName"
              v-bind:placeholder="$t('settings.form[\'firstname.placeholder\']')"
              :class="{ valid: !$v.userAccount.firstName.$invalid, invalid: $v.userAccount.firstName.$invalid }"
              v-model="$v.userAccount.firstName.$model"
            />
            <div v-if="$v.userAccount.firstName.$anyDirty && $v.userAccount.firstName.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.userAccount.firstName.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="lastName" v-text="$t('userManagement.lastName')">Last Name</label>
            <input
              type="text"
              class="form-control"
              id="lastName"
              name="lastName"
              v-bind:placeholder="$t('settings.form[\'lastname.placeholder\']')"
              :class="{ valid: !$v.userAccount.lastName.$invalid, invalid: $v.userAccount.lastName.$invalid }"
              v-model="$v.userAccount.lastName.$model"
            />
            <div v-if="$v.userAccount.lastName.$anyDirty && $v.userAccount.lastName.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.userAccount.lastName.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="email" v-text="$t('userManagement.email')">Email</label>
            <input
              type="email"
              class="form-control"
              id="email"
              name="email"
              v-bind:placeholder="$t('global.form[\'email.placeholder\']')"
              :class="{ valid: !$v.userAccount.email.$invalid, invalid: $v.userAccount.email.$invalid }"
              v-model="$v.userAccount.email.$model"
              email
              required
            />
            <div v-if="$v.userAccount.email.$anyDirty && $v.userAccount.email.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.userAccount.email.required"
                v-text="$t('global.messages.validate.email.required')"
              >
                Your email is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.userAccount.email.email" v-text="$t('global.messages.validate.email.invalid')">
                Your email is invalid.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.userAccount.email.minLength"
                v-text="$t('global.messages.validate.email.minlength')"
              >
                Your email is required to be at least 5 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.userAccount.email.maxLength"
                v-text="$t('global.messages.validate.email.maxlength')"
              >
                Your email cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-check">
            <label class="form-check-label" for="activated">
              <input
                class="form-check-input"
                :disabled="userAccount.id === null"
                type="checkbox"
                id="activated"
                name="activated"
                v-model="userAccount.activated"
              />
              <span v-text="$t('userManagement.activated')">Activated</span>
            </label>
          </div>

          <div class="form-group">
            <label v-text="$t('userManagement.profiles')">Profiles</label>
            <select class="form-control" multiple name="authority" v-model="userAccount.authorities" @click="authoritySelect($event)">
              <option v-for="authority of authoritiesOptions" :value="authority.value" :key="authority.value" :disabled="authority.disabled">{{ authority.value }}</option>
            </select>
          </div>
        </div>

        <div id="paciente" v-if="userAccount.authorities.includes('ROLE_PACIENTE')">

            <div>
              <div class="form-group" v-if="paciente && paciente.id">
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
                <label class="form-control-label" v-text="$t('homecareApp.paciente.fechaNacimiento')" for="paciente-fechaNacimiento">
                  Fecha Nacimiento
                </label>
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
                      close-button >
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
            </div>

          </div>

        <div id="familiar" v-if="userAccount.authorities.includes('ROLE_FAMILIAR')">

          <div>
            <div class="form-group" v-if="familiar && familiar.id">
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
                  v-text="$t('entity.validation.minlength', { min: 5 })">
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
              <label class="form-control-label" v-text="$t('homecareApp.familiar.fechaNacimiento')" for="familiar-fechaNacimiento">
                Fecha Nacimiento
              </label>
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
                    close-button>
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
            <div class="form-group">
              <label class="form-control-label" for="email-paciente" >Email del paciente bajo sus cuidados.</label>
              <input
                type="email"
                class="form-control"
                id="email-paciente"
                name="email-paciente"
                :class="{ valid: !$v.familiar.emailPaciente.$invalid, invalid: $v.familiar.emailPaciente.$invalid }"
                v-model="$v.familiar.emailPaciente.$model"
                minlength="5"
                maxlength="254"
                email
                required
                v-bind:placeholder="$t('global.form[\'email.placeholder\']')"
                data-cy="email-paciente"
              />
              <div v-if="$v.familiar.emailPaciente.$anyDirty && $v.familiar.emailPaciente.$invalid">
                <small
                  class="form-text text-danger"
                  v-if="!$v.familiar.emailPaciente.required"
                  v-text="$t('global.messages.validate.email.required')"
                >
                  Your email is required.
                </small>
                <small
                  class="form-text text-danger"
                  v-if="!$v.familiar.emailPaciente.email"
                  v-text="$t('global.messages.validate.email.invalid')"
                >
                  Your email is invalid.
                </small>
                <small
                  class="form-text text-danger"
                  v-if="!$v.familiar.emailPaciente.minLength"
                  v-text="$t('global.messages.validate.email.minlength')"
                >
                  Your email is required to be at least 5 characters.
                </small>
                <small
                  class="form-text text-danger"
                  v-if="!$v.familiar.emailPaciente.maxLength"
                  v-text="$t('global.messages.validate.email.maxlength')"
                >
                  Your email cannot be longer than 100 characters.
                </small>
              </div>
            </div>

          </div>

        </div>

        <div id="medico"  v-if="userAccount.authorities.includes('ROLE_MEDICO')">
          <div class="form-group" v-if="medico && medico.id">
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
          <div class="form-group" v-if="!userAccount.authorities.includes('ROLE_ENFERMERO')">
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
        </div>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button type="submit" :disabled="!formIsValid() || isSaving" class="btn btn-primary">
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="ts" src="./user-management-edit.component.ts"></script>
