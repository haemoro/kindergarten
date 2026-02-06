package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center_after_school")
class CenterAfterSchool(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
    var independentClassCount: Int? = null,
    var afternoonClassCount: Int? = null,
    @Column(length = 200)
    var operatingHours: String? = null,
    var independentParticipants: Int? = null,
    var afternoonParticipants: Int? = null,
    var regularTeacherCount: Int? = null,
    var contractTeacherCount: Int? = null,
    var dedicatedStaffCount: Int? = null,
    @Column(length = 50)
    var disclosureTiming: String? = null,
) : BaseEntity()
