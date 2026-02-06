package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class MealApiResponse(
    val kinderInfo: List<Meal>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Meal(
    val kinderCode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    val establish: String?,
    val mlsr_oprn_way_tp_cd: String?,
    val cons_ents_nm: String?,
    val al_kpcnt: Int?,
    val mlsr_kpcnt: Int?,
    val ntrt_tchr_agmt_yn: String?,
    val snge_agmt_ntrt_thcnt: Int?,
    val cprt_agmt_ntrt_thcnt: Int?,
    val cprt_agmt_itt_nm: String?,
    val ckcnt: Int?,
    val cmcnt: Int?,
    val mas_mspl_dclr_yn: String?,
    val pbntTmng: String?,
)
