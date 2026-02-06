package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center_teacher")
class CenterTeacher(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
    // Teacher counts
    var directorCount: Int? = null,
    var viceDirectorCount: Int? = null,
    var masterTeacherCount: Int? = null,
    var leadTeacherCount: Int? = null,
    var generalTeacherCount: Int? = null,
    var specialTeacherCount: Int? = null,
    var healthTeacherCount: Int? = null,
    var nutritionTeacherCount: Int? = null,
    var contractTeacherCount: Int? = null,
    var staffCount: Int? = null,
    // Qualifications
    var masterQualCount: Int? = null,
    var grade1QualCount: Int? = null,
    var grade2QualCount: Int? = null,
    var assistantQualCount: Int? = null,
    var specialSchoolQualCount: Int? = null,
    var healthQualCount: Int? = null,
    var nutritionQualCount: Int? = null,
    @Column(length = 50)
    var disclosureTiming: String? = null,
) : BaseEntity()
