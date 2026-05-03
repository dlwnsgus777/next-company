package com.company.model.kanban.query

import com.company.model.kanban.domain.KanbanColumnConfig
import com.company.model.kanban.domain.KanbanColumnConfigRepository
import com.company.model.member.TestMemberFactory
import com.company.model.member.domain.MemberRepository
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
    @Autowired lateinit var memberRepository: MemberRepository

    @Test
    fun `get returns config for the member`() {
        val member = memberRepository.save(TestMemberFactory.member(providerId = "google-1"))
        val otherMember = memberRepository.save(TestMemberFactory.member(providerId = "google-2"))
        val columnsJson = """[{"id":"col1","label":"Todo","statuses":["NOT_APPLIED"],"accentColor":"#ccc","order":0}]"""
        kanbanColumnConfigRepository.save(KanbanColumnConfig(member = member, columns = columnsJson))
        kanbanColumnConfigRepository.save(
            KanbanColumnConfig(
                member = otherMember,
                columns = """[{"id":"other","label":"Other","statuses":["APPLIED"],"accentColor":"#111","order":0}]"""
            )
        )

        val result = kanbanColumnConfigQueryService.get(member)

        assertThat(result).isNotNull
        assertThat(result!!.columns).hasSize(1)
        assertThat(result.columns[0].id).isEqualTo("col1")
    }

    @Test
    fun `get returns null when member has no config`() {
        val member = memberRepository.save(TestMemberFactory.member())

        val result = kanbanColumnConfigQueryService.get(member)

        assertThat(result).isNull()
    }
}
