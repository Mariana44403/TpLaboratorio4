<template>
  <nav class="nav-bar">
    <div class="nav-container">
      <h3 class="nav-brand">Biblioteca</h3>
      <div class="nav-links">
        <!-- enlaces para usuarios autenticados -->
        <ul v-if="isAuthenticated">
          <li>
            <router-link to="/" class="nav-link">Inicio</router-link>
          </li>
          <li>
            <router-link to="/books" class="nav-link">Libro</router-link>
          </li>
          <li>
            <router-link to="/user" class="nav-link">Usuario</router-link>
          </li>
          <li>
            <router-link to="/prestamo" class="nav-link">Prestamo</router-link>
          </li>
          <li>
            <a href="javascript:void(0)" @click="handleLogout" class="nav-link">Cerrar sesión</a>
          </li>
        </ul>
        <!-- enlaces para usuarios no autenticados -->
        <ul v-else>
          <li>
            <router-link to="/login" class="nav-link">Iniciar sesión</router-link>
          </li>
          <li>
            <router-link to="/register" class="nav-link">Registrarse</router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'NavBar',
  computed: {
    ...mapGetters(['isAuthenticated']), // obtiene el estado de autenticación desde Vuex
  },
  methods: {
    handleLogout() {
      localStorage.removeItem('token'); // eliminar el token para cerrar sesión
      this.$store.dispatch('logout'); // acción de Vuex para cerrar sesión
      this.$router.push('/login'); // redirigir a la página de login
    },
  },
};
</script>
<style scoped>
  .nav-bar {
    background-color: #154360;
    color: #fff;
    padding: 10px 20px; 
  }

  .nav-container {
    display: flex; 
    justify-content: space-between; 
    align-items: center;
  }

  .nav-brand {
    font-size: 1.5em; 
  }

  .nav-links {
    display: flex; 
  }

  .nav-links ul {
    display: flex; 
    list-style: none; 
    padding: 0; 
    margin: 0; 
  }

  .nav-links ul li {
    margin-right: 20px; 
    font-size: 16px;
  }

  .nav-links ul li:last-child {
    margin-right: 0; 
  }

  .nav-link {
    text-decoration: none; 
    color: #fff; 
  }

  .nav-link:hover {
    text-decoration: underline;
  }
</style>

