'use client'

import { useEffect } from 'react'
import { useAppStore } from '@/store/useAppStore'

export default function AppInitializer() {
  const loadCompanies = useAppStore((s) => s.loadCompanies)
  const loadKanbanColumns = useAppStore((s) => s.loadKanbanColumns)

  useEffect(() => {
    loadCompanies().catch((error) => console.error('Failed to load companies', error))
    loadKanbanColumns().catch((error) =>
      console.error('Failed to load kanban columns', error)
    )
  }, [loadCompanies, loadKanbanColumns])

  return null
}
