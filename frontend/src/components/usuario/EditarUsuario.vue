<template>
  <div class="edit-user-container">
    <form @submit.prevent="actualizarUsuario" class="edit-user-form">
      <h3 class="edit-user-title">Editar usuario</h3>
      <div class="form-field">
        <label>Usuario</label>
        <input type="text" class="form-control" v-model="usuario.username" placeholder="Username" required>
      </div>
      <div class="form-field">
        <label>Nombre</label>
        <input type="text" class="form-control" v-model="usuario.firstname" placeholder="First name" required>
      </div>
      <div class="form-field">
        <label>Apellido</label>
        <input type="text" class="form-control" v-model="usuario.lastname" placeholder="Last name" required>
      </div>
      <div class="form-field">
        <label>Telefono</label>
        <input type="text" class="form-control" v-model="usuario.phone" placeholder="Telefono" required>
      </div>
      <div class="buttons">
        <button class="submit-button">Actualizar</button>
        <button class="cancel-button" @click="cancel">Cancelar</button>
        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      </div>
    </form>
  </div>
</template>
<script>
import axios from '../../axios';
export default {
  data() {
    return {
      usuario: [],
    };
  },
  async created() {
    const usuarioId = this.$route.params.id;
    if(usuarioId) {
      await this.cargarUsuario(usuarioId);
    }
  },
  methods: {
    async cargarUsuario(usuarioId) {
      try {
        const response = await axios.get(`api/usuario/${usuarioId}`);
        this.usuario = response.data;
      } catch (error) {
        this.errorMessage = 'Error al obtener los datos del libro';
      }
    },
    async actualizarUsuario() {
      try {
        const response = await axios.put(`api/usuario/${this.usuario.id}`, this.usuario);
        console.log('Usuario actualizado:', response.data);
        this.$router.push('/user'); 
      } catch (error) {
        if (error.response && error.response.status === 400) {
          this.errorMessage = error.response.data; 
        } else {
          this.errorMessage = 'Error al actualizar el libro'; 
        }
      }
    },
    cancelar() {
      this.$router.push('/user');
    }
  },
};
</script>
<style scoped>
  .edit-user-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 95vh;
    background-color: #f2f2f2;
  }

  .edit-user-form {
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
    width: 400px;
  }

  .edit-user-title {
    text-align: center;
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