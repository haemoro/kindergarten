package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SafetyCheckApiResponse(
    val kinderInfo: List<SafetyCheck>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SafetyCheck(
    @JsonProperty("kindercode")
    val kinderCode: String?,
    val plyg_ck_yn: String?,
    val plyg_ck_dt: String?,
    val plyg_ck_rs_cd: String?,
    val cctv_ist_yn: String?,
    val cctv_ist_total: String?,
    val cctv_ist_in: String?,
    val cctv_ist_out: String?,
    val fire_avd_yn: String?,
    val fire_avd_dt: String?,
    val fire_safe_yn: String?,
    val fire_safe_dt: String?,
    val gas_ck_yn: String?,
    val gas_ck_dt: String?,
    val elect_ck_yn: String?,
    val elect_ck_dt: String?,
    val pbnttmng: String?,
)
