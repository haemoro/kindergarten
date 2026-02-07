package com.sotti.kindergarten.dto.app

data class RegionListResponse(
    val regions: List<SidoGroup>,
)

data class SidoGroup(
    val sidoCode: String,
    val sidoName: String,
    val sggList: List<SggItem>,
)

data class SggItem(
    val sggCode: String,
    val sggName: String,
)
