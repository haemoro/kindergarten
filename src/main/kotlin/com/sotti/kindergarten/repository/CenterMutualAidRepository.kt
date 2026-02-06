package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CenterMutualAid
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterMutualAidRepository : JpaRepository<CenterMutualAid, UUID> {
    fun findByCenterId(centerId: UUID): CenterMutualAid?

    fun findAllByCenterIdIn(centerIds: List<UUID>): List<CenterMutualAid>
}
