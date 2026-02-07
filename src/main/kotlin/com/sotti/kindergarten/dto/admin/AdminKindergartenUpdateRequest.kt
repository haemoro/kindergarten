package com.sotti.kindergarten.dto.admin

data class AdminKindergartenUpdateRequest(
    val isVerified: Boolean?,
    val isActive: Boolean?,
    val adminMemo: String?,
)
