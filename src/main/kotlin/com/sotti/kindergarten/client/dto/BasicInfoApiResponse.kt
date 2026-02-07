package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BasicInfoApiResponse(
    val kinderInfo: List<BasicInfo>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class BasicInfo(
    @JsonProperty("kindercode")
    val kinderCode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    val establish: String?,
    val rppnname: String?,
    val ldgrname: String?,
    val edate: String?,
    val odate: String?,
    val addr: String?,
    val telno: String?,
    val faxno: String?,
    val hpaddr: String?,
    val opertime: String?,
    val clcnt3: String?,
    val clcnt4: String?,
    val clcnt5: String?,
    val mixclcnt: String?,
    val shclcnt: String?,
    val prmstfcnt: String?,
    val ag3fpcnt: String?,
    val ag4fpcnt: String?,
    val ag5fpcnt: String?,
    val mixfpcnt: String?,
    val spcnfpcnt: String?,
    val ppcnt3: String?,
    val ppcnt4: String?,
    val ppcnt5: String?,
    val mixppcnt: String?,
    val shppcnt: String?,
    val pbnttmng: String?,
    @JsonProperty("rpst_yn")
    val rpstYn: String?,
    val lttdcdnt: String?,
    val lngtcdnt: String?,
)
