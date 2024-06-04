<template>
  <div class="book-container">
    <form @submit.prevent="agregarLibro" class="book-form">
      <h3 class="book-title">Agregar libro</h3> 
      <div class="form-field">
        <label>Título</label>
        <input type="text" v-model="libro.titulo" placeholder="Título" required />
      </div>
      <div class="form-field">
        <label>Autor</label>
        <input type="text" v-model="libro.autor" placeholder="Autor" required />
      </div>
      <div class="form-field">
        <label>Género</label>
        <input type="text" v-model="libro.genero" placeholder="Género" required />
      </div>
      <div class="form-field">
        <label>Número de páginas</label>
        <input type="number" v-model="libro.num_pagina" placeholder="Número de páginas" required />
      </div>
      <div class="form-field">
        <label>Sinopsis</label>
        <input type="text" v-model="libro.sinopsis" placeholder="Sinopsis" required />
      </div>
      <div class="buttons">
        <button type="submit" class="submit-button">Agregar</button>
        <button class="cancel-button" @click="cancelar">Cancelar</button>
        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      </div>
    </form>
  </div>
</template>

<script>
import axios from '../../axios';

export default {
  name: 'AgregarLibro',
  data() {
    return {
      libro: {
        titulo: '',
        autor: '',
        genero: '',
        num_pagina: 0,
        sinopsis: '',
      },
      errorMessage: '',
    };
  },
  methods: {
    // función asíncrona, que utiliza axios para realizar una solicitud HTTP de tipo POST a un servidor
    async agregarLibro() {
      try {
        // Hacer una solicitud POST para agregar un nuevo libro
        const response = await axios.post(
          '/api/libro', // endpoint para agregar libros
          this.libro // datos del libro a enviar
        );
        console.log('Libro agregado:', response.data);
        // redirigir a la lista de libros después de guardar
        this.$router.push('/books'); 
      } catch (error) {
        if (error.response && error.response.status === 400) {
          this.errorMessage = error.response.data;
        } else {
          this.errorMessage = 'Error durante la operación.';
        }
      }
    },
    cancelar() {
      this.$router.push("/books");
    } 
  },
};
</script>

<style scoped>
.book-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 92vh;
  background-color: #f2f2f2;
}

.book-form {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  width: 400px;
}

.book-title {
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
