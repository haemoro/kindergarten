package com.sotti.kindergarten.dto

import java.time.LocalDateTime

data class ErrorResponse(
    val status: Int? = null,
    val code: String,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)
