package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class BusApiResponse(
    val kinderInfo: List<Bus>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Bus(
    val kinderCode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    val establish: String?,
    val vhcl_oprn_yn: String?,
    val opra_vhcnt: Int?,
    val dclr_vhcnt: Int?,
    val psg9_dclr_vhcnt: Int?,
    val psg12_dclr_vhcnt: Int?,
    val psg15_dclr_vhcnt: Int?,
    val pbntTmng: String?,
)
