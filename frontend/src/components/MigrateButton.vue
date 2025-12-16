<script setup lang="ts">
import { THEME_COLORS } from '../constants'

interface Props {
  disabled?: boolean
}

defineProps<Props>()

defineEmits<{
  click: []
}>()
</script>

<template>
  <v-btn
    size="small"
    rounded="pill"
    :disabled="disabled"
    :style="{
      backgroundColor: `${THEME_COLORS.BLACK} !important`,
      color: `${THEME_COLORS.WHITE} !important`,
    }"
    @click="$emit('click')"
  >
    Migrate
  </v-btn>
</template>

