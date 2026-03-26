package com.company.model.kanban.command

import com.company.common.ApiResponse
import com.company.model.kanban.command.dto.SaveKanbanColumnConfigRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/kanban-columns")
class KanbanColumnConfigCommandController(private val kanbanColumnConfigCommandService: KanbanColumnConfigCommandService) {

    @PutMapping
    fun save(@Valid @RequestBody request: SaveKanbanColumnConfigRequest): ResponseEntity<ApiResponse<Nothing>> {
        kanbanColumnConfigCommandService.save(request)
        return ResponseEntity.ok(ApiResponse.ok())
    }
}
