package com.company.model.company.query

import com.company.model.company.domain.Company
import com.company.model.company.domain.CompanyRepository
import com.company.model.company.domain.JobChangeStatus
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CompanyQueryControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var companyRepository: CompanyRepository

    @Test
    fun `GET companies 요청시 200과 페이지 목록을 반환한다`() {
        companyRepository.save(Company(name = "테스트 회사", jobChangeStatus = JobChangeStatus.NOT_APPLIED))

        mockMvc.perform(get("/companies?page=0&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content").isArray)
            .andExpect(jsonPath("$.data.content[0].name").value("테스트 회사"))
            .andExpect(jsonPath("$.data.totalElements").value(1))
    }

    @Test
    fun `회사 목록이 없어도 200과 빈 배열을 반환한다`() {
        mockMvc.perform(get("/companies?page=0&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content").isEmpty)
            .andExpect(jsonPath("$.data.totalElements").value(0))
    }
}
