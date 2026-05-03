package com.company.model.company.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import com.company.model.member.domain.Member

interface CompanyRepository : JpaRepository<Company, Long> {
    fun findAllByOrderByCreatedAtDesc(): List<Company>
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<Company>
    fun findAllByMemberOrderByCreatedAtDesc(member: Member, pageable: Pageable): Page<Company>
    fun findByIdAndMember(id: Long, member: Member): Company?
}
