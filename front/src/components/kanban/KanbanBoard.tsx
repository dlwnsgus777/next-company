'use client'

import { ApplicationStatus, CompanyWithScore } from '@/types'
import { useFilteredCompanies } from '@/hooks/useFilteredCompanies'
import KanbanColumn from './KanbanColumn'

const COLUMNS: {
  key: string
  label: string
  statuses: ApplicationStatus[]
  accentColor: string
}[] = [
  {
    key: 'not_applied',
    label: '지원 전',
    statuses: ['NOT_APPLIED'],
    accentColor: 'bg-gray-400',
  },
  {
    key: 'applied',
    label: '서류 지원',
    statuses: ['APPLIED', 'DOCUMENT_PASS'],
    accentColor: 'bg-blue-400',
  },
  {
    key: 'interview',
    label: '면접 진행',
    statuses: ['FIRST_INTERVIEW', 'SECOND_INTERVIEW'],
    accentColor: 'bg-purple-400',
  },
  {
    key: 'accepted',
    label: '최종 합격',
    statuses: ['FINAL_ACCEPTED'],
    accentColor: 'bg-emerald-400',
  },
  {
    key: 'rejected',
    label: '탈락',
    statuses: ['REJECTED', 'WITHDRAWN'],
    accentColor: 'bg-red-300',
  },
]

function groupByStatus(
  companies: CompanyWithScore[]
): Record<string, CompanyWithScore[]> {
  return COLUMNS.reduce(
    (acc, col) => {
      acc[col.key] = companies.filter((c) => col.statuses.includes(c.applicationStatus))
      return acc
    },
    {} as Record<string, CompanyWithScore[]>
  )
}

export default function KanbanBoard() {
  const companies = useFilteredCompanies()
  const grouped = groupByStatus(companies)

  return (
    <div className="max-w-screen-xl mx-auto px-4 pb-8">
      <div className="grid grid-cols-5 gap-4">
        {COLUMNS.map((col) => (
          <KanbanColumn
            key={col.key}
            label={col.label}
            companies={grouped[col.key]}
            accentColor={col.accentColor}
          />
        ))}
      </div>
    </div>
  )
}
