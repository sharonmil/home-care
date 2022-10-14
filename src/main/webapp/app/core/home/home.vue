<template>
  <div class="home row">
    <div class="col-md-3">
      <span class="hipster img-fluid rounded"></span>
    </div>
    <div class="col-md-9">
      <h1 class="display-4" v-text="$t('home.title')" v-if="!authenticated">Welcome, Java Hipster!</h1>
      <h1 class="display-4" v-else>Bienvenido, {{ firstName }}!</h1>
      <p class="lead" v-text="$t('home.subtitle')">This is your homepage</p>
      <div>
        <div class="alert alert-success" v-if="authenticated">
          <span v-if="username" v-text="$t('home.logged.message', { username: username })">You are logged in as user "{{ username }}"</span>
        </div>
        <table class="table table-striped" v-if="authenticated">
          <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">Datos Personales</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <th scope="row">Login:</th>
            <td>{{ username }}</td>
          </tr>
          <tr>
            <th scope="row">Email:</th>
            <td>{{ email }}</td>
          </tr>
          <tr>
            <th scope="row">Nombres:</th>
            <td>{{ firstName }} {{ lastName }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./home.component.ts"></script>
