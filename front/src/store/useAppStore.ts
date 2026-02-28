import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { Company, CompanyScore, Criteria } from '@/types'

// 기본 평가 기준
const DEFAULT_CRITERIA: Criteria[] = [
  { id: 'c1', name: '복지', weight: 35, order: 0 },
  { id: 'c2', name: '집과의 거리', weight: 20, order: 1 },
  { id: 'c3', name: '연봉', weight: 20, order: 2 },
  { id: 'c4', name: '회사 규모', weight: 20, order: 3 },
  { id: 'c5', name: '기술 스택', weight: 5, order: 4 },
]

interface AppState {
  criteriaList: Criteria[]
  companies: Company[]

  // 평가 기준
  setCriteriaList: (list: Criteria[]) => void

  // 회사 CRUD
  addCompany: (company: Omit<Company, 'id' | 'createdAt' | 'updatedAt'>) => void
  updateCompany: (id: string, data: Partial<Omit<Company, 'id' | 'createdAt'>>) => void
  deleteCompany: (id: string) => void
  updateApplicationStatus: (id: string, status: Company['applicationStatus']) => void
}

export const useAppStore = create<AppState>()(
  persist(
    (set) => ({
      criteriaList: DEFAULT_CRITERIA,
      companies: [],

      setCriteriaList: (list) => set({ criteriaList: list }),

      addCompany: (company) =>
        set((state) => ({
          companies: [
            ...state.companies,
            {
              ...company,
              id: crypto.randomUUID(),
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString(),
            },
          ],
        })),

      updateCompany: (id, data) =>
        set((state) => ({
          companies: state.companies.map((c) =>
            c.id === id ? { ...c, ...data, updatedAt: new Date().toISOString() } : c
          ),
        })),

      deleteCompany: (id) =>
        set((state) => ({
          companies: state.companies.filter((c) => c.id !== id),
        })),

      updateApplicationStatus: (id, status) =>
        set((state) => ({
          companies: state.companies.map((c) =>
            c.id === id ? { ...c, applicationStatus: status, updatedAt: new Date().toISOString() } : c
          ),
        })),
    }),
    {
      name: 'next-company-store',
    }
  )
)
