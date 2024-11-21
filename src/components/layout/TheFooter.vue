<script setup>
</script>

<template>
  <v-footer
    class="bg-grey-lighten-1"
  >
    <v-row
      justify="center"
      no-gutters
    >
      <v-col
        class="text-center mt-4"
        cols="12"
      >
        {{ new Date().getFullYear() }} — <strong>Vue.js 게시판</strong>
      </v-col>
      <v-col
        class="text-center mb-4"
        cols="12"
      >
        <v-btn
          v-for="icon in icons"
          :key="icon"
          class="mx-4"
          :icon="icon"
          variant="text"
        ></v-btn>
      </v-col>
    </v-row>
  </v-footer>
</template>

<script>
const icons = [
  'mdi-facebook',
  'mdi-twitter',
  'mdi-linkedin',
  'mdi-instagram',
]
</script>

<style scoped>
.v-footer {
  width: 100%;
  position: fixed;
  bottom: 0;
}
</style>

