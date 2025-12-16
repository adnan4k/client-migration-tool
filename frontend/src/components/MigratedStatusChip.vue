<script setup lang="ts">
import { computed } from 'vue'
import { THEME_COLORS } from '../constants'

interface Props {
  migrated: boolean
}

const props = defineProps<Props>()

const chipStyle = computed(() => {
  if (props.migrated) {
    return {
      backgroundColor: `${THEME_COLORS.BLACK} !important`,
      color: `${THEME_COLORS.WHITE} !important`,
    }
  }
  return {
    backgroundColor: `${THEME_COLORS.GRAY_LIGHT} !important`,
    color: `${THEME_COLORS.BLACK} !important`,
  }
})
</script>

<template>
  <v-chip size="small" variant="flat" :style="chipStyle">
    {{ migrated ? 'Yes' : 'No' }}
  </v-chip>
</template>

