package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CenterMeal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterMealRepository : JpaRepository<CenterMeal, UUID> {
    fun findByCenterId(centerId: UUID): CenterMeal?

    fun findAllByCenterIdIn(centerIds: List<UUID>): List<CenterMeal>
}
