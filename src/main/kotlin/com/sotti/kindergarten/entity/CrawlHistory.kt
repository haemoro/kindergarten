package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "crawl_history")
class CrawlHistory(
    @Column(nullable = false, length = 50)
    val source: String = "e-childschoolinfo",
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: CrawlStatus = CrawlStatus.RUNNING,
    @Column(columnDefinition = "TEXT")
    var errorMessage: String? = null,
    var itemCount: Int? = null,
    @Column(nullable = false)
    val startedAt: LocalDateTime = LocalDateTime.now(),
    var finishedAt: LocalDateTime? = null,
) : BaseEntity()

enum class CrawlStatus {
    RUNNING,
    SUCCESS,
    FAILED,
}
