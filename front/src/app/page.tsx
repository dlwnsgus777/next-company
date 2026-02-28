import FilterBar from '@/components/layout/FilterBar'

export default function Home() {
  return (
    <main>
      <FilterBar />
      {/* Phase 3에서 KanbanBoard / RankingList 추가 예정 */}
      <div className="max-w-screen-xl mx-auto px-4 py-8 text-center text-gray-400 text-sm">
        칸반 보드 준비 중...
      </div>
    </main>
  )
}
