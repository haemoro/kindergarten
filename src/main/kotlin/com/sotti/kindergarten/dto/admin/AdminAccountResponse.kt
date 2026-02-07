package com.sotti.kindergarten.dto.admin

import com.sotti.kindergarten.entity.AdminRole
import java.time.LocalDateTime
import java.util.UUID

data class AdminAccountResponse(
    val id: UUID,
    val email: String,
    val name: String,
    val role: AdminRole,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
