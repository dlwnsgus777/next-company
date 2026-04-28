import FilterBar from '@/components/layout/FilterBar'
import MainView from '@/components/layout/MainView'
import AppInitializer from '@/components/AppInitializer'
import CompanyDetailModal from '@/components/modal/CompanyDetailModal'
import CompanyFormModal from '@/components/modal/CompanyFormModal'
import CriteriaSettingModal from '@/components/modal/CriteriaSettingModal'
import KanbanColumnSettingModal from '@/components/modal/KanbanColumnSettingModal'

export default function Home() {
  return (
    <main>
      <AppInitializer />
      <FilterBar />
      <MainView />
      <CompanyDetailModal />
      <CompanyFormModal />
      <CriteriaSettingModal />
      <KanbanColumnSettingModal />
    </main>
  )
}
