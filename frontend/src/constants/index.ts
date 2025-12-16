// Theme colors
export const THEME_COLORS = {
  WHITE: '#FFFFFF',
  BLACK: '#000000',
  GRAY_LIGHT: '#E0E0E0',
  GRAY_MEDIUM: '#666666',
} as const

// Table configuration
export interface TableColumn {
  key: string
  title: string
  width?: number
  align?: 'left' | 'center' | 'right'
}

export const TABLE_COLUMNS: {
  LEGACY: TableColumn[]
  MIGRATED: TableColumn[]
} = {
  LEGACY: [
    { key: 'id', title: 'ID', width: 70 },
    { key: 'name', title: 'Name' },
    { key: 'email', title: 'Email' },
    { key: 'migrated', title: 'Migrated', width: 110 },
    { key: 'actions', title: 'Actions', width: 140, align: 'center' },
  ],
  MIGRATED: [
    { key: 'id', title: 'ID', width: 70 },
    { key: 'name', title: 'Name' },
    { key: 'email', title: 'Email' },
  ],
}

// Tab labels
export const TAB_LABELS = {
  LEGACY: 'Legacy Clients',
  MIGRATED: 'Migrated Clients',
} as const

// Notification timeout
export const NOTIFICATION_TIMEOUT = 3000

