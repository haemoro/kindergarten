package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CenterAfterSchool
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterAfterSchoolRepository : JpaRepository<CenterAfterSchool, UUID> {
    fun findByCenterId(centerId: UUID): CenterAfterSchool?

    fun findAllByCenterIdIn(centerIds: List<UUID>): List<CenterAfterSchool>
}
