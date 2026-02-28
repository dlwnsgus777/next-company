'use client'

import { useMemo } from 'react'
import { useAppStore } from '@/store/useAppStore'
import { calcRankedCompanies } from '@/lib/score'
import { CompanyWithScore } from '@/types'

export function useFilteredCompanies(): CompanyWithScore[] {
  const { companies, criteriaList, filterTarget, sortBy } = useAppStore()

  const ranked = useMemo(
    () => calcRankedCompanies(companies, criteriaList),
    [companies, criteriaList]
  )

  const filtered = useMemo(() => {
    if (filterTarget === 'ALL') return ranked
    return ranked.filter((c) => c.targetStatus === filterTarget)
  }, [ranked, filterTarget])

  const sorted = useMemo(() => {
    if (sortBy === 'score') return filtered // 이미 점수순 정렬됨

    const copy = [...filtered]
    if (sortBy === 'name') {
      copy.sort((a, b) => a.name.localeCompare(b.name, 'ko'))
    }
    if (sortBy === 'deadline') {
      copy.sort((a, b) => {
        if (!a.recruitmentDeadline && !b.recruitmentDeadline) return 0
        if (!a.recruitmentDeadline) return 1
        if (!b.recruitmentDeadline) return -1
        return (
          new Date(a.recruitmentDeadline).getTime() -
          new Date(b.recruitmentDeadline).getTime()
        )
      })
    }
    return copy
  }, [filtered, sortBy])

  return sorted
}
