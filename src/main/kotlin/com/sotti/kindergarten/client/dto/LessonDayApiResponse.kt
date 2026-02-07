package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LessonDayApiResponse(
    val kinderInfo: List<LessonDay>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class LessonDay(
    @JsonProperty("kindercode")
    val kinderCode: String?,
    val ag3_lsn_dcnt: String?,
    val ag4_lsn_dcnt: String?,
    val ag5_lsn_dcnt: String?,
    val mix_age_lsn_dcnt: String?,
    val spcl_lsn_dcnt: String?,
    val afsc_pros_lsn_dcnt: String?,
    val ldnum_blw_yn: String?,
    val pbnttmng: String?,
)
