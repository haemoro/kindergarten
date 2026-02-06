package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CenterEnvironment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterEnvironmentRepository : JpaRepository<CenterEnvironment, UUID> {
    fun findByCenterId(centerId: UUID): CenterEnvironment?

    fun findAllByCenterIdIn(centerIds: List<UUID>): List<CenterEnvironment>
}
