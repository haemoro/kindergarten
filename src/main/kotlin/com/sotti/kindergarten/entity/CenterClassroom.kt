package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center_classroom")
class CenterClassroom(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
    var classroomCount: Int? = null,
    var classroomArea: Double? = null,
    var playgroundArea: Double? = null,
    var healthArea: Double? = null,
    var kitchenArea: Double? = null,
    var otherArea: Double? = null,
    @Column(length = 50)
    var disclosureTiming: String? = null,
) : BaseEntity()
