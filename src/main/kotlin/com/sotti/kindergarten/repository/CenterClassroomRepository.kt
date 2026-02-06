package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CenterClassroom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterClassroomRepository : JpaRepository<CenterClassroom, UUID> {
    fun findByCenterId(centerId: UUID): CenterClassroom?

    fun findAllByCenterIdIn(centerIds: List<UUID>): List<CenterClassroom>
}
