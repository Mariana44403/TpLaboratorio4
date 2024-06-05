<template>
  <div class="loan-list-container">
    <div class="header">
      <h2>Lista de prestamos</h2>
      <div class="action-buttons">
        <button @click="agregarPrestamo">Agregar</button>
        <input type="text" placeholder="Buscar usuario, libro, fecha" v-model="searchQuery" @input="filtrarPrestamo"/>
        <button :disabled="!filaSeleccionada" @click="editarPrestamo">Editar</button>
        <button :disabled="!filaSeleccionada" @click="verificarYMostrarConfirmacion">Eliminar</button>
        <button 
          :disabled="!filaSeleccionada || esDevuelto(filaSeleccionada.estado)" 
          @click="devolverPrestamo">
          Realizar devolución
        </button>
      </div>
    </div>
    
    <table>
      <thead>
        <tr>
          <th>Usuario</th>
          <th>Libro</th>
          <th>Fecha de Préstamo</th>
          <th>Fecha de Devolución</th>
          <th>Estado</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="prestamo in prestamosFiltrados" :key="prestamo.id" @click="seleccionarFila(prestamo)" :class="{ selected: filaSeleccionada?.id === prestamo.id }" >
          <td>{{ prestamo.usuario ? prestamo.usuario.username : prestamo.nombreUsuario }}</td>
          <td>{{ prestamo.libro ? prestamo.libro.titulo : prestamo.libroTitulo }}</td>
          <td>{{ prestamo.fecha_prestamo }}</td>
          <td>{{ prestamo.fecha_devolucion }}</td>
          <td>{{ prestamo.estado }}</td> 
        </tr>
      </tbody>
    </table>

    <!-- ventana para prestamos q pueden ser eliminados -->
    <dialog ref="dialogoEliminacion" v-if="mostrarDialogoEliminacion">
      <p>{{ mensajeConfirmacion }}</p>
      <div class="dialog-buttons">
        <button class="dialog-si" @click="eliminarPrestamo">Sí, eliminar</button>
        <button class="dialog-no" @click="cancelarEliminacion">No, cancelar</button>
      </div>
    </dialog>

    <!-- ventana para prestamos q estan activos, y no pueden eliminarse -->
    <dialog ref="dialogoPrestamos" v-if="mostrarDialogoPrestamos">
      <p>{{ mensajePrestamos }}</p>
      <div class="dialog-buttons">
        <button class="dialog-no" @click="cancelarPrestamos">Aceptar</button>
      </div>
    </dialog>
  </div>  
</template>
  
<script>
import axios from '../../axios';
export default {
  data() {
    return {
      prestamos: [],
      filaSeleccionada: null,
      searchQuery: "",
      prestamosFiltrados: [],
      mensajeConfirmacion: "",
      permiteEliminacion: false,
      mostrarDialogoEliminacion: false,
      mostrarDialogoPrestamos: false,
    };
  },
  async created() {
    await this.cargarPrestamos(); 
    this.prestamosFiltrados = this.prestamos;
  },
  methods: {
    async cargarPrestamos() {
      try {
        const response = await axios.get(
          'api/prestamo');
        this.prestamos = response.data;
        this.prestamosFiltrados = response.data;
      } catch (error) {
        console.error("Error al cargar los préstamos:", error);
      }
    },
    async agregarPrestamo() {
      this.$router.push("/agregarPrestamo");
    },
    seleccionarFila(prestamo) {
      this.filaSeleccionada = prestamo;
    },
    async editarPrestamo() {
      if (this.filaSeleccionada) {
        this.$router.push(`/editarPrestamo/${this.filaSeleccionada.id}`);
      }
    },
    async devolverPrestamo() {
      if (this.filaSeleccionada) {
        this.$router.push(`/devolverPrestamo/${this.filaSeleccionada.id}`);
      }
    },
    async eliminarPrestamo() {
      if (this.filaSeleccionada) {
        try {
          const response = await axios.delete(
            `api/prestamo/${this.filaSeleccionada.id}`);
          console.log(response);
          this.cargarPrestamos();
          this.mostrarDialogoEliminacion = false; 
          this.$refs.dialogoEliminacion.close(); 
        } catch (error) {
          console.error("Error al eliminar préstamo:", error);
        }
      }
    },
    async verificarYMostrarConfirmacion() {
      if (this.filaSeleccionada) {
        try {
          const response = await axios.get(
            `api/prestamo/${this.filaSeleccionada.id}/activo`);
          console.log(response);
          if (response.data) {
            // el préstamo está activo
            this.mensajePrestamos = "Este préstamo está activo y no puede ser eliminado.";
            this.mostrarDialogoPrestamos = true;
            this.mostrarDialogoEliminacion = false;
          } else {
            // el préstamo no está activo, se puede eliminar
            this.mensajeConfirmacion = "¿Está seguro de que desea eliminar este préstamo?";
            this.permiteEliminacion = true;
            this.mostrarDialogoPrestamos = false;
            this.mostrarDialogoEliminacion = true;
            this.$nextTick(() => {
              if (this.$refs.dialogoEliminacion) {
                this.$refs.dialogoEliminacion.showModal();
              }
            });
          }
        } catch (error) {
          console.error("Error al verificar el estado del préstamo:", error);
        }
      }
    },
    async filtrarPrestamo() {
      const query = this.searchQuery.toLowerCase();

      try {
        const response = await axios.get(`api/prestamo/search`, {
          params: { query }
        });
        this.prestamosFiltrados = response.data;
      } catch (error) {
        console.error("Error al filtrar prestamos: ", error);
      }
    },
    cancelarEliminacion() {
      this.mostrarDialogoEliminacion = false; 
      if (this.$refs.dialogoEliminacion) {
        this.$refs.dialogoEliminacion.close(); 
      }
    },
    cancelarPrestamos() {
      this.mostrarDialogoPrestamos = false;
      if (this.$refs.dialogoPrestamos) {
        this.$refs.dialogoPrestamos.close();
      }
    },
    esDevuelto(estado) {
      return estado && estado.toLowerCase() === 'devuelto';
    }
  }
};
</script>  
<style scoped>
.loan-list-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between; 
  align-items: center;
}

.action-buttons {
  display: flex;
  margin-bottom: 5px;
}

.action-buttons button {
  margin-left: 10px; 
  padding: 3px 20px; 
  background-color: #1F618D; 
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 6px;
  width: 100px;
}

.action-buttons input {
  margin-left: 10px;
  border-radius: 6px;
  padding: 10px;
}


.action-buttons button:hover {
  background-color: #154360; 
}

table {
  width: 100%; 
  border-collapse: collapse; 
}

th, td {
  padding: 10px; 
  text-align: left; 
  border-bottom: 1px solid #ddd; 
}

th {
  background-color: #1F618D; 
  color: white;
  font-weight: bold; 
}

.selected {
  background-color: #E5E8E8; 
}

dialog {
  display: flex;
  flex-direction: column; 
  justify-content: center;
  align-items: center;
  background-color: white;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.dialog-buttons {
  display: flex; 
  flex-direction: row; 
  justify-content: space-between; 
}

dialog button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  background-color: #f44336; 
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
  margin: 10px;
}

.dialog-no {
  background-color: #E5E7E9;
  color: black;
}

.dialog-no:hover {
  background-color:#BFC9CA;
}

.dialog-si:hover {
  background-color:#C0392B; 
}

.dialog-buttons button {
  margin: 0 10px; 
}

</style>
