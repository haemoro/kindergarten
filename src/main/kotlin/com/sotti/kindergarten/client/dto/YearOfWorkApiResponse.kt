package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class YearOfWorkApiResponse(
    val kinderInfo: List<YearOfWork>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class YearOfWork(
    val kinderCode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    val establish: String?,
    val yy1_undr_thcnt: Int?,
    val yy1_abv_yy2_undr_thcnt: Int?,
    val yy2_abv_yy4_undr_thcnt: Int?,
    val yy4_abv_yy6_undr_thcnt: Int?,
    val yy6_abv_thcnt: Int?,
    val pbntTmng: String?,
)
