package com.company.model.kanban.query

import com.company.common.ApiResponse
import com.company.model.kanban.query.dto.KanbanColumnConfigResponse
import com.company.model.member.OAuth2MemberService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/kanban-columns")
class KanbanColumnConfigQueryController(
    private val kanbanColumnConfigQueryService: KanbanColumnConfigQueryService,
    private val oauth2MemberService: OAuth2MemberService
) {

    @GetMapping
    fun get(@AuthenticationPrincipal principal: OAuth2User?): ResponseEntity<ApiResponse<KanbanColumnConfigResponse?>> {
        val member = oauth2MemberService.getOrCreateCurrentMember(principal)
        val result = kanbanColumnConfigQueryService.get(member)
        return ResponseEntity.ok(ApiResponse.ok(result))
    }
}
