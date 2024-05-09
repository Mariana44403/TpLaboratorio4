<template>
  <div id="app">
    <NavPage/>
    <div class="auth-wrapper">
      <div class="auth-inner">
        <router-view />
      </div>
    </div>
  </div>
</template>
<script>
import NavPage from './components/global/NavPage.vue';
export default {
  name: "App",
  components: {
    NavPage,
  }
  
}
</script>
