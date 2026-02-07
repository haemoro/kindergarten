package com.sotti.kindergarten.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.sotti.kindergarten.dto.FavoriteRequest
import com.sotti.kindergarten.dto.FavoriteResponse
import com.sotti.kindergarten.dto.PageResponse
import com.sotti.kindergarten.exception.CenterNotFoundException
import com.sotti.kindergarten.exception.DuplicateFavoriteException
import com.sotti.kindergarten.exception.FavoriteNotFoundException
import com.sotti.kindergarten.exception.GlobalExceptionHandler
import com.sotti.kindergarten.service.FavoriteService
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.time.LocalDateTime
import java.util.UUID

class FavoriteControllerTest : BehaviorSpec() {
    private lateinit var mockMvc: MockMvc
    private lateinit var favoriteService: FavoriteService
    private lateinit var objectMapper: ObjectMapper

    init {
        beforeSpec {
            favoriteService = mockk()
            objectMapper = ObjectMapper()
            objectMapper.findAndRegisterModules()

            val validator = LocalValidatorFactoryBean()
            validator.afterPropertiesSet()

            mockMvc =
                MockMvcBuilders
                    .standaloneSetup(FavoriteController(favoriteService))
                    .setControllerAdvice(GlobalExceptionHandler())
                    .setValidator(validator)
                    .build()
        }

        Given("GET /api/v1/favorites - 즐겨찾기 목록 조회") {
            val deviceId = "test-device-123"

            When("기본 파라미터로 조회") {
                val response =
                    PageResponse(
                        content =
                            listOf(
                                FavoriteResponse(
                                    id = UUID.randomUUID(),
                                    centerId = UUID.randomUUID(),
                                    centerName = "테스트유치원",
                                    createdAt = LocalDateTime.now(),
                                ),
                            ),
                        page = 0,
                        size = 20,
                        totalElements = 1,
                        totalPages = 1,
                    )

                every { favoriteService.getFavorites(deviceId, 0, 20) } returns response

                Then("200 OK와 즐겨찾기 목록을 반환한다") {
                    mockMvc
                        .perform(
                            get("/api/v1/favorites")
                                .param("deviceId", deviceId),
                        ).andExpect(status().isOk)
                        .andExpect(jsonPath("$.content").isArray)
                        .andExpect(jsonPath("$.content[0].centerName").value("테스트유치원"))
                        .andExpect(jsonPath("$.page").value(0))
                        .andExpect(jsonPath("$.size").value(20))

                    verify(exactly = 1) { favoriteService.getFavorites(deviceId, 0, 20) }
                }
            }

            When("페이징 파라미터로 조회") {
                val response =
                    PageResponse(
                        content = emptyList<FavoriteResponse>(),
                        page = 1,
                        size = 10,
                        totalElements = 0,
                        totalPages = 0,
                    )

                every { favoriteService.getFavorites(deviceId, 1, 10) } returns response

                Then("페이징된 결과를 반환한다") {
                    mockMvc
                        .perform(
                            get("/api/v1/favorites")
                                .param("deviceId", deviceId)
                                .param("page", "1")
                                .param("size", "10"),
                        ).andExpect(status().isOk)
                        .andExpect(jsonPath("$.content").isEmpty)
                        .andExpect(jsonPath("$.page").value(1))
                        .andExpect(jsonPath("$.size").value(10))

                    verify(exactly = 1) { favoriteService.getFavorites(deviceId, 1, 10) }
                }
            }
        }

        Given("POST /api/v1/favorites - 즐겨찾기 추가") {
            When("정상적인 추가 요청") {
                val centerId = UUID.randomUUID()
                val request = FavoriteRequest(deviceId = "test-device-123", centerId = centerId)
                val response =
                    FavoriteResponse(
                        id = UUID.randomUUID(),
                        centerId = centerId,
                        centerName = "새유치원",
                        createdAt = LocalDateTime.now(),
                    )

                every { favoriteService.addFavorite(request) } returns response

                Then("201 Created와 생성된 즐겨찾기를 반환한다") {
                    mockMvc
                        .perform(
                            post("/api/v1/favorites")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)),
                        ).andExpect(status().isCreated)
                        .andExpect(jsonPath("$.centerName").value("새유치원"))
                        .andExpect(jsonPath("$.centerId").value(centerId.toString()))

                    verify(exactly = 1) { favoriteService.addFavorite(request) }
                }
            }

            When("중복된 즐겨찾기 추가 요청") {
                val request = FavoriteRequest(deviceId = "test-device-123", centerId = UUID.randomUUID())

                every { favoriteService.addFavorite(request) } throws DuplicateFavoriteException()

                Then("409 Conflict를 반환한다") {
                    mockMvc
                        .perform(
                            post("/api/v1/favorites")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)),
                        ).andExpect(status().isConflict)

                    verify(exactly = 1) { favoriteService.addFavorite(request) }
                }
            }

            When("존재하지 않는 유치원으로 추가 요청") {
                val centerId = UUID.randomUUID()
                val request = FavoriteRequest(deviceId = "test-device-123", centerId = centerId)

                every { favoriteService.addFavorite(request) } throws CenterNotFoundException(centerId)

                Then("404 Not Found를 반환한다") {
                    mockMvc
                        .perform(
                            post("/api/v1/favorites")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)),
                        ).andExpect(status().isNotFound)

                    verify(exactly = 1) { favoriteService.addFavorite(request) }
                }
            }

            When("deviceId 누락 요청 (validation 실패)") {
                val invalidRequest =
                    mapOf(
                        "deviceId" to "",
                        "centerId" to UUID.randomUUID().toString(),
                    )

                Then("400 Bad Request를 반환한다") {
                    mockMvc
                        .perform(
                            post("/api/v1/favorites")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)),
                        ).andExpect(status().isBadRequest)
                }
            }
        }

        Given("DELETE /api/v1/favorites/{id} - 즐겨찾기 삭제") {
            When("정상적인 삭제 요청") {
                val favoriteId = UUID.randomUUID()
                val deviceId = "test-device-123"

                justRun { favoriteService.removeFavorite(favoriteId, deviceId) }

                Then("204 No Content를 반환한다") {
                    mockMvc
                        .perform(
                            delete("/api/v1/favorites/$favoriteId")
                                .param("deviceId", deviceId),
                        ).andExpect(status().isNoContent)

                    verify(exactly = 1) { favoriteService.removeFavorite(favoriteId, deviceId) }
                }
            }

            When("존재하지 않는 즐겨찾기 삭제 요청") {
                val favoriteId = UUID.randomUUID()
                val deviceId = "test-device-123"

                every { favoriteService.removeFavorite(favoriteId, deviceId) } throws FavoriteNotFoundException()

                Then("404 Not Found를 반환한다") {
                    mockMvc
                        .perform(
                            delete("/api/v1/favorites/$favoriteId")
                                .param("deviceId", deviceId),
                        ).andExpect(status().isNotFound)

                    verify(exactly = 1) { favoriteService.removeFavorite(favoriteId, deviceId) }
                }
            }

            When("다른 사용자의 즐겨찾기 삭제 시도") {
                val favoriteId = UUID.randomUUID()
                val deviceId = "wrong-device-456"

                every { favoriteService.removeFavorite(favoriteId, deviceId) } throws FavoriteNotFoundException()

                Then("404 Not Found를 반환한다") {
                    mockMvc
                        .perform(
                            delete("/api/v1/favorites/$favoriteId")
                                .param("deviceId", deviceId),
                        ).andExpect(status().isNotFound)

                    verify(exactly = 1) { favoriteService.removeFavorite(favoriteId, deviceId) }
                }
            }
        }
    }
}
