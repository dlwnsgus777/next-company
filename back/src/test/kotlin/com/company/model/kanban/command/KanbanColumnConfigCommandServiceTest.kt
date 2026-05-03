package com.company.model.kanban.command

import com.company.model.company.domain.JobChangeStatus
import com.company.model.kanban.command.dto.KanbanColumnDto
import com.company.model.kanban.command.dto.SaveKanbanColumnConfigRequest
import com.company.model.kanban.domain.KanbanColumnConfig
import com.company.model.kanban.domain.KanbanColumnConfigRepository
import com.company.model.member.TestMemberFactory
import com.company.model.member.domain.MemberRepository
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
    @Autowired lateinit var memberRepository: MemberRepository
    @Autowired lateinit var objectMapper: ObjectMapper

    @Test
    fun `save creates config for the member`() {
        val member = memberRepository.save(TestMemberFactory.member())
        val request = SaveKanbanColumnConfigRequest(
            columns = listOf(KanbanColumnDto("col1", "Todo", listOf(JobChangeStatus.NOT_APPLIED), "#ccc", 0))
        )

        kanbanColumnConfigCommandService.save(member, request)

        val saved = kanbanColumnConfigRepository.findByMember(member)
        assertThat(saved).isNotNull
        val columns: List<KanbanColumnDto> = objectMapper.readValue(saved!!.columns, object : TypeReference<List<KanbanColumnDto>>() {})
        assertThat(columns).hasSize(1)
        assertThat(columns[0].id).isEqualTo("col1")
    }

    @Test
    fun `save updates only the member config`() {
        val member = memberRepository.save(TestMemberFactory.member(providerId = "google-1"))
        val otherMember = memberRepository.save(TestMemberFactory.member(providerId = "google-2"))
        kanbanColumnConfigRepository.save(
            KanbanColumnConfig(member = member, columns = """[{"id":"old","label":"Old","statuses":["NOT_APPLIED"],"accentColor":"#000","order":0}]""")
        )
        kanbanColumnConfigRepository.save(
            KanbanColumnConfig(member = otherMember, columns = """[{"id":"other","label":"Other","statuses":["NOT_APPLIED"],"accentColor":"#111","order":0}]""")
        )
        val request = SaveKanbanColumnConfigRequest(
            columns = listOf(KanbanColumnDto("new", "New", listOf(JobChangeStatus.APPLIED), "#fff", 0))
        )

        kanbanColumnConfigCommandService.save(member, request)

        assertThat(kanbanColumnConfigRepository.findAll()).hasSize(2)
        val updated = kanbanColumnConfigRepository.findByMember(member)!!
        val untouched = kanbanColumnConfigRepository.findByMember(otherMember)!!
        assertThat(updated.columns).contains("\"id\":\"new\"")
        assertThat(untouched.columns).contains("\"id\":\"other\"")
    }
}
