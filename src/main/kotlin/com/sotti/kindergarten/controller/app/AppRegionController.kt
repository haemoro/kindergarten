package com.sotti.kindergarten.controller.app

import com.sotti.kindergarten.dto.app.RegionListResponse
import com.sotti.kindergarten.service.RegionService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/app/regions")
@Validated
class AppRegionController(
    private val regionService: RegionService,
) {
    @GetMapping
    fun getRegions(): RegionListResponse = regionService.getRegions()
}
