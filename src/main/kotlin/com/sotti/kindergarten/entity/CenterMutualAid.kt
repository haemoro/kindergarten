package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center_mutual_aid")
class CenterMutualAid(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
    @Column(length = 50)
    var schoolSafetyTarget: String? = null,
    @Column(length = 50)
    var schoolSafetyEnrolled: String? = null,
    @Column(length = 50)
    var educationFacilityTarget: String? = null,
    @Column(length = 50)
    var educationFacilityEnrolled: String? = null,
    @Column(length = 50)
    var disclosureTiming: String? = null,
) : BaseEntity()
