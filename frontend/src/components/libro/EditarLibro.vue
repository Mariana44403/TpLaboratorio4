<template>
  <div class="edit-book-container">
    <form @submit.prevent="actualizarLibro" class="edit-book-form">
      <h3 class="edit-book-title">Editar libro</h3>
      <div class="form-field">
        <label>Título</label>
        <input type="text" v-model="libro.titulo" required />
      </div>
      <div class="form-field">
        <label>Autor</label>
        <input type="text" v-model="libro.autor" required />
      </div>
      <div class="form-field">
        <label>Género</label>
        <input type="text" v-model="libro.genero" required />
      </div>
      <div class="form-field">
        <label>Número de páginas</label>
        <input type="number" v-model="libro.num_pagina" required />
      </div>
      <div class="form-field">
        <label>Sinopsis</label>
        <input type="text" v-model="libro.sinopsis" required />
      </div>
      <div class="buttons">
        <button type="submit" class="submit-button">Actualizar</button> 
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
      libro: [],
    };
  },
  async created() {
    const libroId = this.$route.params.id_libro; // Obtener el ID desde la ruta
    console.log("ID del libro:", libroId); // Verifica el ID
    if(libroId) {
      await this.cargarLibro(libroId);
    }
  },
  methods: {
    async cargarLibro(libroId) {
      try {
        const response = await axios.get(`api/libro/${libroId}`); // Intenta obtener el libro
        console.log("Respuesta del servidor:", response); // Muestra la respuesta
        this.libro = response.data; // Almacena los datos del libro
        console.log(this.libro);
      } catch (error) {
        console.error('Error cargando el libro:', error); // Muestra detalles del error
      }
    },
    async actualizarLibro() {
      try {
        const response = await axios.put(
          `api/libro/${this.libro.id}`,
          this.libro
        );
        console.log('Libro actualizado:', response.data);
        this.$router.push('/books');
      } catch (error) {
        if (error.response && error.response.status === 403) {
          console.error('Error: Acceso prohibido. Verifique sus permisos.'); // Mensaje para error 403
        } else {
          console.error('Error actualizando el libro:', error); // Otros errores
        }
      }
    },
    cancelar() {
      this.$router.push('/books');
    }
  },
};
</script>
<style scoped>
  .edit-book-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 92vh;
    background-color: #f2f2f2;
  }
  
  .edit-book-form {
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
    width: 400px;
  }
  
  .edit-book-title {
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