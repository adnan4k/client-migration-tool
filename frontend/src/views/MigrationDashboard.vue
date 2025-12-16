<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { Client, TabType } from '../types'
import { useClients } from '../composables/useClients'
import { useClientMigration } from '../composables/useClientMigration'
import { useNotification } from '../composables/useNotification'
import { TABLE_COLUMNS, TAB_LABELS, THEME_COLORS, NOTIFICATION_TIMEOUT } from '../constants'
import SegmentedControl from '../components/SegmentedControl.vue'
import ClientTable from '../components/ClientTable.vue'

const activeTab = ref<TabType>('legacy')

const { legacyClients, newClients, loading, loadClients } = useClients()
const { snackbar, notification } = useNotification()

const { migrate } = useClientMigration(async () => {
  await loadClients()
  activeTab.value = 'migrated'
})

const handleMigrate = async (client: Client) => {
  await migrate(client)
}

const segments = [
  { value: 'legacy', label: TAB_LABELS.LEGACY },
  { value: 'migrated', label: TAB_LABELS.MIGRATED },
]

onMounted(loadClients)
</script>

<template>
  <div
    class="d-flex flex-column fill-height"
    :style="{ backgroundColor: THEME_COLORS.WHITE }"
  >
    <div class="pa-4">
      <h1
        class="text-h4 font-weight-medium text-center"
        :style="{ color: THEME_COLORS.BLACK }"
      >
        Client Migration Dashboard
      </h1>
    </div>

    <v-card
      class="flex-grow-1 d-flex flex-column"
      :style="{
        borderRadius: 0,
        backgroundColor: THEME_COLORS.WHITE,
      }"
    >
      <div class="d-flex justify-center pa-4">
        <SegmentedControl v-model="activeTab" :segments="segments" />
      </div>

      <v-divider />

      <v-window v-model="activeTab" class="flex-grow-1 d-flex flex-column">
        <!-- Legacy clients table -->
        <v-window-item value="legacy" class="flex-grow-1 d-flex flex-column">
          <div class="flex-grow-1" style="overflow: auto">
            <ClientTable
              :clients="legacyClients"
              :columns="TABLE_COLUMNS.LEGACY"
              :loading="loading"
              :show-actions="true"
              @migrate="handleMigrate"
            />
          </div>
        </v-window-item>

        <!-- Migrated clients table -->
        <v-window-item value="migrated" class="flex-grow-1 d-flex flex-column">
          <div class="flex-grow-1" style="overflow: auto">
            <!-- Loading state -->
            <template v-if="loading">
              <ClientTable
                :clients="[]"
                :columns="TABLE_COLUMNS.MIGRATED"
                :loading="true"
              />
            </template>

            <!-- No data: show a clean empty state -->
            <template v-else-if="!newClients.length">
              <div class="pa-4"></div>
            </template>

            <!-- Has data: show full table -->
            <template v-else>
              <ClientTable
                :clients="newClients"
                :columns="TABLE_COLUMNS.MIGRATED"
                :loading="false"
              />
            </template>
          </div>
        </v-window-item>
      </v-window>
    </v-card>

    <v-snackbar
      v-model="snackbar"
      :color="notification.type"
      :timeout="NOTIFICATION_TIMEOUT"
    >
      {{ notification.message }}
    </v-snackbar>
  </div>
</template>
