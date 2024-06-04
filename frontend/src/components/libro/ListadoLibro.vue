<template>
  <div class="book-list-container">
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

    <!-- ventana para libros que pueden ser eliminados -->
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

    <!-- ventana para libros que tienen préstamos asociados -->
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
        this.librosFiltrados = this.libros; // inicializa la lista de libros filtrados
      } catch (error) {
        console.error("Error al cargar libros:", error);
      }
    },
    seleccionarFila(libro) {
      this.filaSeleccionada = libro; // guardar el libro seleccionado
    },
    async verificarYMostrarConfirmacion() {
      if (this.filaSeleccionada) {
        try {
          const response = await axios.get(
            `api/libro/${this.filaSeleccionada.id}/tienePrestamos`);

          if (response.data) { // si tiene préstamos asociados
            this.mensajePrestamos = "Este libro tiene préstamos activos y no puede eliminarse.";
            this.mostrarDialogoPrestamos = true; // mostrar el diálogo de préstamo
            this.mostrarDialogoEliminacion = false; // ocultar el diálogo de eliminación
          } else {
            this.mensajeConfirmacion = "¿Desea eliminar este libro?";
            this.permiteEliminacion = true; 
            this.mostrarDialogoPrestamos = false; 
            this.mostrarDialogoEliminacion = true; 
            this.$nextTick(() => {
              if (this.$refs.dialogoEliminacion) {
                this.$refs.dialogoEliminacion.showModal(); // mostrar el diálogo
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
          await this.cargarLibros(); // recargar después de eliminar un libro
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
  justify-content: space-between; 
  align-items: center;
}

.action-buttons {
  display: flex;
  margin-bottom: 5px;
}

.action-buttons button {
  margin-left: 10px; 
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
