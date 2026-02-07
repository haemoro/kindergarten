package com.sotti.kindergarten.dto.admin

import com.sotti.kindergarten.dto.AfterSchoolInfo
import com.sotti.kindergarten.dto.BuildingInfo
import com.sotti.kindergarten.dto.BusInfo
import com.sotti.kindergarten.dto.ClassroomInfo
import com.sotti.kindergarten.dto.EnvironmentInfo
import com.sotti.kindergarten.dto.InsuranceInfo
import com.sotti.kindergarten.dto.LessonDayInfo
import com.sotti.kindergarten.dto.MealInfo
import com.sotti.kindergarten.dto.MutualAidInfo
import com.sotti.kindergarten.dto.SafetyCheckInfo
import com.sotti.kindergarten.dto.SafetyEducationInfo
import com.sotti.kindergarten.dto.TeacherInfo
import com.sotti.kindergarten.dto.YearOfWorkInfo
import java.time.LocalDateTime
import java.util.UUID

data class AdminKindergartenDetailResponse(
    val id: UUID,
    val kinderCode: String,
    val name: String,
    val establishType: String?,
    val officEdu: String?,
    val subOfficeEdu: String?,
    val address: String?,
    val phone: String?,
    val homepage: String?,
    val operatingHours: String?,
    val representativeName: String?,
    val directorName: String?,
    val establishDate: String?,
    val openDate: String?,
    val isVerified: Boolean,
    val isActive: Boolean,
    val adminMemo: String?,
    val building: BuildingInfo?,
    val classroom: ClassroomInfo?,
    val teacher: TeacherInfo?,
    val lessonDay: LessonDayInfo?,
    val meal: MealInfo?,
    val bus: BusInfo?,
    val yearOfWork: YearOfWorkInfo?,
    val environment: EnvironmentInfo?,
    val safetyCheck: SafetyCheckInfo?,
    val safetyEducation: List<SafetyEducationInfo>?,
    val mutualAid: MutualAidInfo?,
    val insurance: List<InsuranceInfo>?,
    val afterSchool: AfterSchoolInfo?,
    val capacity: Int?,
    val currentEnrollment: Int?,
    val totalClassCount: Int?,
    val classCountByAge: Map<String, Int?>?,
    val capacityByAge: Map<String, Int?>?,
    val enrollmentByAge: Map<String, Int?>?,
    val sourceUpdatedAt: LocalDateTime?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)
