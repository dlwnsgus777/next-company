package com.company.model.kanban.query

import com.company.model.kanban.domain.KanbanColumnConfig
import com.company.model.kanban.domain.KanbanColumnConfigRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class KanbanColumnConfigQueryServiceTest {

    @Autowired lateinit var kanbanColumnConfigQueryService: KanbanColumnConfigQueryService
    @Autowired lateinit var kanbanColumnConfigRepository: KanbanColumnConfigRepository
    @Autowired lateinit var objectMapper: ObjectMapper

    @Test
    fun `칸반 설정이 있으면 KanbanColumnConfigResponse를 반환한다`() {
        val columnsJson = """[{"id":"col1","label":"지원 전","statuses":["NOT_APPLIED"],"accentColor":"#ccc","order":0}]"""
        kanbanColumnConfigRepository.save(KanbanColumnConfig(columns = columnsJson))

        val result = kanbanColumnConfigQueryService.get()

        assertThat(result).isNotNull
        assertThat(result!!.columns).hasSize(1)
        assertThat(result.columns[0].id).isEqualTo("col1")
    }

    @Test
    fun `칸반 설정이 없으면 null을 반환한다`() {
        val result = kanbanColumnConfigQueryService.get()
        assertThat(result).isNull()
    }
}
