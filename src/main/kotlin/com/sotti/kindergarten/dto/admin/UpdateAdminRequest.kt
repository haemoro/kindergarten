package com.sotti.kindergarten.dto.admin

import com.sotti.kindergarten.entity.AdminRole

data class UpdateAdminRequest(
    val name: String? = null,
    val role: AdminRole? = null,
    val isActive: Boolean? = null,
)
