package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class AfterSchoolApiResponse(
    val kinderInfo: List<AfterSchool>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AfterSchool(
    val kinderCode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    val establish: String?,
    val inor_clcnt: Int?,
    val pm_rrgn_clcnt: Int?,
    val oper_time: String?,
    val inor_ptcn_kpcnt: Int?,
    val pm_rrgn_ptcn_kpcnt: Int?,
    val fxrl_thcnt: Int?,
    val shcnt_thcnt: Int?,
    val incnt: Int?,
    val pbntTmng: String?,
)
