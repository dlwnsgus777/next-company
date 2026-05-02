package com.company.model.member.auth.dto

import com.company.model.member.domain.Member

data class CurrentMemberResponse(
    val id: Long,
    val email: String,
    val name: String,
    val pictureUrl: String?
) {
    companion object {
        fun from(member: Member): CurrentMemberResponse {
            return CurrentMemberResponse(
                id = member.id,
                email = member.email,
                name = member.name,
                pictureUrl = member.pictureUrl
            )
        }
    }
}
