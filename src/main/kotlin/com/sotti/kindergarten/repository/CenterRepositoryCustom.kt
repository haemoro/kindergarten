package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.Center
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

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
}
