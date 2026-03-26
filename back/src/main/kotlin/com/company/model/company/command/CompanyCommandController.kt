package com.company.model.company.command

import com.company.common.ApiResponse
import com.company.model.company.command.dto.CreateCompanyRequest
import com.company.model.company.command.dto.UpdateCompanyRequest
import com.company.model.company.command.dto.UpdateCompanyStatusRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/companies")
class CompanyCommandController(private val companyCommandService: CompanyCommandService) {

    @PostMapping
    fun create(@Valid @RequestBody request: CreateCompanyRequest): ResponseEntity<ApiResponse<Map<String, Long>>> {
        val id = companyCommandService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(mapOf("id" to id)))
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UpdateCompanyRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        companyCommandService.update(id, request)
        return ResponseEntity.ok(ApiResponse.ok())
    }

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: Long,
        @RequestBody request: UpdateCompanyStatusRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        companyCommandService.updateStatus(id, request.jobChangeStatus)
        return ResponseEntity.ok(ApiResponse.ok())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<ApiResponse<Nothing>> {
        companyCommandService.delete(id)
        return ResponseEntity.ok(ApiResponse.ok())
    }
}
