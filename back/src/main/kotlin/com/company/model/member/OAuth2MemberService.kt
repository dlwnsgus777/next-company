package com.company.model.member

import com.company.model.member.domain.AuthProvider
import com.company.model.member.domain.Member
import com.company.model.member.domain.MemberRepository
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class OAuth2MemberService(private val memberRepository: MemberRepository) {

    @Transactional
    fun saveOrUpdate(user: OAuth2User): Member {
        val providerId = user.requiredAttribute("sub")
        val email = user.requiredAttribute("email")
        val name = user.attribute("name") ?: email
        val pictureUrl = user.attribute("picture")

        val member = memberRepository.findByProviderAndProviderId(AuthProvider.GOOGLE, providerId)
            ?: return memberRepository.save(
                Member(
                    provider = AuthProvider.GOOGLE,
                    providerId = providerId,
                    email = email,
                    name = name,
                    pictureUrl = pictureUrl
                )
            )

        member.updateProfile(name = name, pictureUrl = pictureUrl)
        return member
    }

    @Transactional
    fun getOrCreateCurrentMember(user: OAuth2User?): Member {
        if (user == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login is required")
        }
        return saveOrUpdate(user)
    }

    @Transactional(readOnly = true)
    fun findByGoogleUser(user: OAuth2User): Member? {
        return memberRepository.findByProviderAndProviderId(AuthProvider.GOOGLE, user.requiredAttribute("sub"))
    }

    private fun OAuth2User.requiredAttribute(name: String): String {
        return attribute(name) ?: throw IllegalArgumentException("Google OAuth attribute '$name' is required")
    }

    private fun OAuth2User.attribute(name: String): String? {
        return getAttribute<Any>(name)?.toString()?.takeIf { it.isNotBlank() }
    }
}
