package com.sotti.kindergarten.dto.admin

data class DashboardStatsResponse(
    val totalCount: Long,
    val verifiedCount: Long,
    val unverifiedCount: Long,
    val activeCount: Long,
    val byEstablishType: Map<String, Long>,
)
