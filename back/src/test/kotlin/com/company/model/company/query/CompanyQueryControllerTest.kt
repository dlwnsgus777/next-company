package com.company.model.company.query

import com.company.model.company.domain.Company
import com.company.model.company.domain.CompanyRepository
import com.company.model.member.TestMemberFactory
import com.company.model.member.domain.MemberRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CompanyQueryControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var companyRepository: CompanyRepository
    @Autowired lateinit var memberRepository: MemberRepository

    @Test
    fun `GET companies returns member companies`() {
        val member = memberRepository.save(TestMemberFactory.member())
        companyRepository.save(Company(member = member, name = "Mine"))

        mockMvc.perform(get("/companies?page=0&size=10").with(login()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content").isArray)
            .andExpect(jsonPath("$.data.content[0].name").value("Mine"))
            .andExpect(jsonPath("$.data.totalElements").value(1))
    }

    @Test
    fun `GET companies returns empty page when member has no companies`() {
        mockMvc.perform(get("/companies?page=0&size=10").with(login()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content").isEmpty)
            .andExpect(jsonPath("$.data.totalElements").value(0))
    }

    private fun login() = oauth2Login().attributes {
        it["sub"] = "google-123"
        it["email"] = "google-123@example.com"
        it["name"] = "Test User"
    }
}
