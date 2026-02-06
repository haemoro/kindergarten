package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class BasicInfoApiResponse(
    val kinderInfo: List<BasicInfo>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class BasicInfo(
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
    val clcnt3: Int?,
    val clcnt4: Int?,
    val clcnt5: Int?,
    val mixclcnt: Int?,
    val shclcnt: Int?,
    val prmstfcnt: Int?,
    val ag3fpcnt: Int?,
    val ag4fpcnt: Int?,
    val ag5fpcnt: Int?,
    val mixfpcnt: Int?,
    val spcnfpcnt: Int?,
    val ppcnt3: Int?,
    val ppcnt4: Int?,
    val ppcnt5: Int?,
    val mixppcnt: Int?,
    val shppcnt: Int?,
    val pbnttmng: String?,
    val rpstYn: String?,
    val lttdcdnt: String?,
    val lngtcdnt: String?,
)
