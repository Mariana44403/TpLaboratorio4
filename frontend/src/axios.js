/*// axios.js
import axios from 'axios';

// Crear una instancia de Axios con configuraciones personalizadas
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/', // Asegúrate de usar la URL correcta
  headers: {
    'Content-Type': 'application/json', // Para enviar datos JSON
  },
});

// Middleware para agregar el token JWT si está disponible
axiosInstance.interceptors.request.use(config => {
  const token = localStorage.getItem('token'); // Obtener el token almacenado
  if (token) {
    config.headers['Authorization'] = 'Bearer ' + token; // Agregar el token al encabezado
  }
  return config;
});

export default axiosInstance; // Exporta la instancia para usarla en todo el proyecto
*/

import axios from 'axios';

// Crear una instancia de Axios con configuración predeterminada
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/', // Cambia esto según tu entorno
  headers: {
    'Content-Type': 'application/json', // Predeterminado para solicitudes JSON
  },
});

// Interceptor para agregar el token JWT a las solicitudes
axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem('token'); // Recuperar el token JWT
  if (token) {
    config.headers['Authorization'] = 'Bearer ' + token; // Agregar el token al encabezado
  }
  return config;
}, (error) => {
  return Promise.reject(error); // Manejar errores antes de enviar la solicitud
});

export default axiosInstance; // Exportar la instancia para usarla en todo el proyecto
