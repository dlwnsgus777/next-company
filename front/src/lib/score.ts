import { Company, CompanyWithScore, Criteria } from '@/types'

// 회사 총점 계산
export function calcTotalScore(company: Company, criteriaList: Criteria[]): number {
  if (criteriaList.length === 0) return 0

  const total = criteriaList.reduce((acc, criteria) => {
    const score = company.scores.find((s) => s.criteriaId === criteria.id)
    if (!score) return acc
    return acc + (score.score * criteria.weight) / 100
  }, 0)

  return Math.round(total * 10) / 10
}

// 회사 목록에 총점 + 순위 부여
export function calcRankedCompanies(
  companies: Company[],
  criteriaList: Criteria[]
): CompanyWithScore[] {
  const withScores = companies.map((company) => ({
    ...company,
    totalScore: calcTotalScore(company, criteriaList),
    rank: 0,
  }))

  withScores.sort((a, b) => b.totalScore - a.totalScore)

  withScores.forEach((company, index) => {
    company.rank = index + 1
  })

  return withScores
}

// D-day 계산
export function calcDday(deadline?: string): { label: string; urgent: boolean; critical: boolean } | null {
  if (!deadline) return null

  const today = new Date()
  today.setHours(0, 0, 0, 0)
  const deadlineDate = new Date(deadline)
  deadlineDate.setHours(0, 0, 0, 0)

  const diffMs = deadlineDate.getTime() - today.getTime()
  const diffDays = Math.ceil(diffMs / (1000 * 60 * 60 * 24))

  if (diffDays < 0) return { label: '마감', urgent: true, critical: true }
  if (diffDays === 0) return { label: 'D-day', urgent: true, critical: true }

  return {
    label: `D-${diffDays}`,
    urgent: diffDays <= 5,
    critical: diffDays <= 2,
  }
}
