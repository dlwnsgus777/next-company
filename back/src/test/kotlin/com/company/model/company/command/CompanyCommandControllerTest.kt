package com.company.model.company.command

import com.company.model.company.domain.Company
import com.company.model.company.domain.CompanyRepository
import com.company.model.company.domain.JobChangeStatus
import com.company.model.member.TestMemberFactory
import com.company.model.member.domain.MemberRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CompanyCommandControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var companyRepository: CompanyRepository
    @Autowired lateinit var memberRepository: MemberRepository

    @Test
    fun `POST companies returns 201 and id`() {
        mockMvc.perform(
            post("/companies")
                .with(login())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"Kakao","jobChangeStatus":"APPLIED","memo":"memo"}""")
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.id").isNumber)
    }

    @Test
    fun `POST companies returns 400 when name is empty`() {
        mockMvc.perform(
            post("/companies")
                .with(login())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"","jobChangeStatus":"APPLIED"}""")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `PATCH companies id returns 200`() {
        val member = memberRepository.save(TestMemberFactory.member())
        val company = companyRepository.save(Company(member = member, name = "Naver"))

        mockMvc.perform(
            patch("/companies/${company.id}")
                .with(login())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"Kakao"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
    }

    @Test
    fun `PATCH companies id returns 404 when not found for member`() {
        mockMvc.perform(
            patch("/companies/999")
                .with(login())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"Missing"}""")
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `PATCH companies status returns 200`() {
        val member = memberRepository.save(TestMemberFactory.member())
        val company = companyRepository.save(Company(member = member, name = "Line"))

        mockMvc.perform(
            patch("/companies/${company.id}/status")
                .with(login())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"jobChangeStatus":"APPLIED"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
    }

    @Test
    fun `DELETE companies id returns 200`() {
        val member = memberRepository.save(TestMemberFactory.member())
        val company = companyRepository.save(Company(member = member, name = "Coupang"))

        mockMvc.perform(delete("/companies/${company.id}").with(login()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
    }

    private fun login() = oauth2Login().attributes {
        it["sub"] = "google-123"
        it["email"] = "google-123@example.com"
        it["name"] = "Test User"
    }
}
