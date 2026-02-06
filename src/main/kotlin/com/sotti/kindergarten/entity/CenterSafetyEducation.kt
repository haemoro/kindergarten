package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center_safety_education")
class CenterSafetyEducation(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
    @Column(length = 50)
    var semester: String? = null,
    @Column(length = 50)
    var lifeSafety: String? = null,
    @Column(length = 50)
    var trafficSafety: String? = null,
    @Column(length = 50)
    var violencePrevention: String? = null,
    @Column(length = 50)
    var drugPrevention: String? = null,
    @Column(length = 50)
    var cyberPrevention: String? = null,
    @Column(length = 50)
    var disasterSafety: String? = null,
    @Column(length = 50)
    var occupationalSafety: String? = null,
    @Column(length = 50)
    var firstAid: String? = null,
    @Column(length = 50)
    var disclosureTiming: String? = null,
) : BaseEntity()
