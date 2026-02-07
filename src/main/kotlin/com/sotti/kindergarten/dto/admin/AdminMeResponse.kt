package com.sotti.kindergarten.dto.admin

import com.sotti.kindergarten.entity.AdminRole
import java.time.LocalDateTime
import java.util.UUID

data class AdminMeResponse(
    val id: UUID,
    val email: String,
    val name: String,
    val role: AdminRole,
    val createdAt: LocalDateTime,
)
