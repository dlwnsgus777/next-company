# 프론트엔드 전체 작업 현황

> 최종 업데이트: 2026-03-01
> front_2026_02_28.md + front_2026_03_01.md 통합 관리 파일

---

## 완료된 작업

### 기반 (Phase 1)
- [x] Next.js 16 + TypeScript + Tailwind CSS 세팅
- [x] shadcn/ui 초기화 및 컴포넌트 설치
- [x] zustand 설치 및 스토어 구성 (localStorage persist)
- [x] 타입 정의 (`src/types/index.ts`)
- [x] 점수 계산 유틸 (`src/lib/score.ts`)
- [x] Mock 데이터 작성 (`src/lib/mock.ts`)

### 레이아웃 & 공통 컴포넌트 (Phase 2)
- [x] 전체 레이아웃 (`app/layout.tsx`)
- [x] 헤더 — 로고, 칸반/순위 토글, 칸반설정, 평가기준, 회사추가 버튼
- [x] 필터 바 — 탭 필터(전체/목표O/고려중△), 정렬 셀렉트
- [x] DeadlineBadge 컴포넌트 (D-day, 임박 색상)

### 칸반 보드 (Phase 3)
- [x] KanbanBoard 컴포넌트 (store 기반 동적 컬럼)
- [x] KanbanColumn 컴포넌트 (헤더, 빈 상태)
- [x] CompanyCard 컴포넌트 (순위, 회사명, 총점, 목표여부, D-day, 클릭)

### 순위 보기 (Phase 4)
- [x] RankingList 컴포넌트 (순위, 점수바, 목표여부, 진행상태, D-day)
- [x] 목표/비목표 구분선
- [x] 칸반 ↔ 순위 뷰 토글

### 모달 (Phase 5·6·7)
- [x] CompanyDetailModal — 기본정보, ProgressStepper, 다음단계 버튼, 평가테이블, 총점바, 메모, 수정/삭제
- [x] CompanyFormModal — 추가/수정 공용, 슬라이더↔숫자 동기화, 예상총점 실시간 계산
- [x] CriteriaSettingModal — 추가/삭제/드래그, 합계 100% 검증

### 칸반 컬럼 커스터마이징 (2026-03-01)
- [x] `KanbanColumnConfig` 타입 + `DEFAULT_KANBAN_COLUMNS` + `ACCENT_COLOR_PRESETS`
- [x] store에 `kanbanColumns` 상태·액션 추가, persist 적용
- [x] KanbanBoard → store 기반으로 리팩토링 (하드코딩 제거)
- [x] KanbanColumnSettingModal — 이름/색상/순서/상태배치, 저장 검증
- [x] Header에 `[칸반 설정]` 버튼 (칸반 뷰일 때만 노출)

---

## 남은 작업

### Phase 8 — 통합 마무리
- [x] 칸반 카드 드래그 앤 드롭 (컬럼 간 이동 → 진행 상태 자동 변경)

---

## 파일 구조

```
front/src/
├── app/
│   ├── layout.tsx
│   └── page.tsx
├── components/
│   ├── common/
│   │   └── DeadlineBadge.tsx
│   ├── kanban/
│   │   ├── KanbanBoard.tsx
│   │   ├── KanbanColumn.tsx
│   │   └── CompanyCard.tsx
│   ├── layout/
│   │   ├── Header.tsx
│   │   ├── FilterBar.tsx
│   │   └── MainView.tsx
│   ├── modal/
│   │   ├── CompanyDetailModal.tsx
│   │   ├── CompanyFormModal.tsx
│   │   ├── CriteriaSettingModal.tsx
│   │   └── KanbanColumnSettingModal.tsx
│   ├── ranking/
│   │   └── RankingList.tsx
│   └── ui/  (shadcn)
├── hooks/
│   └── useFilteredCompanies.ts
├── lib/
│   ├── mock.ts
│   ├── score.ts
│   └── utils.ts
├── store/
│   └── useAppStore.ts
└── types/
    └── index.ts
```
