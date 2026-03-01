import FilterBar from '@/components/layout/FilterBar'
import MainView from '@/components/layout/MainView'
import CompanyDetailModal from '@/components/modal/CompanyDetailModal'
import CompanyFormModal from '@/components/modal/CompanyFormModal'
import CriteriaSettingModal from '@/components/modal/CriteriaSettingModal'
import KanbanColumnSettingModal from '@/components/modal/KanbanColumnSettingModal'

export default function Home() {
  return (
    <main>
      <FilterBar />
      <MainView />
      <CompanyDetailModal />
      <CompanyFormModal />
      <CriteriaSettingModal />
      <KanbanColumnSettingModal />
    </main>
  )
}
