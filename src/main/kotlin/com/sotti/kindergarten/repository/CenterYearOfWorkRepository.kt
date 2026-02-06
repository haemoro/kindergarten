package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CenterYearOfWork
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterYearOfWorkRepository : JpaRepository<CenterYearOfWork, UUID> {
    fun findByCenterId(centerId: UUID): CenterYearOfWork?

    fun findAllByCenterIdIn(centerIds: List<UUID>): List<CenterYearOfWork>
}
