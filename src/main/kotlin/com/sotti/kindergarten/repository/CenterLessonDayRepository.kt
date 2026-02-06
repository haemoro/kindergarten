package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CenterLessonDay
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterLessonDayRepository : JpaRepository<CenterLessonDay, UUID> {
    fun findByCenterId(centerId: UUID): CenterLessonDay?

    fun findAllByCenterIdIn(centerIds: List<UUID>): List<CenterLessonDay>
}
