package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center_safety_check")
class CenterSafetyCheck(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
    @Column(length = 10)
    var fireEvacuationYn: String? = null,
    @Column(length = 50)
    var fireEvacuationDate: String? = null,
    @Column(length = 10)
    var gasCheckYn: String? = null,
    @Column(length = 50)
    var gasCheckDate: String? = null,
    @Column(length = 10)
    var fireSafetyYn: String? = null,
    @Column(length = 50)
    var fireSafetyDate: String? = null,
    @Column(length = 10)
    var electricCheckYn: String? = null,
    @Column(length = 50)
    var electricCheckDate: String? = null,
    @Column(length = 10)
    var playgroundCheckYn: String? = null,
    @Column(length = 50)
    var playgroundCheckDate: String? = null,
    @Column(length = 50)
    var playgroundCheckResult: String? = null,
    @Column(length = 10)
    var cctvInstalled: String? = null,
    var cctvTotal: Int? = null,
    var cctvIndoor: Int? = null,
    var cctvOutdoor: Int? = null,
    @Column(length = 50)
    var disclosureTiming: String? = null,
) : BaseEntity()
