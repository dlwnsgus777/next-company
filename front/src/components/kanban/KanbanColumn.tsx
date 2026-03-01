'use client'

import { useState } from 'react'
import { ApplicationStatus, CompanyWithScore } from '@/types'
import { useAppStore } from '@/store/useAppStore'
import CompanyCard from './CompanyCard'
import { cn } from '@/lib/utils'

interface KanbanColumnProps {
  label: string
  companies: CompanyWithScore[]
  accentColor: string
  statuses: ApplicationStatus[]
}

export default function KanbanColumn({ label, companies, accentColor, statuses }: KanbanColumnProps) {
  const updateApplicationStatus = useAppStore((s) => s.updateApplicationStatus)
  const [isDragOver, setIsDragOver] = useState(false)

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault()
    e.dataTransfer.dropEffect = 'move'
    setIsDragOver(true)
  }

  const handleDragLeave = (e: React.DragEvent) => {
    // 자식 요소로 이동 시 false 처리 방지: currentTarget 기준으로 판단
    if (!e.currentTarget.contains(e.relatedTarget as Node)) {
      setIsDragOver(false)
    }
  }

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    setIsDragOver(false)
    const companyId = e.dataTransfer.getData('companyId')
    if (!companyId || statuses.length === 0) return
    updateApplicationStatus(companyId, statuses[0])
  }

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

      {/* 카드 목록 + Drop Zone */}
      <div
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
        onDrop={handleDrop}
        className={cn(
          'flex flex-col gap-2.5 min-h-[120px] rounded-xl p-1 transition-colors duration-150',
          isDragOver && 'bg-blue-50 ring-2 ring-blue-200 ring-inset'
        )}
      >
        {companies.length === 0 ? (
          <div
            className={cn(
              'flex items-center justify-center h-24 rounded-xl border-2 border-dashed text-sm transition-colors',
              isDragOver
                ? 'border-blue-300 text-blue-400'
                : 'border-gray-200 text-gray-400'
            )}
          >
            {isDragOver ? '여기에 놓기' : '아직 없어요'}
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
