package com.sotti.kindergarten.dto

import jakarta.validation.constraints.Size
import java.util.UUID

data class CenterCompareRequest(
    @field:Size(min = 2, max = 4)
    val centerIds: List<UUID>,
    val lat: Double?,
    val lng: Double?,
)
