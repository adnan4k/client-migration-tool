import type { Client } from '../types'
import { migrateClient, getErrorMessage } from '../api/clientApi'
import { useNotification } from './useNotification'

export function useClientMigration(onSuccess?: () => void) {
  const { showSuccess, showError } = useNotification()

  const migrate = async (client: Client) => {
    try {
      await migrateClient(client.id)
      showSuccess(`Client "${client.name}" migrated successfully`)
      onSuccess?.()
    } catch (error) {
      const errorMessage = getErrorMessage(error)
      showError(errorMessage)
      throw error
    }
  }

  return {
    migrate,
  }
}

