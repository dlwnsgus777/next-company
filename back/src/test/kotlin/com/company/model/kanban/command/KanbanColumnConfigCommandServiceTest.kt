package com.company.model.kanban.command

import com.company.model.company.domain.JobChangeStatus
import com.company.model.kanban.command.dto.KanbanColumnDto
import com.company.model.kanban.command.dto.SaveKanbanColumnConfigRequest
import com.company.model.kanban.domain.KanbanColumnConfig
import com.company.model.kanban.domain.KanbanColumnConfigRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class KanbanColumnConfigCommandServiceTest {

    @Autowired lateinit var kanbanColumnConfigCommandService: KanbanColumnConfigCommandService
    @Autowired lateinit var kanbanColumnConfigRepository: KanbanColumnConfigRepository
    @Autowired lateinit var objectMapper: ObjectMapper

    @Test
    fun `칸반 설정이 없을 때 save하면 새로 생성된다`() {
        val request = SaveKanbanColumnConfigRequest(
            columns = listOf(KanbanColumnDto("col1", "지원 전", listOf(JobChangeStatus.NOT_APPLIED), "#ccc", 0))
        )

        kanbanColumnConfigCommandService.save(request)

        val saved = kanbanColumnConfigRepository.findTopByOrderByIdDesc()
        assertThat(saved).isNotNull
        val columns: List<KanbanColumnDto> = objectMapper.readValue(saved!!.columns, object : TypeReference<List<KanbanColumnDto>>() {})
        assertThat(columns).hasSize(1)
        assertThat(columns[0].id).isEqualTo("col1")
    }

    @Test
    fun `칸반 설정이 있을 때 save하면 기존 설정이 업데이트된다`() {
        kanbanColumnConfigRepository.save(KanbanColumnConfig(columns = """[{"id":"old","label":"구","statuses":["NOT_APPLIED"],"accentColor":"#000","order":0}]"""))
        val request = SaveKanbanColumnConfigRequest(
            columns = listOf(KanbanColumnDto("new", "신규", listOf(JobChangeStatus.APPLIED), "#fff", 0))
        )

        kanbanColumnConfigCommandService.save(request)

        val configs = kanbanColumnConfigRepository.findAll()
        assertThat(configs).hasSize(1)
        val columns: List<KanbanColumnDto> = objectMapper.readValue(configs[0].columns, object : TypeReference<List<KanbanColumnDto>>() {})
        assertThat(columns[0].id).isEqualTo("new")
    }
}
