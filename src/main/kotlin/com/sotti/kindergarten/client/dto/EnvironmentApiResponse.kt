package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class EnvironmentApiResponse(
    val kinderInfo: List<Environment>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Environment(
    @JsonProperty("kindercode")
    val kinderCode: String?,
    val arql_chk_dt: String?,
    val arql_chk_rslt_tp_cd: String?,
    val fxtm_dsnf_trgt_yn: String?,
    val fxtm_dsnf_chk_dt: String?,
    val fxtm_dsnf_chk_rslt_tp_cd: String?,
    val tp_01: String?,
    val tp_02: String?,
    val tp_03: String?,
    val tp_04: String?,
    val unwt_qlwt_insc_yn: String?,
    val qlwt_insc_dt: String?,
    val qlwt_insc_stby_yn: String?,
    val mdst_chk_dt: String?,
    val mdst_chk_rslt_cd: String?,
    val ilmn_chk_dt: String?,
    val ilmn_chk_rslt_cd: String?,
    val pbnttmng: String?,
)
