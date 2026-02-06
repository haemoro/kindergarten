package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class BuildingApiResponse(
    val kinderInfo: List<Building>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Building(
    val kinderCode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    val establish: String?,
    val archyy: String?,
    val floorcnt: Int?,
    val bldgprusarea: String?,
    val grottar: String?,
    val pbntTmng: String?,
)
