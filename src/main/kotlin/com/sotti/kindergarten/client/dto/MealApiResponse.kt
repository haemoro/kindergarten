package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MealApiResponse(
    val kinderInfo: List<Meal>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Meal(
    @JsonProperty("kindercode")
    val kinderCode: String?,
    val mlsr_oprn_way_tp_cd: String?,
    val cons_ents_nm: String?,
    val al_kpcnt: String?,
    val mlsr_kpcnt: String?,
    val ntrt_tchr_agmt_yn: String?,
    val snge_agmt_ntrt_thcnt: String?,
    val cprt_agmt_ntrt_thcnt: String?,
    val cprt_agmt_itt_nm: String?,
    val ckcnt: String?,
    val cmcnt: String?,
    val mas_mspl_dclr_yn: String?,
    val pbnttmng: String?,
)
