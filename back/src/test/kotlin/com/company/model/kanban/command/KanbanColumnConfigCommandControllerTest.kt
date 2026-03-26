package com.company.model.kanban.command

import com.company.model.kanban.domain.KanbanColumnConfigRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class KanbanColumnConfigCommandControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var kanbanColumnConfigRepository: KanbanColumnConfigRepository

    @Test
    fun `PUT kanban-columns 요청시 200을 반환한다`() {
        val body = """{"columns":[{"id":"col1","label":"지원 전","statuses":["NOT_APPLIED"],"accentColor":"#ccc","order":0}]}"""

        mockMvc.perform(
            put("/kanban-columns")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
    }

    @Test
    fun `PUT kanban-columns 요청시 columns가 비어있으면 400을 반환한다`() {
        val body = """{"columns":[]}"""

        mockMvc.perform(
            put("/kanban-columns")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(status().isBadRequest)
    }
}
