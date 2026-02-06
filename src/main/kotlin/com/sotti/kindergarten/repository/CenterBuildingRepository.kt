package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CenterBuilding
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterBuildingRepository : JpaRepository<CenterBuilding, UUID> {
    fun findByCenterId(centerId: UUID): CenterBuilding?

    fun findAllByCenterIdIn(centerIds: List<UUID>): List<CenterBuilding>
}
