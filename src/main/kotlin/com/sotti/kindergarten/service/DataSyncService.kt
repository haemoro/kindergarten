package com.sotti.kindergarten.service

import com.sotti.kindergarten.client.KindergartenApiClient
import com.sotti.kindergarten.entity.Center
import com.sotti.kindergarten.entity.CenterAfterSchool
import com.sotti.kindergarten.entity.CenterBuilding
import com.sotti.kindergarten.entity.CenterBus
import com.sotti.kindergarten.entity.CenterClassroom
import com.sotti.kindergarten.entity.CenterEnvironment
import com.sotti.kindergarten.entity.CenterInsurance
import com.sotti.kindergarten.entity.CenterLessonDay
import com.sotti.kindergarten.entity.CenterMeal
import com.sotti.kindergarten.entity.CenterMutualAid
import com.sotti.kindergarten.entity.CenterSafetyCheck
import com.sotti.kindergarten.entity.CenterSafetyEducation
import com.sotti.kindergarten.entity.CenterTeacher
import com.sotti.kindergarten.entity.CenterYearOfWork
import com.sotti.kindergarten.repository.CenterAfterSchoolRepository
import com.sotti.kindergarten.repository.CenterBuildingRepository
import com.sotti.kindergarten.repository.CenterBusRepository
import com.sotti.kindergarten.repository.CenterClassroomRepository
import com.sotti.kindergarten.repository.CenterEnvironmentRepository
import com.sotti.kindergarten.repository.CenterInsuranceRepository
import com.sotti.kindergarten.repository.CenterLessonDayRepository
import com.sotti.kindergarten.repository.CenterMealRepository
import com.sotti.kindergarten.repository.CenterMutualAidRepository
import com.sotti.kindergarten.repository.CenterRepository
import com.sotti.kindergarten.repository.CenterSafetyCheckRepository
import com.sotti.kindergarten.repository.CenterSafetyEducationRepository
import com.sotti.kindergarten.repository.CenterTeacherRepository
import com.sotti.kindergarten.repository.CenterYearOfWorkRepository
import com.sotti.kindergarten.repository.RegionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class DataSyncService(
    private val apiClient: KindergartenApiClient,
    private val regionRepository: RegionRepository,
    private val centerRepository: CenterRepository,
    private val buildingRepository: CenterBuildingRepository,
    private val classroomRepository: CenterClassroomRepository,
    private val teacherRepository: CenterTeacherRepository,
    private val lessonDayRepository: CenterLessonDayRepository,
    private val mealRepository: CenterMealRepository,
    private val busRepository: CenterBusRepository,
    private val yearOfWorkRepository: CenterYearOfWorkRepository,
    private val environmentRepository: CenterEnvironmentRepository,
    private val safetyCheckRepository: CenterSafetyCheckRepository,
    private val safetyEducationRepository: CenterSafetyEducationRepository,
    private val mutualAidRepository: CenterMutualAidRepository,
    private val insuranceRepository: CenterInsuranceRepository,
    private val afterSchoolRepository: CenterAfterSchoolRepository,
) {
    private val logger = LoggerFactory.getLogger(DataSyncService::class.java)
    private val geometryFactory = GeometryFactory(PrecisionModel(), 4326)

    companion object {
        private const val MAX_RETRY_ATTEMPTS = 3
    }

    @Scheduled(cron = "0 0 3 * * *")
    fun scheduledSync() {
        logger.info("Starting scheduled data sync at ${LocalDateTime.now()}")
        syncAllData()
        logger.info("Completed scheduled data sync at ${LocalDateTime.now()}")
    }

    fun syncAllData() {
        val regions = regionRepository.findAll()
        logger.info("Found ${regions.size} regions to sync")

        regions.forEach { region ->
            try {
                logger.info("Syncing data for ${region.sidoName} ${region.sggName} (sido=${region.sidoCode}, sgg=${region.sggCode})")
                syncRegion(region.sidoCode, region.sggCode)
            } catch (e: Exception) {
                logger.error("Failed to sync region ${region.sidoCode}-${region.sggCode}: ${e.message}", e)
            }
        }
    }

    fun syncSingleRegion(
        sidoCode: String,
        sggCode: String,
    ) {
        logger.info("Starting single region sync for sido=$sidoCode, sgg=$sggCode")
        syncRegion(sidoCode, sggCode)
        logger.info("Completed single region sync for sido=$sidoCode, sgg=$sggCode")
    }

    @Transactional
    fun syncRegion(
        sidoCode: String,
        sggCode: String,
    ) {
        runBlocking {
            syncBasicInfo(sidoCode, sggCode)
            syncBuilding(sidoCode, sggCode)
            syncClassArea(sidoCode, sggCode)
            syncTeacher(sidoCode, sggCode)
            syncLessonDay(sidoCode, sggCode)
            syncMeal(sidoCode, sggCode)
            syncBus(sidoCode, sggCode)
            syncYearOfWork(sidoCode, sggCode)
            syncEnvironment(sidoCode, sggCode)
            syncSafetyCheck(sidoCode, sggCode)
            syncSafetyEducation(sidoCode, sggCode)
            syncMutualAid(sidoCode, sggCode)
            syncInsurance(sidoCode, sggCode)
            syncAfterSchool(sidoCode, sggCode)
        }
    }

    private suspend fun syncBasicInfo(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("BasicInfo", sidoCode, sggCode) {
            val responses = apiClient.getAllBasicInfo(sidoCode, sggCode)
            val allInfos = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allInfos.size} basic info records for $sidoCode-$sggCode")

            allInfos.forEach { info ->
                info.kinderCode?.let { kinderCode ->
                    info.kindername?.let { name ->
                        val center =
                            centerRepository.findByKinderCode(kinderCode) ?: Center(
                                kinderCode = kinderCode,
                                name = name,
                            )

                        // Assuming entities will have var fields after fix
                        center.apply {
                            this.name = info.kindername ?: this.name
                            officEdu = info.officeedu
                            subOfficeEdu = info.subofficeedu
                            establishType = info.establish
                            representativeName = info.rppnname
                            directorName = info.ldgrname
                            establishDate = info.edate
                            openDate = info.odate
                            address = info.addr
                            phone = info.telno
                            fax = info.faxno
                            homepage = info.hpaddr
                            operatingHours = info.opertime
                            classCount3 = info.clcnt3?.toIntOrNull()
                            classCount4 = info.clcnt4?.toIntOrNull()
                            classCount5 = info.clcnt5?.toIntOrNull()
                            mixedClassCount = info.mixclcnt?.toIntOrNull()
                            specialClassCount = info.shclcnt?.toIntOrNull()
                            totalCapacity = info.prmstfcnt?.toIntOrNull()
                            capacity3 = info.ag3fpcnt?.toIntOrNull()
                            capacity4 = info.ag4fpcnt?.toIntOrNull()
                            capacity5 = info.ag5fpcnt?.toIntOrNull()
                            mixedCapacity = info.mixfpcnt?.toIntOrNull()
                            specialCapacity = info.spcnfpcnt?.toIntOrNull()
                            enrollment3 = info.ppcnt3?.toIntOrNull()
                            enrollment4 = info.ppcnt4?.toIntOrNull()
                            enrollment5 = info.ppcnt5?.toIntOrNull()
                            mixedEnrollment = info.mixppcnt?.toIntOrNull()
                            specialEnrollment = info.shppcnt?.toIntOrNull()
                            location = parseLocation(info.lttdcdnt, info.lngtcdnt)
                            disclosureTiming = info.pbnttmng
                            actingDirector = info.rpstYn
                            sourceUpdatedAt = LocalDateTime.now()
                            updatedAt = LocalDateTime.now()
                        }

                        centerRepository.save(center)
                    }
                }
            }
        }
    }

    private suspend fun syncBuilding(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("Building", sidoCode, sggCode) {
            val responses = apiClient.getAllBuilding(sidoCode, sggCode)
            val allBuildings = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allBuildings.size} building records for $sidoCode-$sggCode")

            allBuildings.forEach { building ->
                building.kinderCode?.let { kinderCode ->
                    centerRepository.findByKinderCode(kinderCode)?.let { center ->
                        center.id?.let { centerId ->
                            val entity =
                                buildingRepository.findByCenterId(centerId) ?: CenterBuilding(center = center)

                            entity.apply {
                                archYear = building.archyy
                                floorCount = building.floorcnt?.replace(Regex("[^0-9]"), "")?.toIntOrNull()
                                buildingArea = building.bldgprusarea?.replace(Regex("[^0-9.]"), "")?.toDoubleOrNull()
                                totalLandArea = building.grottar?.replace(Regex("[^0-9.]"), "")?.toDoubleOrNull()
                                disclosureTiming = building.pbnttmng
                                updatedAt = LocalDateTime.now()
                            }

                            buildingRepository.save(entity)
                        }
                    }
                }
            }
        }
    }

    private suspend fun syncClassArea(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("ClassArea", sidoCode, sggCode) {
            val responses = apiClient.getAllClassArea(sidoCode, sggCode)
            val allAreas = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allAreas.size} class area records for $sidoCode-$sggCode")

            allAreas.forEach { area ->
                area.kinderCode?.let { kinderCode ->
                    centerRepository.findByKinderCode(kinderCode)?.let { center ->
                        center.id?.let { centerId ->
                            val entity =
                                classroomRepository.findByCenterId(centerId) ?: CenterClassroom(center = center)

                            entity.apply {
                                classroomCount = area.crcnt?.replace(Regex("[^0-9]"), "")?.toIntOrNull()
                                classroomArea = area.clsrarea?.replace(Regex("[^0-9.]"), "")?.toDoubleOrNull()
                                playgroundArea = area.phgrindrarea?.replace(Regex("[^0-9.]"), "")?.toDoubleOrNull()
                                healthArea = area.hlsparea?.replace(Regex("[^0-9.]"), "")?.toDoubleOrNull()
                                kitchenArea = area.ktchmssparea?.replace(Regex("[^0-9.]"), "")?.toDoubleOrNull()
                                otherArea = area.otsparea?.replace(Regex("[^0-9.]"), "")?.toDoubleOrNull()
                                disclosureTiming = area.pbnttmng
                                updatedAt = LocalDateTime.now()
                            }

                            classroomRepository.save(entity)
                        }
                    }
                }
            }
        }
    }

    private suspend fun syncTeacher(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("Teacher", sidoCode, sggCode) {
            val responses = apiClient.getAllTeacher(sidoCode, sggCode)
            val allTeachers = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allTeachers.size} teacher records for $sidoCode-$sggCode")

            allTeachers.forEach { teacher ->
                teacher.kinderCode?.let { kinderCode ->
                    centerRepository.findByKinderCode(kinderCode)?.let { center ->
                        center.id?.let { centerId ->
                            val entity =
                                teacherRepository.findByCenterId(centerId) ?: CenterTeacher(center = center)

                            entity.apply {
                                directorCount = teacher.drcnt?.toIntOrNull()
                                viceDirectorCount = teacher.adcnt?.toIntOrNull()
                                masterTeacherCount = teacher.hdst_thcnt?.toIntOrNull()
                                leadTeacherCount = teacher.asps_thcnt?.toIntOrNull()
                                generalTeacherCount = teacher.gnrl_thcnt?.toIntOrNull()
                                specialTeacherCount = teacher.spcn_thcnt?.toIntOrNull()
                                healthTeacherCount = teacher.ntcnt?.toIntOrNull()
                                nutritionTeacherCount = teacher.ntrt_thcnt?.toIntOrNull()
                                contractTeacherCount = teacher.shcnt_thcnt?.toIntOrNull()
                                staffCount = teacher.owcnt?.toIntOrNull()
                                masterQualCount = teacher.hdst_tchr_qacnt?.toIntOrNull()
                                grade1QualCount = teacher.rgth_gd1_qacnt?.toIntOrNull()
                                grade2QualCount = teacher.rgth_gd2_qacnt?.toIntOrNull()
                                assistantQualCount = teacher.asth_qacnt?.toIntOrNull()
                                specialSchoolQualCount = teacher.spsc_tchr_qacnt?.toIntOrNull()
                                healthQualCount = teacher.nth_qacnt?.toIntOrNull()
                                nutritionQualCount = teacher.ntth_qacnt?.toIntOrNull()
                                disclosureTiming = teacher.pbnttmng
                                updatedAt = LocalDateTime.now()
                            }

                            teacherRepository.save(entity)
                        }
                    }
                }
            }
        }
    }

    private suspend fun syncLessonDay(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("LessonDay", sidoCode, sggCode) {
            val responses = apiClient.getAllLessonDay(sidoCode, sggCode)
            val allLessonDays = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allLessonDays.size} lesson day records for $sidoCode-$sggCode")

            allLessonDays.forEach { lessonDay ->
                lessonDay.kinderCode?.let { kinderCode ->
                    centerRepository.findByKinderCode(kinderCode)?.let { center ->
                        center.id?.let { centerId ->
                            val entity =
                                lessonDayRepository.findByCenterId(centerId) ?: CenterLessonDay(center = center)

                            entity.apply {
                                lessonDays3 = lessonDay.ag3_lsn_dcnt?.toIntOrNull()
                                lessonDays4 = lessonDay.ag4_lsn_dcnt?.toIntOrNull()
                                lessonDays5 = lessonDay.ag5_lsn_dcnt?.toIntOrNull()
                                mixedLessonDays = lessonDay.mix_age_lsn_dcnt?.toIntOrNull()
                                specialLessonDays = lessonDay.spcl_lsn_dcnt?.toIntOrNull()
                                afterSchoolLessonDays = lessonDay.afsc_pros_lsn_dcnt?.toIntOrNull()
                                belowLegalDays = lessonDay.ldnum_blw_yn
                                disclosureTiming = lessonDay.pbnttmng
                                updatedAt = LocalDateTime.now()
                            }

                            lessonDayRepository.save(entity)
                        }
                    }
                }
            }
        }
    }

    private suspend fun syncMeal(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("Meal", sidoCode, sggCode) {
            val responses = apiClient.getAllMeal(sidoCode, sggCode)
            val allMeals = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allMeals.size} meal records for $sidoCode-$sggCode")

            allMeals.forEach { meal ->
                meal.kinderCode?.let { kinderCode ->
                    centerRepository.findByKinderCode(kinderCode)?.let { center ->
                        center.id?.let { centerId ->
                            val entity =
                                mealRepository.findByCenterId(centerId) ?: CenterMeal(center = center)

                            entity.apply {
                                mealOperationType = meal.mlsr_oprn_way_tp_cd
                                consignmentCompany = meal.cons_ents_nm
                                totalChildren = meal.al_kpcnt?.toIntOrNull()
                                mealChildren = meal.mlsr_kpcnt?.toIntOrNull()
                                nutritionTeacherAssigned = meal.ntrt_tchr_agmt_yn
                                singleNutritionTeacherCount = meal.snge_agmt_ntrt_thcnt?.toIntOrNull()
                                jointNutritionTeacherCount = meal.cprt_agmt_ntrt_thcnt?.toIntOrNull()
                                jointInstitutionName = meal.cprt_agmt_itt_nm
                                cookCount = meal.ckcnt?.toIntOrNull()
                                cookingStaffCount = meal.cmcnt?.toIntOrNull()
                                massKitchenRegistered = meal.mas_mspl_dclr_yn
                                disclosureTiming = meal.pbnttmng
                                updatedAt = LocalDateTime.now()
                            }

                            mealRepository.save(entity)
                        }
                    }
                }
            }
        }
    }

    private suspend fun syncBus(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("Bus", sidoCode, sggCode) {
            val responses = apiClient.getAllBus(sidoCode, sggCode)
            val allBuses = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allBuses.size} bus records for $sidoCode-$sggCode")

            allBuses.forEach { bus ->
                bus.kinderCode?.let { kinderCode ->
                    centerRepository.findByKinderCode(kinderCode)?.let { center ->
                        center.id?.let { centerId ->
                            val entity =
                                busRepository.findByCenterId(centerId) ?: CenterBus(center = center)

                            entity.apply {
                                busOperating = bus.vhcl_oprn_yn
                                operatingBusCount = bus.opra_vhcnt?.toIntOrNull()
                                registeredBusCount = bus.dclr_vhcnt?.toIntOrNull()
                                bus9Seat = bus.psg9_dclr_vhcnt?.toIntOrNull()
                                bus12Seat = bus.psg12_dclr_vhcnt?.toIntOrNull()
                                bus15Seat = bus.psg15_dclr_vhcnt?.toIntOrNull()
                                disclosureTiming = bus.pbnttmng
                                updatedAt = LocalDateTime.now()
                            }

                            busRepository.save(entity)
                        }
                    }
                }
            }
        }
    }

    private suspend fun syncYearOfWork(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("YearOfWork", sidoCode, sggCode) {
            val responses = apiClient.getAllYearOfWork(sidoCode, sggCode)
            val allYearOfWorks = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allYearOfWorks.size} year of work records for $sidoCode-$sggCode")

            allYearOfWorks.forEach { yearOfWork ->
                yearOfWork.kinderCode?.let { kinderCode ->
                    centerRepository.findByKinderCode(kinderCode)?.let { center ->
                        center.id?.let { centerId ->
                            val entity =
                                yearOfWorkRepository.findByCenterId(centerId) ?: CenterYearOfWork(center = center)

                            entity.apply {
                                under1Year = yearOfWork.yy1_undr_thcnt?.toIntOrNull()
                                between1And2Years = yearOfWork.yy1_abv_yy2_undr_thcnt?.toIntOrNull()
                                between2And4Years = yearOfWork.yy2_abv_yy4_undr_thcnt?.toIntOrNull()
                                between4And6Years = yearOfWork.yy4_abv_yy6_undr_thcnt?.toIntOrNull()
                                over6Years = yearOfWork.yy6_abv_thcnt?.toIntOrNull()
                                disclosureTiming = yearOfWork.pbnttmng
                                updatedAt = LocalDateTime.now()
                            }

                            yearOfWorkRepository.save(entity)
                        }
                    }
                }
            }
        }
    }

    private suspend fun syncEnvironment(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("Environment", sidoCode, sggCode) {
            val responses = apiClient.getAllEnvironment(sidoCode, sggCode)
            val allEnvironments = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allEnvironments.size} environment records for $sidoCode-$sggCode")

            allEnvironments.forEach { env ->
                env.kinderCode?.let { kinderCode ->
                    centerRepository.findByKinderCode(kinderCode)?.let { center ->
                        center.id?.let { centerId ->
                            val entity =
                                environmentRepository.findByCenterId(centerId) ?: CenterEnvironment(center = center)

                            entity.apply {
                                airQualityCheckDate = env.arql_chk_dt
                                airQualityCheckResult = env.arql_chk_rslt_tp_cd
                                regularDisinfectionRequired = env.fxtm_dsnf_trgt_yn
                                regularDisinfectionDate = env.fxtm_dsnf_chk_dt
                                regularDisinfectionResult = env.fxtm_dsnf_chk_rslt_tp_cd
                                waterType01 = env.tp_01
                                waterType02 = env.tp_02
                                waterType03 = env.tp_03
                                waterType04 = env.tp_04
                                groundwaterTestRequired = env.unwt_qlwt_insc_yn
                                groundwaterTestDate = env.qlwt_insc_dt
                                groundwaterTestResult = env.qlwt_insc_stby_yn
                                dustCheckDate = env.mdst_chk_dt
                                dustCheckResult = env.mdst_chk_rslt_cd
                                lightCheckDate = env.ilmn_chk_dt
                                lightCheckResult = env.ilmn_chk_rslt_cd
                                disclosureTiming = env.pbnttmng
                                updatedAt = LocalDateTime.now()
                            }

                            environmentRepository.save(entity)
                        }
                    }
                }
            }
        }
    }

    private suspend fun syncSafetyCheck(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("SafetyCheck", sidoCode, sggCode) {
            val responses = apiClient.getAllSafetyCheck(sidoCode, sggCode)
            val allSafetyChecks = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allSafetyChecks.size} safety check records for $sidoCode-$sggCode")

            allSafetyChecks.forEach { safety ->
                safety.kinderCode?.let { kinderCode ->
                    centerRepository.findByKinderCode(kinderCode)?.let { center ->
                        center.id?.let { centerId ->
                            val entity =
                                safetyCheckRepository.findByCenterId(centerId) ?: CenterSafetyCheck(center = center)

                            entity.apply {
                                fireEvacuationYn = safety.fire_avd_yn
                                fireEvacuationDate = safety.fire_avd_dt
                                gasCheckYn = safety.gas_ck_yn
                                gasCheckDate = safety.gas_ck_dt
                                fireSafetyYn = safety.fire_safe_yn
                                fireSafetyDate = safety.fire_safe_dt
                                electricCheckYn = safety.elect_ck_yn
                                electricCheckDate = safety.elect_ck_dt
                                playgroundCheckYn = safety.plyg_ck_yn
                                playgroundCheckDate = safety.plyg_ck_dt
                                playgroundCheckResult = safety.plyg_ck_rs_cd
                                cctvInstalled = safety.cctv_ist_yn
                                cctvTotal = safety.cctv_ist_total?.toIntOrNull()
                                cctvIndoor = safety.cctv_ist_in?.toIntOrNull()
                                cctvOutdoor = safety.cctv_ist_out?.toIntOrNull()
                                disclosureTiming = safety.pbnttmng
                                updatedAt = LocalDateTime.now()
                            }

                            safetyCheckRepository.save(entity)
                        }
                    }
                }
            }
        }
    }

    private suspend fun syncSafetyEducation(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("SafetyEducation", sidoCode, sggCode) {
            val responses = apiClient.getAllSafetyEducation(sidoCode, sggCode)
            val allSafetyEducations = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allSafetyEducations.size} safety education records for $sidoCode-$sggCode")

            allSafetyEducations.forEach { edu ->
                edu.kinderCode?.let { kinderCode ->
                    centerRepository.findByKinderCode(kinderCode)?.let { center ->
                        center.id?.let { centerId ->
                            safetyEducationRepository.findAllByCenterId(centerId).forEach {
                                safetyEducationRepository.delete(it)
                            }

                            val entity = CenterSafetyEducation(center = center)

                            entity.apply {
                                semester = edu.pbnt_sem_sc_cd
                                lifeSafety = edu.safe_tp_cd1
                                trafficSafety = edu.safe_tp_cd2
                                violencePrevention = edu.safe_tp_cd3
                                drugPrevention = edu.safe_tp_cd4
                                cyberPrevention = edu.safe_tp_cd5
                                disasterSafety = edu.safe_tp_cd6
                                occupationalSafety = edu.safe_tp_cd7
                                firstAid = edu.safe_tp_cd8
                                disclosureTiming = edu.pbnttmng
                            }

                            safetyEducationRepository.save(entity)
                        }
                    }
                }
            }
        }
    }

    private suspend fun syncMutualAid(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("MutualAid", sidoCode, sggCode) {
            val responses = apiClient.getAllMutualAid(sidoCode, sggCode)
            val allMutualAids = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allMutualAids.size} mutual aid records for $sidoCode-$sggCode")

            allMutualAids.forEach { aid ->
                aid.kinderCode?.let { kinderCode ->
                    centerRepository.findByKinderCode(kinderCode)?.let { center ->
                        center.id?.let { centerId ->
                            val entity =
                                mutualAidRepository.findByCenterId(centerId) ?: CenterMutualAid(center = center)

                            entity.apply {
                                schoolSafetyTarget = aid.school_ds_yn
                                schoolSafetyEnrolled = aid.school_ds_en
                                educationFacilityTarget = aid.educate_ds_yn
                                educationFacilityEnrolled = aid.educate_ds_en
                                disclosureTiming = aid.pbnttmng
                                updatedAt = LocalDateTime.now()
                            }

                            mutualAidRepository.save(entity)
                        }
                    }
                }
            }
        }
    }

    private suspend fun syncInsurance(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("Insurance", sidoCode, sggCode) {
            val responses = apiClient.getAllInsurance(sidoCode, sggCode)
            val allInsurances = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allInsurances.size} insurance records for $sidoCode-$sggCode")

            allInsurances.forEach { ins ->
                ins.kinderCode?.let { kinderCode ->
                    centerRepository.findByKinderCode(kinderCode)?.let { center ->
                        center.id?.let { centerId ->
                            insuranceRepository.findAllByCenterId(centerId).forEach {
                                insuranceRepository.delete(it)
                            }

                            val entity = CenterInsurance(center = center)

                            entity.apply {
                                insuranceName = ins.insurance_nm
                                targetYn = ins.insurance_yn
                                enrolledYn = ins.insurance_en
                                company1 = ins.company1
                                company2 = ins.company2
                                company3 = ins.company3
                                disclosureTiming = ins.pbnttmng
                            }

                            insuranceRepository.save(entity)
                        }
                    }
                }
            }
        }
    }

    private suspend fun syncAfterSchool(
        sidoCode: String,
        sggCode: String,
    ) {
        retryOnFailure("AfterSchool", sidoCode, sggCode) {
            val responses = apiClient.getAllAfterSchool(sidoCode, sggCode)
            val allAfterSchools = responses.flatMap { it.kinderInfo ?: emptyList() }
            logger.info("Processing ${allAfterSchools.size} after school records for $sidoCode-$sggCode")

            allAfterSchools.forEach { afterSchool ->
                afterSchool.kinderCode?.let { kinderCode ->
                    centerRepository.findByKinderCode(kinderCode)?.let { center ->
                        center.id?.let { centerId ->
                            val entity =
                                afterSchoolRepository.findByCenterId(centerId) ?: CenterAfterSchool(center = center)

                            entity.apply {
                                independentClassCount = afterSchool.inor_clcnt?.toIntOrNull()
                                afternoonClassCount = afterSchool.pm_rrgn_clcnt?.toIntOrNull()
                                operatingHours = afterSchool.oper_time
                                independentParticipants = afterSchool.inor_ptcn_kpcnt?.toIntOrNull()
                                afternoonParticipants = afterSchool.pm_rrgn_ptcn_kpcnt?.toIntOrNull()
                                regularTeacherCount = afterSchool.fxrl_thcnt?.toIntOrNull()
                                contractTeacherCount = afterSchool.shcnt_thcnt?.toIntOrNull()
                                dedicatedStaffCount = afterSchool.incnt?.toIntOrNull()
                                disclosureTiming = afterSchool.pbnttmng
                                updatedAt = LocalDateTime.now()
                            }

                            afterSchoolRepository.save(entity)
                        }
                    }
                }
            }
        }
    }

    private fun parseLocation(
        latitude: String?,
        longitude: String?,
    ): org.locationtech.jts.geom.Point? {
        if (latitude.isNullOrBlank() || longitude.isNullOrBlank()) return null

        return try {
            val lat = latitude.toDouble()
            val lng = longitude.toDouble()
            geometryFactory.createPoint(Coordinate(lng, lat))
        } catch (e: Exception) {
            logger.warn("Failed to parse location: lat=$latitude, lng=$longitude", e)
            null
        }
    }

    private suspend fun retryOnFailure(
        apiName: String,
        sidoCode: String,
        sggCode: String,
        block: suspend () -> Unit,
    ) {
        repeat(MAX_RETRY_ATTEMPTS) { attempt ->
            try {
                block()
                return
            } catch (e: Exception) {
                logger.error("$apiName sync failed for $sidoCode-$sggCode (attempt ${attempt + 1}/$MAX_RETRY_ATTEMPTS): ${e.message}", e)
                if (attempt == MAX_RETRY_ATTEMPTS - 1) {
                    throw e
                }
                delay(1000L * (attempt + 1))
            }
        }
    }
}
