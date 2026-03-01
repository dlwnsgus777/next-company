'use client'

import { useFilteredCompanies } from '@/hooks/useFilteredCompanies'
import { useAppStore } from '@/store/useAppStore'
import { APPLICATION_STATUS_LABEL } from '@/types'
import DeadlineBadge from '@/components/common/DeadlineBadge'
import { Progress } from '@/components/ui/progress'
import { cn } from '@/lib/utils'

const TARGET_STYLE = {
  O: { label: 'O 목표', className: 'bg-emerald-50 text-emerald-700 border-emerald-200' },
  '△': { label: '△ 고려중', className: 'bg-amber-50 text-amber-700 border-amber-200' },
  X: { label: 'X 비목표', className: 'bg-gray-50 text-gray-500 border-gray-200' },
}

export default function RankingList() {
  const companies = useFilteredCompanies()
  const openCompanyDetail = useAppStore((s) => s.openCompanyDetail)

  if (companies.length === 0) {
    return (
      <div className="max-w-screen-md mx-auto px-4 py-16 text-center text-gray-400 text-sm">
        등록된 회사가 없어요
      </div>
    )
  }

  // 목표(O) 회사가 끝나고 비목표가 시작되는 인덱스
  const firstNonTargetIndex = companies.findIndex((c) => c.targetStatus !== 'O')

  return (
    <div className="max-w-screen-md mx-auto px-4 py-6 flex flex-col gap-1.5">
      {companies.map((company, index) => (
        <div key={company.id}>
          {/* 목표 / 비목표 구분선 */}
          {index === firstNonTargetIndex && firstNonTargetIndex > 0 && (
            <div className="flex items-center gap-3 my-3">
              <div className="flex-1 border-t border-dashed border-gray-300" />
              <span className="text-xs text-gray-400 shrink-0">목표 외</span>
              <div className="flex-1 border-t border-dashed border-gray-300" />
            </div>
          )}

          {/* 행 */}
          <div
            onClick={() => openCompanyDetail(company.id)}
            className={cn(
              'flex items-center gap-4 px-4 py-3.5 rounded-xl cursor-pointer',
              'bg-white border border-gray-200',
              'hover:border-gray-400 hover:shadow-sm transition-all duration-150'
            )}
          >
            {/* 순위 */}
            <span className="w-8 text-center text-sm font-bold text-gray-400 shrink-0">
              #{company.rank}
            </span>

            {/* 회사명 + 점수 바 */}
            <div className="flex-1 min-w-0 flex flex-col gap-2">
              <span className="text-sm font-semibold text-gray-900 truncate">
                {company.name}
              </span>
              <Progress value={company.totalScore} className="h-1.5" />
            </div>

            {/* 점수 */}
            <div className="shrink-0 text-right w-14">
              <span className="text-lg font-bold text-gray-900">{company.totalScore}</span>
              <span className="text-xs text-gray-400 ml-0.5">점</span>
            </div>

            {/* 목표 여부 배지 */}
            <span
              className={cn(
                'shrink-0 text-xs font-medium px-2 py-0.5 rounded-full border',
                TARGET_STYLE[company.targetStatus].className
              )}
            >
              {TARGET_STYLE[company.targetStatus].label}
            </span>

            {/* 진행 상태 */}
            <span className="shrink-0 text-xs text-gray-500 w-16 text-right">
              {APPLICATION_STATUS_LABEL[company.applicationStatus]}
            </span>

            {/* D-day */}
            <DeadlineBadge deadline={company.recruitmentDeadline} />
          </div>
        </div>
      ))}
    </div>
  )
}
