package com.company.model.member.domain

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByProviderAndProviderId(provider: AuthProvider, providerId: String): Member?
}
