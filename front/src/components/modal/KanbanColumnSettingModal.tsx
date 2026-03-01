'use client'

import { useState, useEffect, useRef } from 'react'
import { useAppStore } from '@/store/useAppStore'
import {
  KanbanColumnConfig,
  ApplicationStatus,
  APPLICATION_STATUS_LABEL,
  ACCENT_COLOR_PRESETS,
} from '@/types'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Separator } from '@/components/ui/separator'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { cn } from '@/lib/utils'
import { GripVertical, Plus, Trash2 } from 'lucide-react'

const ALL_STATUSES: ApplicationStatus[] = [
  'NOT_APPLIED',
  'APPLIED',
  'DOCUMENT_PASS',
  'FIRST_INTERVIEW',
  'SECOND_INTERVIEW',
  'FINAL_ACCEPTED',
  'REJECTED',
  'WITHDRAWN',
]

type DraftColumn = KanbanColumnConfig

function buildDraft(columns: KanbanColumnConfig[]): DraftColumn[] {
  return columns.map((c) => ({ ...c, statuses: [...c.statuses] }))
}

// status → columnId 역매핑
function buildStatusMap(columns: DraftColumn[]): Record<ApplicationStatus, string> {
  const map = {} as Record<ApplicationStatus, string>
  for (const col of columns) {
    for (const s of col.statuses) {
      map[s] = col.id
    }
  }
  return map
}

export default function KanbanColumnSettingModal() {
  const { modal, kanbanColumns, closeModal, setKanbanColumns } = useAppStore()
  const { open } = modal.kanbanSetting

  const [columns, setColumns] = useState<DraftColumn[]>([])
  const dragIndexRef = useRef<number | null>(null)
  const [draggingId, setDraggingId] = useState<string | null>(null)

  // 모달 열릴 때 초기화
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(() => {
    if (open) setColumns(buildDraft(kanbanColumns))
  }, [open])

  // status → columnId 역매핑
  const statusMap = buildStatusMap(columns)

  // 미배치 상태 목록
  const unassigned = ALL_STATUSES.filter((s) => !statusMap[s])

  const isValid =
    unassigned.length === 0 &&
    columns.length >= 1 &&
    columns.every((col) => col.label.trim() !== '')

  // ── 컬럼 편집 ──────────────────────────────────────────────────────────────

  const handleLabelChange = (id: string, label: string) =>
    setColumns((prev) => prev.map((col) => (col.id === id ? { ...col, label } : col)))

  const handleColorChange = (id: string, accentColor: string) =>
    setColumns((prev) =>
      prev.map((col) => (col.id === id ? { ...col, accentColor } : col))
    )

  const handleAddColumn = () =>
    setColumns((prev) => [
      ...prev,
      {
        id: crypto.randomUUID(),
        label: '',
        statuses: [],
        accentColor: 'bg-gray-400',
        order: prev.length,
      },
    ])

  const handleDeleteColumn = (id: string) => {
    // 삭제된 컬럼의 statuses는 미배치 상태로 전환됨 (자동)
    setColumns((prev) =>
      prev
        .filter((col) => col.id !== id)
        .map((col, i) => ({ ...col, order: i }))
    )
  }

  // ── 상태 배치 (Select) ────────────────────────────────────────────────────

  const handleStatusAssign = (status: ApplicationStatus, targetColId: string) => {
    setColumns((prev) =>
      prev.map((col) => {
        // 기존 컬럼에서 제거
        const without = col.statuses.filter((s) => s !== status)
        // 대상 컬럼에 추가
        if (col.id === targetColId) {
          return { ...col, statuses: [...without, status] }
        }
        return { ...col, statuses: without }
      })
    )
  }

  // ── 드래그 앤 드롭 ────────────────────────────────────────────────────────

  const handleDragStart = (index: number, id: string) => {
    dragIndexRef.current = index
    setDraggingId(id)
  }

  const handleDragOver = (e: React.DragEvent, targetIndex: number) => {
    e.preventDefault()
    const srcIndex = dragIndexRef.current
    if (srcIndex === null || srcIndex === targetIndex) return

    setColumns((prev) => {
      const next = [...prev]
      const [moved] = next.splice(srcIndex, 1)
      next.splice(targetIndex, 0, moved)
      return next.map((col, i) => ({ ...col, order: i }))
    })
    dragIndexRef.current = targetIndex
  }

  const handleDragEnd = () => {
    dragIndexRef.current = null
    setDraggingId(null)
  }

  // ── 저장 ──────────────────────────────────────────────────────────────────

  const handleSave = () => {
    setKanbanColumns(columns.map((col, i) => ({ ...col, order: i })))
    closeModal('kanbanSetting')
  }

  // ── 렌더 ──────────────────────────────────────────────────────────────────

  return (
    <Dialog open={open} onOpenChange={() => closeModal('kanbanSetting')}>
      <DialogContent className="max-w-lg max-h-[90vh] overflow-y-auto flex flex-col gap-5">
        <DialogHeader>
          <DialogTitle>칸반 컬럼 설정</DialogTitle>
        </DialogHeader>

        {/* ── 컬럼 목록 ── */}
        <section className="space-y-3">
          <h3 className="text-sm font-semibold text-gray-700">컬럼 관리</h3>

          {/* 헤더 라벨 */}
          <div className="grid grid-cols-[20px_1fr_auto_32px] gap-2 px-1 text-xs font-medium text-gray-400">
            <span />
            <span>컬럼 이름</span>
            <span>색상</span>
            <span />
          </div>

          <div className="space-y-2">
            {columns.map((col, index) => (
              <div
                key={col.id}
                draggable
                onDragStart={() => handleDragStart(index, col.id)}
                onDragOver={(e) => handleDragOver(e, index)}
                onDragEnd={handleDragEnd}
                className={cn(
                  'grid grid-cols-[20px_1fr_auto_32px] items-center gap-2',
                  'rounded-lg border bg-white px-2 py-2 transition-opacity select-none',
                  draggingId === col.id ? 'opacity-40 border-dashed' : 'border-gray-200'
                )}
              >
                {/* 드래그 핸들 */}
                <GripVertical className="w-4 h-4 text-gray-300 cursor-grab active:cursor-grabbing shrink-0" />

                {/* 컬럼 이름 */}
                <Input
                  placeholder="컬럼 이름"
                  value={col.label}
                  onChange={(e) => handleLabelChange(col.id, e.target.value)}
                  className={cn(
                    'h-8 text-sm',
                    col.label.trim() === '' && 'border-red-300'
                  )}
                />

                {/* 색상 선택 */}
                <div className="flex items-center gap-1 px-1">
                  {ACCENT_COLOR_PRESETS.map((preset) => (
                    <button
                      key={preset.value}
                      title={preset.label}
                      onClick={() => handleColorChange(col.id, preset.value)}
                      className={cn(
                        'w-4 h-4 rounded-full transition-transform',
                        preset.value,
                        col.accentColor === preset.value
                          ? 'ring-2 ring-offset-1 ring-gray-500 scale-125'
                          : 'hover:scale-110'
                      )}
                    />
                  ))}
                </div>

                {/* 삭제 */}
                <button
                  onClick={() => handleDeleteColumn(col.id)}
                  disabled={columns.length <= 1}
                  className={cn(
                    'flex items-center justify-center w-8 h-8 rounded-md transition-colors',
                    columns.length <= 1
                      ? 'text-gray-200 cursor-not-allowed'
                      : 'text-gray-400 hover:text-red-500 hover:bg-red-50'
                  )}
                >
                  <Trash2 className="w-3.5 h-3.5" />
                </button>
              </div>
            ))}
          </div>

          {/* 컬럼 추가 */}
          <button
            onClick={handleAddColumn}
            className="flex items-center gap-1.5 text-sm text-blue-600 hover:text-blue-700 px-1 w-fit"
          >
            <Plus className="w-3.5 h-3.5" />
            컬럼 추가
          </button>
        </section>

        <Separator />

        {/* ── 상태 배치 ── */}
        <section className="space-y-3">
          <div className="flex items-center justify-between">
            <h3 className="text-sm font-semibold text-gray-700">진행 상태 배치</h3>
            {unassigned.length > 0 ? (
              <span className="text-xs text-red-500 font-medium">
                미배치 {unassigned.length}개
              </span>
            ) : (
              <span className="text-xs text-emerald-600 font-medium">모두 배치됨 ✓</span>
            )}
          </div>

          <div className="space-y-2">
            {ALL_STATUSES.map((status) => {
              const assignedColId = statusMap[status] ?? ''
              return (
                <div key={status} className="flex items-center gap-3">
                  <span
                    className={cn(
                      'flex-1 text-sm',
                      assignedColId ? 'text-gray-700' : 'text-red-500 font-medium'
                    )}
                  >
                    {APPLICATION_STATUS_LABEL[status]}
                    {!assignedColId && (
                      <span className="ml-1.5 text-xs text-red-400">(미배치)</span>
                    )}
                  </span>
                  <Select
                    value={assignedColId}
                    onValueChange={(colId) => handleStatusAssign(status, colId)}
                  >
                    <SelectTrigger className="w-36 h-8 text-xs">
                      <SelectValue placeholder="컬럼 선택" />
                    </SelectTrigger>
                    <SelectContent>
                      {columns.map((col) => (
                        <SelectItem key={col.id} value={col.id}>
                          <div className="flex items-center gap-2">
                            <span
                              className={cn('w-2 h-2 rounded-full shrink-0', col.accentColor)}
                            />
                            {col.label || '(이름 없음)'}
                          </div>
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              )
            })}
          </div>
        </section>

        {/* ── 버튼 ── */}
        <div className="flex justify-end gap-2 pt-2 border-t">
          <Button variant="ghost" onClick={() => closeModal('kanbanSetting')}>
            취소
          </Button>
          <Button onClick={handleSave} disabled={!isValid}>
            저장
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  )
}
