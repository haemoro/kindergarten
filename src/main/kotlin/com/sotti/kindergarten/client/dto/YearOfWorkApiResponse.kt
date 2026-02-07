package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class YearOfWorkApiResponse(
    val kinderInfo: List<YearOfWork>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class YearOfWork(
    @JsonProperty("kindercode")
    val kinderCode: String?,
    val yy1_undr_thcnt: String?,
    val yy1_abv_yy2_undr_thcnt: String?,
    val yy2_abv_yy4_undr_thcnt: String?,
    val yy4_abv_yy6_undr_thcnt: String?,
    val yy6_abv_thcnt: String?,
    val pbnttmng: String?,
)
