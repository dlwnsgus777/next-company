import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import {
  Company,
  Criteria,
  KanbanColumnConfig,
  DEFAULT_KANBAN_COLUMNS,
  DEFAULT_CRITERIA,
} from '@/types'
import { companyApi, kanbanApi } from '@/lib/api'

type FilterTarget = 'ALL' | 'O' | '△'
type SortBy = 'score' | 'name' | 'deadline'
type ViewMode = 'kanban' | 'rank'

interface ModalState {
  companyDetail: { open: boolean; companyId?: number }
  companyForm: { open: boolean; companyId?: number } // companyId 없으면 추가, 있으면 수정
  criteria: { open: boolean }
  kanbanSetting: { open: boolean }
}

interface AppState {
  // 데이터
  criteriaList: Criteria[]
  companies: Company[]
  kanbanColumns: KanbanColumnConfig[]

  // UI 상태
  filterTarget: FilterTarget
  sortBy: SortBy
  viewMode: ViewMode
  modal: ModalState

  // 평가 기준 액션
  setCriteriaList: (list: Criteria[]) => void

  // 서버 동기화
  loadCompanies: () => Promise<void>
  loadKanbanColumns: () => Promise<void>

  // 칸반 컬럼 액션
  setKanbanColumns: (columns: KanbanColumnConfig[]) => Promise<void>

  // 회사 CRUD
  addCompany: (company: Omit<Company, 'id' | 'createdAt' | 'updatedAt'>) => Promise<void>
  updateCompany: (id: number, data: Partial<Omit<Company, 'id' | 'createdAt'>>) => Promise<void>
  deleteCompany: (id: number) => Promise<void>
  updateApplicationStatus: (id: number, status: Company['jobChangeStatus']) => Promise<void>

  // UI 액션
  setFilterTarget: (filter: FilterTarget) => void
  setSortBy: (sort: SortBy) => void
  setViewMode: (mode: ViewMode) => void

  // 모달 액션
  openCompanyDetail: (companyId: number) => void
  openCompanyAdd: () => void
  openCompanyEdit: (companyId: number) => void
  openCriteria: () => void
  openKanbanSetting: () => void
  closeModal: (modal: keyof ModalState) => void
}

export const useAppStore = create<AppState>()(
  persist(
    (set, get) => ({
      criteriaList: DEFAULT_CRITERIA,
      companies: [],
      kanbanColumns: DEFAULT_KANBAN_COLUMNS,

      filterTarget: 'ALL',
      sortBy: 'score',
      viewMode: 'kanban',
      modal: {
        companyDetail: { open: false },
        companyForm: { open: false },
        criteria: { open: false },
        kanbanSetting: { open: false },
      },

      setCriteriaList: (list) => set({ criteriaList: list }),

      loadCompanies: async () => {
        const companies = await companyApi.getCompanies()
        set({ companies })
      },

      loadKanbanColumns: async () => {
        const kanbanColumns = await kanbanApi.getKanbanColumns()
        set({ kanbanColumns: kanbanColumns ?? DEFAULT_KANBAN_COLUMNS })
      },

      setKanbanColumns: async (kanbanColumns) => {
        await kanbanApi.saveKanbanColumns(kanbanColumns)
        set({ kanbanColumns })
      },

      addCompany: async (company) => {
        await companyApi.createCompany({
          name: company.name,
          jobChangeStatus: company.jobChangeStatus,
          memo: company.memo ?? null,
        })
        await get().loadCompanies()
      },

      updateCompany: async (id, data) => {
        await companyApi.updateCompany(id, {
          name: data.name,
          jobChangeStatus: data.jobChangeStatus,
          memo: data.memo ?? null,
        })
        await get().loadCompanies()
      },

      deleteCompany: async (id) => {
        await companyApi.deleteCompany(id)
        set((state) => ({
          companies: state.companies.filter((c) => c.id !== id),
        }))
      },

      updateApplicationStatus: async (id, status) => {
        await companyApi.updateCompanyStatus(id, status)
        set((state) => ({
          companies: state.companies.map((c) =>
            c.id === id
              ? { ...c, jobChangeStatus: status, updatedAt: new Date().toISOString() }
              : c
          ),
        }))
      },

      setFilterTarget: (filterTarget) => set({ filterTarget }),
      setSortBy: (sortBy) => set({ sortBy }),
      setViewMode: (viewMode) => set({ viewMode }),

      openCompanyDetail: (companyId) =>
        set((state) => ({
          modal: { ...state.modal, companyDetail: { open: true, companyId } },
        })),
      openCompanyAdd: () =>
        set((state) => ({
          modal: { ...state.modal, companyForm: { open: true, companyId: undefined } },
        })),
      openCompanyEdit: (companyId) =>
        set((state) => ({
          modal: { ...state.modal, companyForm: { open: true, companyId } },
        })),
      openCriteria: () =>
        set((state) => ({
          modal: { ...state.modal, criteria: { open: true } },
        })),
      openKanbanSetting: () =>
        set((state) => ({
          modal: { ...state.modal, kanbanSetting: { open: true } },
        })),
      closeModal: (modal) =>
        set((state) => ({
          modal: {
            ...state.modal,
            [modal]: { open: false },
          },
        })),
    }),
    {
      name: 'next-company-store',
      partialize: (state) => ({
        criteriaList: state.criteriaList,
      }),
    }
  )
)
