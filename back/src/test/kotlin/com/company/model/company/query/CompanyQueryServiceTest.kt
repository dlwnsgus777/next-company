package com.company.model.company.query

import com.company.config.entity.BaseEntity
import com.company.model.company.domain.Company
import com.company.model.company.domain.CompanyRepository
import com.company.model.company.domain.JobChangeStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.given
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class CompanyQueryServiceTest {

    @Mock
    lateinit var companyRepository: CompanyRepository

    @InjectMocks
    lateinit var companyQueryService: CompanyQueryService

    private fun Company.withCreatedAt(time: LocalDateTime = LocalDateTime.now()): Company {
        val field = BaseEntity::class.java.getDeclaredField("createdAt")
        field.isAccessible = true
        field.set(this, time)
        return this
    }

    @Test
    fun `회사 목록 페이징 조회시 Page Output을 반환한다`() {
        val pageable = PageRequest.of(0, 10)
        val company = Company(name = "테스트 회사", jobChangeStatus = JobChangeStatus.NOT_APPLIED).withCreatedAt()
        val page = PageImpl(listOf(company), pageable, 1)
        given(companyRepository.findAllByOrderByCreatedAtDesc(pageable)).willReturn(page)

        val result = companyQueryService.getAll(pageable)

        assertThat(result.totalElements).isEqualTo(1)
        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].name).isEqualTo("테스트 회사")
    }

    @Test
    fun `회사 목록이 비어있으면 빈 페이지를 반환한다`() {
        val pageable = PageRequest.of(0, 10)
        given(companyRepository.findAllByOrderByCreatedAtDesc(pageable)).willReturn(PageImpl(emptyList(), pageable, 0))

        val result = companyQueryService.getAll(pageable)

        assertThat(result.content).isEmpty()
        assertThat(result.totalElements).isEqualTo(0)
    }
}
