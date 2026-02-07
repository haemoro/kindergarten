package com.sotti.kindergarten.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.sotti.kindergarten.entity.Center
import com.sotti.kindergarten.entity.QCenter
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.util.UUID

class CenterRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val entityManager: EntityManager,
) : CenterRepositoryCustom {
    private val qCenter = QCenter.center

    override fun findAllWithFilters(
        establishType: String?,
        name: String?,
        pageable: Pageable,
    ): Page<Center> {
        val builder = BooleanBuilder()

        establishType?.let {
            builder.and(qCenter.establishType.eq(it))
        }
        name?.let {
            builder.and(qCenter.name.contains(it))
        }

        val ids =
            jpaQueryFactory
                .select(qCenter.id)
                .from(qCenter)
                .where(builder)
                .orderBy(qCenter.updatedAt.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        if (ids.isEmpty()) {
            return PageableExecutionUtils.getPage(emptyList(), pageable) { 0L }
        }

        val content =
            jpaQueryFactory
                .selectFrom(qCenter)
                .fetchAllOneToOne()
                .where(qCenter.id.`in`(ids))
                .orderBy(qCenter.updatedAt.desc())
                .fetch()

        val countQuery = {
            jpaQueryFactory
                .select(qCenter.count())
                .from(qCenter)
                .where(builder)
                .fetchOne() ?: 0L
        }

        return PageableExecutionUtils.getPage(content, pageable, countQuery)
    }

    override fun findNearby(
        lat: Double,
        lng: Double,
        radiusMeters: Double,
        establishType: String?,
        name: String?,
        pageable: Pageable,
    ): Page<Center> {
        val idSql =
            buildString {
                append(
                    """
                    SELECT c.id
                    FROM center c
                    WHERE ST_DWithin(c.location, ST_MakePoint(:lng, :lat)::geography, :radiusMeters)
                    """.trimIndent(),
                )
                if (establishType != null) {
                    append(" AND c.establish_type = :establishType")
                }
                if (name != null) {
                    append(" AND c.name LIKE :name")
                }
                append(" ORDER BY ST_Distance(c.location, ST_MakePoint(:lng, :lat)::geography) ASC")
            }

        val idQuery =
            entityManager.createNativeQuery(idSql).apply {
                setParameter("lat", lat)
                setParameter("lng", lng)
                setParameter("radiusMeters", radiusMeters)
                if (establishType != null) setParameter("establishType", establishType)
                if (name != null) setParameter("name", "%$name%")
                firstResult = pageable.offset.toInt()
                maxResults = pageable.pageSize
            }

        @Suppress("UNCHECKED_CAST")
        val ids = (idQuery.resultList as List<UUID>)

        if (ids.isEmpty()) {
            return PageableExecutionUtils.getPage(emptyList(), pageable) { 0L }
        }

        val centers =
            jpaQueryFactory
                .selectFrom(qCenter)
                .fetchAllOneToOne()
                .where(qCenter.id.`in`(ids))
                .fetch()

        val idOrder = ids.withIndex().associate { (index, id) -> id to index }
        val content = centers.sortedBy { idOrder[it.id] }

        val countSql =
            buildString {
                append(
                    """
                    SELECT COUNT(*)
                    FROM center c
                    WHERE ST_DWithin(c.location, ST_MakePoint(:lng, :lat)::geography, :radiusMeters)
                    """.trimIndent(),
                )
                if (establishType != null) {
                    append(" AND c.establish_type = :establishType")
                }
                if (name != null) {
                    append(" AND c.name LIKE :name")
                }
            }

        val countQuery = {
            val cq =
                entityManager.createNativeQuery(countSql).apply {
                    setParameter("lat", lat)
                    setParameter("lng", lng)
                    setParameter("radiusMeters", radiusMeters)
                    if (establishType != null) setParameter("establishType", establishType)
                    if (name != null) setParameter("name", "%$name%")
                }
            (cq.singleResult as Number).toLong()
        }

        return PageableExecutionUtils.getPage(content, pageable, countQuery)
    }

    private fun JPAQuery<Center>.fetchAllOneToOne(): JPAQuery<Center> =
        this
            .leftJoin(qCenter.building)
            .fetchJoin()
            .leftJoin(qCenter.classroom)
            .fetchJoin()
            .leftJoin(qCenter.teacher)
            .fetchJoin()
            .leftJoin(qCenter.lessonDay)
            .fetchJoin()
            .leftJoin(qCenter.meal)
            .fetchJoin()
            .leftJoin(qCenter.bus)
            .fetchJoin()
            .leftJoin(qCenter.yearOfWork)
            .fetchJoin()
            .leftJoin(qCenter.environment)
            .fetchJoin()
            .leftJoin(qCenter.safetyCheck)
            .fetchJoin()
            .leftJoin(qCenter.mutualAid)
            .fetchJoin()
            .leftJoin(qCenter.afterSchool)
            .fetchJoin()
}
