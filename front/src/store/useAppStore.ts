import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { Company, Criteria, KanbanColumnConfig, DEFAULT_KANBAN_COLUMNS } from '@/types'
import { MOCK_COMPANIES, MOCK_CRITERIA } from '@/lib/mock'

// 기본 평가 기준
const DEFAULT_CRITERIA: Criteria[] = MOCK_CRITERIA

type FilterTarget = 'ALL' | 'O' | '△'
type SortBy = 'score' | 'name' | 'deadline'
type ViewMode = 'kanban' | 'rank'

interface ModalState {
  companyDetail: { open: boolean; companyId?: string }
  companyForm: { open: boolean; companyId?: string } // companyId 없으면 추가, 있으면 수정
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

  // 칸반 컬럼 액션
  setKanbanColumns: (columns: KanbanColumnConfig[]) => void

  // 회사 CRUD
  addCompany: (company: Omit<Company, 'id' | 'createdAt' | 'updatedAt'>) => void
  updateCompany: (id: string, data: Partial<Omit<Company, 'id' | 'createdAt'>>) => void
  deleteCompany: (id: string) => void
  updateApplicationStatus: (id: string, status: Company['applicationStatus']) => void

  // UI 액션
  setFilterTarget: (filter: FilterTarget) => void
  setSortBy: (sort: SortBy) => void
  setViewMode: (mode: ViewMode) => void

  // 모달 액션
  openCompanyDetail: (companyId: string) => void
  openCompanyAdd: () => void
  openCompanyEdit: (companyId: string) => void
  openCriteria: () => void
  openKanbanSetting: () => void
  closeModal: (modal: keyof ModalState) => void
}

export const useAppStore = create<AppState>()(
  persist(
    (set) => ({
      criteriaList: DEFAULT_CRITERIA,
      companies: MOCK_COMPANIES,
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

      setKanbanColumns: (kanbanColumns) => set({ kanbanColumns }),

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
            c.id === id
              ? { ...c, applicationStatus: status, updatedAt: new Date().toISOString() }
              : c
          ),
        })),

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
        companies: state.companies,
        kanbanColumns: state.kanbanColumns,
      }),
    }
  )
)
