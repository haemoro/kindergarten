package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CenterTeacher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterTeacherRepository : JpaRepository<CenterTeacher, UUID> {
    fun findByCenterId(centerId: UUID): CenterTeacher?

    fun findAllByCenterIdIn(centerIds: List<UUID>): List<CenterTeacher>
}
