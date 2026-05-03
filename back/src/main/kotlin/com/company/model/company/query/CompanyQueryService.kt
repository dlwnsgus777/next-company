package com.company.model.company.query

import com.company.model.company.domain.CompanyRepository
import com.company.model.member.domain.Member
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CompanyQueryService(
    private val companyRepository: CompanyRepository,
    private val objectMapper: ObjectMapper
) {

    fun getAll(member: Member, pageable: Pageable): Page<CompanyOutput> =
        companyRepository.findAllByMemberOrderByCreatedAtDesc(member, pageable)
            .map { CompanyOutput.from(it, objectMapper) }
}
