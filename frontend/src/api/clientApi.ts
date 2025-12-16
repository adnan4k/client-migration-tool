import axios, { type AxiosError } from 'axios'
import type { Client } from '../types'

const baseURL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api'

const api = axios.create({
  baseURL,
  timeout: 10000,
})

// Error handling utility
export function getErrorMessage(error: unknown): string {
  if (axios.isAxiosError(error)) {
    const axiosError = error as AxiosError<{ message?: string }>
    return axiosError.response?.data?.message ?? axiosError.message ?? 'An error occurred'
  }
  if (error instanceof Error) {
    return error.message
  }
  return 'An unexpected error occurred'
}

export async function fetchLegacyClients(): Promise<Client[]> {
  try {
    const response = await api.get<Client[]>('/legacy/clients')
    return response.data
  } catch (error) {
    throw new Error(`Failed to fetch legacy clients: ${getErrorMessage(error)}`)
  }
}

export async function fetchNewClients(): Promise<Client[]> {
  try {
    const response = await api.get<Client[]>('/new/clients')
    return response.data
  } catch (error) {
    throw new Error(`Failed to fetch migrated clients: ${getErrorMessage(error)}`)
  }
}

export async function migrateClient(id: number): Promise<Client> {
  try {
    const response = await api.post<Client>(`/migrate/${id}`)
    return response.data
  } catch (error) {
    throw new Error(`Failed to migrate client: ${getErrorMessage(error)}`)
  }
}
