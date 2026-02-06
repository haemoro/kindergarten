package com.sotti.kindergarten.controller

import com.sotti.kindergarten.dto.CenterCompareRequest
import com.sotti.kindergarten.dto.CenterCompareResponse
import com.sotti.kindergarten.dto.CenterDetailResponse
import com.sotti.kindergarten.dto.CenterListResponse
import com.sotti.kindergarten.dto.PageResponse
import com.sotti.kindergarten.service.CenterService
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/centers")
@Validated
class CenterController(
    private val centerService: CenterService,
) {
    @GetMapping
    fun listCenters(
        @RequestParam(required = false) lat: Double?,
        @RequestParam(required = false) lng: Double?,
        @RequestParam(required = false, defaultValue = "2") radiusKm: Double,
        @RequestParam(required = false) type: String?,
        @RequestParam(required = false) q: String?,
        @RequestParam(required = false, defaultValue = "distance") sort: String,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "20") size: Int,
    ): PageResponse<CenterListResponse> =
        centerService.listCenters(
            lat = lat,
            lng = lng,
            radiusKm = radiusKm,
            establishType = type,
            query = q,
            sort = sort,
            page = page,
            size = size,
        )

    @GetMapping("/{id}")
    fun getCenterDetail(
        @PathVariable id: UUID,
    ): CenterDetailResponse = centerService.getCenterDetail(id)

    @PostMapping("/compare")
    fun compareCenters(
        @RequestBody @Valid request: CenterCompareRequest,
    ): CenterCompareResponse = centerService.compareCenters(request)
}
