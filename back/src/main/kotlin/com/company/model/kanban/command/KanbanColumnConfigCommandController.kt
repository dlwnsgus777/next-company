package com.company.model.kanban.command

import com.company.common.ApiResponse
import com.company.model.kanban.command.dto.SaveKanbanColumnConfigRequest
import com.company.model.member.OAuth2MemberService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/kanban-columns")
class KanbanColumnConfigCommandController(
    private val kanbanColumnConfigCommandService: KanbanColumnConfigCommandService,
    private val oauth2MemberService: OAuth2MemberService
) {

    @PutMapping
    fun save(
        @AuthenticationPrincipal principal: OAuth2User?,
        @Valid @RequestBody request: SaveKanbanColumnConfigRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        val member = oauth2MemberService.getOrCreateCurrentMember(principal)
        kanbanColumnConfigCommandService.save(member, request)
        return ResponseEntity.ok(ApiResponse.ok())
    }
}
