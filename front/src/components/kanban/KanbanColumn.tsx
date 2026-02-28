'use client'

import { CompanyWithScore } from '@/types'
import CompanyCard from './CompanyCard'

interface KanbanColumnProps {
  label: string
  companies: CompanyWithScore[]
  accentColor: string
}

export default function KanbanColumn({ label, companies, accentColor }: KanbanColumnProps) {
  return (
    <div className="flex flex-col min-w-[220px] w-full">
      {/* 컬럼 헤더 */}
      <div className="flex items-center gap-2 mb-3 px-1">
        <span className={`w-2 h-2 rounded-full ${accentColor}`} />
        <span className="text-sm font-semibold text-gray-700">{label}</span>
        <span className="ml-auto text-xs font-medium text-gray-400 bg-gray-100 px-2 py-0.5 rounded-full">
          {companies.length}
        </span>
      </div>

      {/* 카드 목록 */}
      <div className="flex flex-col gap-2.5 min-h-[120px]">
        {companies.length === 0 ? (
          <div className="flex items-center justify-center h-24 rounded-xl border-2 border-dashed border-gray-200 text-gray-400 text-sm">
            아직 없어요
          </div>
        ) : (
          companies.map((company) => (
            <CompanyCard key={company.id} company={company} />
          ))
        )}
      </div>
    </div>
  )
}
