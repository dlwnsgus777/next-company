'use client'

import { useAppStore } from '@/store/useAppStore'
import KanbanBoard from '@/components/kanban/KanbanBoard'
import RankingList from '@/components/ranking/RankingList'

export default function MainView() {
  const viewMode = useAppStore((s) => s.viewMode)

  if (viewMode === 'rank') {
    return <RankingList />
  }

  return <KanbanBoard />
}
