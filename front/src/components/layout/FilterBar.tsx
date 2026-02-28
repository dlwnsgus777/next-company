'use client'

import { useAppStore } from '@/store/useAppStore'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'

const FILTER_TABS = [
  { value: 'ALL', label: '전체' },
  { value: 'O', label: '목표 O' },
  { value: '△', label: '고려중 △' },
] as const

const SORT_OPTIONS = [
  { value: 'score', label: '점수순' },
  { value: 'name', label: '이름순' },
  { value: 'deadline', label: '마감일순' },
] as const

export default function FilterBar() {
  const { filterTarget, sortBy, setFilterTarget, setSortBy } = useAppStore()

  return (
    <div className="max-w-screen-xl mx-auto px-4 py-3 flex items-center justify-between">
      {/* 필터 탭 */}
      <div className="flex items-center gap-1">
        {FILTER_TABS.map((tab) => (
          <button
            key={tab.value}
            onClick={() => setFilterTarget(tab.value)}
            className={`px-3 py-1.5 rounded-md text-sm font-medium transition-colors ${
              filterTarget === tab.value
                ? 'bg-gray-900 text-white'
                : 'text-gray-500 hover:bg-gray-100'
            }`}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* 정렬 */}
      <Select value={sortBy} onValueChange={(v) => setSortBy(v as typeof sortBy)}>
        <SelectTrigger className="w-28 h-8 text-sm">
          <SelectValue />
        </SelectTrigger>
        <SelectContent>
          {SORT_OPTIONS.map((opt) => (
            <SelectItem key={opt.value} value={opt.value}>
              {opt.label}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>
    </div>
  )
}
