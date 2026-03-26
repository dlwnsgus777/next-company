package com.company.model.kanban.query

import com.company.common.ApiResponse
import com.company.model.kanban.query.dto.KanbanColumnConfigResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/kanban-columns")
class KanbanColumnConfigQueryController(private val kanbanColumnConfigQueryService: KanbanColumnConfigQueryService) {

    @GetMapping
    fun get(): ResponseEntity<ApiResponse<KanbanColumnConfigResponse?>> {
        val result = kanbanColumnConfigQueryService.get()
        return ResponseEntity.ok(ApiResponse.ok(result))
    }
}
