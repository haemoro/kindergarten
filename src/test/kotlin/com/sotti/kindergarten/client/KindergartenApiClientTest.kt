package com.sotti.kindergarten.client

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.jackson.jackson
import kotlinx.coroutines.runBlocking

class KindergartenApiClientTest :
    DescribeSpec({
        describe("KindergartenApiClient") {
            val properties =
                KindergartenApiProperties(
                    baseUrl = "https://e-childschoolinfo.moe.go.kr/api",
                    key = "test-api-key",
                )

            context("getBasicInfo") {
                it("should successfully fetch basic info") {
                    val mockEngine =
                        MockEngine { request ->
                            request.url.parameters["key"] shouldBe "test-api-key"
                            request.url.parameters["sidoCode"] shouldBe "11"
                            request.url.parameters["sggCode"] shouldBe "680"
                            request.url.parameters["pageCnt"] shouldBe "100"
                            request.url.parameters["currentPage"] shouldBe "1"

                            respond(
                                content =
                                    """
                                    {
                                        "kinderInfo": [
                                            {
                                                "kindercode": "11680001",
                                                "kindername": "테스트유치원",
                                                "establish": "사립",
                                                "addr": "서울특별시 강남구",
                                                "telno": "02-1234-5678",
                                                "lttdcdnt": "37.5665",
                                                "lngtcdnt": "126.9780"
                                            }
                                        ],
                                        "totalCount": 1
                                    }
                                    """.trimIndent(),
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json"),
                            )
                        }

                    val client =
                        HttpClient(mockEngine) {
                            install(ContentNegotiation) {
                                jackson()
                            }
                        }

                    val apiClient = KindergartenApiClient(client, properties)

                    runBlocking {
                        val response = apiClient.getBasicInfo("11", "680")

                        response shouldNotBe null
                        response.totalCount shouldBe 1
                        response.kinderInfo?.size shouldBe 1
                        response.kinderInfo?.first()?.kinderCode shouldBe "11680001"
                        response.kinderInfo?.first()?.kindername shouldBe "테스트유치원"
                    }
                }

                it("should handle retry on failure") {
                    var attemptCount = 0
                    val mockEngine =
                        MockEngine { request ->
                            attemptCount++
                            if (attemptCount < 2) {
                                respond(
                                    content = "Server Error",
                                    status = HttpStatusCode.InternalServerError,
                                )
                            } else {
                                respond(
                                    content =
                                        """
                                        {
                                            "kinderInfo": [
                                                {
                                                    "kindercode": "11680001",
                                                    "kindername": "테스트유치원"
                                                }
                                            ],
                                            "totalCount": 1
                                        }
                                        """.trimIndent(),
                                    status = HttpStatusCode.OK,
                                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                                )
                            }
                        }

                    val client =
                        HttpClient(mockEngine) {
                            install(ContentNegotiation) {
                                jackson()
                            }
                        }

                    val apiClient = KindergartenApiClient(client, properties)

                    runBlocking {
                        val response = apiClient.getBasicInfo("11", "680")

                        attemptCount shouldBe 2
                        response.totalCount shouldBe 1
                    }
                }
            }

            context("getAllBasicInfo") {
                it("should fetch all pages when total count exceeds page count") {
                    var currentRequestPage = 0
                    val mockEngine =
                        MockEngine { request ->
                            currentRequestPage++
                            val page = request.url.parameters["currentPage"]?.toInt() ?: 1

                            val content =
                                if (page == 1) {
                                    """
                                    {
                                        "kinderInfo": [
                                            {"kindercode": "11680001", "kindername": "유치원1"}
                                        ],
                                        "totalCount": 150
                                    }
                                    """.trimIndent()
                                } else {
                                    """
                                    {
                                        "kinderInfo": [
                                            {"kindercode": "11680002", "kindername": "유치원2"}
                                        ],
                                        "totalCount": 150
                                    }
                                    """.trimIndent()
                                }

                            respond(
                                content = content,
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json"),
                            )
                        }

                    val client =
                        HttpClient(mockEngine) {
                            install(ContentNegotiation) {
                                jackson()
                            }
                        }

                    val apiClient = KindergartenApiClient(client, properties)

                    runBlocking {
                        val responses = apiClient.getAllBasicInfo("11", "680", pageCnt = 100)

                        responses.size shouldBe 2
                        currentRequestPage shouldBe 2
                        responses[0].totalCount shouldBe 150
                        responses[1].totalCount shouldBe 150
                    }
                }
            }

            context("getBuilding") {
                it("should successfully fetch building info") {
                    val mockEngine =
                        MockEngine { request ->
                            request.url.parameters["key"] shouldBe "test-api-key"

                            respond(
                                content =
                                    """
                                    {
                                        "kinderInfo": [
                                            {
                                                "kindercode": "11680001",
                                                "archyy": "2010",
                                                "floorcnt": "지상3층",
                                                "bldgprusarea": "1500.5㎡"
                                            }
                                        ],
                                        "totalCount": 1
                                    }
                                    """.trimIndent(),
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json"),
                            )
                        }

                    val client =
                        HttpClient(mockEngine) {
                            install(ContentNegotiation) {
                                jackson()
                            }
                        }

                    val apiClient = KindergartenApiClient(client, properties)

                    runBlocking {
                        val response = apiClient.getBuilding("11", "680")

                        response shouldNotBe null
                        response.totalCount shouldBe 1
                        response.kinderInfo?.size shouldBe 1
                        response.kinderInfo?.first()?.kinderCode shouldBe "11680001"
                        response.kinderInfo?.first()?.archyy shouldBe "2010"
                    }
                }
            }

            context("getAllBuilding") {
                it("should fetch all building pages") {
                    var pageCounter = 0
                    val mockEngine =
                        MockEngine { request ->
                            pageCounter++
                            respond(
                                content =
                                    """
                                    {
                                        "kinderInfo": [
                                            {"kindercode": "1168000$pageCounter"}
                                        ],
                                        "totalCount": 50
                                    }
                                    """.trimIndent(),
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json"),
                            )
                        }

                    val client =
                        HttpClient(mockEngine) {
                            install(ContentNegotiation) {
                                jackson()
                            }
                        }

                    val apiClient = KindergartenApiClient(client, properties)

                    runBlocking {
                        val responses = apiClient.getAllBuilding("11", "680", pageCnt = 30)

                        responses.size shouldBe 2
                        pageCounter shouldBe 2
                    }
                }
            }

            context("error handling") {
                it("should throw exception after max retry attempts") {
                    val mockEngine =
                        MockEngine { request ->
                            respond(
                                content = "Server Error",
                                status = HttpStatusCode.InternalServerError,
                            )
                        }

                    val client =
                        HttpClient(mockEngine) {
                            install(ContentNegotiation) {
                                jackson()
                            }
                        }

                    val apiClient = KindergartenApiClient(client, properties)

                    runBlocking {
                        try {
                            apiClient.getBasicInfo("11", "680")
                            throw AssertionError("Expected exception was not thrown")
                        } catch (e: Exception) {
                            e shouldNotBe null
                        }
                    }
                }
            }
        }
    })
