package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CenterSafetyEducation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterSafetyEducationRepository : JpaRepository<CenterSafetyEducation, UUID> {
    fun findAllByCenterId(centerId: UUID): List<CenterSafetyEducation>

    fun findAllByCenterIdIn(centerIds: List<UUID>): List<CenterSafetyEducation>
}
