import Vuex from 'vuex';

const store = new Vuex.Store({
  state: {
    isAuthenticated: false,
  },
  getters: {
    isAuthenticated: (state) => state.isAuthenticated,
  },
  mutations: {
    setAuthentication(state, status) {
      state.isAuthenticated = status;
    },
  },
  actions: {
    login({ commit }) {
      commit('setAuthentication', true); // Cambia el estado de autenticación a true
    },
    logout({ commit }) {
      commit('setAuthentication', false); // Cambia el estado de autenticación a false
    },
  },
});

export default store;
