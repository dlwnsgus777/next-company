package com.company.model.company.query

import com.company.common.ApiResponse
import com.company.model.company.query.dto.CompanyResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/companies")
class CompanyQueryController(private val companyQueryService: CompanyQueryService) {

    @GetMapping
    fun getAll(
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<CompanyResponse>>> {
        val result = companyQueryService.getAll(pageable).map { CompanyResponse.from(it) }
        return ResponseEntity.ok(ApiResponse.ok(result))
    }
}
