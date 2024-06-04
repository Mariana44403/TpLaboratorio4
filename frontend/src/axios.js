import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/', 
  headers: {
    'Content-Type': 'application/json',
  },
});

// interceptor para agregar el token JWT a las solicitudes
axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem('token'); // recuperar el token JWT
  if (token) {
    config.headers['Authorization'] = 'Bearer ' + token; // agregar el token al encabezado
  }
  return config;
}, (error) => {
  return Promise.reject(error); 
});

export default axiosInstance; 
