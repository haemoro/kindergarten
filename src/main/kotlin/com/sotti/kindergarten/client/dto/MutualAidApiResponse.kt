package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class MutualAidApiResponse(
    val kinderInfo: List<MutualAid>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MutualAid(
    val kinderCode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    @JsonProperty("estb_pt")
    val estbPt: String?,
    val school_ds_yn: String?,
    val school_ds_en: String?,
    val educate_ds_yn: String?,
    val educate_ds_en: String?,
    val pbntTmng: String?,
)
