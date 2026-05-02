'use client'

import { useEffect } from 'react'
import { useAppStore } from '@/store/useAppStore'

export default function AppInitializer() {
  const currentMember = useAppStore((s) => s.currentMember)
  const loadCurrentMember = useAppStore((s) => s.loadCurrentMember)
  const loadCompanies = useAppStore((s) => s.loadCompanies)
  const loadKanbanColumns = useAppStore((s) => s.loadKanbanColumns)

  useEffect(() => {
    loadCurrentMember().catch((error) => console.error('Failed to load current member', error))
  }, [loadCurrentMember])

  useEffect(() => {
    if (!currentMember) {
      return
    }

    loadCompanies().catch((error) => console.error('Failed to load companies', error))
    loadKanbanColumns().catch((error) => console.error('Failed to load kanban columns', error))
  }, [currentMember, loadCompanies, loadKanbanColumns])

  return null
}
