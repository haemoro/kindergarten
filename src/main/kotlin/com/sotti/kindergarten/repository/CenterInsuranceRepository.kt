package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CenterInsurance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterInsuranceRepository : JpaRepository<CenterInsurance, UUID> {
    fun findAllByCenterId(centerId: UUID): List<CenterInsurance>

    fun findAllByCenterIdIn(centerIds: List<UUID>): List<CenterInsurance>
}
