package com.company.model.company.domain

import com.company.model.member.TestMemberFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CompanyTest {

    private val member = TestMemberFactory.member()

    @Test
    fun `default jobChangeStatus is NOT_APPLIED`() {
        val company = Company(member = member, name = "Test")
        assertThat(company.jobChangeStatus).isEqualTo(JobChangeStatus.NOT_APPLIED)
    }

    @Test
    fun `name can be changed`() {
        val company = Company(member = member, name = "Old")
        company.name = "New"
        assertThat(company.name).isEqualTo("New")
    }

    @Test
    fun `memo accepts null`() {
        val company = Company(member = member, name = "Company", memo = null)
        assertThat(company.memo).isNull()
    }
}
