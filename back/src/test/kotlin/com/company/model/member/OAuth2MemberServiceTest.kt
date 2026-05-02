package com.company.model.member

import com.company.model.member.domain.AuthProvider
import com.company.model.member.domain.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class OAuth2MemberServiceTest {

    @Autowired lateinit var oauth2MemberService: OAuth2MemberService
    @Autowired lateinit var memberRepository: MemberRepository

    @Test
    fun `구글 OAuth 사용자로 회원을 생성한다`() {
        val principal = googleUser(
            sub = "google-123",
            email = "user@example.com",
            name = "홍길동",
            picture = "https://example.com/avatar.png"
        )

        val member = oauth2MemberService.saveOrUpdate(principal)

        val saved = memberRepository.findById(member.id).orElseThrow()
        assertThat(saved.provider).isEqualTo(AuthProvider.GOOGLE)
        assertThat(saved.providerId).isEqualTo("google-123")
        assertThat(saved.email).isEqualTo("user@example.com")
        assertThat(saved.name).isEqualTo("홍길동")
        assertThat(saved.pictureUrl).isEqualTo("https://example.com/avatar.png")
    }

    @Test
    fun `이미 가입된 구글 회원이면 이름과 프로필 이미지를 갱신한다`() {
        oauth2MemberService.saveOrUpdate(
            googleUser(
                sub = "google-123",
                email = "user@example.com",
                name = "홍길동",
                picture = "https://example.com/old.png"
            )
        )

        val updated = oauth2MemberService.saveOrUpdate(
            googleUser(
                sub = "google-123",
                email = "user@example.com",
                name = "홍길동2",
                picture = "https://example.com/new.png"
            )
        )

        assertThat(memberRepository.findAll()).hasSize(1)
        assertThat(updated.name).isEqualTo("홍길동2")
        assertThat(updated.pictureUrl).isEqualTo("https://example.com/new.png")
    }

    private fun googleUser(
        sub: String,
        email: String,
        name: String,
        picture: String
    ): OAuth2User {
        return DefaultOAuth2User(
            listOf(SimpleGrantedAuthority("ROLE_USER")),
            mapOf(
                "sub" to sub,
                "email" to email,
                "name" to name,
                "picture" to picture
            ),
            "sub"
        )
    }
}
