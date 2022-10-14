<template>
  <div v-if="2 == 1">
    <div class="row justify-content-center">
      <div class="col-md-8 toastify-container">
        <h1 v-text="$t('register.title')" id="register-title" data-cy="registerTitle">Registration</h1>

        <div class="alert alert-success" role="alert" v-if="success" v-html="$t('register.messages.success')">
          <strong>Registration saved!</strong> Please check your email for confirmation.
        </div>

        <div class="alert alert-danger" role="alert" v-if="error" v-html="$t('register.messages.error.fail')">
          <strong>Registration failed!</strong> Please try again later.
        </div>

        <div class="alert alert-danger" role="alert" v-if="errorUserExists" v-html="$t('register.messages.error.userexists')">
          <strong>Login name already registered!</strong> Please choose another one.
        </div>

        <div class="alert alert-danger" role="alert" v-if="errorEmailExists" v-html="$t('register.messages.error.emailexists')">
          <strong>Email is already in use!</strong> Please choose another one.
        </div>
      </div>
    </div>
    <div class="row justify-content-center">
      <div class="col-md-8">
        <form id="register-form" name="registerForm" role="form" v-on:submit.prevent="register()" v-if="!success" no-validate>
          <div class="form-group">
            <label class="form-control-label" for="username" v-text="$t('global.form[\'username.label\']')">Username</label>
            <input
              type="text"
              class="form-control"
              v-model="$v.registerAccount.login.$model"
              id="username"
              name="login"
              :class="{ valid: !$v.registerAccount.login.$invalid, invalid: $v.registerAccount.login.$invalid }"
              required
              minlength="1"
              maxlength="50"
              pattern="^[a-zA-Z0-9!#$&'*+=?^_`{|}~.-]+@?[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$"
              v-bind:placeholder="$t('global.form[\'username.placeholder\']')"
              data-cy="username"
            />
            <div v-if="$v.registerAccount.login.$anyDirty && $v.registerAccount.login.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.login.required"
                v-text="$t('register.messages.validate.login.required')"
              >
                Your username is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.login.minLength"
                v-text="$t('register.messages.validate.login.minlength')"
              >
                Your username is required to be at least 1 character.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.login.maxLength"
                v-text="$t('register.messages.validate.login.maxlength')"
              >
                Your username cannot be longer than 50 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.login.pattern"
                v-text="$t('register.messages.validate.login.pattern')"
              >
                Your username can only contain letters and digits.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="email" v-text="$t('global.form[\'email.label\']')">Email</label>
            <input
              type="email"
              class="form-control"
              id="email"
              name="email"
              :class="{ valid: !$v.registerAccount.email.$invalid, invalid: $v.registerAccount.email.$invalid }"
              v-model="$v.registerAccount.email.$model"
              minlength="5"
              maxlength="254"
              email
              required
              v-bind:placeholder="$t('global.form[\'email.placeholder\']')"
              data-cy="email"
            />
            <div v-if="$v.registerAccount.email.$anyDirty && $v.registerAccount.email.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.email.required"
                v-text="$t('global.messages.validate.email.required')"
              >
                Your email is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.email.email"
                v-text="$t('global.messages.validate.email.invalid')"
              >
                Your email is invalid.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.email.minLength"
                v-text="$t('global.messages.validate.email.minlength')"
              >
                Your email is required to be at least 5 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.email.maxLength"
                v-text="$t('global.messages.validate.email.maxlength')"
              >
                Your email cannot be longer than 100 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="firstPassword" v-text="$t('global.form[\'newpassword.label\']')">New password</label>
            <input
              type="password"
              class="form-control"
              id="firstPassword"
              name="password"
              :class="{ valid: !$v.registerAccount.password.$invalid, invalid: $v.registerAccount.password.$invalid }"
              v-model="$v.registerAccount.password.$model"
              minlength="4"
              maxlength="50"
              required
              v-bind:placeholder="$t('global.form[\'newpassword.placeholder\']')"
              data-cy="firstPassword"
            />
            <div v-if="$v.registerAccount.password.$anyDirty && $v.registerAccount.password.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.password.required"
                v-text="$t('global.messages.validate.newpassword.required')"
              >
                Your password is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.password.minLength"
                v-text="$t('global.messages.validate.newpassword.minlength')"
              >
                Your password is required to be at least 4 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.password.maxLength"
                v-text="$t('global.messages.validate.newpassword.maxlength')"
              >
                Your password cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="secondPassword" v-text="$t('global.form[\'confirmpassword.label\']')"
              >New password confirmation</label>
            <input
              type="password"
              class="form-control"
              id="secondPassword"
              name="confirmPasswordInput"
              :class="{ valid: !$v.confirmPassword.$invalid, invalid: $v.confirmPassword.$invalid }"
              v-model="$v.confirmPassword.$model"
              minlength="4"
              maxlength="50"
              required
              v-bind:placeholder="$t('global.form[\'confirmpassword.placeholder\']')"
              data-cy="secondPassword"
            />
            <div v-if="$v.confirmPassword.$dirty && $v.confirmPassword.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.confirmPassword.required"
                v-text="$t('global.messages.validate.confirmpassword.required')"
              >
                Your confirmation password is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.confirmPassword.minLength"
                v-text="$t('global.messages.validate.confirmpassword.minlength')"
              >
                Your confirmation password is required to be at least 4 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.confirmPassword.maxLength"
                v-text="$t('global.messages.validate.confirmpassword.maxlength')"
              >
                Your confirmation password cannot be longer than 50 characters.
              </small>
              <small class="form-text text-danger" v-if="!$v.confirmPassword.sameAsPassword" v-text="$t('global.messages.error.dontmatch')">
                The password and its confirmation do not match!
              </small>
            </div>
          </div>

          <div class="form-group">
            <label class="form-control-label" for="firstName" v-text="$t('settings.form.firstname')">First Name</label>
            <input
              type="text"
              class="form-control"
              id="firstName"
              name="firstName"
              v-bind:placeholder="$t('settings.form[\'firstname.placeholder\']')"
              :class="{ valid: !$v.registerAccount.firstName.$invalid, invalid: $v.registerAccount.firstName.$invalid }"
              v-model="$v.registerAccount.firstName.$model"
              minlength="1"
              maxlength="50"
              required
              data-cy="firstname"
            />
            <div v-if="$v.registerAccount.firstName.$anyDirty && $v.registerAccount.firstName.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.firstName.required"
                v-text="$t('settings.messages.validate.firstname.required')"
              >
                Your first name is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.firstName.minLength"
                v-text="$t('settings.messages.validate.firstname.minlength')"
              >
                Your first name is required to be at least 1 character.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.firstName.maxLength"
                v-text="$t('settings.messages.validate.firstname.maxlength')"
              >
                Your first name cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="lastName" v-text="$t('settings.form.lastname')">Last Name</label>
            <input
              type="text"
              class="form-control"
              id="lastName"
              name="lastName"
              v-bind:placeholder="$t('settings.form[\'lastname.placeholder\']')"
              :class="{ valid: !$v.registerAccount.lastName.$invalid, invalid: $v.registerAccount.lastName.$invalid }"
              v-model="$v.registerAccount.lastName.$model"
              minlength="1"
              maxlength="50"
              required
              data-cy="lastname"
            />
            <div v-if="$v.registerAccount.lastName.$anyDirty && $v.registerAccount.lastName.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.lastName.required"
                v-text="$t('settings.messages.validate.lastname.required')"
              >
                Your last name is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.lastName.minLength"
                v-text="$t('settings.messages.validate.lastname.minlength')"
              >
                Your last name is required to be at least 1 character.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.registerAccount.lastName.maxLength"
                v-text="$t('settings.messages.validate.lastname.maxlength')"
              >
                Your last name cannot be longer than 50 characters.
              </small>
            </div>
          </div>


          <div class="form-group">
            <label class="form-control-label" for="register-userType" >Tipo de Usuario</label>
            <!--   :class="{ valid: !$v.registerAccount.userType.$invalid, invalid: $v.registerAccount.userType.$invalid }"  -->
            <select class="form-control" id="register-userType" data-cy="paciente" name="paciente"
                    :class="{ valid: !$v.registerAccount.userType.$invalid, invalid: $v.registerAccount.userType.$invalid }"
                    v-model="registerAccount.userType"
                    required
            >

              <option v-bind:value="null"></option>
              <option
                v-bind:value="registerAccount.userType && userTypeElement === registerAccount.userType ? registerAccount.userType : userTypeElement"
                v-for="userTypeElement in ['Paciente', 'Familiar']"
                :key="userTypeElement"
              >
                {{ userTypeElement }}
              </option>
            </select>

            <div v-if="!registerAccount.userType && $v.registerAccount.email.$anyDirty">
              <small
                class="form-text text-danger"
                v-if="!registerAccount.userType">
                El tipo de usuario es requerido
              </small>
            </div>
          </div>
          <div id="familiar" v-if="registerAccount.userType === 'Familiar'">


            <h2
              id="homecareApp.familiar.home.createOrEditLabel">
              Datos Personales
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
                    v-text="$t('entity.validation.minlength', { min: 5 })">
                    This field is required to be at least 5 characters.
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
          <div id="paciente" v-if="registerAccount.userType === 'Paciente'">

            <h2
              id="homecareApp.paciente.home.createOrEditLabel"
              data-cy="PacienteCreateUpdateHeading"
            >
              Datos Personales
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
          <button type="submit" :disabled="!formIsValid()" class="btn btn-primary" v-text="$t('register.form.button')" data-cy="submit">
            Register
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./register.component.ts"></script>
