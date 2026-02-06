package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LessonDayApiResponse(
    val kinderInfo: List<LessonDay>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class LessonDay(
    val kinderCode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    val establish: String?,
    val ag3_lsn_dcnt: Int?,
    val ag4_lsn_dcnt: Int?,
    val ag5_lsn_dcnt: Int?,
    val mix_age_lsn_dcnt: Int?,
    val spcl_lsn_dcnt: Int?,
    val afsc_pros_lsn_dcnt: Int?,
    val ldnum_blw_yn: String?,
    val pbntTmng: String?,
)
