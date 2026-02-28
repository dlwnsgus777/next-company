import type { Metadata } from 'next'
import { Geist } from 'next/font/google'
import './globals.css'
import Header from '@/components/layout/Header'

const geistSans = Geist({
  variable: '--font-geist-sans',
  subsets: ['latin'],
})

export const metadata: Metadata = {
  title: '이직 도우미',
  description: '나만의 기준으로 회사를 평가하고 지원 과정을 트래킹하세요',
}

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <html lang="ko">
      <body className={`${geistSans.variable} antialiased bg-gray-50 min-h-screen`}>
        <Header />
        {children}
      </body>
    </html>
  )
}
