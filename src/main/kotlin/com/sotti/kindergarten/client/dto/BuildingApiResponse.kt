package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BuildingApiResponse(
    val kinderInfo: List<Building>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Building(
    @JsonProperty("kindercode")
    val kinderCode: String?,
    val archyy: String?,
    val floorcnt: String?,
    val bldgprusarea: String?,
    val grottar: String?,
    val pbnttmng: String?,
)
