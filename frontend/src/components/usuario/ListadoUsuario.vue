<template>
  <div class="user-list-container">
    <!-- Encabezado -->
    <div class="header">
      <h2>Lista de Usuarios</h2>
      <div class="action-buttons">
        <button @click="agregarUsuario">Agregar</button>
        <input type="text" v-model="searchQuery" placeholder="Buscar por usuario, nombre, apellido o país" @input="filtrarUsuario" />
        <button  
          :disabled="!filaSeleccionada" @click="editarUsuario">Editar</button>
        <button 
          :disabled="!filaSeleccionada" @click="mostrarConfirmacionEliminacion">Eliminar</button>
      </div>
    </div>

    <!-- Tabla de usuarios -->
    <table>
      <thead>
        <tr>
          <th>Usuario</th>
          <th>Nombre</th>
          <th>Apellido</th>
          <th>Telefono</th>
          <th>Rol</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="usuario in usuariosFiltrados"
          :key="usuario.id"
          @click="seleccionarFila(usuario)"
          :class="{ selected: filaSeleccionada?.id === usuario.id }"
        >
          <td>{{ usuario.username }}</td>
          <td>{{ usuario.firstname }}</td>
          <td>{{ usuario.lastname }}</td>
          <td>{{ usuario.phone }}</td>
          <td>{{ usuario.role }}</td>
        </tr>
      </tbody>
    </table>

    <!-- Ventana para libros que pueden ser eliminados -->
    <dialog ref="dialogoEliminacion" 
    v-if="mostrarDialogoEliminacion">
      <p>{{ mensajeConfirmacion }}</p>
      <div class="dialog-buttons">
        <button class="dialog-si" @click="eliminarUsuario">Sí, eliminar</button>
        <button class="dialog-no" @click="cancelarEliminacion">No, cancelar</button>
      </div>
    </dialog>

    <!-- Ventana para libros q tienen prestamos activos, y no pueden ser eliminados -->
    <dialog ref="dialogoPrestamos" 
    v-if="mostrarDialogoPrestamos">
      <p>{{ mensajePrestamos }}</p>
      <div class="dialog-buttons">
        <button class="dialog-no" @click="cancelarPrestamos">No, cancelar</button>
      </div>
    </dialog>
  </div>
</template>

<script>
import axios from '../../axios';

export default {
  data() {
    return {
      usuarios: [],
      usuariosFiltrados: [],
      filaSeleccionada: null,
      searchQuery: "",
      mensajeConfirmacion: "",
      permiteEliminacion: false,
      mostrarDialogoEliminacion: false,
      mostrarDialogoPrestamos: false,
    };
  },
  async created() {
    await this.cargarUsuarios();
    this.usuariosFiltrados = this.usuarios;
  },
  methods: {
    async cargarUsuarios() {
      try {
        const response = await axios.get(
          'api/usuario'
        );
        this.usuarios = response.data;
        this.usuariosFiltrados = this.usuarios;
      } catch (error) {
        console.error("Error al cargar usuarios:", error);
      }
    },
    seleccionarFila(usuario) {
      this.filaSeleccionada = usuario; // Guarda el usuario seleccionado
    },
    async mostrarConfirmacionEliminacion() {
      if (this.filaSeleccionada) {
        try {
          // Verificar si el usuario tiene préstamos asociados
          const response = await axios.get(
            `api/usuario/${this.filaSeleccionada.id}/tienePrestamosActivos`);

          if (response.data) { // Si tiene préstamos asociados
            this.mensajePrestamos = "Este usuario tiene préstamos activos y no puede eliminarse.";
            this.mostrarDialogoPrestamos = true; // Mostrar el diálogo de préstamo
            this.mostrarDialogoEliminacion = false; // Ocultar el diálogo de eliminación
          } else {
            // No tiene prestamos asociados 
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
    async eliminarUsuario() {
      try {
        await axios.delete(
          `api/usuario/${this.filaSeleccionada.id}`);
        this.cargarUsuarios(); // Recargar la lista después de la eliminación
        this.mostrarDialogoEliminacion = false; // Desactiva el diálogo
        this.$refs.dialogoEliminacion.close(); // Cerrar el diálogo después de eliminar
      } catch (error) {
        console.error("Error al eliminar el usuario:", error);
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
    agregarUsuario() {
      this.$router.push("/agregarUsuario");
    },
    editarUsuario() {
      if (this.filaSeleccionada) {
        this.$router.push(`/editarUsuario/${this.filaSeleccionada.id}`);
      }
    },
    filtrarUsuario() {
      const query = this.searchQuery.toLowerCase();
      this.usuariosFiltrados = this.usuarios.filter((usuario) => {
        return (
          usuario.username.toLowerCase().includes(query) ||
          usuario.firstname.toLowerCase().includes(query) ||
          usuario.lastname.toLowerCase().includes(query) ||
          usuario.phone.toLowerCase().includes(query) ||
          usuario.role.toLowerCase().includes(query)
        );
      });
    },
  },
};
</script>  
<style scoped>
.user-list-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between; /* Para alinear el título y los botones */
  align-items: center;
}

.action-buttons {
  display: flex;
  margin-bottom: 10px;
}

input {
  margin-left: 10px;
  border-radius: 6px;
  padding: 10px;
}

.action-buttons button {
  margin-left: 10px; /* Espaciado entre botones */
  padding: 5px 10px; 
  background-color: #1F618D; 
  color: white;
  border: none;
  cursor: pointer;
  width: 100px;
  border-radius: 6px;
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
  font-weight: bold; 
  color: white;
}

.selected {
  background-color: #E5E8E8;
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

.dialog-botones button {
  margin: 0 10px; /* Margen entre botones para mantener el espacio */
}


</style>
