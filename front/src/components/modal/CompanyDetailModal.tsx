'use client'

import { useAppStore } from '@/store/useAppStore'
import { APPLICATION_STATUS_LABEL, APPLICATION_STATUS_ORDER } from '@/types'
import { calcTotalScore } from '@/lib/score'
import { calcDday } from '@/lib/score'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { Progress } from '@/components/ui/progress'
import { Button } from '@/components/ui/button'
import { Separator } from '@/components/ui/separator'
import { cn } from '@/lib/utils'
import { ExternalLink, ChevronRight } from 'lucide-react'

const TARGET_STYLE = {
  O: { label: 'O 목표', className: 'bg-emerald-50 text-emerald-700 border-emerald-200' },
  '△': { label: '△ 고려중', className: 'bg-amber-50 text-amber-700 border-amber-200' },
  X: { label: 'X 비목표', className: 'bg-gray-50 text-gray-500 border-gray-200' },
}

export default function CompanyDetailModal() {
  const {
    modal,
    companies,
    criteriaList,
    closeModal,
    openCompanyEdit,
    deleteCompany,
    updateApplicationStatus,
  } = useAppStore()

  const { open, companyId } = modal.companyDetail
  const company = companies.find((c) => c.id === companyId)

  if (!company) return null

  const totalScore = calcTotalScore(company, criteriaList)
  const dday = calcDday(company.recruitmentDeadline)

  const currentStepIndex = APPLICATION_STATUS_ORDER.indexOf(
    company.applicationStatus as (typeof APPLICATION_STATUS_ORDER)[number]
  )
  const isTerminal =
    company.applicationStatus === 'REJECTED' ||
    company.applicationStatus === 'WITHDRAWN'
  const nextStatus =
    !isTerminal && currentStepIndex < APPLICATION_STATUS_ORDER.length - 1
      ? APPLICATION_STATUS_ORDER[currentStepIndex + 1]
      : null

  const handleDelete = () => {
    if (confirm(`"${company.name}" 회사를 삭제하시겠습니까?`)) {
      deleteCompany(company.id)
      closeModal('companyDetail')
    }
  }

  const handleEdit = () => {
    closeModal('companyDetail')
    openCompanyEdit(company.id)
  }

  return (
    <Dialog open={open} onOpenChange={() => closeModal('companyDetail')}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto flex flex-col gap-5">
        {/* 헤더 */}
        <DialogHeader>
          <div className="flex items-center gap-3 flex-wrap">
            <DialogTitle className="text-xl font-bold">{company.name}</DialogTitle>
            <span
              className={cn(
                'text-xs font-medium px-2 py-0.5 rounded-full border',
                TARGET_STYLE[company.targetStatus].className
              )}
            >
              {TARGET_STYLE[company.targetStatus].label}
            </span>
          </div>

          {/* 기본 정보 */}
          <div className="flex flex-wrap gap-4 text-sm text-gray-500 pt-1">
            {company.jobPostingUrl && (
              <a
                href={company.jobPostingUrl}
                target="_blank"
                rel="noopener noreferrer"
                className="flex items-center gap-1 text-blue-600 hover:underline"
              >
                채용공고
                <ExternalLink className="w-3 h-3" />
              </a>
            )}
            {company.recruitmentDeadline ? (
              <span className="flex items-center gap-1.5">
                마감일: {company.recruitmentDeadline}
                {dday && (
                  <span
                    className={cn(
                      'text-xs font-semibold px-1.5 py-0.5 rounded',
                      dday.critical
                        ? 'bg-red-100 text-red-600'
                        : dday.urgent
                          ? 'bg-orange-100 text-orange-600'
                          : 'bg-gray-100 text-gray-500'
                    )}
                  >
                    {dday.label}
                  </span>
                )}
              </span>
            ) : (
              <span>마감일: 미정</span>
            )}
          </div>
        </DialogHeader>

        <Separator />

        {/* 진행 현황 */}
        <section className="space-y-3">
          <h3 className="text-sm font-semibold text-gray-700">진행 현황</h3>

          {isTerminal ? (
            <div className="flex items-center gap-3">
              <div className="flex items-center gap-1 flex-wrap opacity-40">
                {APPLICATION_STATUS_ORDER.map((status, index) => (
                  <div key={status} className="flex items-center">
                    <span className="text-xs px-2.5 py-1 rounded-full bg-gray-100 text-gray-400 font-medium">
                      {APPLICATION_STATUS_LABEL[status]}
                    </span>
                    {index < APPLICATION_STATUS_ORDER.length - 1 && (
                      <ChevronRight className="w-3 h-3 text-gray-300 mx-0.5" />
                    )}
                  </div>
                ))}
              </div>
              <span
                className={cn(
                  'text-sm font-semibold px-3 py-1 rounded-full shrink-0',
                  company.applicationStatus === 'REJECTED'
                    ? 'bg-red-50 text-red-600'
                    : 'bg-gray-100 text-gray-500'
                )}
              >
                {APPLICATION_STATUS_LABEL[company.applicationStatus]}
              </span>
            </div>
          ) : (
            <div className="flex items-center gap-1 flex-wrap">
              {APPLICATION_STATUS_ORDER.map((status, index) => {
                const isCompleted = index < currentStepIndex
                const isCurrent = index === currentStepIndex
                return (
                  <div key={status} className="flex items-center">
                    <span
                      className={cn(
                        'text-xs px-2.5 py-1 rounded-full font-medium',
                        isCurrent
                          ? 'bg-blue-600 text-white'
                          : isCompleted
                            ? 'bg-blue-100 text-blue-700'
                            : 'bg-gray-100 text-gray-400'
                      )}
                    >
                      {APPLICATION_STATUS_LABEL[status]}
                    </span>
                    {index < APPLICATION_STATUS_ORDER.length - 1 && (
                      <ChevronRight className="w-3 h-3 text-gray-300 mx-0.5" />
                    )}
                  </div>
                )
              })}
            </div>
          )}

          {nextStatus && (
            <Button
              variant="outline"
              size="sm"
              className="text-xs"
              onClick={() => updateApplicationStatus(company.id, nextStatus)}
            >
              다음 단계로 이동: {APPLICATION_STATUS_LABEL[nextStatus]} →
            </Button>
          )}
        </section>

        <Separator />

        {/* 평가 항목 */}
        <section className="space-y-3">
          <h3 className="text-sm font-semibold text-gray-700">평가 항목</h3>

          <div className="rounded-lg border overflow-hidden">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-gray-50 border-b">
                  <th className="text-left px-4 py-2.5 font-medium text-gray-600 w-1/4">
                    항목
                  </th>
                  <th className="text-left px-4 py-2.5 font-medium text-gray-600">
                    실제 정보
                  </th>
                  <th className="text-right px-4 py-2.5 font-medium text-gray-600 w-20">
                    점수
                  </th>
                </tr>
              </thead>
              <tbody>
                {criteriaList.map((criteria, index) => {
                  const scoreObj = company.scores.find(
                    (s) => s.criteriaId === criteria.id
                  )
                  const score = scoreObj?.score ?? 0
                  const actualInfo = scoreObj?.actualInfo ?? ''
                  return (
                    <tr
                      key={criteria.id}
                      className={cn(
                        'border-b last:border-b-0',
                        index % 2 === 0 ? 'bg-white' : 'bg-gray-50/50'
                      )}
                    >
                      <td className="px-4 py-3">
                        <div className="font-medium text-gray-800">
                          {criteria.name}
                        </div>
                        <div className="text-xs text-gray-400">
                          가중치 {criteria.weight}%
                        </div>
                      </td>
                      <td className="px-4 py-3 text-gray-600">
                        {actualInfo || (
                          <span className="text-gray-300">-</span>
                        )}
                      </td>
                      <td className="px-4 py-3 text-right">
                        <span className="font-semibold text-gray-900">
                          {score}
                        </span>
                        <span className="text-gray-400 text-xs">/100</span>
                      </td>
                    </tr>
                  )
                })}
              </tbody>
            </table>
          </div>

          {/* 총점 바 */}
          <div className="flex items-center gap-3 px-1 pt-1">
            <span className="text-sm font-medium text-gray-700 shrink-0 w-8">
              총점
            </span>
            <Progress value={totalScore} className="flex-1 h-2" />
            <div className="shrink-0 text-right w-16">
              <span className="text-xl font-bold text-gray-900">{totalScore}</span>
              <span className="text-xs text-gray-400 ml-0.5">점</span>
            </div>
          </div>
        </section>

        {/* 메모 */}
        {company.memo && (
          <>
            <Separator />
            <section className="space-y-2">
              <h3 className="text-sm font-semibold text-gray-700">메모</h3>
              <p className="text-sm text-gray-600 bg-gray-50 rounded-lg px-4 py-3 whitespace-pre-wrap leading-relaxed">
                {company.memo}
              </p>
            </section>
          </>
        )}

        {/* 액션 버튼 */}
        <div className="flex justify-between pt-2 border-t">
          <Button variant="destructive" size="sm" onClick={handleDelete}>
            삭제
          </Button>
          <Button variant="outline" size="sm" onClick={handleEdit}>
            수정
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  )
}
