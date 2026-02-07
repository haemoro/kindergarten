package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.Center
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface CenterRepositoryCustom {
    fun findNearby(
        lat: Double,
        lng: Double,
        radiusMeters: Double,
        establishType: String?,
        name: String?,
        pageable: Pageable,
    ): Page<Center>

    fun findAllWithFilters(
        establishType: String?,
        name: String?,
        pageable: Pageable,
    ): Page<Center>

    fun findNearbyActive(
        lat: Double,
        lng: Double,
        radiusMeters: Double,
        establishType: String?,
        name: String?,
        pageable: Pageable,
    ): Page<Center>

    fun findAllActiveWithFilters(
        establishType: String?,
        name: String?,
        pageable: Pageable,
    ): Page<Center>

    fun findAllWithAdminFilters(
        keyword: String?,
        establishType: String?,
        isVerified: Boolean?,
        isActive: Boolean?,
        pageable: Pageable,
    ): Page<Center>

    fun findMapMarkers(
        lat: Double,
        lng: Double,
        radiusMeters: Double,
        establishType: String?,
    ): List<MapMarkerProjection>
}

data class MapMarkerProjection(
    val id: UUID,
    val name: String,
    val establishType: String?,
    val lat: Double,
    val lng: Double,
)
