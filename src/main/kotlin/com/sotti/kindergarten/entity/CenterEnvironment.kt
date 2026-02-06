package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center_environment")
class CenterEnvironment(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
    @Column(length = 50)
    var airQualityCheckDate: String? = null,
    @Column(length = 50)
    var airQualityCheckResult: String? = null,
    @Column(length = 50)
    var regularDisinfectionRequired: String? = null,
    @Column(length = 50)
    var regularDisinfectionDate: String? = null,
    @Column(length = 50)
    var regularDisinfectionResult: String? = null,
    @Column(length = 50)
    var waterType01: String? = null,
    @Column(length = 50)
    var waterType02: String? = null,
    @Column(length = 50)
    var waterType03: String? = null,
    @Column(length = 50)
    var waterType04: String? = null,
    @Column(length = 50)
    var groundwaterTestRequired: String? = null,
    @Column(length = 50)
    var groundwaterTestDate: String? = null,
    @Column(length = 50)
    var groundwaterTestResult: String? = null,
    @Column(length = 50)
    var dustCheckDate: String? = null,
    @Column(length = 50)
    var dustCheckResult: String? = null,
    @Column(length = 50)
    var lightCheckDate: String? = null,
    @Column(length = 50)
    var lightCheckResult: String? = null,
    @Column(length = 50)
    var disclosureTiming: String? = null,
) : BaseEntity()
