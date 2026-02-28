'use client'

import { useAppStore } from '@/store/useAppStore'
import { Button } from '@/components/ui/button'
import { LayoutGrid, List, Settings, Plus } from 'lucide-react'

export default function Header() {
  const { viewMode, setViewMode, openCompanyAdd, openCriteria } = useAppStore()

  return (
    <header className="sticky top-0 z-10 bg-white border-b border-gray-200">
      <div className="max-w-screen-xl mx-auto px-4 h-14 flex items-center justify-between gap-4">
        {/* 로고 */}
        <h1 className="text-lg font-bold text-gray-900 shrink-0">이직 도우미</h1>

        {/* 우측 액션 */}
        <div className="flex items-center gap-2">
          {/* 칸반 / 순위 토글 */}
          <div className="flex items-center border border-gray-200 rounded-lg overflow-hidden">
            <button
              onClick={() => setViewMode('kanban')}
              className={`flex items-center gap-1.5 px-3 py-1.5 text-sm transition-colors ${
                viewMode === 'kanban'
                  ? 'bg-gray-900 text-white'
                  : 'bg-white text-gray-500 hover:bg-gray-50'
              }`}
            >
              <LayoutGrid size={14} />
              칸반
            </button>
            <button
              onClick={() => setViewMode('rank')}
              className={`flex items-center gap-1.5 px-3 py-1.5 text-sm transition-colors ${
                viewMode === 'rank'
                  ? 'bg-gray-900 text-white'
                  : 'bg-white text-gray-500 hover:bg-gray-50'
              }`}
            >
              <List size={14} />
              순위
            </button>
          </div>

          {/* 평가기준 설정 */}
          <Button
            variant="outline"
            size="sm"
            className="gap-1.5 text-gray-600"
            onClick={openCriteria}
          >
            <Settings size={14} />
            평가기준
          </Button>

          {/* 회사 추가 */}
          <Button size="sm" className="gap-1.5" onClick={openCompanyAdd}>
            <Plus size={14} />
            회사 추가
          </Button>
        </div>
      </div>
    </header>
  )
}
