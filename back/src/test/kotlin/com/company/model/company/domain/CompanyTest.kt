package com.company.model.company.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CompanyTest {

    @Test
    fun `기본 applicationStatus는 NOT_APPLIED이다`() {
        val company = Company(name = "테스트 회사")
        assertThat(company.jobChangeStatus).isEqualTo(JobChangeStatus.NOT_APPLIED)
    }

    @Test
    fun `name 필드를 변경할 수 있다`() {
        val company = Company(name = "원래 이름")
        company.name = "새 이름"
        assertThat(company.name).isEqualTo("새 이름")
    }

    @Test
    fun `memo는 null을 허용한다`() {
        val company = Company(name = "회사", memo = null)
        assertThat(company.memo).isNull()
    }

    @Test
    fun `memo 필드를 변경할 수 있다`() {
        val company = Company(name = "회사", memo = "기존 메모")
        company.memo = "새 메모"
        assertThat(company.memo).isEqualTo("새 메모")
    }
}
