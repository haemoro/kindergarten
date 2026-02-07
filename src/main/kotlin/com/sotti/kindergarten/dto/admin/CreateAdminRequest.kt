package com.sotti.kindergarten.dto.admin

import com.sotti.kindergarten.entity.AdminRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateAdminRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    val password: String,
    @field:NotBlank(message = "Name is required")
    val name: String,
    @field:NotNull(message = "Role is required")
    val role: AdminRole,
)
