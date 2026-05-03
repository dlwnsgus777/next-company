package com.company.model.kanban.domain

import com.company.model.member.TestMemberFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KanbanColumnConfigTest {

    private val member = TestMemberFactory.member()

    @Test
    fun `columns stores json string`() {
        val json = """[{"id":"col1","label":"Todo"}]"""
        val config = KanbanColumnConfig(member = member, columns = json)
        assertThat(config.columns).isEqualTo(json)
    }

    @Test
    fun `columns can be changed`() {
        val config = KanbanColumnConfig(member = member, columns = "[]")
        val newJson = """[{"id":"col2","label":"Done"}]"""
        config.columns = newJson
        assertThat(config.columns).isEqualTo(newJson)
    }
}
