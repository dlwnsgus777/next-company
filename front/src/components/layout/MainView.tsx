'use client'

import { useAppStore } from '@/store/useAppStore'
import KanbanBoard from '@/components/kanban/KanbanBoard'

export default function MainView() {
  const viewMode = useAppStore((s) => s.viewMode)

  if (viewMode === 'rank') {
    // Phase 4에서 RankingList 추가 예정
    return (
      <div className="max-w-screen-xl mx-auto px-4 py-8 text-center text-gray-400 text-sm">
        순위 보기 준비 중...
      </div>
    )
  }

  return <KanbanBoard />
}
