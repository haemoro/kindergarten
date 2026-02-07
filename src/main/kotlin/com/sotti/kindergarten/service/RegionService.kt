package com.sotti.kindergarten.service

import com.sotti.kindergarten.dto.app.RegionListResponse
import com.sotti.kindergarten.dto.app.SggItem
import com.sotti.kindergarten.dto.app.SidoGroup
import com.sotti.kindergarten.repository.RegionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RegionService(
    private val regionRepository: RegionRepository,
) {
    fun getRegions(): RegionListResponse {
        val regions = regionRepository.findAllByOrderBySidoCodeAscSggCodeAsc()
        val grouped = regions.groupBy { it.sidoCode to it.sidoName }
        val sidoGroups =
            grouped.map { (key, items) ->
                SidoGroup(
                    sidoCode = key.first,
                    sidoName = key.second,
                    sggList = items.map { SggItem(sggCode = it.sggCode, sggName = it.sggName) },
                )
            }
        return RegionListResponse(regions = sidoGroups)
    }
}
