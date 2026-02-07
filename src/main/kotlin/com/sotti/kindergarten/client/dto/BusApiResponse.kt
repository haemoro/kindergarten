package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BusApiResponse(
    val kinderInfo: List<Bus>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Bus(
    @JsonProperty("kindercode")
    val kinderCode: String?,
    val vhcl_oprn_yn: String?,
    val opra_vhcnt: String?,
    val dclr_vhcnt: String?,
    val psg9_dclr_vhcnt: String?,
    val psg12_dclr_vhcnt: String?,
    val psg15_dclr_vhcnt: String?,
    val pbnttmng: String?,
)
