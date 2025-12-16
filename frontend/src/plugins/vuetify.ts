import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'

import { createVuetify } from 'vuetify'

export const vuetify = createVuetify({
  theme: {
    defaultTheme: 'blackWhite',
    themes: {
      blackWhite: {
        dark: false,
        colors: {
          primary: '#000000',
          secondary: '#000000',
          accent: '#000000',
          error: '#F44336',
          info: '#000000',
          success: '#4CAF50',
          warning: '#000000',
          background: '#FFFFFF',
          surface: '#FFFFFF',
          'on-primary': '#FFFFFF',
          'on-secondary': '#FFFFFF',
          'on-surface': '#000000',
          'on-background': '#000000',
        },
      },
    },
  },
})


