package com.company.model.company.command

import com.company.common.ApiResponse
import com.company.model.company.command.dto.CreateCompanyRequest
import com.company.model.company.command.dto.UpdateCompanyRequest
import com.company.model.company.command.dto.UpdateCompanyStatusRequest
import com.company.model.member.OAuth2MemberService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/companies")
class CompanyCommandController(
    private val companyCommandService: CompanyCommandService,
    private val oauth2MemberService: OAuth2MemberService
) {

    @PostMapping
    fun create(
        @AuthenticationPrincipal principal: OAuth2User?,
        @Valid @RequestBody request: CreateCompanyRequest
    ): ResponseEntity<ApiResponse<Map<String, Long>>> {
        val member = oauth2MemberService.getOrCreateCurrentMember(principal)
        val id = companyCommandService.create(member, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(mapOf("id" to id)))
    }

    @PatchMapping("/{id}")
    fun update(
        @AuthenticationPrincipal principal: OAuth2User?,
        @PathVariable id: Long,
        @RequestBody request: UpdateCompanyRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        val member = oauth2MemberService.getOrCreateCurrentMember(principal)
        companyCommandService.update(member, id, request)
        return ResponseEntity.ok(ApiResponse.ok())
    }

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @AuthenticationPrincipal principal: OAuth2User?,
        @PathVariable id: Long,
        @RequestBody request: UpdateCompanyStatusRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        val member = oauth2MemberService.getOrCreateCurrentMember(principal)
        companyCommandService.updateStatus(member, id, request.jobChangeStatus)
        return ResponseEntity.ok(ApiResponse.ok())
    }

    @DeleteMapping("/{id}")
    fun delete(
        @AuthenticationPrincipal principal: OAuth2User?,
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<Nothing>> {
        val member = oauth2MemberService.getOrCreateCurrentMember(principal)
        companyCommandService.delete(member, id)
        return ResponseEntity.ok(ApiResponse.ok())
    }
}
