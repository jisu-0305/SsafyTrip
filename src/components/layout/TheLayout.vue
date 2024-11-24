<template>
  <v-app>
    <TheNavbar />
    <v-main>
      <v-container fluid class="main-container">
        <slot></slot>
      </v-container>
    </v-main>
  </v-app>
</template>

<style scoped>
.main-container {
  max-width: 1440px;
  margin: 0 auto;
  padding: 32px;
}
</style> 