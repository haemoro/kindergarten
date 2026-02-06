package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center_insurance")
class CenterInsurance(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
    @Column(length = 200)
    var insuranceName: String? = null,
    @Column(length = 10)
    var targetYn: String? = null,
    @Column(length = 10)
    var enrolledYn: String? = null,
    @Column(length = 200)
    var company1: String? = null,
    @Column(length = 200)
    var company2: String? = null,
    @Column(length = 200)
    var company3: String? = null,
    @Column(length = 50)
    var disclosureTiming: String? = null,
) : BaseEntity()
