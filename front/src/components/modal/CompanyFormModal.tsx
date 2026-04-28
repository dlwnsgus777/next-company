'use client'

import { useState, useEffect, useMemo } from 'react'
import { useAppStore } from '@/store/useAppStore'
import {
  TargetStatus,
  JobChangeStatus,
  JOB_CHANGE_STATUS_LABEL,
  Criteria,
  Company,
} from '@/types'
import { calcTotalScore } from '@/lib/score'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { Slider } from '@/components/ui/slider'
import { Separator } from '@/components/ui/separator'
import { Progress } from '@/components/ui/progress'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'

// ─── 상수 ────────────────────────────────────────────────────────────────────

const TARGET_OPTIONS: { value: TargetStatus; label: string }[] = [
  { value: 'O', label: 'O 목표' },
  { value: '△', label: '△ 고려중' },
  { value: 'X', label: 'X 비목표' },
]

const ALL_STATUSES: JobChangeStatus[] = [
  'NOT_APPLIED',
  'APPLIED',
  'DOCUMENT_PASS',
  'FIRST_INTERVIEW',
  'SECOND_INTERVIEW',
  'FINAL_ACCEPTED',
  'REJECTED',
  'WITHDRAWN',
]

// ─── 폼 타입 & 초기화 ────────────────────────────────────────────────────────

interface FormScore {
  criteriaId: string
  actualInfo: string
  score: number
}

interface FormState {
  name: string
  targetStatus: TargetStatus
  jobPostingUrl: string
  recruitmentDeadline: string
  jobChangeStatus: JobChangeStatus
  scores: FormScore[]
  memo: string
}

function buildInitialState(criteriaList: Criteria[], company?: Company): FormState {
  return {
    name: company?.name ?? '',
    targetStatus: company?.targetStatus ?? 'O',
    jobPostingUrl: company?.jobPostingUrl ?? '',
    recruitmentDeadline: company?.recruitmentDeadline ?? '',
    jobChangeStatus: company?.jobChangeStatus ?? 'NOT_APPLIED',
    scores: criteriaList.map((c) => {
      const existing = company?.scores.find((s) => s.criteriaId === c.id)
      return {
        criteriaId: c.id,
        actualInfo: existing?.actualInfo ?? '',
        score: existing?.score ?? 0,
      }
    }),
    memo: company?.memo ?? '',
  }
}

// ─── 컴포넌트 ────────────────────────────────────────────────────────────────

export default function CompanyFormModal() {
  const { modal, companies, criteriaList, closeModal, addCompany, updateCompany } =
    useAppStore()

  const { open, companyId } = modal.companyForm
  const isEdit = !!companyId
  const company = companies.find((c) => c.id === companyId)

  const [form, setForm] = useState<FormState>(() =>
    buildInitialState(criteriaList, company)
  )

  // 모달이 열릴 때마다 폼 초기화
  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    if (open) setForm(buildInitialState(criteriaList, company))
  }, [open, criteriaList, company])

  // 예상 총점 실시간 계산
  const previewScore = useMemo(
    () => calcTotalScore({ scores: form.scores } as Company, criteriaList),
    [form.scores, criteriaList]
  )

  // ── 핸들러 ────────────────────────────────────────────────────────────────

  const setScoreField = (
    criteriaId: string,
    field: 'actualInfo' | 'score',
    value: string | number
  ) =>
    setForm((prev) => ({
      ...prev,
      scores: prev.scores.map((s) =>
        s.criteriaId === criteriaId ? { ...s, [field]: value } : s
      ),
    }))

  const handleScoreNumberInput = (criteriaId: string, raw: string) => {
    const clamped = Math.min(100, Math.max(0, Number(raw) || 0))
    setScoreField(criteriaId, 'score', clamped)
  }

  const handleSave = () => {
    if (!form.name.trim()) return

    const payload = {
      name: form.name.trim(),
      targetStatus: form.targetStatus,
      jobPostingUrl: form.jobPostingUrl || undefined,
      recruitmentDeadline: form.recruitmentDeadline || undefined,
      jobChangeStatus: form.jobChangeStatus,
      scores: form.scores,
      memo: form.memo,
    }

    if (isEdit && companyId) {
      updateCompany(companyId, payload)
    } else {
      addCompany(payload)
    }
    closeModal('companyForm')
  }

  // ── 렌더 ──────────────────────────────────────────────────────────────────

  return (
    <Dialog open={open} onOpenChange={() => closeModal('companyForm')}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto flex flex-col gap-5">
        <DialogHeader>
          <DialogTitle>{isEdit ? '회사 수정' : '회사 추가'}</DialogTitle>
        </DialogHeader>

        {/* ── 기본 정보 ── */}
        <section className="space-y-4">
          <h3 className="text-sm font-semibold text-gray-700">기본 정보</h3>

          <div className="grid grid-cols-2 gap-4">
            {/* 기업명 */}
            <div className="col-span-2 space-y-1.5">
              <Label htmlFor="name">
                기업명 <span className="text-red-400">*</span>
              </Label>
              <Input
                id="name"
                placeholder="회사명을 입력하세요"
                value={form.name}
                onChange={(e) => setForm((p) => ({ ...p, name: e.target.value }))}
              />
            </div>

            {/* 목표 여부 */}
            <div className="space-y-1.5">
              <Label>목표 여부</Label>
              <Select
                value={form.targetStatus}
                onValueChange={(v) =>
                  setForm((p) => ({ ...p, targetStatus: v as TargetStatus }))
                }
              >
                <SelectTrigger className="w-full">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {TARGET_OPTIONS.map((opt) => (
                    <SelectItem key={opt.value} value={opt.value}>
                      {opt.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {/* 진행 상태 */}
            <div className="space-y-1.5">
              <Label>진행 상태</Label>
              <Select
                value={form.jobChangeStatus}
                onValueChange={(v) =>
                  setForm((p) => ({ ...p, jobChangeStatus: v as JobChangeStatus }))
                }
              >
                <SelectTrigger className="w-full">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {ALL_STATUSES.map((s) => (
                    <SelectItem key={s} value={s}>
                      {JOB_CHANGE_STATUS_LABEL[s]}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {/* 채용공고 링크 */}
            <div className="col-span-2 space-y-1.5">
              <Label htmlFor="jobPostingUrl">채용공고 링크</Label>
              <Input
                id="jobPostingUrl"
                placeholder="https://..."
                value={form.jobPostingUrl}
                onChange={(e) =>
                  setForm((p) => ({ ...p, jobPostingUrl: e.target.value }))
                }
              />
            </div>

            {/* 마감일 */}
            <div className="space-y-1.5">
              <Label htmlFor="recruitmentDeadline">마감일</Label>
              <Input
                id="recruitmentDeadline"
                type="date"
                value={form.recruitmentDeadline}
                onChange={(e) =>
                  setForm((p) => ({ ...p, recruitmentDeadline: e.target.value }))
                }
              />
            </div>
          </div>
        </section>

        <Separator />

        {/* ── 평가 점수 ── */}
        <section className="space-y-4">
          {/* 헤더 + 예상 총점 */}
          <div className="flex items-center justify-between">
            <h3 className="text-sm font-semibold text-gray-700">평가 점수</h3>
            <div className="flex items-center gap-2">
              <span className="text-xs text-gray-500">예상 총점</span>
              <span className="text-lg font-bold text-gray-900">
                {previewScore}
                <span className="text-xs font-normal text-gray-400 ml-0.5">점</span>
              </span>
            </div>
          </div>

          <Progress value={previewScore} className="h-1.5" />

          {/* 항목별 입력 */}
          <div className="space-y-6">
            {criteriaList.map((criteria) => {
              const formScore = form.scores.find(
                (s) => s.criteriaId === criteria.id
              ) ?? { criteriaId: criteria.id, actualInfo: '', score: 0 }

              return (
                <div key={criteria.id} className="space-y-2">
                  <p className="text-sm font-medium text-gray-800">
                    {criteria.name}
                    <span className="text-xs font-normal text-gray-400 ml-1">
                      (가중치 {criteria.weight}%)
                    </span>
                  </p>

                  {/* 실제 정보 */}
                  <Input
                    placeholder={`실제 정보 입력 (예: 강남역 근처)`}
                    value={formScore.actualInfo}
                    onChange={(e) =>
                      setScoreField(criteria.id, 'actualInfo', e.target.value)
                    }
                  />

                  {/* 슬라이더 ↔ 숫자 입력 동기화 */}
                  <div className="flex items-center gap-3">
                    <Slider
                      className="flex-1"
                      min={0}
                      max={100}
                      step={1}
                      value={[formScore.score]}
                      onValueChange={([val]) =>
                        setScoreField(criteria.id, 'score', val)
                      }
                    />
                    <Input
                      type="number"
                      min={0}
                      max={100}
                      className="w-16 text-center tabular-nums"
                      value={formScore.score}
                      onChange={(e) =>
                        handleScoreNumberInput(criteria.id, e.target.value)
                      }
                    />
                    <span className="text-xs text-gray-400 shrink-0">/100</span>
                  </div>
                </div>
              )
            })}
          </div>
        </section>

        <Separator />

        {/* ── 메모 ── */}
        <section className="space-y-1.5">
          <Label htmlFor="memo">메모</Label>
          <Textarea
            id="memo"
            placeholder="기타 특이사항이나 메모를 입력하세요"
            rows={3}
            value={form.memo}
            onChange={(e) => setForm((p) => ({ ...p, memo: e.target.value }))}
          />
        </section>

        {/* ── 버튼 ── */}
        <div className="flex justify-end gap-2 pt-2 border-t">
          <Button variant="ghost" onClick={() => closeModal('companyForm')}>
            취소
          </Button>
          <Button onClick={handleSave} disabled={!form.name.trim()}>
            {isEdit ? '저장' : '추가'}
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  )
}
