// import "./assets/main.css";

import { createApp } from "vue";
import { createPinia } from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";

import vuetify from "./plugins/vuetify";

import App from "./App.vue";
import router from "./router";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap";

import '@/assets/styles/base.css'
import '@/assets/styles/common.css'
import '@/assets/styles/global.css'


const app = createApp(App);


// pinia
const pinia = createPinia();
app.use(pinia);

// pinia-persistence
pinia.use(piniaPluginPersistedstate);

// router
app.use(router);

// vuetify
app.use(vuetify)

app.mount("#app");
