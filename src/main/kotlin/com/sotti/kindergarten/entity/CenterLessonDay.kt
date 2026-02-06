package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center_lesson_day")
class CenterLessonDay(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
    var lessonDays3: Int? = null,
    var lessonDays4: Int? = null,
    var lessonDays5: Int? = null,
    var mixedLessonDays: Int? = null,
    var specialLessonDays: Int? = null,
    var afterSchoolLessonDays: Int? = null,
    @Column(length = 50)
    var belowLegalDays: String? = null,
    @Column(length = 50)
    var disclosureTiming: String? = null,
) : BaseEntity()
