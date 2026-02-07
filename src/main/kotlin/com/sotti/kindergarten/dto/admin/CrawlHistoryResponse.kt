package com.sotti.kindergarten.dto.admin

import java.time.LocalDateTime
import java.util.UUID

data class CrawlHistoryResponse(
    val id: UUID,
    val source: String,
    val status: String,
    val errorMessage: String?,
    val itemCount: Int?,
    val startedAt: LocalDateTime,
    val finishedAt: LocalDateTime?,
)
