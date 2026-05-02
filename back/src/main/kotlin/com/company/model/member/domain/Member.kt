package com.company.model.member.domain

import com.company.config.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "member",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_member_provider_provider_id", columnNames = ["provider", "provider_id"]),
        UniqueConstraint(name = "uk_member_email", columnNames = ["email"])
    ]
)
class Member(
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    val provider: AuthProvider,

    @Column(name = "provider_id", nullable = false, length = 255)
    val providerId: String,

    @Column(nullable = false, length = 255)
    val email: String,

    @Column(nullable = false, length = 100)
    var name: String,

    @Column(name = "picture_url", length = 2048)
    var pictureUrl: String? = null,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun updateProfile(name: String, pictureUrl: String?) {
        this.name = name
        this.pictureUrl = pictureUrl
    }
}
