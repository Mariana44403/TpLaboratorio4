<template>
  <div class="loan-container">
    <form @submit.prevent="agregarPrestamo" class="loan-form">
      <h3 class="loan-title">Agregar préstamo</h3>
      <div class="form-field">
        <label>Usuario</label>
        <select v-model="usuarioSeleccionado" required>
          <option value="" disabled selected>Seleccione un usuario: </option>
          <option v-for="usuario in usuarios" :key="usuario.id" :value="usuario.id">
            {{ usuario.username }}
          </option>
        </select>
      </div>
      <div class="form-field">
        <label>Libro</label>
        <select v-model="libroSeleccionado" required>
          <option value="" disabled selected>Seleccionar un libro: </option>
          <option v-for="libro in libros" :key="libro.id" :value="libro.id">
            {{ libro.titulo }}
          </option>
        </select>
      </div>
      <div class="form-field">
        <label>Fecha de préstamo</label>
        <input type="date" v-model="fechaPrestamo" required />
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
  name: 'AgregarPrestamo',
  data() {
    return {
      usuarios: [],
      libros: [],
      usuarioSeleccionado: "", // Campo de selección para el usuario
      libroSeleccionado: "", // Campo de selección para el libro
      fechaPrestamo: null,
      fechaDevolucion: null,
      estado: "",
      errorMessage: "",
    };
  },
  // Este es un "hook" del ciclo de vida de Vue que se ejecuta cuando el componente se crea, antes de que se renderice por primera vez. En este caso, se utiliza para cargar datos iniciales. Usuario y Libros.
  async created() {
    await this.cargarUsuarios();
    await this.cargarLibros();
  },
  methods: {
    async cargarUsuarios() {
      try {
        const response = await axios.get(
          'api/usuario');
        this.usuarios = response.data;
      } catch (error) {
        console.error("Error al cargar usuarios:", error);
      }
    },
    async cargarLibros() {
      try {
        const response = await axios.get(
          'api/libro');
        
        // Filtra los libros q tengan el estado "Disponible"
        this.libros = response.data.filter(libro => libro.estado === "Disponible");
      } catch (error) {
        console.error("Error al cargar libros:", error);
      }
    },
    async agregarPrestamo() {
      try {
        const prestamo = {
          id_usuario: this.usuarioSeleccionado,
          id_libro: this.libroSeleccionado,
          fecha_prestamo: this.fechaPrestamo,
        };
      
        const response = await axios.post(
        'api/prestamo', 
        prestamo);

        console.log("Préstamo agregado:", response.data);
        this.$router.push("/prestamo"); 
      } catch (error) {
        if (error.response && error.response.status === 400) {
          this.errorMessage = "Datos no válidos.";
        } else {
          this.errorMessage = "Error al crear el préstamo.";
        }
      }
    }
  },
};
</script>

<style scoped>
.loan-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f2f2f2;
}

.loan-form {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  width: 400px;
}

.loan-title {
  text-align: center;
}

.form-field {
  margin-bottom: 15px;
}

select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

input{
  width: 377px;
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
