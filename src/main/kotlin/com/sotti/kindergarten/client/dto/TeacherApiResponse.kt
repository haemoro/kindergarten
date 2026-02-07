package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TeacherApiResponse(
    val kinderInfo: List<Teacher>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Teacher(
    @JsonProperty("kindercode")
    val kinderCode: String?,
    val drcnt: String?,
    val adcnt: String?,
    val hdst_thcnt: String?,
    val asps_thcnt: String?,
    val gnrl_thcnt: String?,
    val spcn_thcnt: String?,
    val ntcnt: String?,
    val ntrt_thcnt: String?,
    val shcnt_thcnt: String?,
    val owcnt: String?,
    val hdst_tchr_qacnt: String?,
    val rgth_gd1_qacnt: String?,
    val rgth_gd2_qacnt: String?,
    val asth_qacnt: String?,
    val spsc_tchr_qacnt: String?,
    val nth_qacnt: String?,
    val ntth_qacnt: String?,
    val pbnttmng: String?,
)
