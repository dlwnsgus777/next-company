package com.company.model.member.auth

import com.company.common.ApiResponse
import com.company.model.member.OAuth2MemberService
import com.company.model.member.auth.dto.CurrentMemberResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val oauth2MemberService: OAuth2MemberService) {

    @GetMapping("/me")
    fun me(@AuthenticationPrincipal principal: OAuth2User?): ResponseEntity<ApiResponse<CurrentMemberResponse>> {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("로그인이 필요합니다."))
        }

        val member = oauth2MemberService.findByGoogleUser(principal)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("로그인이 필요합니다."))

        return ResponseEntity.ok(ApiResponse.ok(CurrentMemberResponse.from(member)))
    }

    @PostMapping("/logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication?
    ): ResponseEntity<ApiResponse<Nothing>> {
        SecurityContextLogoutHandler().logout(request, response, authentication)
        response.addHeader("Set-Cookie", "JSESSIONID=; Path=/; Max-Age=0; HttpOnly")
        return ResponseEntity.ok(ApiResponse.ok())
    }
}
