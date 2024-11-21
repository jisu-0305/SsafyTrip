<script setup>
import TheNavbar from "@/components/layout/TheNavbar.vue";
// import TheFooter from "@/components/layout/TheFooter.vue";
</script>

<template>
  <v-app>
    <TheNavbar />
    <v-main>
      <RouterView />
    </v-main>
  </v-app>
</template>


<style scoped></style>
