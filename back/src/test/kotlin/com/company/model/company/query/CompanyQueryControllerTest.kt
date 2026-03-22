package com.company.model.company.query

import com.company.config.SecurityConfig
import com.company.model.company.domain.JobChangeStatus
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(CompanyQueryController::class)
@Import(SecurityConfig::class)
@MockBean(JpaMetamodelMappingContext::class)
class CompanyQueryControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var companyQueryService: CompanyQueryService

    @Test
    fun `GET companies 요청시 200과 페이지 목록을 반환한다`() {
        val output = CompanyOutput(
            id = 1L,
            name = "테스트 회사",
            jobChangeStatus = JobChangeStatus.NOT_APPLIED,
            memo = null,
            createdAt = LocalDateTime.now()
        )
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(listOf(output), pageable, 1)
        given(companyQueryService.getAll(any())).willReturn(page)

        mockMvc.perform(get("/companies?page=0&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content").isArray)
            .andExpect(jsonPath("$.data.content[0].name").value("테스트 회사"))
            .andExpect(jsonPath("$.data.totalElements").value(1))
    }

    @Test
    fun `회사 목록이 없어도 200과 빈 배열을 반환한다`() {
        val pageable = PageRequest.of(0, 10)
        given(companyQueryService.getAll(any())).willReturn(PageImpl(emptyList(), pageable, 0))

        mockMvc.perform(get("/companies?page=0&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content").isEmpty)
            .andExpect(jsonPath("$.data.totalElements").value(0))
    }
}
