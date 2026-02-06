package com.sotti.kindergarten.service

import com.sotti.kindergarten.dto.AfterSchoolInfo
import com.sotti.kindergarten.dto.BuildingInfo
import com.sotti.kindergarten.dto.BusInfo
import com.sotti.kindergarten.dto.CenterCompareRequest
import com.sotti.kindergarten.dto.CenterCompareResponse
import com.sotti.kindergarten.dto.CenterDetailResponse
import com.sotti.kindergarten.dto.CenterListResponse
import com.sotti.kindergarten.dto.ClassroomInfo
import com.sotti.kindergarten.dto.ComparisonItem
import com.sotti.kindergarten.dto.EnvironmentInfo
import com.sotti.kindergarten.dto.InsuranceInfo
import com.sotti.kindergarten.dto.LessonDayInfo
import com.sotti.kindergarten.dto.MealInfo
import com.sotti.kindergarten.dto.MutualAidInfo
import com.sotti.kindergarten.dto.PageResponse
import com.sotti.kindergarten.dto.SafetyCheckInfo
import com.sotti.kindergarten.dto.SafetyEducationInfo
import com.sotti.kindergarten.dto.TeacherInfo
import com.sotti.kindergarten.dto.YearOfWorkInfo
import com.sotti.kindergarten.entity.Center
import com.sotti.kindergarten.exception.CenterNotFoundException
import com.sotti.kindergarten.exception.InvalidCompareRequestException
import com.sotti.kindergarten.repository.CenterRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Service
@Transactional(readOnly = true)
class CenterService(
    private val centerRepository: CenterRepository,
) {
    fun listCenters(
        lat: Double?,
        lng: Double?,
        radiusKm: Double?,
        establishType: String?,
        query: String?,
        sort: String?,
        page: Int,
        size: Int,
    ): PageResponse<CenterListResponse> {
        val pageable = PageRequest.of(page, size, getSort(sort))

        val centersPage =
            if (lat != null && lng != null && radiusKm != null) {
                val radiusMeters = radiusKm * 1000
                centerRepository.findNearby(lat, lng, radiusMeters, establishType, query, pageable)
            } else {
                centerRepository.findAllWithFilters(establishType, query, pageable)
            }

        val content =
            centersPage.content.map { center ->
                val location = center.location
                val distance =
                    if (lat != null && lng != null && location != null) {
                        calculateDistance(lat, lng, location.y, location.x)
                    } else {
                        null
                    }

                toCenterListResponse(center, distance)
            }

        return PageResponse(
            content = content,
            page = centersPage.number,
            size = centersPage.size,
            totalElements = centersPage.totalElements,
            totalPages = centersPage.totalPages,
        )
    }

    fun getCenterDetail(id: UUID): CenterDetailResponse {
        val center = centerRepository.findById(id).orElseThrow { CenterNotFoundException(id) }
        return toCenterDetailResponse(center)
    }

    fun compareCenters(request: CenterCompareRequest): CenterCompareResponse {
        if (request.centerIds.size !in 2..4) {
            throw InvalidCompareRequestException()
        }

        val centers = centerRepository.findAllById(request.centerIds)
        if (centers.size != request.centerIds.size) {
            val foundIds = centers.map { it.id }.toSet()
            val missingId = request.centerIds.firstOrNull { it !in foundIds }
            throw CenterNotFoundException(missingId ?: request.centerIds.first())
        }

        val comparisons =
            centers.map { center ->
                val location = center.location
                val distance =
                    if (request.lat != null && request.lng != null && location != null) {
                        calculateDistance(request.lat, request.lng, location.y, location.x)
                    } else {
                        null
                    }

                toComparisonItem(center, distance)
            }

        return CenterCompareResponse(centers = comparisons)
    }

    private fun getSort(sort: String?): Sort =
        when (sort) {
            "name" -> Sort.by("name").ascending()
            "updated" -> Sort.by("updatedAt").descending()
            else -> Sort.by("updatedAt").descending()
        }

    private fun toCenterListResponse(
        center: Center,
        distanceKm: Double?,
    ): CenterListResponse {
        val currentEnrollment = calculateCurrentEnrollment(center)
        val totalClassCount = calculateTotalClassCount(center)

        return CenterListResponse(
            id = center.id!!,
            name = center.name,
            establishType = center.establishType,
            address = center.address,
            phone = center.phone,
            lat = center.location?.y,
            lng = center.location?.x,
            distanceKm = distanceKm,
            capacity = center.totalCapacity,
            currentEnrollment = currentEnrollment,
            totalClassCount = totalClassCount,
            mealProvided = center.meal?.mealOperationType != null,
            busAvailable = center.bus?.busOperating?.equals("Y", ignoreCase = true),
            extendedCare = center.afterSchool != null,
        )
    }

    private fun toCenterDetailResponse(center: Center): CenterDetailResponse =
        CenterDetailResponse(
            id = center.id!!,
            name = center.name,
            establishType = center.establishType,
            address = center.address,
            phone = center.phone,
            lat = center.location?.y,
            lng = center.location?.x,
            distanceKm = null,
            capacity = center.totalCapacity,
            currentEnrollment = calculateCurrentEnrollment(center),
            totalClassCount = calculateTotalClassCount(center),
            operatingHours = center.operatingHours,
            homepage = center.homepage,
            fax = center.fax,
            representativeName = center.representativeName,
            directorName = center.directorName,
            establishDate = parseDate(center.establishDate),
            openDate = parseDate(center.openDate),
            disclosureTiming = center.disclosureTiming,
            classCountByAge =
                mapOf(
                    "age3" to center.classCount3,
                    "age4" to center.classCount4,
                    "age5" to center.classCount5,
                    "mixed" to center.mixedClassCount,
                    "special" to center.specialClassCount,
                ),
            capacityByAge =
                mapOf(
                    "age3" to center.capacity3,
                    "age4" to center.capacity4,
                    "age5" to center.capacity5,
                    "mixed" to center.mixedCapacity,
                    "special" to center.specialCapacity,
                ),
            enrollmentByAge =
                mapOf(
                    "age3" to center.enrollment3,
                    "age4" to center.enrollment4,
                    "age5" to center.enrollment5,
                    "mixed" to center.mixedEnrollment,
                    "special" to center.specialEnrollment,
                ),
            building =
                center.building?.let {
                    BuildingInfo(
                        archYear = it.archYear?.toIntOrNull(),
                        floorCount = it.floorCount,
                        buildingArea = it.buildingArea,
                        totalLandArea = it.totalLandArea,
                    )
                },
            classroom =
                center.classroom?.let {
                    ClassroomInfo(
                        classroomCount = it.classroomCount,
                        classroomArea = it.classroomArea,
                        playgroundArea = it.playgroundArea,
                        healthArea = it.healthArea,
                        kitchenArea = it.kitchenArea,
                        otherArea = it.otherArea,
                    )
                },
            teacher =
                center.teacher?.let {
                    TeacherInfo(
                        directorCount = it.directorCount,
                        headTeacherCount = it.leadTeacherCount,
                        assistantTeacherCount = it.generalTeacherCount,
                        nurseCount = it.healthTeacherCount,
                        nutritionistCount = it.nutritionTeacherCount,
                        cookCount = it.staffCount,
                        teacherQualified = it.grade1QualCount,
                        teacherUnderQualified = it.grade2QualCount,
                        teacherNonQualified = it.assistantQualCount,
                        eduCourseQualified = it.masterQualCount,
                        eduCourseUnderQualified = null,
                        eduCourseNonQualified = null,
                        childCareQualified = null,
                        childCareUnderQualified = null,
                        childCareNonQualified = null,
                    )
                },
            lessonDay =
                center.lessonDay?.let {
                    LessonDayInfo(
                        lessonDaysAge3 = it.lessonDays3,
                        lessonDaysAge4 = it.lessonDays4,
                        lessonDaysAge5 = it.lessonDays5,
                        lessonDaysMixed = it.mixedLessonDays,
                        belowLegalDays = it.belowLegalDays,
                    )
                },
            meal =
                center.meal?.let {
                    MealInfo(
                        mealOperationType = it.mealOperationType,
                        consignmentCompany = it.consignmentCompany,
                        mealChildren = it.mealChildren,
                        cookCount = it.cookCount,
                        lunchProvided = null,
                        dinnerProvided = null,
                        mealCost = null,
                        mealCostReason = null,
                    )
                },
            bus =
                center.bus?.let {
                    BusInfo(
                        busOperating = it.busOperating,
                        operatingCount = it.operatingBusCount,
                        registeredCount = it.registeredBusCount,
                        seat9Count = it.bus9Seat,
                        seat12Count = it.bus12Seat,
                        seat24Count = null,
                        seat35Count = null,
                        seat45Count = null,
                    )
                },
            yearOfWork =
                center.yearOfWork?.let {
                    YearOfWorkInfo(
                        teachersUnder1Year = it.under1Year,
                        teachers1To2Years = it.between1And2Years,
                        teachers2To3Years = null,
                        teachers3To4Years = null,
                        teachers4To5Years = it.between4And6Years,
                        teachers5To6Years = null,
                        teachers6PlusYears = it.over6Years,
                    )
                },
            environment =
                center.environment?.let {
                    EnvironmentInfo(
                        airQualityCheck = it.airQualityCheckResult,
                        disinfectionCheck = it.regularDisinfectionResult,
                        waterQualityCheck = it.groundwaterTestResult,
                        dustMeasurement = it.dustCheckResult,
                        lightMeasurement = it.lightCheckResult,
                    )
                },
            safetyCheck =
                center.safetyCheck?.let {
                    SafetyCheckInfo(
                        fireInsuranceCheck = it.fireSafetyYn,
                        gasCheck = it.gasCheckYn,
                        electricCheck = it.electricCheckYn,
                        playgroundCheck = it.playgroundCheckYn,
                        cctvInstalled = it.cctvInstalled,
                        cctvTotal = it.cctvTotal,
                    )
                },
            safetyEducation =
                center.safetyEducations
                    .map {
                        SafetyEducationInfo(
                            semester = it.semester,
                            trafficSafety = it.trafficSafety?.toIntOrNull(),
                            preventionAbuse = null,
                            kidnappingPrevention = null,
                            sexualAbusePrevention = null,
                            disasterSafety = it.disasterSafety?.toIntOrNull(),
                            violencePrevention = it.violencePrevention?.toIntOrNull(),
                            drugPrevention = it.drugPrevention?.toIntOrNull(),
                            firstAid = it.firstAid?.toIntOrNull(),
                        )
                    }.ifEmpty { null },
            mutualAid =
                center.mutualAid?.let {
                    MutualAidInfo(
                        schoolSafetyEnrolled = it.schoolSafetyEnrolled,
                        educationFacilityEnrolled = it.educationFacilityEnrolled,
                    )
                },
            insurance =
                center.insurances
                    .map {
                        val companies = listOfNotNull(it.company1, it.company2, it.company3).joinToString(", ")
                        InsuranceInfo(
                            insuranceName = it.insuranceName,
                            enrolled = it.enrolledYn,
                            companies = companies.ifEmpty { null },
                        )
                    }.ifEmpty { null },
            afterSchool =
                center.afterSchool?.let {
                    AfterSchoolInfo(
                        classCountAge3 = it.independentClassCount,
                        classCountAge4 = it.afternoonClassCount,
                        classCountAge5 = null,
                        classCountMixed = null,
                        participantsAge3 = it.independentParticipants,
                        participantsAge4 = it.afternoonParticipants,
                        participantsAge5 = null,
                        participantsMixed = null,
                        teachersAge3 = it.regularTeacherCount,
                        teachersAge4 = it.contractTeacherCount,
                        teachersAge5 = it.dedicatedStaffCount,
                        teachersMixed = null,
                    )
                },
            sourceUpdatedAt = center.sourceUpdatedAt,
        )

    private fun toComparisonItem(
        center: Center,
        distanceKm: Double?,
    ): ComparisonItem {
        val teacherCount =
            center.teacher?.let {
                (it.directorCount ?: 0) +
                    (it.viceDirectorCount ?: 0) +
                    (it.masterTeacherCount ?: 0) +
                    (it.leadTeacherCount ?: 0) +
                    (it.generalTeacherCount ?: 0) +
                    (it.specialTeacherCount ?: 0)
            }

        return ComparisonItem(
            id = center.id!!,
            name = center.name,
            establishType = center.establishType,
            address = center.address,
            distanceKm = distanceKm,
            capacity = center.totalCapacity,
            currentEnrollment = calculateCurrentEnrollment(center),
            teacherCount = teacherCount,
            classCount = calculateTotalClassCount(center),
            mealProvided = center.meal?.mealOperationType != null,
            busAvailable = center.bus?.busOperating?.equals("Y", ignoreCase = true),
            extendedCare = center.afterSchool != null,
            buildingArea = center.building?.buildingArea,
            classroomArea = center.classroom?.classroomArea,
            cctvInstalled = center.safetyCheck?.cctvInstalled?.equals("Y", ignoreCase = true),
            cctvTotal = center.safetyCheck?.cctvTotal,
        )
    }

    private fun calculateCurrentEnrollment(center: Center): Int? {
        val enrollments =
            listOf(
                center.enrollment3,
                center.enrollment4,
                center.enrollment5,
                center.mixedEnrollment,
                center.specialEnrollment,
            ).filterNotNull()

        return if (enrollments.isEmpty()) null else enrollments.sum()
    }

    private fun calculateTotalClassCount(center: Center): Int? {
        val counts =
            listOf(
                center.classCount3,
                center.classCount4,
                center.classCount5,
                center.mixedClassCount,
                center.specialClassCount,
            ).filterNotNull()

        return if (counts.isEmpty()) null else counts.sum()
    }

    private fun parseDate(dateString: String?): LocalDate? =
        try {
            dateString?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
        } catch (e: Exception) {
            null
        }

    private fun calculateDistance(
        lat1: Double,
        lng1: Double,
        lat2: Double,
        lng2: Double,
    ): Double {
        val earthRadiusKm = 6371.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)

        val a =
            sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLng / 2) * sin(dLng / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadiusKm * c
    }
}
