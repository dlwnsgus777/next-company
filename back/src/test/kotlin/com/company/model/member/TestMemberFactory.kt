package com.company.model.member

import com.company.model.member.domain.AuthProvider
import com.company.model.member.domain.Member

object TestMemberFactory {
    fun member(
        providerId: String = "google-123",
        email: String = "$providerId@example.com",
        name: String = "Test User"
    ): Member {
        return Member(
            provider = AuthProvider.GOOGLE,
            providerId = providerId,
            email = email,
            name = name,
            pictureUrl = null
        )
    }
}
