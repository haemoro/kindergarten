package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class TeacherApiResponse(
    val kinderInfo: List<Teacher>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Teacher(
    val kinderCode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    val establish: String?,
    val drcnt: Int?,
    val adcnt: Int?,
    val hdst_thcnt: Int?,
    val asps_thcnt: Int?,
    val gnrl_thcnt: Int?,
    val spcn_thcnt: Int?,
    val ntcnt: Int?,
    val ntrt_thcnt: Int?,
    val shcnt_thcnt: Int?,
    val owcnt: Int?,
    val hdst_tchr_qacnt: Int?,
    val rgth_gd1_qacnt: Int?,
    val rgth_gd2_qacnt: Int?,
    val asth_qacnt: Int?,
    val spsc_tchr_qacnt: Int?,
    val nth_qacnt: Int?,
    val ntth_qacnt: Int?,
    val pbntTmng: String?,
)
