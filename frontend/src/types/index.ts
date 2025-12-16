export interface Client {
  id: number
  name: string
  email: string
  migrated: boolean
}

export type TabType = 'legacy' | 'migrated'

export type NotificationType = 'success' | 'error' | 'info' | 'warning'

export interface Notification {
  message: string
  type: NotificationType
}

