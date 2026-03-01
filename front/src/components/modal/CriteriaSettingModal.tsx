'use client'

import { useState, useEffect, useMemo, useRef } from 'react'
import { useAppStore } from '@/store/useAppStore'
import { Criteria } from '@/types'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { cn } from '@/lib/utils'
import { GripVertical, Plus, Trash2 } from 'lucide-react'

type DraftCriteria = Criteria

function buildDraft(criteriaList: Criteria[]): DraftCriteria[] {
  return criteriaList.map((c) => ({ ...c }))
}

export default function CriteriaSettingModal() {
  const { modal, criteriaList, closeModal, setCriteriaList } = useAppStore()
  const { open } = modal.criteria

  const [items, setItems] = useState<DraftCriteria[]>([])
  const dragIndexRef = useRef<number | null>(null)
  const [draggingId, setDraggingId] = useState<string | null>(null)

  // 모달이 열릴 때마다 초기화
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(() => {
    if (open) setItems(buildDraft(criteriaList))
  }, [open])

  const totalWeight = useMemo(
    () => items.reduce((acc, item) => acc + (Number(item.weight) || 0), 0),
    [items]
  )

  const isValid = totalWeight === 100 && items.every((item) => item.name.trim() !== '')

  // ── 항목 편집 핸들러 ─────────────────────────────────────────────────────

  const handleNameChange = (id: string, name: string) =>
    setItems((prev) => prev.map((item) => (item.id === id ? { ...item, name } : item)))

  const handleWeightChange = (id: string, raw: string) => {
    const weight = Math.min(100, Math.max(0, Number(raw) || 0))
    setItems((prev) =>
      prev.map((item) => (item.id === id ? { ...item, weight } : item))
    )
  }

  const handleAdd = () =>
    setItems((prev) => [
      ...prev,
      { id: crypto.randomUUID(), name: '', weight: 0, order: prev.length },
    ])

  const handleDelete = (id: string) =>
    setItems((prev) =>
      prev.filter((item) => item.id !== id).map((item, i) => ({ ...item, order: i }))
    )

  // ── 드래그 앤 드롭 (HTML5 native) ─────────────────────────────────────────

  const handleDragStart = (index: number, id: string) => {
    dragIndexRef.current = index
    setDraggingId(id)
  }

  const handleDragOver = (e: React.DragEvent, targetIndex: number) => {
    e.preventDefault()
    const srcIndex = dragIndexRef.current
    if (srcIndex === null || srcIndex === targetIndex) return

    setItems((prev) => {
      const next = [...prev]
      const [moved] = next.splice(srcIndex, 1)
      next.splice(targetIndex, 0, moved)
      return next.map((item, i) => ({ ...item, order: i }))
    })
    dragIndexRef.current = targetIndex
  }

  const handleDragEnd = () => {
    dragIndexRef.current = null
    setDraggingId(null)
  }

  // ── 저장 ──────────────────────────────────────────────────────────────────

  const handleSave = () => {
    setCriteriaList(items.map((item, i) => ({ ...item, order: i })))
    closeModal('criteria')
  }

  // ── 렌더 ──────────────────────────────────────────────────────────────────

  return (
    <Dialog open={open} onOpenChange={() => closeModal('criteria')}>
      <DialogContent className="max-w-md flex flex-col gap-4">
        <DialogHeader>
          <DialogTitle>평가기준 설정</DialogTitle>
        </DialogHeader>

        {/* 컬럼 라벨 */}
        <div className="grid grid-cols-[20px_1fr_76px_32px] gap-2 px-1 text-xs font-medium text-gray-400">
          <span />
          <span>항목명</span>
          <span className="text-center">가중치 (%)</span>
          <span />
        </div>

        {/* 항목 목록 */}
        <div className="space-y-2">
          {items.map((item, index) => (
            <div
              key={item.id}
              draggable
              onDragStart={() => handleDragStart(index, item.id)}
              onDragOver={(e) => handleDragOver(e, index)}
              onDragEnd={handleDragEnd}
              className={cn(
                'grid grid-cols-[20px_1fr_76px_32px] items-center gap-2',
                'rounded-lg border bg-white px-2 py-2 transition-opacity select-none',
                draggingId === item.id ? 'opacity-40 border-dashed' : 'border-gray-200'
              )}
            >
              {/* 드래그 핸들 */}
              <GripVertical className="w-4 h-4 text-gray-300 cursor-grab active:cursor-grabbing shrink-0" />

              {/* 항목명 */}
              <Input
                placeholder="항목명"
                value={item.name}
                onChange={(e) => handleNameChange(item.id, e.target.value)}
                className={cn(
                  'h-8 text-sm',
                  item.name.trim() === '' && 'border-red-300 focus-visible:border-red-400'
                )}
              />

              {/* 가중치 */}
              <Input
                type="number"
                min={0}
                max={100}
                placeholder="0"
                value={item.weight}
                onChange={(e) => handleWeightChange(item.id, e.target.value)}
                className="h-8 text-sm text-center"
              />

              {/* 삭제 버튼 */}
              <button
                onClick={() => handleDelete(item.id)}
                disabled={items.length <= 1}
                className={cn(
                  'flex items-center justify-center w-8 h-8 rounded-md transition-colors',
                  items.length <= 1
                    ? 'text-gray-200 cursor-not-allowed'
                    : 'text-gray-400 hover:text-red-500 hover:bg-red-50'
                )}
              >
                <Trash2 className="w-3.5 h-3.5" />
              </button>
            </div>
          ))}
        </div>

        {/* 항목 추가 버튼 */}
        <button
          onClick={handleAdd}
          className="flex items-center gap-1.5 text-sm text-blue-600 hover:text-blue-700 px-1 w-fit"
        >
          <Plus className="w-3.5 h-3.5" />
          항목 추가
        </button>

        {/* 합계 표시 */}
        <div className="flex items-center justify-between rounded-lg bg-gray-50 px-4 py-2.5">
          <span className="text-sm text-gray-600">합계</span>
          <div className="flex items-center gap-2">
            {totalWeight !== 100 && (
              <span className="text-xs text-red-400">
                100%에서 {totalWeight > 100 ? `+${totalWeight - 100}` : `${totalWeight - 100}`} 차이
              </span>
            )}
            <span
              className={cn(
                'text-lg font-bold tabular-nums',
                totalWeight === 100 ? 'text-emerald-600' : 'text-red-500'
              )}
            >
              {totalWeight}%
            </span>
          </div>
        </div>

        {/* 버튼 */}
        <div className="flex justify-end gap-2 pt-1 border-t">
          <Button variant="ghost" onClick={() => closeModal('criteria')}>
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
