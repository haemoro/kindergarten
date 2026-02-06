package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CenterBus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterBusRepository : JpaRepository<CenterBus, UUID> {
    fun findByCenterId(centerId: UUID): CenterBus?

    fun findAllByCenterIdIn(centerIds: List<UUID>): List<CenterBus>
}
