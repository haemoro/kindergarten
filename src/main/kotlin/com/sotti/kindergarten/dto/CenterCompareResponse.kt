package com.sotti.kindergarten.dto

import java.util.UUID

data class CenterCompareResponse(
    val centers: List<ComparisonItem>,
)

data class ComparisonItem(
    val id: UUID,
    val name: String,
    val establishType: String?,
    val address: String?,
    val distanceKm: Double?,
    val capacity: Int?,
    val currentEnrollment: Int?,
    val teacherCount: Int?,
    val classCount: Int?,
    val mealProvided: Boolean?,
    val busAvailable: Boolean?,
    val extendedCare: Boolean?,
    val buildingArea: Double?,
    val classroomArea: Double?,
    val cctvInstalled: Boolean?,
    val cctvTotal: Int?,
)
