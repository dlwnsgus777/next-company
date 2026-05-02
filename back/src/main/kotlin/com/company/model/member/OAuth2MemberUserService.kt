package com.company.model.member

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2MemberUserService(
    private val oauth2MemberService: OAuth2MemberService
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private val delegate = DefaultOAuth2UserService()

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val user = delegate.loadUser(userRequest)
        if (userRequest.clientRegistration.registrationId == "google") {
            oauth2MemberService.saveOrUpdate(user)
        }
        return user
    }
}
