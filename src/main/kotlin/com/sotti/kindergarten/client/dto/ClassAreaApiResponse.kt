package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ClassAreaApiResponse(
    val kinderInfo: List<ClassArea>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ClassArea(
    val kinderCode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    val establish: String?,
    val crcnt: Int?,
    val clsrarea: String?,
    val phgrindrarea: String?,
    val hlsparea: String?,
    val ktchmssparea: String?,
    val otsparea: String?,
    val pbntTmng: String?,
)
