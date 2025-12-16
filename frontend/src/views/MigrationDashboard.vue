<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { Client, TabType } from '../types'
import { useClients } from '../composables/useClients'
import { useClientMigration } from '../composables/useClientMigration'
import { useNotification } from '../composables/useNotification'
import { TABLE_COLUMNS, TAB_LABELS, THEME_COLORS, NOTIFICATION_TIMEOUT } from '../constants'
import ClientTable from '../components/ClientTable.vue'

const activeTab = ref<TabType>('legacy')
const drawer = ref(true)

const { legacyClients, newClients, loading, loadClients } = useClients()
const { snackbar, notification } = useNotification()

const { migrate } = useClientMigration(async () => {
  await loadClients()
  activeTab.value = 'migrated'
})

const handleMigrate = async (client: Client) => {
  await migrate(client)
}

const navigationItems = [
  { value: 'legacy', title: TAB_LABELS.LEGACY, icon: 'mdi-database' },
  { value: 'migrated', title: TAB_LABELS.MIGRATED, icon: 'mdi-check-circle' },
]

onMounted(loadClients)
</script>

<template>
  <div class="d-flex fill-height" :style="{ backgroundColor: THEME_COLORS.WHITE }">
    <!-- Sidebar Navigation -->
    <v-navigation-drawer
      v-model="drawer"
      permanent
      :style="{
        backgroundColor: THEME_COLORS.WHITE,
        borderRight: `1px solid ${THEME_COLORS.GRAY_LIGHT}`,
      }"
      width="280"
    >
      <div class="pa-4">
        <h2
          class="text-h5 font-weight-bold"
          :style="{ color: THEME_COLORS.BLACK }"
        >
          Migration Dashboard
        </h2>
      </div>

      <v-divider />

      <v-list :style="{ backgroundColor: THEME_COLORS.WHITE }">
        <v-list-item
          v-for="item in navigationItems"
          :key="item.value"
          :value="item.value"
          :active="activeTab === item.value"
          @click="activeTab = item.value as TabType"
          :style="{
            backgroundColor: activeTab === item.value ? THEME_COLORS.BLACK : 'transparent',
            color: activeTab === item.value ? THEME_COLORS.WHITE : THEME_COLORS.BLACK,
            margin: '4px 8px',
            borderRadius: '8px',
            cursor: 'pointer',
          }"
        >
          <template #prepend>
            <v-icon
              :icon="item.icon"
              :color="activeTab === item.value ? THEME_COLORS.WHITE : THEME_COLORS.BLACK"
            />
          </template>
          <v-list-item-title
            class="font-weight-medium"
            :style="{
              color: activeTab === item.value ? THEME_COLORS.WHITE : THEME_COLORS.BLACK,
            }"
          >
            {{ item.title }}
          </v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <!-- Main Content -->
    <v-main class="d-flex flex-column fill-height" :style="{ backgroundColor: THEME_COLORS.WHITE }">
      <div class="pa-4">
        <h1
          class="text-h4 font-weight-medium"
          :style="{ color: THEME_COLORS.BLACK }"
        >
          {{ activeTab === 'legacy' ? TAB_LABELS.LEGACY : TAB_LABELS.MIGRATED }}
        </h1>
      </div>

      <v-divider />

      <div class="flex-grow-1 d-flex flex-column" style="overflow: auto">
        <!-- Legacy clients table -->
        <template v-if="activeTab === 'legacy'">
          <ClientTable
            :clients="legacyClients"
            :columns="TABLE_COLUMNS.LEGACY"
            :loading="loading"
            :show-actions="true"
            @migrate="handleMigrate"
          />
        </template>

        <!-- Migrated clients table -->
        <template v-else>
          <!-- Loading state -->
          <template v-if="loading">
            <ClientTable
              :clients="[]"
              :columns="TABLE_COLUMNS.MIGRATED"
              :loading="true"
            />
          </template>

          <!-- No data: show friendly empty state message -->
          <template v-else-if="!newClients.length">
            <div
              class="d-flex flex-column align-center justify-center"
              style="height: 100%; min-height: 400px;"
            >
              <v-icon
                icon="mdi-information-outline"
                size="64"
                :style="{ color: THEME_COLORS.GRAY_MEDIUM, marginBottom: '16px' }"
              />
              <h3
                class="text-h6 font-weight-medium mb-2"
                :style="{ color: THEME_COLORS.BLACK }"
              >
                No Migrated Clients Yet
              </h3>
              <p
                class="text-body-1 text-center"
                :style="{ color: THEME_COLORS.GRAY_MEDIUM, maxWidth: '400px' }"
              >
                Start migrating clients from the Legacy Clients section to see them here.
              </p>
            </div>
          </template>

          <!-- Has data: show full table -->
          <template v-else>
            <ClientTable
              :clients="newClients"
              :columns="TABLE_COLUMNS.MIGRATED"
              :loading="false"
            />
          </template>
        </template>
      </div>
    </v-main>

    <v-snackbar
      v-model="snackbar"
      :color="notification.type"
      :timeout="NOTIFICATION_TIMEOUT"
    >
      {{ notification.message }}
    </v-snackbar>
  </div>
</template>
