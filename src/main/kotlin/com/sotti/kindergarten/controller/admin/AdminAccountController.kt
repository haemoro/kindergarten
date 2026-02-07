package com.sotti.kindergarten.controller.admin

import com.sotti.kindergarten.dto.admin.AdminAccountResponse
import com.sotti.kindergarten.dto.admin.CreateAdminRequest
import com.sotti.kindergarten.dto.admin.UpdateAdminRequest
import com.sotti.kindergarten.security.AdminUserDetails
import com.sotti.kindergarten.service.AdminAccountService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/admin/admins")
class AdminAccountController(
    private val adminAccountService: AdminAccountService,
) {
    @GetMapping
    fun listAdmins(): ResponseEntity<List<AdminAccountResponse>> = ResponseEntity.ok(adminAccountService.listAdmins())

    @GetMapping("/{id}")
    fun getAdmin(
        @PathVariable id: UUID,
    ): ResponseEntity<AdminAccountResponse> = ResponseEntity.ok(adminAccountService.getAdmin(id))

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    fun createAdmin(
        @Valid @RequestBody request: CreateAdminRequest,
    ): ResponseEntity<AdminAccountResponse> = ResponseEntity.status(HttpStatus.CREATED).body(adminAccountService.createAdmin(request))

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    fun updateAdmin(
        @PathVariable id: UUID,
        @RequestBody request: UpdateAdminRequest,
    ): ResponseEntity<AdminAccountResponse> = ResponseEntity.ok(adminAccountService.updateAdmin(id, request))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    fun deleteAdmin(
        @PathVariable id: UUID,
        @AuthenticationPrincipal userDetails: AdminUserDetails,
    ): ResponseEntity<Void> {
        adminAccountService.deleteAdmin(id, userDetails.id)
        return ResponseEntity.noContent().build()
    }
}
