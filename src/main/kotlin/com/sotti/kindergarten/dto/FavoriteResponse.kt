package com.sotti.kindergarten.dto

import java.time.LocalDateTime
import java.util.UUID

data class FavoriteResponse(
    val id: UUID,
    val centerId: UUID,
    val centerName: String,
    val createdAt: LocalDateTime,
)
