package com.company.model.company.command

import com.company.model.company.command.dto.CompanyScoreRequest
import com.company.model.company.command.dto.CreateCompanyRequest
import com.company.model.company.command.dto.UpdateCompanyRequest
import com.company.model.company.domain.Company
import com.company.model.company.domain.CompanyRepository
import com.company.model.company.domain.JobChangeStatus
import com.company.model.member.TestMemberFactory
import com.company.model.member.domain.MemberRepository
import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class CompanyCommandServiceTest {

    @Autowired lateinit var companyCommandService: CompanyCommandService
    @Autowired lateinit var companyRepository: CompanyRepository
    @Autowired lateinit var memberRepository: MemberRepository

    @Test
    fun `create stores company with member`() {
        val member = memberRepository.save(TestMemberFactory.member())
        val request = CreateCompanyRequest(name = "Kakao", jobChangeStatus = JobChangeStatus.APPLIED, memo = "memo")

        val id = companyCommandService.create(member, request)

        val saved = companyRepository.findById(id).orElseThrow()
        assertThat(saved.member.id).isEqualTo(member.id)
        assertThat(saved.name).isEqualTo("Kakao")
        assertThat(saved.jobChangeStatus).isEqualTo(JobChangeStatus.APPLIED)
        assertThat(saved.memo).isEqualTo("memo")
    }

    @Test
    fun `create stores extended fields`() {
        val member = memberRepository.save(TestMemberFactory.member())
        val request = CreateCompanyRequest(
            name = "Toss",
            targetStatus = "X",
            jobPostingUrl = "https://example.com/jobs/1",
            recruitmentDeadline = java.time.LocalDate.of(2026, 5, 31),
            jobChangeStatus = JobChangeStatus.APPLIED,
            scores = listOf(CompanyScoreRequest(criteriaId = "c1", actualInfo = "good", score = 80)),
            memo = "extended"
        )

        val id = companyCommandService.create(member, request)

        val saved = companyRepository.findById(id).orElseThrow()
        assertThat(saved.targetStatus).isEqualTo("X")
        assertThat(saved.jobPostingUrl).isEqualTo("https://example.com/jobs/1")
        assertThat(saved.recruitmentDeadline).isEqualTo(java.time.LocalDate.of(2026, 5, 31))
        assertThat(saved.scores).contains("\"criteriaId\":\"c1\"")
        assertThat(saved.scores).contains("\"score\":80")
    }

    @Test
    fun `update changes only member company`() {
        val member = memberRepository.save(TestMemberFactory.member(providerId = "google-1"))
        val otherMember = memberRepository.save(TestMemberFactory.member(providerId = "google-2"))
        val company = companyRepository.save(Company(member = member, name = "Naver"))
        val otherCompany = companyRepository.save(Company(member = otherMember, name = "Other"))

        companyCommandService.update(member, company.id, UpdateCompanyRequest(name = "Kakao", memo = "changed"))

        assertThat(companyRepository.findById(company.id).orElseThrow().name).isEqualTo("Kakao")
        assertThat(companyRepository.findById(otherCompany.id).orElseThrow().name).isEqualTo("Other")
        assertThatThrownBy { companyCommandService.update(member, otherCompany.id, UpdateCompanyRequest(name = "nope")) }
            .isInstanceOf(EntityNotFoundException::class.java)
    }

    @Test
    fun `updateStatus changes only member company`() {
        val member = memberRepository.save(TestMemberFactory.member())
        val company = companyRepository.save(Company(member = member, name = "Line"))

        companyCommandService.updateStatus(member, company.id, JobChangeStatus.APPLIED)

        assertThat(companyRepository.findById(company.id).orElseThrow().jobChangeStatus).isEqualTo(JobChangeStatus.APPLIED)
    }

    @Test
    fun `delete removes only member company`() {
        val member = memberRepository.save(TestMemberFactory.member())
        val company = companyRepository.save(Company(member = member, name = "Coupang"))

        companyCommandService.delete(member, company.id)

        assertThat(companyRepository.existsById(company.id)).isFalse()
    }
}
