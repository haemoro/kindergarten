package com.sotti.kindergarten.dto.admin

import java.time.LocalDateTime
import java.util.UUID

data class AdminKindergartenListResponse(
    val id: UUID,
    val kinderCode: String,
    val name: String,
    val establishType: String?,
    val officEdu: String?,
    val subOfficeEdu: String?,
    val address: String?,
    val isVerified: Boolean,
    val isActive: Boolean,
    val updatedAt: LocalDateTime?,
)
