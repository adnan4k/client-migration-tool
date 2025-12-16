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
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  showActions: false,
})

const emit = defineEmits<{
  migrate: [client: Client]
}>()

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
        <td :colspan="columns.length">
          <v-skeleton-loader type="table-row@5" />
        </td>
      </tr>
      <tr v-else v-for="client in clients" :key="client.id" :style="rowStyle">
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

