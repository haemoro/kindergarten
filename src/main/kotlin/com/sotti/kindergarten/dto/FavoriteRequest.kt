package com.sotti.kindergarten.dto

import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class FavoriteRequest(
    @field:NotBlank
    val deviceId: String,
    val centerId: UUID,
)
