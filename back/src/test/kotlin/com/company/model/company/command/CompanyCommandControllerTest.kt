package com.company.model.company.command

import com.company.model.company.domain.Company
import com.company.model.company.domain.CompanyRepository
import com.company.model.company.domain.JobChangeStatus
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CompanyCommandControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var companyRepository: CompanyRepository

    @Test
    fun `POST companies 요청시 201과 id를 반환한다`() {
        mockMvc.perform(
            post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"카카오","jobChangeStatus":"APPLIED","memo":"메모"}""")
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").isNumber)
    }

    @Test
    fun `POST companies 요청시 name이 없으면 400을 반환한다`() {
        mockMvc.perform(
            post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"","jobChangeStatus":"APPLIED"}""")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `PATCH companies id 요청시 200을 반환한다`() {
        val company = companyRepository.save(Company(name = "네이버", jobChangeStatus = JobChangeStatus.NOT_APPLIED))

        mockMvc.perform(
            patch("/companies/${company.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"카카오"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
    }

    @Test
    fun `PATCH companies id 요청시 존재하지 않으면 404를 반환한다`() {
        mockMvc.perform(
            patch("/companies/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"없는회사"}""")
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `PATCH companies id status 요청시 200을 반환한다`() {
        val company = companyRepository.save(Company(name = "라인", jobChangeStatus = JobChangeStatus.NOT_APPLIED))

        mockMvc.perform(
            patch("/companies/${company.id}/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"jobChangeStatus":"APPLIED"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
    }

    @Test
    fun `PATCH companies id status 요청시 존재하지 않으면 404를 반환한다`() {
        mockMvc.perform(
            patch("/companies/999/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"jobChangeStatus":"APPLIED"}""")
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `DELETE companies id 요청시 200을 반환한다`() {
        val company = companyRepository.save(Company(name = "쿠팡", jobChangeStatus = JobChangeStatus.NOT_APPLIED))

        mockMvc.perform(delete("/companies/${company.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
    }

    @Test
    fun `DELETE companies id 요청시 존재하지 않으면 404를 반환한다`() {
        mockMvc.perform(delete("/companies/999"))
            .andExpect(status().isNotFound)
    }
}
