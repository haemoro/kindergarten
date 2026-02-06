package com.sotti.kindergarten.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.sotti.kindergarten.dto.CenterCompareRequest
import com.sotti.kindergarten.dto.CenterCompareResponse
import com.sotti.kindergarten.dto.CenterDetailResponse
import com.sotti.kindergarten.dto.CenterListResponse
import com.sotti.kindergarten.dto.ComparisonItem
import com.sotti.kindergarten.dto.PageResponse
import com.sotti.kindergarten.exception.CenterNotFoundException
import com.sotti.kindergarten.exception.GlobalExceptionHandler
import com.sotti.kindergarten.exception.InvalidCompareRequestException
import com.sotti.kindergarten.service.CenterService
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.UUID

@WebMvcTest(CenterController::class)
class CenterControllerTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @TestConfiguration
    class TestConfig {
        @Bean
        fun centerService(): CenterService = mockk()
    }

    private lateinit var mockMvc: MockMvc
    private lateinit var centerService: CenterService
    private lateinit var objectMapper: ObjectMapper

    init {
        beforeSpec {
            centerService = mockk()
            objectMapper = ObjectMapper()
            mockMvc =
                MockMvcBuilders
                    .standaloneSetup(CenterController(centerService))
                    .setControllerAdvice(GlobalExceptionHandler())
                    .build()
        }

        Given("GET /api/v1/centers - 유치원 목록 조회") {
            When("기본 파라미터로 조회") {
                val response =
                    PageResponse(
                        content =
                            listOf(
                                CenterListResponse(
                                    id = UUID.randomUUID(),
                                    name = "테스트유치원",
                                    establishType = "국공립",
                                    address = "서울시 강남구",
                                    phone = "02-1234-5678",
                                    lat = 37.5,
                                    lng = 127.0,
                                    distanceKm = null,
                                    capacity = 100,
                                    currentEnrollment = 80,
                                    totalClassCount = 5,
                                    mealProvided = true,
                                    busAvailable = true,
                                    extendedCare = false,
                                ),
                            ),
                        page = 0,
                        size = 20,
                        totalElements = 1,
                        totalPages = 1,
                    )

                every {
                    centerService.listCenters(null, null, 2.0, null, null, "distance", 0, 20)
                } returns response

                Then("200 OK와 페이징된 결과를 반환한다") {
                    mockMvc
                        .perform(get("/api/v1/centers"))
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.content").isArray)
                        .andExpect(jsonPath("$.content[0].name").value("테스트유치원"))
                        .andExpect(jsonPath("$.page").value(0))
                        .andExpect(jsonPath("$.size").value(20))
                        .andExpect(jsonPath("$.totalElements").value(1))
                }
            }

            When("반경 검색 파라미터로 조회") {
                val response =
                    PageResponse(
                        content =
                            listOf(
                                CenterListResponse(
                                    id = UUID.randomUUID(),
                                    name = "근처유치원",
                                    establishType = "사립",
                                    address = "서울시 서초구",
                                    phone = "02-2345-6789",
                                    lat = 37.51,
                                    lng = 127.01,
                                    distanceKm = 1.2,
                                    capacity = 80,
                                    currentEnrollment = 60,
                                    totalClassCount = 4,
                                    mealProvided = false,
                                    busAvailable = true,
                                    extendedCare = true,
                                ),
                            ),
                        page = 0,
                        size = 20,
                        totalElements = 1,
                        totalPages = 1,
                    )

                every {
                    centerService.listCenters(37.5, 127.0, 3.0, null, null, "distance", 0, 20)
                } returns response

                Then("반경 내 유치원 목록을 반환한다") {
                    mockMvc
                        .perform(
                            get("/api/v1/centers")
                                .param("lat", "37.5")
                                .param("lng", "127.0")
                                .param("radiusKm", "3"),
                        ).andExpect(status().isOk)
                        .andExpect(jsonPath("$.content[0].name").value("근처유치원"))
                        .andExpect(jsonPath("$.content[0].distanceKm").value(1.2))
                }
            }

            When("필터 파라미터로 조회") {
                val response =
                    PageResponse(
                        content = emptyList<CenterListResponse>(),
                        page = 0,
                        size = 20,
                        totalElements = 0,
                        totalPages = 0,
                    )

                every {
                    centerService.listCenters(null, null, 2.0, "국공립", "테스트", "name", 0, 20)
                } returns response

                Then("필터링된 결과를 반환한다") {
                    mockMvc
                        .perform(
                            get("/api/v1/centers")
                                .param("type", "국공립")
                                .param("q", "테스트")
                                .param("sort", "name"),
                        ).andExpect(status().isOk)
                        .andExpect(jsonPath("$.content").isEmpty)
                }
            }
        }

        Given("GET /api/v1/centers/{id} - 유치원 상세 조회") {
            When("존재하는 유치원 조회") {
                val centerId = UUID.randomUUID()
                val response =
                    CenterDetailResponse(
                        id = centerId,
                        name = "테스트유치원",
                        establishType = "국공립",
                        address = "서울시 강남구",
                        phone = "02-1234-5678",
                        lat = 37.5,
                        lng = 127.0,
                        distanceKm = null,
                        capacity = 100,
                        currentEnrollment = 80,
                        totalClassCount = 5,
                        operatingHours = "07:00-19:00",
                        homepage = "http://test.com",
                        fax = "02-1234-5679",
                        representativeName = "홍길동",
                        directorName = "김철수",
                        establishDate = null,
                        openDate = null,
                        disclosureTiming = null,
                        classCountByAge = null,
                        capacityByAge = null,
                        enrollmentByAge = null,
                        building = null,
                        classroom = null,
                        teacher = null,
                        lessonDay = null,
                        meal = null,
                        bus = null,
                        yearOfWork = null,
                        environment = null,
                        safetyCheck = null,
                        safetyEducation = null,
                        mutualAid = null,
                        insurance = null,
                        afterSchool = null,
                        sourceUpdatedAt = null,
                    )

                every { centerService.getCenterDetail(centerId) } returns response

                Then("200 OK와 상세 정보를 반환한다") {
                    mockMvc
                        .perform(get("/api/v1/centers/$centerId"))
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").value(centerId.toString()))
                        .andExpect(jsonPath("$.name").value("테스트유치원"))
                        .andExpect(jsonPath("$.establishType").value("국공립"))
                }
            }

            When("존재하지 않는 유치원 조회") {
                val centerId = UUID.randomUUID()
                every { centerService.getCenterDetail(centerId) } throws CenterNotFoundException(centerId)

                Then("404 Not Found를 반환한다") {
                    mockMvc
                        .perform(get("/api/v1/centers/$centerId"))
                        .andExpect(status().isNotFound)
                }
            }
        }

        Given("POST /api/v1/centers/compare - 유치원 비교") {
            When("2개 유치원 비교 요청") {
                val center1Id = UUID.randomUUID()
                val center2Id = UUID.randomUUID()
                val request = CenterCompareRequest(centerIds = listOf(center1Id, center2Id), lat = null, lng = null)
                val response =
                    CenterCompareResponse(
                        centers =
                            listOf(
                                ComparisonItem(
                                    id = center1Id,
                                    name = "유치원1",
                                    establishType = "국공립",
                                    address = "서울시 강남구",
                                    distanceKm = null,
                                    capacity = 100,
                                    currentEnrollment = 80,
                                    teacherCount = 10,
                                    classCount = 5,
                                    mealProvided = true,
                                    busAvailable = true,
                                    extendedCare = false,
                                    buildingArea = 500.0,
                                    classroomArea = 300.0,
                                    cctvInstalled = true,
                                    cctvTotal = 10,
                                ),
                                ComparisonItem(
                                    id = center2Id,
                                    name = "유치원2",
                                    establishType = "사립",
                                    address = "서울시 서초구",
                                    distanceKm = null,
                                    capacity = 80,
                                    currentEnrollment = 60,
                                    teacherCount = 8,
                                    classCount = 4,
                                    mealProvided = false,
                                    busAvailable = false,
                                    extendedCare = true,
                                    buildingArea = 400.0,
                                    classroomArea = 250.0,
                                    cctvInstalled = true,
                                    cctvTotal = 8,
                                ),
                            ),
                    )

                every { centerService.compareCenters(request) } returns response

                Then("200 OK와 비교 결과를 반환한다") {
                    mockMvc
                        .perform(
                            post("/api/v1/centers/compare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)),
                        ).andExpect(status().isOk)
                        .andExpect(jsonPath("$.centers").isArray)
                        .andExpect(jsonPath("$.centers.length()").value(2))
                        .andExpect(jsonPath("$.centers[0].name").value("유치원1"))
                        .andExpect(jsonPath("$.centers[1].name").value("유치원2"))
                }
            }

            When("1개만 요청 (잘못된 요청)") {
                val request = CenterCompareRequest(centerIds = listOf(UUID.randomUUID()), lat = null, lng = null)

                every { centerService.compareCenters(request) } throws InvalidCompareRequestException()

                Then("400 Bad Request를 반환한다") {
                    mockMvc
                        .perform(
                            post("/api/v1/centers/compare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)),
                        ).andExpect(status().isBadRequest)
                }
            }
        }
    }
}
