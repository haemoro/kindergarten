package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center_building")
class CenterBuilding(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
    @Column(length = 50)
    var archYear: String? = null,
    var floorCount: Int? = null,
    var buildingArea: Double? = null,
    var totalLandArea: Double? = null,
    @Column(length = 50)
    var disclosureTiming: String? = null,
) : BaseEntity()
