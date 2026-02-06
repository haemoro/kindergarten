package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.Center
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterRepository : JpaRepository<Center, UUID> {
    fun findByKinderCode(kinderCode: String): Center?

    @Query(
        """
        SELECT c.*,
               ST_Distance(c.location, ST_MakePoint(:lng, :lat)::geography) as distance
        FROM center c
        WHERE ST_DWithin(c.location, ST_MakePoint(:lng, :lat)::geography, :radiusMeters)
        AND (:establishType IS NULL OR c.establish_type = :establishType)
        AND (:name IS NULL OR c.name LIKE CONCAT('%', :name, '%'))
        ORDER BY distance ASC
        """,
        nativeQuery = true,
    )
    fun findNearby(
        @Param("lat") lat: Double,
        @Param("lng") lng: Double,
        @Param("radiusMeters") radiusMeters: Double,
        @Param("establishType") establishType: String?,
        @Param("name") name: String?,
        pageable: Pageable,
    ): Page<Center>

    @Query(
        """
        SELECT c FROM Center c
        WHERE (:establishType IS NULL OR c.establishType = :establishType)
        AND (:name IS NULL OR c.name LIKE CONCAT('%', :name, '%'))
        ORDER BY c.updatedAt DESC
        """,
    )
    fun findAllWithFilters(
        @Param("establishType") establishType: String?,
        @Param("name") name: String?,
        pageable: Pageable,
    ): Page<Center>

    fun findAllByKinderCodeIn(kinderCodes: List<String>): List<Center>
}
