import FilterBar from '@/components/layout/FilterBar'
import MainView from '@/components/layout/MainView'
import CompanyDetailModal from '@/components/modal/CompanyDetailModal'

export default function Home() {
  return (
    <main>
      <FilterBar />
      <MainView />
      <CompanyDetailModal />
    </main>
  )
}
