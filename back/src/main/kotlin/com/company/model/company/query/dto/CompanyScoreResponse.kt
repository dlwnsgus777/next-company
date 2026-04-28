package com.company.model.company.query.dto

data class CompanyScoreResponse(
    val criteriaId: String,
    val actualInfo: String = "",
    val score: Int = 0
)
