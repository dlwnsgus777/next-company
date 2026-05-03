package com.company.model.member

import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser

class OAuth2MemberOidcUserServiceTest {

    @Test
    fun `Google OIDC 로그인 사용자로 회원을 생성하거나 갱신한다`() {
        val delegate = mock<OAuth2UserService<OidcUserRequest, OidcUser>>()
        val oauth2MemberService = mock<OAuth2MemberService>()
        val oidcUserRequest = mock<OidcUserRequest>()
        val oidcUser = mock<OidcUser>()
        val service = OAuth2MemberOidcUserService(oauth2MemberService, delegate)

        whenever(delegate.loadUser(oidcUserRequest)).thenReturn(oidcUser)

        service.loadUser(oidcUserRequest)

        verify(oauth2MemberService).saveOrUpdate(oidcUser)
    }
}
