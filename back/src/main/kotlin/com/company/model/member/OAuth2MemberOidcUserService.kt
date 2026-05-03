package com.company.model.member

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service

@Service
class OAuth2MemberOidcUserService internal constructor(
    private val oauth2MemberService: OAuth2MemberService,
    private val delegate: OAuth2UserService<OidcUserRequest, OidcUser>
) : OAuth2UserService<OidcUserRequest, OidcUser> {

    @Autowired
    constructor(oauth2MemberService: OAuth2MemberService) : this(oauth2MemberService, OidcUserService())

    override fun loadUser(userRequest: OidcUserRequest): OidcUser {
        val user = delegate.loadUser(userRequest)
        oauth2MemberService.saveOrUpdate(user)
        return user
    }
}
