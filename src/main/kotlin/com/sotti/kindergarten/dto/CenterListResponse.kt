package com.sotti.kindergarten.dto

import java.util.UUID

data class CenterListResponse(
    val id: UUID,
    val name: String,
    val establishType: String?,
    val address: String?,
    val phone: String?,
    val lat: Double?,
    val lng: Double?,
    val distanceKm: Double?,
    val capacity: Int?,
    val currentEnrollment: Int?,
    val totalClassCount: Int?,
    val mealProvided: Boolean?,
    val busAvailable: Boolean?,
    val extendedCare: Boolean?,
)
