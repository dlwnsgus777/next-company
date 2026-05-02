'use client'

import { Chrome } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { authApi } from '@/lib/api'

export default function LoginPanel() {
  return (
    <main className="min-h-screen bg-slate-50 flex items-center justify-center px-4">
      <section className="w-full max-w-sm border border-slate-200 bg-white p-6 shadow-sm rounded-lg">
        <div className="space-y-2">
          <h1 className="text-xl font-semibold text-slate-950">이직 도우미</h1>
          <p className="text-sm text-slate-600">Google 계정으로 로그인해 회사 목록을 관리하세요.</p>
        </div>
        <Button
          type="button"
          className="mt-6 w-full gap-2"
          onClick={() => {
            window.location.href = authApi.getGoogleLoginUrl()
          }}
        >
          <Chrome size={16} />
          Google로 계속하기
        </Button>
      </section>
    </main>
  )
}
