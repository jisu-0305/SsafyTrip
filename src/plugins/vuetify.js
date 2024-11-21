import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css'

export default createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          primary: '#212121',    // grey-darken-4
          secondary: '#E0E0E0',  // grey-lighten-2
          'grey-darken-1': '#64748B'
        }
      }
    }
  },
  defaults: {
    global: {
      font: {
        family: 'Noto Sans KR, sans-serif'
      }
    }
  }
})