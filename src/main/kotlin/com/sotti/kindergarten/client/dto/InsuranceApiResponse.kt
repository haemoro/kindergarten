package com.sotti.kindergarten.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class InsuranceApiResponse(
    val kinderInfo: List<Insurance>?,
    val status: String?,
    val totalCount: Int?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Insurance(
    val kinderCode: String?,
    val officeedu: String?,
    val subofficeedu: String?,
    val kindername: String?,
    @JsonProperty("estb_pt")
    val estbPt: String?,
    val insurance_nm: String?,
    val insurance_en: String?,
    val insurance_yn: String?,
    val company1: String?,
    val company2: String?,
    val company3: String?,
    val pbntTmng: String?,
)
