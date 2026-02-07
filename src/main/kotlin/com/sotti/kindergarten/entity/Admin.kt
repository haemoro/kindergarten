package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "admin")
class Admin(
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(nullable = false)
    var password: String,
    @Column(nullable = false)
    var name: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: AdminRole = AdminRole.ADMIN,
    @Column(nullable = false)
    var isActive: Boolean = true,
) : BaseEntity()

enum class AdminRole {
    SUPER_ADMIN,
    ADMIN,
}
