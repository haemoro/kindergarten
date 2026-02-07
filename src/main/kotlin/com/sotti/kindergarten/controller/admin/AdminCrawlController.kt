package com.sotti.kindergarten.controller.admin

import com.sotti.kindergarten.dto.PageResponse
import com.sotti.kindergarten.dto.admin.CrawlHistoryResponse
import com.sotti.kindergarten.dto.admin.CrawlTriggerRequest
import com.sotti.kindergarten.dto.admin.DashboardStatsResponse
import com.sotti.kindergarten.service.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
@Validated
class AdminCrawlController(
    private val adminService: AdminService,
) {
    @GetMapping("/crawl-histories")
    fun getCrawlHistories(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<PageResponse<CrawlHistoryResponse>> {
        val result = adminService.getCrawlHistories(page, size)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/crawl/trigger")
    fun triggerCrawl(
        @RequestBody request: CrawlTriggerRequest,
    ): ResponseEntity<CrawlHistoryResponse> {
        val result = adminService.triggerCrawl(request)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/dashboard/stats")
    fun getDashboardStats(): ResponseEntity<DashboardStatsResponse> {
        val result = adminService.getDashboardStats()
        return ResponseEntity.ok(result)
    }
}
