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

// 칸반 컬럼 설정
export interface KanbanColumnConfig {
  id: string
  label: string
  statuses: ApplicationStatus[]
  accentColor: string // Tailwind bg 클래스
  order: number
}

export const DEFAULT_KANBAN_COLUMNS: KanbanColumnConfig[] = [
  { id: 'col-1', label: '지원 전',   statuses: ['NOT_APPLIED'],                         accentColor: 'bg-gray-400',    order: 0 },
  { id: 'col-2', label: '서류',      statuses: ['APPLIED', 'DOCUMENT_PASS'],            accentColor: 'bg-blue-400',    order: 1 },
  { id: 'col-3', label: '면접',      statuses: ['FIRST_INTERVIEW', 'SECOND_INTERVIEW'], accentColor: 'bg-purple-400',  order: 2 },
  { id: 'col-4', label: '최종 합격', statuses: ['FINAL_ACCEPTED'],                      accentColor: 'bg-emerald-400', order: 3 },
  { id: 'col-5', label: '탈락',      statuses: ['REJECTED', 'WITHDRAWN'],               accentColor: 'bg-red-400',     order: 4 },
]

export const ACCENT_COLOR_PRESETS: { label: string; value: string }[] = [
  { label: '회색',   value: 'bg-gray-400' },
  { label: '파랑',   value: 'bg-blue-400' },
  { label: '하늘',   value: 'bg-sky-400' },
  { label: '보라',   value: 'bg-purple-400' },
  { label: '초록',   value: 'bg-emerald-400' },
  { label: '주황',   value: 'bg-orange-400' },
  { label: '빨강',   value: 'bg-red-400' },
  { label: '분홍',   value: 'bg-pink-400' },
]
