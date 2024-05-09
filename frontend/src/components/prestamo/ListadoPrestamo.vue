<template>
  <div class="loan-list-container">
    <!-- Encabezado -->
    <div class="header">
      <h2>Lista de prestamos</h2>
      <div class="action-buttons">
        <button @click="agregarPrestamo">Agregar</button>
        <input type="text" placeholder="Buscar usuario, libro, fecha" v-model="searchQuery" @input="filtrarPrestamo"/>
        <button :disabled="!filaSeleccionada" @click="editarPrestamo">Editar</button>
        <button :disabled="!filaSeleccionada" @click="verificarYMostrarConfirmacion">Eliminar</button>
        <button @click="devolverPrestamo">Realizar devolución</button>
      </div>
    </div>

    <!-- Tabla de prestamos -->
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
          <td>{{ prestamo.usuario ? prestamo.usuario.username : 'Usuario desconocido' }}</td> 
          <td>{{ prestamo.libro ? prestamo.libro.titulo : 'Libro desconocido' }}</td> 
          <td>{{ prestamo.fecha_prestamo }}</td>
          <td>{{ prestamo.fecha_devolucion }}</td>
          <td>{{ prestamo.estado }}</td> 
        </tr>
      </tbody>
    </table>

    <!-- Ventana para prestamos q pueden ser eliminados -->
    <dialog ref="dialogoEliminacion" v-if="mostrarDialogoEliminacion">
      <p>{{ mensajeConfirmacion }}</p>
      <div class="dialog-buttons">
        <button class="dialog-si" @click="eliminarPrestamo">Sí, eliminar</button>
        <button class="dialog-no" @click="cancelarEliminacion">No, cancelar</button>
      </div>
    </dialog>

    <!-- Ventana para prestamos q estan activos, y no pueden eliminarse -->
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
    await this.cargarPrestamos(); // Cargar todos los préstamos al inicio
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
          this.mostrarDialogoEliminacion = false; // Desactiva el diálogo
          this.$refs.dialogoEliminacion.close(); // Cerrar el diálogo después de eliminar
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
            // El préstamo está activo
            this.mensajePrestamos = "Este préstamo está activo y no puede ser eliminado.";
            this.mostrarDialogoPrestamos = true;
            this.mostrarDialogoEliminacion = false;
          } else {
            // El préstamo no está activo, se puede eliminar
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
    filtrarPrestamo() {
      const query = this.searchQuery.toLowerCase();
      this.prestamosFiltrados = this.prestamos.filter((prestamo) => {
        const usuarioNombre = prestamo.usuario?.username?.toLowerCase() || ""; // Asegúrate de que usuario existe
        const libroTitulo = prestamo.libro?.titulo?.toLowerCase() || ""; // Asegúrate de que libro existe
        const fechaPrestamo = prestamo.fecha_prestamo.toLowerCase();
        const fechaDevolucion = prestamo.fecha_devolucion?.toLowerCase() || "";
        return (
          usuarioNombre.includes(query) ||
          libroTitulo.includes(query) ||
          fechaPrestamo.includes(query) ||
          fechaDevolucion.includes(query)
        );
      });
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
  justify-content: space-between; /* Para alinear el título y los botones */
  align-items: center;
}

.action-buttons {
  display: flex;
  margin-bottom: 5px;
}

.action-buttons button {
  margin-left: 10px; /* Espaciado entre botones */
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
  background-color: #E5E8E8; /* Para resaltar la fila seleccionada */
}

dialog {
  display: flex;
  flex-direction: column; /* Los elementos dentro del diálogo se alinean verticalmente */
  justify-content: center;
  align-items: center;
  background-color: white;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.dialog-buttons {
  display: flex; /* Alineación horizontal */
  flex-direction: row; /* Botones uno al lado del otro */
  justify-content: space-between; /* Espaciado uniforme entre botones */
}

dialog button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  background-color: #f44336; /* Color rojo para el botón de eliminación */
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
  background-color:#BFC9CA; /* Cambia el color cuando se pasa el ratón */
}

.dialog-si:hover {
  background-color:#C0392B; /* Cambia el color cuando se pasa el ratón */
}

.dialog-buttons button {
  margin: 0 10px; /* Margen entre botones para mantener el espacio */
}

</style>
