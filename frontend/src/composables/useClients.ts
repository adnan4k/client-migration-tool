import { ref } from 'vue'
import type { Client } from '../types'
import { fetchLegacyClients, fetchNewClients } from '../api/clientApi'

export function useClients() {
  const legacyClients = ref<Client[]>([])
  const newClients = ref<Client[]>([])
  const loading = ref(false)

  const loadClients = async () => {
    loading.value = true
    try {
      const [legacy, migrated] = await Promise.all([
        fetchLegacyClients(),
        fetchNewClients(),
      ])
      legacyClients.value = legacy
      newClients.value = migrated
    } finally {
      loading.value = false
    }
  }

  return {
    legacyClients,
    newClients,
    loading,
    loadClients,
  }
}

