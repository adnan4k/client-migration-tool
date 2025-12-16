<script setup lang="ts">
import { computed } from 'vue'
import { THEME_COLORS } from '../constants'

interface Segment {
  value: string
  label: string
}

interface Props {
  modelValue: string
  segments: Segment[]
}

const props = defineProps<Props>()
const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const activeValue = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const isActive = (value: string) => activeValue.value === value

const getSegmentStyle = (value: string) => {
  const baseStyle = {
    padding: '8px 24px',
    border: 'none',
    borderRadius: '6px',
    fontWeight: 500,
    cursor: 'pointer',
    transition: 'all 0.2s',
    textTransform: 'uppercase',
    fontSize: '14px',
  }

  if (isActive(value)) {
    return {
      ...baseStyle,
      backgroundColor: THEME_COLORS.BLACK,
      color: THEME_COLORS.WHITE,
    }
  }

  return {
    ...baseStyle,
    backgroundColor: 'transparent',
    color: THEME_COLORS.GRAY_MEDIUM,
  }
}
</script>

<template>
  <div
    class="segmented-control"
    :style="{
      display: 'inline-flex',
      borderRadius: '8px',
      backgroundColor: THEME_COLORS.GRAY_LIGHT,
      padding: '4px',
      gap: 0,
    }"
  >
    <button
      v-for="segment in segments"
      :key="segment.value"
      :class="['segment', { active: isActive(segment.value) }]"
      :style="getSegmentStyle(segment.value)"
      @click="activeValue = segment.value"
    >
      {{ segment.label }}
    </button>
  </div>
</template>

