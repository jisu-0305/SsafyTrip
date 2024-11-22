<script setup>
import TheNavbar from "@/components/layout/TheNavbar.vue";
// import TheFooter from "@/components/layout/TheFooter.vue";
import AttractionPlan from "@/views/plan/AttractionPlan.vue";
</script>

<template>
  <v-app>
    <TheNavbar />
    <v-main>
      <RouterView />
    </v-main>
    <!-- <AttractionPlan /> -->
  </v-app>
</template>


<style scoped></style>
