// 평가 기준
export interface Criteria {
  id: string
  name: string
  weight: number // 합계 100
  order: number
}

// 회사 목표 여부
export type TargetStatus = 'O' | '△' | 'X'

// 지원 진행 상태
export type ApplicationStatus =
  | 'NOT_APPLIED'
  | 'APPLIED'
  | 'DOCUMENT_PASS'
  | 'FIRST_INTERVIEW'
  | 'SECOND_INTERVIEW'
  | 'FINAL_ACCEPTED'
  | 'REJECTED'
  | 'WITHDRAWN'

export const APPLICATION_STATUS_LABEL: Record<ApplicationStatus, string> = {
  NOT_APPLIED: '지원 전',
  APPLIED: '서류 지원',
  DOCUMENT_PASS: '서류 합격',
  FIRST_INTERVIEW: '1차 면접',
  SECOND_INTERVIEW: '2차 면접',
  FINAL_ACCEPTED: '최종 합격',
  REJECTED: '탈락',
  WITHDRAWN: '지원 취소',
}

export const APPLICATION_STATUS_ORDER: ApplicationStatus[] = [
  'NOT_APPLIED',
  'APPLIED',
  'DOCUMENT_PASS',
  'FIRST_INTERVIEW',
  'SECOND_INTERVIEW',
  'FINAL_ACCEPTED',
]

// 회사별 항목 점수
export interface CompanyScore {
  criteriaId: string
  actualInfo: string  // 실제 정보 텍스트
  score: number       // 0 ~ 100
}

// 회사
export interface Company {
  id: string
  name: string
  targetStatus: TargetStatus
  jobPostingUrl?: string
  recruitmentDeadline?: string  // ISO date string
  applicationStatus: ApplicationStatus
  scores: CompanyScore[]
  memo?: string
  createdAt: string
  updatedAt: string
}

// 총점 계산 결과
export interface CompanyWithScore extends Company {
  totalScore: number
  rank: number
}
