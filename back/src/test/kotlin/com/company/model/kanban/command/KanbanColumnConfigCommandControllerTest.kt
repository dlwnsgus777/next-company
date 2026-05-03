package com.company.model.kanban.command

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class KanbanColumnConfigCommandControllerTest {

    @Autowired lateinit var mockMvc: MockMvc

    @Test
    fun `PUT kanban-columns returns 200`() {
        val body = """{"columns":[{"id":"col1","label":"Todo","statuses":["NOT_APPLIED"],"accentColor":"#ccc","order":0}]}"""

        mockMvc.perform(
            put("/kanban-columns")
                .with(login())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
    }

    @Test
    fun `PUT kanban-columns returns 400 when columns is empty`() {
        val body = """{"columns":[]}"""

        mockMvc.perform(
            put("/kanban-columns")
                .with(login())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(status().isBadRequest)
    }

    private fun login() = oauth2Login().attributes {
        it["sub"] = "google-123"
        it["email"] = "google-123@example.com"
        it["name"] = "Test User"
    }
}
