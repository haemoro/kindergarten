package com.sotti.kindergarten.dto

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class CenterDetailResponse(
    // Basic info
    val id: UUID,
    val name: String,
    val establishType: String?,
    val address: String?,
    val phone: String?,
    val lat: Double?,
    val lng: Double?,
    val distanceKm: Double?,
    val capacity: Int?,
    val currentEnrollment: Int?,
    val totalClassCount: Int?,
    val operatingHours: String?,
    val homepage: String?,
    val fax: String?,
    val representativeName: String?,
    val directorName: String?,
    val establishDate: LocalDate?,
    val openDate: LocalDate?,
    val disclosureTiming: String?,
    // Age-specific breakdown
    val classCountByAge: Map<String, Int?>?,
    val capacityByAge: Map<String, Int?>?,
    val enrollmentByAge: Map<String, Int?>?,
    // Related data
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
    val sourceUpdatedAt: LocalDateTime?,
)

data class BuildingInfo(
    val archYear: Int?,
    val floorCount: Int?,
    val buildingArea: Double?,
    val totalLandArea: Double?,
)

data class ClassroomInfo(
    val classroomCount: Int?,
    val classroomArea: Double?,
    val playgroundArea: Double?,
    val healthArea: Double?,
    val kitchenArea: Double?,
    val otherArea: Double?,
)

data class TeacherInfo(
    val directorCount: Int?,
    val headTeacherCount: Int?,
    val assistantTeacherCount: Int?,
    val nurseCount: Int?,
    val nutritionistCount: Int?,
    val cookCount: Int?,
    val teacherQualified: Int?,
    val teacherUnderQualified: Int?,
    val teacherNonQualified: Int?,
    val eduCourseQualified: Int?,
    val eduCourseUnderQualified: Int?,
    val eduCourseNonQualified: Int?,
    val childCareQualified: Int?,
    val childCareUnderQualified: Int?,
    val childCareNonQualified: Int?,
)

data class LessonDayInfo(
    val lessonDaysAge3: Int?,
    val lessonDaysAge4: Int?,
    val lessonDaysAge5: Int?,
    val lessonDaysMixed: Int?,
    val belowLegalDays: String?,
)

data class MealInfo(
    val mealOperationType: String?,
    val consignmentCompany: String?,
    val mealChildren: Int?,
    val cookCount: Int?,
    val lunchProvided: String?,
    val dinnerProvided: String?,
    val mealCost: Int?,
    val mealCostReason: String?,
)

data class BusInfo(
    val busOperating: String?,
    val operatingCount: Int?,
    val registeredCount: Int?,
    val seat9Count: Int?,
    val seat12Count: Int?,
    val seat24Count: Int?,
    val seat35Count: Int?,
    val seat45Count: Int?,
)

data class YearOfWorkInfo(
    val teachersUnder1Year: Int?,
    val teachers1To2Years: Int?,
    val teachers2To3Years: Int?,
    val teachers3To4Years: Int?,
    val teachers4To5Years: Int?,
    val teachers5To6Years: Int?,
    val teachers6PlusYears: Int?,
)

data class EnvironmentInfo(
    val airQualityCheck: String?,
    val disinfectionCheck: String?,
    val waterQualityCheck: String?,
    val dustMeasurement: String?,
    val lightMeasurement: String?,
)

data class SafetyCheckInfo(
    val fireInsuranceCheck: String?,
    val gasCheck: String?,
    val electricCheck: String?,
    val playgroundCheck: String?,
    val cctvInstalled: String?,
    val cctvTotal: Int?,
)

data class SafetyEducationInfo(
    val semester: String?,
    val trafficSafety: Int?,
    val preventionAbuse: Int?,
    val kidnappingPrevention: Int?,
    val sexualAbusePrevention: Int?,
    val disasterSafety: Int?,
    val violencePrevention: Int?,
    val drugPrevention: Int?,
    val firstAid: Int?,
)

data class MutualAidInfo(
    val schoolSafetyEnrolled: String?,
    val educationFacilityEnrolled: String?,
)

data class InsuranceInfo(
    val insuranceName: String?,
    val enrolled: String?,
    val companies: String?,
)

data class AfterSchoolInfo(
    val classCountAge3: Int?,
    val classCountAge4: Int?,
    val classCountAge5: Int?,
    val classCountMixed: Int?,
    val participantsAge3: Int?,
    val participantsAge4: Int?,
    val participantsAge5: Int?,
    val participantsMixed: Int?,
    val teachersAge3: Int?,
    val teachersAge4: Int?,
    val teachersAge5: Int?,
    val teachersMixed: Int?,
)
