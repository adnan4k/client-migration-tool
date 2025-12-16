<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { Client } from '../api/clientApi'
import { fetchLegacyClients, fetchNewClients, migrateClient } from '../api/clientApi'

const legacyClients = ref<Client[]>([])
const newClients = ref<Client[]>([])
const loading = ref(false)

const activeTab = ref<'legacy' | 'migrated'>('legacy')

const snackbar = ref(false)
const snackbarColor = ref<'success' | 'error'>('success')
const snackbarMessage = ref('')

async function loadData() {
  loading.value = true
  try {
    const [legacy, migrated] = await Promise.all([fetchLegacyClients(), fetchNewClients()])
    legacyClients.value = legacy
    newClients.value = migrated
  } finally {
    loading.value = false
  }
}

async function handleMigrate(client: Client) {
  try {
    await migrateClient(client.id)
    snackbarColor.value = 'success'
    snackbarMessage.value = `Client "${client.name}" migrated successfully`
    snackbar.value = true
    await loadData()
    activeTab.value = 'migrated'
  } catch (error: any) {
    snackbarColor.value = 'error'
    snackbarMessage.value =
      error?.response?.data?.message ?? `Failed to migrate client "${client.name}"`
    snackbar.value = true
  }
}

onMounted(loadData)
</script>

<template>
  <div class="d-flex flex-column fill-height" style="background-color: #FFFFFF;">
    <div class="pa-4">
      <h1 class="text-h4 font-weight-medium text-center" style="color: #000000;">Client Migration Dashboard</h1>
    </div>

    <v-card class="flex-grow-1 d-flex flex-column" style="border-radius: 0; background-color: #FFFFFF;">
      <v-tabs
        v-model="activeTab"
        bg-color="white"
        color="black"
        slider-color="black"
        align-tabs="center"
        class="elevation-0"
      >
        <v-tab value="legacy" class="text-button text-uppercase font-weight-medium">
          Legacy Clients
        </v-tab>
        <v-tab value="migrated" class="text-button text-uppercase font-weight-medium">
          Migrated Clients
        </v-tab>
      </v-tabs>

      <v-divider />

      <v-window v-model="activeTab" class="flex-grow-1 d-flex flex-column">
        <!-- Legacy clients table -->
        <v-window-item value="legacy" class="flex-grow-1 d-flex flex-column">
          <div class="flex-grow-1" style="overflow: auto">
            <v-table class="w-100" style="background-color: #FFFFFF;">
              <thead>
                <tr style="background-color: #000000;">
                  <th class="text-left text-white" style="width: 70px; color: #FFFFFF !important;">ID</th>
                  <th class="text-left text-white" style="color: #FFFFFF !important;">Name</th>
                  <th class="text-left text-white" style="color: #FFFFFF !important;">Email</th>
                  <th class="text-left text-white" style="width: 110px; color: #FFFFFF !important;">Migrated</th>
                  <th class="text-center text-white" style="width: 140px; color: #FFFFFF !important;">Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="loading">
                  <td colspan="5">
                    <v-skeleton-loader type="table-row@5" />
                  </td>
                </tr>
                <tr v-else v-for="client in legacyClients" :key="client.id" style="border-bottom: 1px solid #E0E0E0;">
                  <td style="color: #000000;">{{ client.id }}</td>
                  <td style="color: #000000;">{{ client.name }}</td>
                  <td style="color: #000000;">{{ client.email }}</td>
                  <td>
                    <v-chip 
                      :color="client.migrated ? 'success' : 'grey'" 
                      size="small" 
                      variant="flat"
                      :style="client.migrated ? 'background-color: #000000 !important; color: #FFFFFF !important;' : 'background-color: #E0E0E0 !important; color: #000000 !important;'"
                    >
                      {{ client.migrated ? 'Yes' : 'No' }}
                    </v-chip>
                  </td>
                  <td class="text-center">
                    <v-btn
                      color="primary"
                      size="small"
                      rounded="pill"
                      :disabled="client.migrated"
                      @click="handleMigrate(client)"
                      style="background-color: #000000 !important; color: #FFFFFF !important;"
                    >
                      Migrate
                    </v-btn>
                  </td>
                </tr>
              </tbody>
            </v-table>
          </div>
        </v-window-item>

        <!-- Migrated clients table -->
        <v-window-item value="migrated" class="flex-grow-1 d-flex flex-column">
          <div class="flex-grow-1" style="overflow: auto">
            <!-- Loading state -->
            <template v-if="loading">
              <v-table class="w-100" style="background-color: #FFFFFF;">
                <tbody>
                  <tr>
                    <td colspan="3">
                      <v-skeleton-loader type="table-row@5" />
                    </td>
                  </tr>
                </tbody>
              </v-table>
            </template>

            <!-- No data: show a clean empty state (no table, no message) -->
            <template v-else-if="!newClients.length">
              <div class="pa-4"></div>
            </template>

            <!-- Has data: show full table -->
            <template v-else>
              <v-table class="w-100" style="background-color: #FFFFFF;">
                <thead>
                  <tr style="background-color: #000000;">
                    <th class="text-left text-white" style="width: 70px; color: #FFFFFF !important;">ID</th>
                    <th class="text-left text-white" style="color: #FFFFFF !important;">Name</th>
                    <th class="text-left text-white" style="color: #FFFFFF !important;">Email</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="client in newClients" :key="client.id" style="border-bottom: 1px solid #E0E0E0;">
                    <td style="color: #000000;">{{ client.id }}</td>
                    <td style="color: #000000;">{{ client.name }}</td>
                    <td style="color: #000000;">{{ client.email }}</td>
                  </tr>
                </tbody>
              </v-table>
            </template>
          </div>
        </v-window-item>
      </v-window>
    </v-card>

    <v-snackbar v-model="snackbar" :color="snackbarColor" timeout="3000">
      {{ snackbarMessage }}
    </v-snackbar>
  </div>
</template>


