package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center_bus")
class CenterBus(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
    @Column(length = 50)
    var busOperating: String? = null,
    var operatingBusCount: Int? = null,
    var registeredBusCount: Int? = null,
    var bus9Seat: Int? = null,
    var bus12Seat: Int? = null,
    var bus15Seat: Int? = null,
    @Column(length = 50)
    var disclosureTiming: String? = null,
) : BaseEntity()
