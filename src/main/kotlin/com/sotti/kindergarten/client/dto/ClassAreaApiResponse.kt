package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ClassAreaApiResponse(
    val kinderInfo: List<ClassArea>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ClassArea(
    @JsonProperty("kindercode")
    val kinderCode: String?,
    val crcnt: String?,
    val clsrarea: String?,
    val phgrindrarea: String?,
    val hlsparea: String?,
    val ktchmssparea: String?,
    val otsparea: String?,
    val pbnttmng: String?,
)
