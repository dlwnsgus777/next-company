'use client'

import { CompanyWithScore } from '@/types'
import { useAppStore } from '@/store/useAppStore'
import DeadlineBadge from '@/components/common/DeadlineBadge'
import { cn } from '@/lib/utils'

interface CompanyCardProps {
  company: CompanyWithScore
}

const TARGET_STYLE = {
  O: { label: 'O 목표', className: 'bg-emerald-50 text-emerald-700 border-emerald-200' },
  '△': { label: '△ 고려중', className: 'bg-amber-50 text-amber-700 border-amber-200' },
  X: { label: 'X 비목표', className: 'bg-gray-50 text-gray-500 border-gray-200' },
}

export default function CompanyCard({ company }: CompanyCardProps) {
  const openCompanyDetail = useAppStore((s) => s.openCompanyDetail)
  const target = TARGET_STYLE[company.targetStatus]

  return (
    <div
      onClick={() => openCompanyDetail(company.id)}
      className={cn(
        'bg-white rounded-xl border border-gray-200 p-3.5 cursor-pointer',
        'hover:border-gray-400 hover:shadow-sm transition-all duration-150',
        'flex flex-col gap-2'
      )}
    >
      {/* 순위 */}
      <div className="flex items-center justify-between">
        <span className="text-xs font-bold text-gray-400">#{company.rank}위</span>
        <DeadlineBadge deadline={company.recruitmentDeadline} />
      </div>

      {/* 회사명 */}
      <p className="font-semibold text-gray-900 text-sm leading-tight line-clamp-2">
        {company.name}
      </p>

      {/* 총점 */}
      <div className="flex items-baseline gap-1">
        <span className="text-2xl font-bold text-gray-900">{company.totalScore}</span>
        <span className="text-xs text-gray-400">점</span>
      </div>

      {/* 목표 여부 배지 */}
      <span
        className={cn(
          'self-start text-xs font-medium px-2 py-0.5 rounded-full border',
          target.className
        )}
      >
        {target.label}
      </span>
    </div>
  )
}
