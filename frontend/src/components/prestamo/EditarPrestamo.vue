<template>
  <div class="edit-loan-container">
    <form @submit.prevent="actualizarPrestamo" class="edit-loan-form"> 
      <h3 class="edit-loan-title">Editar prestamo</h3>
      <div class="form-field">
        <label>Seleccione un libro:</label>
        <select v-model="prestamo.id_libro">
          <option v-for="libro in libros" :key="libro.id" :value="libro.id">
            {{ libro.titulo }}
          </option>
        </select>
      </div>
      <div class="form-field">
        <label>Seleccione un usuario:</label>
        <select id="usuario" v-model="prestamo.id_usuario">
            <option v-for="usuario in usuarios" :key="usuario.id" :value="usuario.id">
                {{ usuario.username }} 
            </option>
        </select>
      </div>
      <div class="form-field">
        <label>Fecha del préstamo:</label>
        <input type="date" id="fecha_prestamo" v-model="prestamo.fecha_prestamo" required>
      </div>
      <div class="form-field" v-if="mostrarFechaDevolucion">
        <label>Fecha de devolución:</label>
        <input type="date" id="fecha_devolucion" v-model="prestamo.fecha_devolucion">
      </div>
      <div class="buttons">
        <button type="submit" class="submit-button">Actualizar</button> 
        <button class="cancel-button" @click="cancelar">Cancelar</button>
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
      usuarios: [], 
      libros: [],   
      prestamo: {}, 
      errorMessage: "", 
    };
  },
  computed: {
    mostrarFechaDevolucion() {
      return this.prestamo.fecha_devolucion !== null;
    }
  },
  async created() {
    await this.cargarUsuarios(); 
    await this.cargarLibros();   
    const prestamoId = this.$route.params.id; // obtiene el ID del préstamo de la URL
    if (prestamoId) {
      await this.cargarPrestamo(prestamoId); // carga el prestamo a modificar
    }
  },
  methods: {
    async cargarUsuarios() {
      try {
        const response = await axios.get('api/usuario');
        this.usuarios = response.data; // guarda los usuarios en la data
      } catch (error) {
        console.error("Error al cargar usuarios:", error);
      }
    },
    async cargarLibros() {
      try {
        const response = await axios.get('api/libro');
        this.libros = response.data; 
      } catch (error) {
        console.error("Error al cargar libros:", error);
      }
    },
    async cargarPrestamo(prestamoId) {
      try {
        const response = await axios.get(`api/prestamo/${prestamoId}`);
        this.prestamo = response.data;
      } catch (error) {
        console.error("Error al cargar el préstamo:", error);
        this.errorMessage = "No se pudo cargar el préstamo.";
      }
    },
    async actualizarPrestamo() {
      try {
        const prestamoActualizado = {
          id: this.prestamo.id, 
          id_libro: this.prestamo.id_libro,
          id_usuario: this.prestamo.id_usuario, 
          fecha_prestamo: this.prestamo.fecha_prestamo,
          fecha_devolucion: this.prestamo.fecha_devolucion,
          estado: this.prestamo.estado,
        };
        const response = await axios.put(
          `api/prestamo/${this.prestamo.id}`, 
          prestamoActualizado
        );
        console.log("Prestamo actualizado:", response.data);
        this.$router.push("/prestamo"); 
      } catch (error) {
        console.error("Error al actualizar el préstamo:", error);
        this.errorMessage = "No se pudo actualizar el préstamo.";
      }
    },
    cancelar() {
      this.$router.push("/prestamo");
    }
  },
};
</script>
<style scoped>
  .edit-loan-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 92vh;
    background-color: #f2f2f2;
  }

  .edit-loan-form {
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
    width: 400px;
  }

  .edit-loan-title {
    text-align: center;
  }

  .form-field {
    margin-bottom: 15px;
  }

  select{
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
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