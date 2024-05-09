<template>
  <div class="login-container">
    <form @submit.prevent="login" class="login-form">
      <h3 class="login-title">Iniciar sesión</h3>
      <div class="form-field">
        <label>Usuario</label>
        <input type="text" v-model="username" placeholder="Usuario" required>
      </div>
      <div class="form-field">
        <label>Contraseña</label>
        <input type="password" v-model="password" placeholder="Contraseña" required>
      </div>
      <button type="submit" class="submit-button">Iniciar sesión</button>
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    </form>
  </div>
</template>

<script>
import axios from '../../axios';
import store from '@/store/vuex'; // Para trabajar con Vuex

export default {
  data() {
    return {
      username: '',
      password: '',
      errorMessage: '', 
    };
  },
  methods: {
    async login() {
      try {
        const response = await axios.post('http://localhost:8080/auth/login', {
          username: this.username,
          password: this.password,
        });
        const token = response.data.token;
        // Guarda el token 
        localStorage.setItem('token', token);
        // Actualiza el estado de autenticación en Vuex. 
        // setAuthentication es una mutación de Vuex q marca al usuario como autenticado.
        store.commit('setAuthentication', true);
        this.$router.push('/');
      } catch (error) {
        this.errorMessage = 'Error al iniciar sesión.';
      }
    },
  },
};
</script>

<style scoped>
  .login-container {
    display: flex;
    justify-content: center;
    align-items: center; 
    height: 92vh; 
    background-color: #f2f2f2; 
  }

  .login-form {
    background-color: white; 
    padding: 20px; 
    border-radius: 5px; 
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
    width: 400px;
  }

  .login-title {
    text-align: center; 
    margin-bottom: 5px;
  }

  .form-field {
    margin-bottom: 15px; 
  }

  input {
    width: 376px; 
    padding: 10px; 
    border: 1px solid #ccc; 
    border-radius: 5px; 
  }


  .submit-button {
    width: 100%; 
    padding: 10px; 
    background-color: #154360; 
    color: white;
    border: none; 
    border-radius: 5px; 
    cursor: pointer; 
  }

  .submit-button:hover {
    background-color: #1A5276; 
  }

  .error-message {
    color: red; 
    text-align: center; 
  }
</style>
