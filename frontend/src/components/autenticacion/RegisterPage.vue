<template>
  <div class="register-container">
    <form @submit.prevent="registerUser" class="register-form">
      <h3 class="register-title">Registrarse</h3>
      <div class="form-field">
        <label>Usuario</label>
        <input type="text" class="form-control" v-model="username" placeholder="Usuario" required>
      </div>
      <div class="form-field">
        <label>Contraseña</label>
        <input type="password" class="form-control" v-model="password" placeholder="Contraseña" required>
      </div>
      <div class="form-field">
        <label>Nombre</label>
        <input type="text" class="form-control" v-model="firstname" placeholder="Nombre" required>
      </div>
      <div class="form-field">
        <label>Apellido</label>
        <input type="text" class="form-control" v-model="lastname" placeholder="Apellido" required>
      </div>
      <div class="form-field">
        <label>Telefono</label>
        <input type="text" class="form-control" v-model="phone" placeholder="Telefono" required>
      </div>
      <button class="submit-button">Registrarse</button>
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    </form>
  </div>
</template>
<script>
import axios from '../../axios';
export default {
  name: 'RegisterPage',
  data() {
    return {
      username: '',
      password: '',
      firstname: '',
      lastname: '',
      phone: '',
      errorMessage: '', 
    };
  },
  methods: {
    async registerUser() {
      try {
        const response = await axios.post('auth/register', {
          username: this.username,
          password: this.password,
          firstname: this.firstname,
          lastname: this.lastname,
          phone: this.phone,
        });

        console.log('Administrador registrado:', response.data);
        this.$router.push('/login'); // redirige después del registro
      } catch (error) {
        if (error.response && error.response.status === 400) {
          this.errorMessage = error.response.data; // muestra el mensaje de error del backend
        } else {
          this.errorMessage = 'Error durante la registración.'; // mensaje de error generico
        }
      }
    },
  },
};
</script>
<style scoped>
  .register-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 92vh;
    background-color: #f2f2f2;
  }

  .register-form {
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
    width: 400px;
  }

  .register-title {
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