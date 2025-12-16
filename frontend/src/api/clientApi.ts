import axios from 'axios'

export interface Client {
  id: number
  name: string
  email: string
  migrated: boolean
}

const baseURL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api'

const api = axios.create({
  baseURL,
})

export async function fetchLegacyClients(): Promise<Client[]> {
  const response = await api.get<Client[]>('/legacy/clients')
  return response.data
}

export async function fetchNewClients(): Promise<Client[]> {
  const response = await api.get<Client[]>('/new/clients')
  return response.data
}

export async function migrateClient(id: number): Promise<Client> {
  const response = await api.post<Client>(`/migrate/${id}`)
  return response.data
}
