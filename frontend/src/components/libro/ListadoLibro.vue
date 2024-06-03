<template>
  <div class="book-list-container">
    <!-- Encabezado -->
    <div class="header">
      <h2>Lista de libros</h2>
      <div class="action-buttons">
        <button @click="agregarLibro">Agregar</button>
        <input
  type="text"
  placeholder="Buscar por título, autor o género"
  v-model="searchQuery"
  @input="filtrarLibros"
/>
        <button
          :disabled="!filaSeleccionada"
          @click="editarLibroSeleccionado"
        >
          Editar
        </button>
        <button
          :disabled="!filaSeleccionada"
          @click="verificarYMostrarConfirmacion"
        >
          Eliminar
        </button>
      </div>
    </div>

    <!-- Tabla de libros -->
    <table>
      <thead>
        <tr>
          <th>Título</th>
          <th>Autor</th>
          <th>Género</th>
          <th>Páginas</th>
          <th>Sinopsis</th>
        </tr>
      </thead>
      <tbody>
        <!-- Crea filas dinámicamente para cada libro en librosFiltrados, proporciona un evento de clic para seleccionar la fila -->
        <tr
          v-for="libro in librosFiltrados"
          :key="libro.id"
          @click="seleccionarFila(libro)"
          :class="{ selected: filaSeleccionada?.id === libro.id }"
        >
          <td>{{ libro.titulo }}</td>
          <td>{{ libro.autor }}</td>
          <td>{{ libro.genero }}</td>
          <td>{{ libro.num_pagina }}</td>
          <td>{{ libro.sinopsis }}</td>
        </tr>
      </tbody>
    </table>

    <!-- Ventana para libros que pueden ser eliminados -->
    <dialog ref="dialogoEliminacion" v-if="mostrarDialogoEliminacion">
      <p>{{ mensajeConfirmacion }}</p>
      <div class="dialog-buttons">
        <button
          class="dialog-si"
          @click="eliminarLibro"
        >
          Sí, eliminar
        </button>
        <button class="dialog-no" @click="cancelarEliminacion">No, cancelar</button>
      </div>
    </dialog>

    <!-- Ventana para libros que tienen préstamos asociados -->
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
      searchQuery: "",
      libros: [],
      librosFiltrados: [],
      filaSeleccionada: null,
      mensajeConfirmacion: "",
      permiteEliminacion: false,
      mostrarDialogoEliminacion: false,
      mostrarDialogoPrestamos: false,
    };
  },
  async created() {
    await this.cargarLibros(); 
    this.librosFiltrados = this.libros;
  },
  methods: {
    async cargarLibros() {
      try {
        const response = await axios.get('api/libro');
        this.libros = response.data;
        this.librosFiltrados = this.libros; // Inicializa la lista de libros filtrados
      } catch (error) {
        console.error("Error al cargar libros:", error);
      }
    },
    seleccionarFila(libro) {
      this.filaSeleccionada = libro; // Guardar el libro seleccionado
    },
    async verificarYMostrarConfirmacion() {
      if (this.filaSeleccionada) {
        try {
          const response = await axios.get(
            `api/libro/${this.filaSeleccionada.id}/tienePrestamos`);

          if (response.data) { // Si tiene préstamos asociados
            this.mensajePrestamos = "Este libro tiene préstamos activos y no puede eliminarse.";
            this.mostrarDialogoPrestamos = true; // Mostrar el diálogo de préstamo
            this.mostrarDialogoEliminacion = false; // Ocultar el diálogo de eliminación
          } else {
            this.mensajeConfirmacion = "¿Desea eliminar este libro?";
            this.permiteEliminacion = true; 
            this.mostrarDialogoPrestamos = false; // No mostrar el diálogo de préstamo
            this.mostrarDialogoEliminacion = true; // Mostrar el diálogo de eliminación
            this.$nextTick(() => {
              if (this.$refs.dialogoEliminacion) {
                this.$refs.dialogoEliminacion.showModal(); // Mostrar el diálogo
              }
            });
          }
        } catch (error) {
          console.error("Error al verificar préstamos asociados:", error);
        }
      }
    },
    async eliminarLibro() {
      if (this.permiteEliminacion && this.filaSeleccionada) {
        try {
          await axios.delete(
            `api/libro/${this.filaSeleccionada.id}`);
          this.cancelarEliminacion();
          await this.cargarLibros(); // Recargar después de eliminar un libro
        } catch (error) {
          console.error("Error al eliminar el libro:", error);
        }
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
    agregarLibro() {
      this.$router.push("/agregarLibro");
    },
    editarLibroSeleccionado() {
      if (this.filaSeleccionada) {
        this.$router.push(`/editarLibro/${this.filaSeleccionada.id}`);
      }
    },
    async filtrarLibros() {
    const query = this.searchQuery.toLowerCase();

    try {
      const response = await axios.get(`api/libro/search`, {
        params: { query }
      });
      this.librosFiltrados = response.data;
    } catch (error) {
      console.error("Error al filtrar libros:", error);
    }
  },
  },
};
</script>
<style scoped>
.book-list-container {
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
  padding: 10px 10px; 
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
