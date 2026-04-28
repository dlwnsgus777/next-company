package com.company.model.company.query

import com.fasterxml.jackson.databind.ObjectMapper
import com.company.model.company.domain.CompanyRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CompanyQueryService(
    private val companyRepository: CompanyRepository,
    private val objectMapper: ObjectMapper
) {

    fun getAll(pageable: Pageable): Page<CompanyOutput> =
        companyRepository.findAllByOrderByCreatedAtDesc(pageable).map { CompanyOutput.from(it, objectMapper) }
}
