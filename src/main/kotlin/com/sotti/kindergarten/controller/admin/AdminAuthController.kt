package com.sotti.kindergarten.controller.admin

import com.sotti.kindergarten.dto.admin.AdminMeResponse
import com.sotti.kindergarten.dto.admin.ChangePasswordRequest
import com.sotti.kindergarten.dto.admin.LoginRequest
import com.sotti.kindergarten.dto.admin.LoginResponse
import com.sotti.kindergarten.security.AdminUserDetails
import com.sotti.kindergarten.service.AdminAuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/auth")
class AdminAuthController(
    private val adminAuthService: AdminAuthService,
) {
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
    ): ResponseEntity<LoginResponse> = ResponseEntity.ok(adminAuthService.login(request))

    @GetMapping("/me")
    fun getMe(
        @AuthenticationPrincipal userDetails: AdminUserDetails,
    ): ResponseEntity<AdminMeResponse> = ResponseEntity.ok(adminAuthService.getMe(userDetails))

    @PatchMapping("/password")
    fun changePassword(
        @AuthenticationPrincipal userDetails: AdminUserDetails,
        @Valid @RequestBody request: ChangePasswordRequest,
    ): ResponseEntity<Void> {
        adminAuthService.changePassword(userDetails, request)
        return ResponseEntity.noContent().build()
    }
}
