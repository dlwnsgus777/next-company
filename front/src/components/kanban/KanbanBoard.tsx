'use client'

import { CompanyWithScore } from '@/types'
import { useAppStore } from '@/store/useAppStore'
import { useFilteredCompanies } from '@/hooks/useFilteredCompanies'
import KanbanColumn from './KanbanColumn'

function groupByColumn(
  companies: CompanyWithScore[],
  columns: ReturnType<typeof useAppStore>['kanbanColumns']
): Record<string, CompanyWithScore[]> {
  return columns.reduce(
    (acc, col) => {
      acc[col.id] = companies.filter((c) => col.statuses.includes(c.applicationStatus))
      return acc
    },
    {} as Record<string, CompanyWithScore[]>
  )
}

export default function KanbanBoard() {
  const kanbanColumns = useAppStore((s) => s.kanbanColumns)
  const companies = useFilteredCompanies()
  const grouped = groupByColumn(companies, kanbanColumns)

  const colCount = kanbanColumns.length
  const gridClass =
    colCount <= 3 ? 'grid-cols-3' :
    colCount === 4 ? 'grid-cols-4' :
    colCount === 5 ? 'grid-cols-5' :
    'grid-cols-6'

  return (
    <div className="max-w-screen-xl mx-auto px-4 pb-8">
      <div className={`grid ${gridClass} gap-4`}>
        {kanbanColumns.map((col) => (
          <KanbanColumn
            key={col.id}
            label={col.label}
            companies={grouped[col.id]}
            accentColor={col.accentColor}
          />
        ))}
      </div>
    </div>
  )
}
