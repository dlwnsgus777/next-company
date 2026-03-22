package com.company.model.company.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CompanyRepository : JpaRepository<Company, Long> {
    fun findAllByOrderByCreatedAtDesc(): List<Company>
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<Company>
}
