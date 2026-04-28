package com.company.model.company.command

import com.company.model.company.command.dto.CreateCompanyRequest
import com.company.model.company.command.dto.CompanyScoreRequest
import com.company.model.company.command.dto.UpdateCompanyRequest
import com.company.model.company.domain.Company
import com.company.model.company.domain.CompanyRepository
import com.company.model.company.domain.JobChangeStatus
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

    @Test
    fun `회사 생성시 저장된 id를 반환한다`() {
        val request = CreateCompanyRequest(name = "카카오", jobChangeStatus = JobChangeStatus.APPLIED, memo = "메모")

        val id = companyCommandService.create(request)

        val saved = companyRepository.findById(id).orElseThrow()
        assertThat(saved.name).isEqualTo("카카오")
        assertThat(saved.jobChangeStatus).isEqualTo(JobChangeStatus.APPLIED)
        assertThat(saved.memo).isEqualTo("메모")
    }

    @Test
    fun `회사 생성시 화면 확장 필드를 함께 저장한다`() {
        val request = CreateCompanyRequest(
            name = "토스",
            targetStatus = "△",
            jobPostingUrl = "https://example.com/jobs/1",
            recruitmentDeadline = java.time.LocalDate.of(2026, 5, 31),
            jobChangeStatus = JobChangeStatus.APPLIED,
            scores = listOf(CompanyScoreRequest(criteriaId = "c1", actualInfo = "복지 좋음", score = 80)),
            memo = "확장 필드"
        )

        val id = companyCommandService.create(request)

        val saved = companyRepository.findById(id).orElseThrow()
        assertThat(saved.targetStatus).isEqualTo("△")
        assertThat(saved.jobPostingUrl).isEqualTo("https://example.com/jobs/1")
        assertThat(saved.recruitmentDeadline).isEqualTo(java.time.LocalDate.of(2026, 5, 31))
        assertThat(saved.scores).contains("\"criteriaId\":\"c1\"")
        assertThat(saved.scores).contains("\"score\":80")
    }

    @Test
    fun `회사 수정시 이름과 메모가 변경된다`() {
        val company = companyRepository.save(Company(name = "네이버", jobChangeStatus = JobChangeStatus.NOT_APPLIED))
        val request = UpdateCompanyRequest(name = "카카오", memo = "변경메모")

        companyCommandService.update(company.id, request)

        val updated = companyRepository.findById(company.id).orElseThrow()
        assertThat(updated.name).isEqualTo("카카오")
        assertThat(updated.memo).isEqualTo("변경메모")
    }

    @Test
    fun `회사 수정시 화면 확장 필드를 함께 변경한다`() {
        val company = companyRepository.save(Company(name = "네이버", jobChangeStatus = JobChangeStatus.NOT_APPLIED))
        val request = UpdateCompanyRequest(
            targetStatus = "X",
            jobPostingUrl = "https://example.com/jobs/2",
            recruitmentDeadline = java.time.LocalDate.of(2026, 6, 30),
            scores = listOf(CompanyScoreRequest(criteriaId = "c2", actualInfo = "가까움", score = 95))
        )

        companyCommandService.update(company.id, request)

        val updated = companyRepository.findById(company.id).orElseThrow()
        assertThat(updated.targetStatus).isEqualTo("X")
        assertThat(updated.jobPostingUrl).isEqualTo("https://example.com/jobs/2")
        assertThat(updated.recruitmentDeadline).isEqualTo(java.time.LocalDate.of(2026, 6, 30))
        assertThat(updated.scores).contains("\"criteriaId\":\"c2\"")
        assertThat(updated.scores).contains("\"score\":95")
    }

    @Test
    fun `존재하지 않는 회사 수정시 EntityNotFoundException을 던진다`() {
        val request = UpdateCompanyRequest(name = "없는회사")

        assertThatThrownBy { companyCommandService.update(999L, request) }
            .isInstanceOf(EntityNotFoundException::class.java)
    }

    @Test
    fun `회사 상태 변경시 jobChangeStatus가 변경된다`() {
        val company = companyRepository.save(Company(name = "라인", jobChangeStatus = JobChangeStatus.NOT_APPLIED))

        companyCommandService.updateStatus(company.id, JobChangeStatus.APPLIED)

        val updated = companyRepository.findById(company.id).orElseThrow()
        assertThat(updated.jobChangeStatus).isEqualTo(JobChangeStatus.APPLIED)
    }

    @Test
    fun `존재하지 않는 회사 상태 변경시 EntityNotFoundException을 던진다`() {
        assertThatThrownBy { companyCommandService.updateStatus(999L, JobChangeStatus.APPLIED) }
            .isInstanceOf(EntityNotFoundException::class.java)
    }

    @Test
    fun `회사 삭제시 DB에서 제거된다`() {
        val company = companyRepository.save(Company(name = "쿠팡", jobChangeStatus = JobChangeStatus.NOT_APPLIED))

        companyCommandService.delete(company.id)

        assertThat(companyRepository.existsById(company.id)).isFalse()
    }

    @Test
    fun `존재하지 않는 회사 삭제시 EntityNotFoundException을 던진다`() {
        assertThatThrownBy { companyCommandService.delete(999L) }
            .isInstanceOf(EntityNotFoundException::class.java)
    }
}
