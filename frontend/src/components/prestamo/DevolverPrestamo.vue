<template>
  <div class="return-loan-container">
    <form @submit.prevent="devolverPrestamo" class="return-loan-form">
      <h3 class="return-loan-title">Devolver préstamo</h3>
      <div class="form-field">
        <label>Fecha de Devolución</label>
        <input type="date" v-model="fecha_devolucion" required />
      </div>
      <div class="buttons">
        <button type="submit" class="submit-button">Devolver</button>
        <button class="cancel-button" @click="cancelar">Cancelar</button>
        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      </div>
    </form>
  </div>
</template>
<script>
import axios from '../../axios';  
export default {
  name: 'DevolverPrestamo',
  data() {
    return {
      prestamo: {},
      fecha_devolucion: "",
      errorMessage: "",
    };
  },
  async created() {
    const id_prestamo = this.$route.params.id;
    if(id_prestamo) {
      await this.cargarPrestamo(id_prestamo);
    }
  },
  methods: {
    async cargarPrestamo(id_prestamo) {
      try {
        const response = await axios.get(`api/prestamo/${id_prestamo}`);
        this.prestamo = response.data;
        console.log(this.prestamo);
      } catch (error) {
        console.error("Error al cargar el préstamo:", error);
        this.errorMessage = "No se pudo cargar el préstamo.";
      }
    },
    async devolverPrestamo() {
      try {
        const response = await axios.put(`api/prestamo/${this.prestamo.id}/devolver`,
        { fecha_devolucion: this.fecha_devolucion });
        console.log("Préstamo devuelto:", response.data);
        this.$router.push("/prestamo");
      } catch (error) {
        if (error.response) {
          const status = error.response.status;
          if (status === 403) {
            console.error("Acceso denegado:", error);
            this.errorMessage = "No tienes permiso para esta acción.";
          } else if (status === 404) {
            console.error("Préstamo no encontrado:", error);
            this.errorMessage = "El préstamo no fue encontrado.";
          } else {
            console.error("Error durante la operación:", error);
            this.errorMessage = "Error durante la operación.";
          }
        } else {
          console.error("Error sin respuesta del servidor:", error);
          this.errorMessage = "No se pudo conectar con el servidor.";
        }
      }
    },
    cancelar() {
      this.$router.push('/prestamo');
    }
  }
};
</script>  
<style scoped>
  .return-loan-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 92vh;
    background-color: #f2f2f2;
  }

  .return-loan-form {
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
    width: 400px;
  }
  
  .return-loan-title {
    text-align: center;
    margin-bottom: 5px;
  }

  .form-field {
    margin-bottom: 10px;
  }
  
  input {
    width: 377px;
    padding: 10px;
    border-radius: 5px;
    border: 1px solid #ccc;
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
  }
  </style>
  