<template>
    <div class="user-container">
        <form @submit.prevent="agregarUsuario" class="user-form">
            <h3 class="user-title">Agregar usuario</h3>
            <div class="form-field">
                <label>Usuario</label>
                <input type="text" v-model="usuario.username" placeholder="Usuario" required>
            </div>
            <div class="form-field">
                <label>Nombre</label>
                <input type="text" v-model="usuario.firstname" placeholder="Nombre" required>
            </div>
            <div class="form-field">
                <label>Apellido</label>
                <input type="text" v-model="usuario.lastname" placeholder="Apellido" required>
            </div>
            <div class="form-field">
                <label>Telefono</label>
                <input type="text" v-model="usuario.phone" placeholder="Telefono" required>
            </div>
            <div class="buttons">
              <button class="submit-button">Agregar</button>
              <button class="cancel-button" @click="cancelar">Cancelar</button>
              <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
            </div>
        </form>
    </div>
</template>
<script>
import axios from '../../axios';

export default {
  name: 'AgregarUsuario',
  data() {
    return {
        usuario: {
          username: '',
          firstname: '',
          lastname: '',
          phone: '',
        },
      errorMessage: '', // Para mostrar mensajes de error
    };
  },
  methods: {
    async agregarUsuario() {
      try {
        const response = await axios.post(
          'api/usuario', 
          this.usuario
        );
        console.log('Usuario agregado:', response.data);
        this.$router.push('/user'); // Redirige después del registro
      } catch (error) {
        if (error.response && error.response.status === 400) {
          this.errorMessage = error.response.data; // Muestra el mensaje de error del backend
        } else {
          this.errorMessage = 'An error occurred during registration.'; // Mensaje genérico para otros errores
        }
      }
    },
    cancelar() {
      this.$router.push("/user");
    }
  },
};
</script>
<style scoped>
.user-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f2f2f2;
}

.user-form {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  width: 400px;
}

.user-title {
  text-align: center;
}

.form-field {
  margin-bottom: 15px;
}

input {
  width: 378px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.buttons {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.buttons button {
  width: 49%;
  padding: 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.submit-button {
  background-color: #154360;
  color: white;
}

.submit-button:hover {
  background-color: #1A5276;
}

.cancel-button {
  background-color: #E5E7E9;
  color: #000;
}

.cancel-button:hover {
  background-color: #dee2e2;
}

.error-message {
  color: red;
  text-align: center;
}
</style>
