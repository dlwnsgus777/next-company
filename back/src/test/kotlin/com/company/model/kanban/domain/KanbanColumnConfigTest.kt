package com.company.model.kanban.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KanbanColumnConfigTest {

    @Test
    fun `columns 필드에 JSON 문자열을 저장할 수 있다`() {
        val json = """[{"id":"col1","label":"지원 예정"}]"""
        val config = KanbanColumnConfig(columns = json)
        assertThat(config.columns).isEqualTo(json)
    }

    @Test
    fun `columns 필드를 변경할 수 있다`() {
        val config = KanbanColumnConfig(columns = "[]")
        val newJson = """[{"id":"col2","label":"서류 전형"}]"""
        config.columns = newJson
        assertThat(config.columns).isEqualTo(newJson)
    }
}
