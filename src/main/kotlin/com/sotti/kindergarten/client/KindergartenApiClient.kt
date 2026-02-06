package com.sotti.kindergarten.client

import com.sotti.kindergarten.client.dto.AfterSchoolApiResponse
import com.sotti.kindergarten.client.dto.BasicInfoApiResponse
import com.sotti.kindergarten.client.dto.BuildingApiResponse
import com.sotti.kindergarten.client.dto.BusApiResponse
import com.sotti.kindergarten.client.dto.ClassAreaApiResponse
import com.sotti.kindergarten.client.dto.EnvironmentApiResponse
import com.sotti.kindergarten.client.dto.InsuranceApiResponse
import com.sotti.kindergarten.client.dto.LessonDayApiResponse
import com.sotti.kindergarten.client.dto.MealApiResponse
import com.sotti.kindergarten.client.dto.MutualAidApiResponse
import com.sotti.kindergarten.client.dto.SafetyCheckApiResponse
import com.sotti.kindergarten.client.dto.SafetyEducationApiResponse
import com.sotti.kindergarten.client.dto.TeacherApiResponse
import com.sotti.kindergarten.client.dto.YearOfWorkApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class KindergartenApiClient(
    private val httpClient: HttpClient,
    private val properties: KindergartenApiProperties,
) {
    private val logger = LoggerFactory.getLogger(KindergartenApiClient::class.java)

    companion object {
        private const val MAX_RETRY_ATTEMPTS = 3
        private const val RETRY_DELAY_MS = 1000L
    }

    suspend fun getBasicInfo(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): BasicInfoApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/basicInfo2.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllBasicInfo(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<BasicInfoApiResponse> {
        val results = mutableListOf<BasicInfoApiResponse>()
        var currentPage = 1
        var hasMore = true

        while (hasMore) {
            val response = getBasicInfo(sidoCode, sggCode, pageCnt, currentPage, timing)
            results.add(response)

            val totalCount = response.totalCount ?: 0
            val fetchedCount = currentPage * pageCnt
            hasMore = fetchedCount < totalCount

            if (hasMore) {
                currentPage++
                delay(200) // Rate limiting
            }
        }

        return results
    }

    suspend fun getBuilding(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): BuildingApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/building.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllBuilding(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<BuildingApiResponse> =
        fetchAllPages(
            fetcher = { page -> getBuilding(sidoCode, sggCode, pageCnt, page, timing) },
            totalCountGetter = { it.totalCount ?: 0 },
            pageCnt = pageCnt,
        )

    suspend fun getClassArea(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): ClassAreaApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/classArea.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllClassArea(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<ClassAreaApiResponse> =
        fetchAllPages(
            fetcher = { page -> getClassArea(sidoCode, sggCode, pageCnt, page, timing) },
            totalCountGetter = { it.totalCount ?: 0 },
            pageCnt = pageCnt,
        )

    suspend fun getTeacher(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): TeacherApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/teachersInfo.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllTeacher(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<TeacherApiResponse> =
        fetchAllPages(
            fetcher = { page -> getTeacher(sidoCode, sggCode, pageCnt, page, timing) },
            totalCountGetter = { it.totalCount ?: 0 },
            pageCnt = pageCnt,
        )

    suspend fun getLessonDay(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): LessonDayApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/lessonDay.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllLessonDay(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<LessonDayApiResponse> =
        fetchAllPages(
            fetcher = { page -> getLessonDay(sidoCode, sggCode, pageCnt, page, timing) },
            totalCountGetter = { it.totalCount ?: 0 },
            pageCnt = pageCnt,
        )

    suspend fun getMeal(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): MealApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/schoolMeal.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllMeal(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<MealApiResponse> =
        fetchAllPages(
            fetcher = { page -> getMeal(sidoCode, sggCode, pageCnt, page, timing) },
            totalCountGetter = { it.totalCount ?: 0 },
            pageCnt = pageCnt,
        )

    suspend fun getBus(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): BusApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/schoolBus.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllBus(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<BusApiResponse> =
        fetchAllPages(
            fetcher = { page -> getBus(sidoCode, sggCode, pageCnt, page, timing) },
            totalCountGetter = { it.totalCount ?: 0 },
            pageCnt = pageCnt,
        )

    suspend fun getYearOfWork(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): YearOfWorkApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/yearOfWork.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllYearOfWork(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<YearOfWorkApiResponse> =
        fetchAllPages(
            fetcher = { page -> getYearOfWork(sidoCode, sggCode, pageCnt, page, timing) },
            totalCountGetter = { it.totalCount ?: 0 },
            pageCnt = pageCnt,
        )

    suspend fun getEnvironment(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): EnvironmentApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/environmentHygiene.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllEnvironment(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<EnvironmentApiResponse> =
        fetchAllPages(
            fetcher = { page -> getEnvironment(sidoCode, sggCode, pageCnt, page, timing) },
            totalCountGetter = { it.totalCount ?: 0 },
            pageCnt = pageCnt,
        )

    suspend fun getSafetyCheck(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): SafetyCheckApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/safetyEdu.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllSafetyCheck(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<SafetyCheckApiResponse> =
        fetchAllPages(
            fetcher = { page -> getSafetyCheck(sidoCode, sggCode, pageCnt, page, timing) },
            totalCountGetter = { it.totalCount ?: 0 },
            pageCnt = pageCnt,
        )

    suspend fun getSafetyEducation(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): SafetyEducationApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/safetyInstruct.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllSafetyEducation(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<SafetyEducationApiResponse> =
        fetchAllPages(
            fetcher = { page -> getSafetyEducation(sidoCode, sggCode, pageCnt, page, timing) },
            totalCountGetter = { it.totalCount ?: 0 },
            pageCnt = pageCnt,
        )

    suspend fun getMutualAid(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): MutualAidApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/deductionSociety.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllMutualAid(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<MutualAidApiResponse> =
        fetchAllPages(
            fetcher = { page -> getMutualAid(sidoCode, sggCode, pageCnt, page, timing) },
            totalCountGetter = { it.totalCount ?: 0 },
            pageCnt = pageCnt,
        )

    suspend fun getInsurance(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): InsuranceApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/insurance.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllInsurance(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<InsuranceApiResponse> =
        fetchAllPages(
            fetcher = { page -> getInsurance(sidoCode, sggCode, pageCnt, page, timing) },
            totalCountGetter = { it.totalCount ?: 0 },
            pageCnt = pageCnt,
        )

    suspend fun getAfterSchool(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        currentPage: Int = 1,
        timing: String? = null,
    ): AfterSchoolApiResponse =
        retryOnFailure {
            httpClient
                .get("${properties.baseUrl}/afterSchoolPresent.do") {
                    parameter("key", properties.key)
                    parameter("sidoCode", sidoCode)
                    parameter("sggCode", sggCode)
                    parameter("pageCnt", pageCnt)
                    parameter("currentPage", currentPage)
                    timing?.let { parameter("timing", it) }
                }.body()
        }

    suspend fun getAllAfterSchool(
        sidoCode: String,
        sggCode: String,
        pageCnt: Int = 100,
        timing: String? = null,
    ): List<AfterSchoolApiResponse> =
        fetchAllPages(
            fetcher = { page -> getAfterSchool(sidoCode, sggCode, pageCnt, page, timing) },
            totalCountGetter = { it.totalCount ?: 0 },
            pageCnt = pageCnt,
        )

    private suspend fun <T> retryOnFailure(block: suspend () -> T): T {
        var lastException: Exception? = null
        repeat(MAX_RETRY_ATTEMPTS) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                lastException = e
                logger.warn("API call failed (attempt ${attempt + 1}/$MAX_RETRY_ATTEMPTS): ${e.message}")
                if (attempt < MAX_RETRY_ATTEMPTS - 1) {
                    delay(RETRY_DELAY_MS * (attempt + 1))
                }
            }
        }
        throw lastException ?: RuntimeException("Unknown error during API call")
    }

    private suspend fun <T> fetchAllPages(
        fetcher: suspend (Int) -> T,
        totalCountGetter: (T) -> Int,
        pageCnt: Int,
    ): List<T> {
        val results = mutableListOf<T>()
        var currentPage = 1
        var hasMore = true

        while (hasMore) {
            val response = fetcher(currentPage)
            results.add(response)

            val totalCount = totalCountGetter(response)
            val fetchedCount = currentPage * pageCnt
            hasMore = fetchedCount < totalCount

            if (hasMore) {
                currentPage++
                delay(200) // Rate limiting
            }
        }

        return results
    }
}
