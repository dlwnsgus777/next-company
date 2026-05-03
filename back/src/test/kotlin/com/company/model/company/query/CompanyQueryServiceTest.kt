package com.company.model.company.query

import com.company.model.company.domain.Company
import com.company.model.company.domain.CompanyRepository
import com.company.model.company.domain.JobChangeStatus
import com.company.model.member.TestMemberFactory
import com.company.model.member.domain.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class CompanyQueryServiceTest {

    @Autowired
    lateinit var companyQueryService: CompanyQueryService

    @Autowired
    lateinit var companyRepository: CompanyRepository

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    fun `getAll returns only companies owned by the member`() {
        val member = memberRepository.save(TestMemberFactory.member(providerId = "google-1"))
        val otherMember = memberRepository.save(TestMemberFactory.member(providerId = "google-2"))
        companyRepository.save(
            Company(
                member = member,
                name = "mine",
                targetStatus = "O",
                jobPostingUrl = "https://example.com/jobs/1",
                recruitmentDeadline = java.time.LocalDate.of(2026, 5, 31),
                jobChangeStatus = JobChangeStatus.NOT_APPLIED,
                scores = """[{"criteriaId":"c1","actualInfo":"good","score":80}]"""
            )
        )
        companyRepository.save(Company(member = otherMember, name = "other"))

        val result = companyQueryService.getAll(member, PageRequest.of(0, 10))

        assertThat(result.totalElements).isEqualTo(1)
        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].name).isEqualTo("mine")
        assertThat(result.content[0].targetStatus).isEqualTo("O")
        assertThat(result.content[0].jobPostingUrl).isEqualTo("https://example.com/jobs/1")
        assertThat(result.content[0].recruitmentDeadline).isEqualTo(java.time.LocalDate.of(2026, 5, 31))
        assertThat(result.content[0].scores).hasSize(1)
        assertThat(result.content[0].scores[0].criteriaId).isEqualTo("c1")
        assertThat(result.content[0].scores[0].score).isEqualTo(80)
    }

    @Test
    fun `getAll returns empty page when member has no companies`() {
        val member = memberRepository.save(TestMemberFactory.member())

        val result = companyQueryService.getAll(member, PageRequest.of(0, 10))

        assertThat(result.content).isEmpty()
        assertThat(result.totalElements).isEqualTo(0)
    }
}
