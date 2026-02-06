package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center_year_of_work")
class CenterYearOfWork(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
    var under1Year: Int? = null,
    var between1And2Years: Int? = null,
    var between2And4Years: Int? = null,
    var between4And6Years: Int? = null,
    var over6Years: Int? = null,
    @Column(length = 50)
    var disclosureTiming: String? = null,
) : BaseEntity()
