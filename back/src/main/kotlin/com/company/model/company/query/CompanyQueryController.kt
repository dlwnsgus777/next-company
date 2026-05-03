package com.company.model.company.query

import com.company.common.ApiResponse
import com.company.model.company.query.dto.CompanyResponse
import com.company.model.member.OAuth2MemberService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/companies")
class CompanyQueryController(
    private val companyQueryService: CompanyQueryService,
    private val oauth2MemberService: OAuth2MemberService
) {

    @GetMapping
    fun getAll(
        @AuthenticationPrincipal principal: OAuth2User?,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<CompanyResponse>>> {
        val member = oauth2MemberService.getOrCreateCurrentMember(principal)
        val result = companyQueryService.getAll(member, pageable).map { CompanyResponse.from(it) }
        return ResponseEntity.ok(ApiResponse.ok(result))
    }
}
