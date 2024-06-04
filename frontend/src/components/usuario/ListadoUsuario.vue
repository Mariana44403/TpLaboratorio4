<template>
  <div class="user-list-container">
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

    <!-- ventana para usuarios que pueden ser eliminados -->
    <dialog ref="dialogoEliminacion" 
    v-if="mostrarDialogoEliminacion">
      <p>{{ mensajeConfirmacion }}</p>
      <div class="dialog-buttons">
        <button class="dialog-si" @click="eliminarUsuario">Sí, eliminar</button>
        <button class="dialog-no" @click="cancelarEliminacion">No, cancelar</button>
      </div>
    </dialog>

    <!-- ventana para usuarios q tienen prestamos activos, y no pueden ser eliminados -->
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
      this.filaSeleccionada = usuario; 
    },
    async mostrarConfirmacionEliminacion() {
      if (this.filaSeleccionada) {
        try {
          const response = await axios.get(
            `api/usuario/${this.filaSeleccionada.id}/tienePrestamosActivos`
          );

          if (response.data) { 
            this.mensajePrestamos = "Este usuario tiene préstamos activos y no puede eliminarse.";
            this.mostrarDialogoPrestamos = true; 
            this.mostrarDialogoEliminacion = false; 
          } else {
            this.mensajeConfirmacion = "¿Desea eliminar este usuario?";
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
          console.error("Error al verificar préstamos asociados:", error);
        }
      }
    },
    async eliminarUsuario() {
      try {
        await axios.delete(
          `api/usuario/${this.filaSeleccionada.id}`);
        this.cargarUsuarios(); 
        this.mostrarDialogoEliminacion = false;
        this.$refs.dialogoEliminacion.close(); 
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
    async filtrarUsuario() {
      const query = this.searchQuery.toLowerCase();

      try {
        const response = await axios.get(`api/usuario/search`, {
          params: { query }
        });
        this.usuariosFiltrados = response.data;
      } catch(error) {
        console.error("Error al filtrar usuarios: ", error);
      }
    }
  },
};
</script>  
<style scoped>
.user-list-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between; 
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
  margin-left: 10px; 
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

.dialog-botones button {
  margin: 0 10px; 
}


</style>
