import { ref } from 'vue'
import type { Notification, NotificationType } from '../types'

const snackbar = ref(false)
const notification = ref<Notification>({
  message: '',
  type: 'success',
})

export function useNotification() {
  const showNotification = (message: string, type: NotificationType = 'success') => {
    notification.value = { message, type }
    snackbar.value = true
  }

  const showSuccess = (message: string) => showNotification(message, 'success')
  const showError = (message: string) => showNotification(message, 'error')
  const showInfo = (message: string) => showNotification(message, 'info')
  const showWarning = (message: string) => showNotification(message, 'warning')

  return {
    snackbar,
    notification,
    showNotification,
    showSuccess,
    showError,
    showInfo,
    showWarning,
  }
}

