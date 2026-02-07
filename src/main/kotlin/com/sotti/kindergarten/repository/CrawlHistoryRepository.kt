package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.CrawlHistory
import com.sotti.kindergarten.entity.CrawlStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CrawlHistoryRepository : JpaRepository<CrawlHistory, UUID> {
    fun findAllByOrderByStartedAtDesc(pageable: Pageable): Page<CrawlHistory>

    fun existsByStatus(status: CrawlStatus): Boolean
}
