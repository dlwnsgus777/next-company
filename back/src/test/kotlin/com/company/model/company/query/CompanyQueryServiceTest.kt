package com.company.model.company.query

import com.company.model.company.domain.Company
import com.company.model.company.domain.CompanyRepository
import com.company.model.company.domain.JobChangeStatus
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

    @Test
    fun `회사 목록 페이징 조회시 Page Output을 반환한다`() {
        companyRepository.save(Company(name = "테스트 회사", jobChangeStatus = JobChangeStatus.NOT_APPLIED))

        val result = companyQueryService.getAll(PageRequest.of(0, 10))

        assertThat(result.totalElements).isEqualTo(1)
        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].name).isEqualTo("테스트 회사")
    }

    @Test
    fun `회사 목록이 비어있으면 빈 페이지를 반환한다`() {
        val result = companyQueryService.getAll(PageRequest.of(0, 10))

        assertThat(result.content).isEmpty()
        assertThat(result.totalElements).isEqualTo(0)
    }
}
