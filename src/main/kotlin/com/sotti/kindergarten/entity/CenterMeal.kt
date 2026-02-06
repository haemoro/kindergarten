package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "center_meal")
class CenterMeal(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
    @Column(length = 100)
    var mealOperationType: String? = null,
    @Column(length = 200)
    var consignmentCompany: String? = null,
    var totalChildren: Int? = null,
    var mealChildren: Int? = null,
    @Column(length = 50)
    var nutritionTeacherAssigned: String? = null,
    var singleNutritionTeacherCount: Int? = null,
    var jointNutritionTeacherCount: Int? = null,
    @Column(length = 200)
    var jointInstitutionName: String? = null,
    var cookCount: Int? = null,
    var cookingStaffCount: Int? = null,
    @Column(length = 50)
    var massKitchenRegistered: String? = null,
    @Column(length = 50)
    var disclosureTiming: String? = null,
) : BaseEntity()
