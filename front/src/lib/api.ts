import {
  Company,
  CompanyScore,
  JobChangeStatus,
  KanbanColumnConfig,
  TargetStatus,
} from '@/types'

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? 'http://localhost:8080'

export interface ApiResponse<T> {
  success: boolean
  data: T | null
  message: string | null
}

export interface PageResponse<T> {
  content: T[]
  totalPages: number
  totalElements: number
  number: number
  size: number
}

export interface CompanyResponse {
  id: number
  name: string
  targetStatus: TargetStatus
  jobPostingUrl: string | null
  recruitmentDeadline: string | null
  jobChangeStatus: JobChangeStatus
  scores: CompanyScore[]
  memo: string | null
  createdAt: string
  updatedAt?: string
}

export interface CreateCompanyRequest {
  name: string
  targetStatus?: TargetStatus
  jobPostingUrl?: string | null
  recruitmentDeadline?: string | null
  jobChangeStatus?: JobChangeStatus
  scores?: CompanyScore[]
  memo?: string | null
}

export interface UpdateCompanyRequest {
  name?: string
  targetStatus?: TargetStatus
  jobPostingUrl?: string | null
  recruitmentDeadline?: string | null
  jobChangeStatus?: JobChangeStatus
  scores?: CompanyScore[]
  memo?: string | null
}

export interface SaveKanbanColumnConfigRequest {
  columns: KanbanColumnConfig[]
}

type RequestBody = object | unknown[] | null

async function request<T>(path: string, init: RequestInit = {}): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...init,
    headers: {
      'Content-Type': 'application/json',
      ...init.headers,
    },
  })

  const text = await response.text()
  const payload = text ? (JSON.parse(text) as ApiResponse<T>) : null

  if (!response.ok) {
    throw new Error(payload?.message ?? `Request failed with ${response.status}`)
  }

  if (payload && !payload.success) {
    throw new Error(payload.message ?? 'Request failed')
  }

  return payload?.data as T
}

function withJson(method: string, body?: RequestBody): RequestInit {
  return {
    method,
    body: body === undefined ? undefined : JSON.stringify(body),
  }
}

export const api = {
  get: <T>(path: string) => request<T>(path),
  post: <T>(path: string, body?: RequestBody) => request<T>(path, withJson('POST', body)),
  patch: <T>(path: string, body?: RequestBody) => request<T>(path, withJson('PATCH', body)),
  put: <T>(path: string, body?: RequestBody) => request<T>(path, withJson('PUT', body)),
  delete: <T>(path: string) => request<T>(path, { method: 'DELETE' }),
}

export function toCompany(response: CompanyResponse): Company {
  return {
    id: response.id,
    name: response.name,
    targetStatus: response.targetStatus,
    jobPostingUrl: response.jobPostingUrl ?? undefined,
    recruitmentDeadline: response.recruitmentDeadline ?? undefined,
    jobChangeStatus: response.jobChangeStatus,
    scores: response.scores ?? ([] satisfies CompanyScore[]),
    memo: response.memo ?? '',
    createdAt: response.createdAt,
    updatedAt: response.updatedAt ?? response.createdAt,
  }
}

export const companyApi = {
  async getCompanies(): Promise<Company[]> {
    const page = await api.get<PageResponse<CompanyResponse>>('/companies')
    return page.content.map(toCompany)
  },

  createCompany(request: CreateCompanyRequest): Promise<{ id: number }> {
    return api.post<{ id: number }>('/companies', request)
  },

  updateCompany(id: number, request: UpdateCompanyRequest): Promise<null> {
    return api.patch<null>(`/companies/${id}`, request)
  },

  updateCompanyStatus(id: number, jobChangeStatus: JobChangeStatus): Promise<null> {
    return api.patch<null>(`/companies/${id}/status`, { jobChangeStatus })
  },

  deleteCompany(id: number): Promise<null> {
    return api.delete<null>(`/companies/${id}`)
  },
}

export const kanbanApi = {
  async getKanbanColumns(): Promise<KanbanColumnConfig[] | null> {
    const response = await api.get<{ columns: KanbanColumnConfig[] } | null>('/kanban-columns')
    return response?.columns ?? null
  },

  saveKanbanColumns(columns: KanbanColumnConfig[]): Promise<null> {
    return api.put<null>('/kanban-columns', { columns } satisfies SaveKanbanColumnConfigRequest)
  },
}
