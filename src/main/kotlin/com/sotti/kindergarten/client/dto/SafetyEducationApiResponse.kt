package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SafetyEducationApiResponse(
    val kinderInfo: List<SafetyEducation>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SafetyEducation(
    val kinderCode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    @JsonProperty("estb_pt")
    val estbPt: String?,
    val pbnt_sem_sc_cd: String?,
    val safe_tp_cd1: String?,
    val safe_tp_cd2: String?,
    val safe_tp_cd3: String?,
    val safe_tp_cd4: String?,
    val safe_tp_cd5: String?,
    val safe_tp_cd6: String?,
    val safe_tp_cd7: String?,
    val safe_tp_cd8: String?,
    val pbntTmng: String?,
)
