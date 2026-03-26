package com.company.model.kanban.query

import com.company.model.kanban.domain.KanbanColumnConfig
import com.company.model.kanban.domain.KanbanColumnConfigRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class KanbanColumnConfigQueryControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var kanbanColumnConfigRepository: KanbanColumnConfigRepository

    @Test
    fun `GET kanban-columns 요청시 200과 설정을 반환한다`() {
        val columnsJson = """[{"id":"col1","label":"지원 전","statuses":["NOT_APPLIED"],"accentColor":"#ccc","order":0}]"""
        kanbanColumnConfigRepository.save(KanbanColumnConfig(columns = columnsJson))

        mockMvc.perform(get("/kanban-columns"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.columns").isArray)
            .andExpect(jsonPath("$.data.columns[0].id").value("col1"))
    }

    @Test
    fun `칸반 설정이 없어도 GET 요청시 200을 반환한다`() {
        mockMvc.perform(get("/kanban-columns"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
    }
}
