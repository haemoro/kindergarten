package com.sotti.kindergarten.controller.admin

import com.sotti.kindergarten.dto.PageResponse
import com.sotti.kindergarten.dto.admin.AdminBatchStatusRequest
import com.sotti.kindergarten.dto.admin.AdminBatchStatusResponse
import com.sotti.kindergarten.dto.admin.AdminKindergartenDetailResponse
import com.sotti.kindergarten.dto.admin.AdminKindergartenListResponse
import com.sotti.kindergarten.dto.admin.AdminKindergartenUpdateRequest
import com.sotti.kindergarten.service.AdminService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/admin/kindergartens")
@Validated
class AdminKindergartenController(
    private val adminService: AdminService,
) {
    @GetMapping
    fun listKindergartens(
        @RequestParam(required = false) keyword: String?,
        @RequestParam(required = false) type: String?,
        @RequestParam(required = false) isVerified: Boolean?,
        @RequestParam(required = false) isActive: Boolean?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<PageResponse<AdminKindergartenListResponse>> {
        val result =
            adminService.listKindergartens(
                keyword = keyword,
                establishType = type,
                isVerified = isVerified,
                isActive = isActive,
                page = page,
                size = size,
            )
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    fun getKindergartenDetail(
        @PathVariable id: UUID,
    ): ResponseEntity<AdminKindergartenDetailResponse> {
        val result = adminService.getKindergartenDetail(id)
        return ResponseEntity.ok(result)
    }

    @PatchMapping("/{id}")
    fun updateKindergarten(
        @PathVariable id: UUID,
        @RequestBody request: AdminKindergartenUpdateRequest,
    ): ResponseEntity<AdminKindergartenDetailResponse> {
        val result = adminService.updateKindergarten(id, request)
        return ResponseEntity.ok(result)
    }

    @PatchMapping("/batch-status")
    fun batchUpdateStatus(
        @Valid @RequestBody request: AdminBatchStatusRequest,
    ): ResponseEntity<AdminBatchStatusResponse> {
        val result = adminService.batchUpdateStatus(request)
        return ResponseEntity.ok(result)
    }
}
