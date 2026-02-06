package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CenterSafetyCheck
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterSafetyCheckRepository : JpaRepository<CenterSafetyCheck, UUID> {
    fun findByCenterId(centerId: UUID): CenterSafetyCheck?

    fun findAllByCenterIdIn(centerIds: List<UUID>): List<CenterSafetyCheck>
}
