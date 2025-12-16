<script setup lang="ts">
import { computed } from 'vue'
import type { Client } from '../types'
import { THEME_COLORS, type TableColumn } from '../constants'
import MigratedStatusChip from './MigratedStatusChip.vue'
import MigrateButton from './MigrateButton.vue'

interface Props {
  clients: Client[]
  columns: TableColumn[]
  loading?: boolean
  showActions?: boolean
  selectable?: boolean
  selectedIds?: number[]
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  showActions: false,
  selectable: false,
  selectedIds: () => [],
})

const emit = defineEmits<{
  migrate: [client: Client]
  'update:selectedIds': [ids: number[]]
}>()

const selectableClients = computed(() => {
  return props.clients.filter((client) => !client.migrated)
})

const allSelected = computed(() => {
  if (!props.selectable || selectableClients.value.length === 0) return false
  return selectableClients.value.every((client) => props.selectedIds?.includes(client.id))
})

const someSelected = computed(() => {
  if (!props.selectable) return false
  return props.selectedIds && props.selectedIds.length > 0 && !allSelected.value
})

const toggleSelectAll = () => {
  if (!props.selectable) return
  if (allSelected.value) {
    emit('update:selectedIds', [])
  } else {
    emit('update:selectedIds', selectableClients.value.map((c) => c.id))
  }
}

const toggleSelect = (clientId: number) => {
  if (!props.selectable) return
  const current = props.selectedIds || []
  if (current.includes(clientId)) {
    emit('update:selectedIds', current.filter((id) => id !== clientId))
  } else {
    emit('update:selectedIds', [...current, clientId])
  }
}

const isSelected = (clientId: number) => {
  return props.selectedIds?.includes(clientId) ?? false
}

const tableHeaderStyle = {
  backgroundColor: THEME_COLORS.BLACK,
  color: THEME_COLORS.WHITE,
}

const tableCellStyle = {
  color: THEME_COLORS.BLACK,
}

const rowStyle = {
  borderBottom: `1px solid ${THEME_COLORS.GRAY_LIGHT}`,
}
</script>

<template>
  <v-table class="w-100" :style="{ backgroundColor: THEME_COLORS.WHITE }">
    <thead>
      <tr :style="tableHeaderStyle">
        <th
          v-if="selectable"
          class="text-center text-white"
          style="width: 50px; color: #FFFFFF !important;"
        >
          <v-checkbox
            :model-value="allSelected"
            :indeterminate="someSelected"
            @update:model-value="toggleSelectAll"
            color="white"
            hide-details
            density="compact"
          />
        </th>
        <th
          v-for="column in columns"
          :key="column.key"
          :class="`text-${column.align || 'left'} text-white`"
          :style="{
            width: column.width ? `${column.width}px` : undefined,
            color: `${THEME_COLORS.WHITE} !important`,
          }"
        >
          {{ column.title }}
        </th>
      </tr>
    </thead>
    <tbody>
      <tr v-if="loading">
        <td :colspan="selectable ? columns.length + 1 : columns.length">
          <v-skeleton-loader type="table-row@5" />
        </td>
      </tr>
      <tr v-else v-for="client in clients" :key="client.id" :style="rowStyle">
        <td v-if="selectable" class="text-center" :style="tableCellStyle">
          <v-checkbox
            :model-value="isSelected(client.id)"
            :disabled="client.migrated"
            @update:model-value="toggleSelect(client.id)"
            color="black"
            hide-details
            density="compact"
          />
        </td>
        <td v-for="column in columns" :key="column.key" :style="tableCellStyle">
          <template v-if="column.key === 'migrated'">
            <MigratedStatusChip :migrated="client.migrated" />
          </template>
          <template v-else-if="column.key === 'actions'">
            <div :class="`text-${column.align || 'center'}`">
              <MigrateButton
                :disabled="client.migrated"
                @click="emit('migrate', client)"
              />
            </div>
          </template>
          <template v-else>
            {{ client[column.key as keyof Client] }}
          </template>
        </td>
      </tr>
    </tbody>
  </v-table>
</template>

