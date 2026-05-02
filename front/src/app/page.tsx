'use client'

import FilterBar from '@/components/layout/FilterBar'
import MainView from '@/components/layout/MainView'
import AppInitializer from '@/components/AppInitializer'
import CompanyDetailModal from '@/components/modal/CompanyDetailModal'
import CompanyFormModal from '@/components/modal/CompanyFormModal'
import CriteriaSettingModal from '@/components/modal/CriteriaSettingModal'
import KanbanColumnSettingModal from '@/components/modal/KanbanColumnSettingModal'
import LoginPanel from '@/components/auth/LoginPanel'
import { useAppStore } from '@/store/useAppStore'

function AuthenticatedHome() {
  const { authChecked, currentMember } = useAppStore()

  if (!authChecked) {
    return <main className="min-h-screen bg-slate-50" />
  }

  if (!currentMember) {
    return <LoginPanel />
  }

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

export default function Home() {
  return (
    <>
      <AppInitializer />
      <AuthenticatedHome />
    </>
  )
}
