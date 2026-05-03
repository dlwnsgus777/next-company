package com.company.model.kanban.query

import com.company.model.kanban.domain.KanbanColumnConfig
import com.company.model.kanban.domain.KanbanColumnConfigRepository
import com.company.model.member.TestMemberFactory
import com.company.model.member.domain.MemberRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class KanbanColumnConfigQueryControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var kanbanColumnConfigRepository: KanbanColumnConfigRepository
    @Autowired lateinit var memberRepository: MemberRepository

    @Test
    fun `GET kanban-columns returns member config`() {
        val member = memberRepository.save(TestMemberFactory.member())
        val columnsJson = """[{"id":"col1","label":"Todo","statuses":["NOT_APPLIED"],"accentColor":"#ccc","order":0}]"""
        kanbanColumnConfigRepository.save(KanbanColumnConfig(member = member, columns = columnsJson))

        mockMvc.perform(get("/kanban-columns").with(login()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.columns").isArray)
            .andExpect(jsonPath("$.data.columns[0].id").value("col1"))
    }

    @Test
    fun `GET kanban-columns returns 200 when config is missing`() {
        mockMvc.perform(get("/kanban-columns").with(login()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
    }

    private fun login() = oauth2Login().attributes {
        it["sub"] = "google-123"
        it["email"] = "google-123@example.com"
        it["name"] = "Test User"
    }
}
