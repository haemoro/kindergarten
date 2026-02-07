package com.sotti.kindergarten.dto.admin

import jakarta.validation.constraints.Size
import java.util.UUID

data class AdminBatchStatusRequest(
    @field:Size(min = 1, max = 100)
    val ids: List<UUID>,
    val isActive: Boolean? = null,
    val isVerified: Boolean? = null,
)
