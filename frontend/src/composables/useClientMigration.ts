import type { Client } from '../types'
import { migrateClient, migrateClientsBulk, getErrorMessage } from '../api/clientApi'
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

  const migrateBulk = async (clients: Client[]) => {
    try {
      const ids = clients.map((client) => client.id)
      const result = await migrateClientsBulk(ids)

      if (result.failedCount === 0) {
        showSuccess(`Successfully migrated ${result.successCount} client(s)`)
        onSuccess?.()
      } else if (result.successCount > 0) {
        const errorMessages = result.errors.join('; ')
        showError(
          `Migrated ${result.successCount} client(s), ${result.failedCount} failed. ${errorMessages}`
        )
        onSuccess?.()
      } else {
        const errorMessages = result.errors.join('; ')
        showError(`Failed to migrate clients. ${errorMessages}`)
      }

      return {
        success: result.successCount,
        failed: result.failedCount,
        errors: result.errors,
      }
    } catch (error) {
      const errorMessage = getErrorMessage(error)
      showError(`Failed to migrate clients: ${errorMessage}`)
      throw error
    }
  }

  return {
    migrate,
    migrateBulk,
  }
}

