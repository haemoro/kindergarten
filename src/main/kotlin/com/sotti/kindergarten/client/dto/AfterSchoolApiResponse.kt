package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class AfterSchoolApiResponse(
    val kinderInfo: List<AfterSchool>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AfterSchool(
    @JsonProperty("kindercode")
    val kinderCode: String?,
    val inor_clcnt: String?,
    val pm_rrgn_clcnt: String?,
    val oper_time: String?,
    val inor_ptcn_kpcnt: String?,
    val pm_rrgn_ptcn_kpcnt: String?,
    val fxrl_thcnt: String?,
    val shcnt_thcnt: String?,
    val incnt: String?,
    val pbnttmng: String?,
)
