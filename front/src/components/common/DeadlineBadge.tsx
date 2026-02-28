'use client'

import { calcDday } from '@/lib/score'
import { cn } from '@/lib/utils'

interface DeadlineBadgeProps {
  deadline?: string
  className?: string
}

export default function DeadlineBadge({ deadline, className }: DeadlineBadgeProps) {
  const dday = calcDday(deadline)
  if (!dday) return null

  return (
    <span
      className={cn(
        'text-xs font-semibold px-1.5 py-0.5 rounded',
        dday.critical
          ? 'bg-red-100 text-red-600'
          : dday.urgent
            ? 'bg-orange-100 text-orange-600'
            : 'bg-gray-100 text-gray-500',
        className
      )}
    >
      {dday.label}
    </span>
  )
}
