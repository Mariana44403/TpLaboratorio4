import { createRouter, createWebHistory } from 'vue-router';
// Global
import HomePage from '@/components/global/HomePage.vue';
// Autenticacion
import LoginPage from '@/components/autenticacion/LoginPage.vue';
import RegisterPage from '@/components/autenticacion/RegisterPage.vue';
// Libro
import ListadoLibro from '@/components/libro/ListadoLibro.vue';
import AgregarLibro from '@/components/libro/AgregarLibro.vue';
import EditarLibro from '@/components/libro/EditarLibro.vue';
// Usuario
import ListadoUsuario from '@/components/usuario/ListadoUsuario.vue';
import AgregarUsuario from '@/components/usuario/AgregarUsuario.vue';
import EditarUsuario from '@/components/usuario/EditarUsuario.vue';
// Prestamo
import AgregarPrestamo from '@/components/prestamo/AgregarPrestamo.vue';
import ListadoPrestamo from '@/components/prestamo/ListadoPrestamo.vue';
import EditarPrestamo from '@/components/prestamo/EditarPrestamo.vue';
import DevolverPrestamo from '@/components/prestamo/DevolverPrestamo.vue';

const routes = [
    {
        path: '/',
        name: 'HomePage',
        component: HomePage,
    },
    {
        path: '/login',
        name: 'Login',
        component: LoginPage,
    },
    {
        path: '/register',
        name: 'Register',
        component: RegisterPage
    },
    {
        path: '/books',
        name: 'ListadoLibro',
        component: ListadoLibro
    },
    {
        path: '/agregarLibro',
        name: 'AgregarLibro',
        component: AgregarLibro
    },
    {
        path: '/editarLibro/:id_libro', 
        component: EditarLibro,
    },
    {
        path: '/user',
        name: 'ListadoUsuario',
        component: ListadoUsuario
    },
    {
        path: '/agregarUsuario',
        name: 'AgregarUsuario',
        component: AgregarUsuario
    },
    {
        path: '/prestamo',
        name: 'ListadoPrestamo',
        component: ListadoPrestamo
    },
    {
        path: '/agregarPrestamo',
        name: 'AgregarPrestamo',
        component: AgregarPrestamo
    },
    {
        path: '/editarPrestamo/:id',
        name: 'EditarPrestamo',
        component: EditarPrestamo
    },
    {
        path: '/editarUsuario/:id',
        name: 'EditarUsuario',
        component: EditarUsuario
    },
    {
        path: '/devolverPrestamo/:id',
        name: 'DevolverPrestamo',
        component: DevolverPrestamo
    }
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
});

export default router;
