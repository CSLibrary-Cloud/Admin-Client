package com.cslibrary.admin.server

import com.cslibrary.admin.data.ReportData
import com.cslibrary.admin.data.ReportRequest
import com.cslibrary.admin.data.SealedUser
import com.cslibrary.admin.data.UserState
import com.cslibrary.admin.error.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.response.MockRestResponseCreators

@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class ServerModelTest {
    @Autowired
    private lateinit var serverModel: ServerModel

    // Mock Server
    private val serverAddress: String = "http://localhost:8080"
    private lateinit var mockServer: MockRestServiceServer
    private lateinit var clientHttpRequestFactory: ClientHttpRequestFactory

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    @BeforeEach
    fun initTest() {
        // Backup Original Server Communication
        clientHttpRequestFactory = serverModel.restTemplate.requestFactory

        // Hijack and make Mock Server
        mockServer = MockRestServiceServer.bindTo(serverModel.restTemplate)
            .ignoreExpectOrder(true).build()
    }

    @AfterEach
    fun deInitTest() {
        // Restore RequestFactory
        serverModel.restTemplate.requestFactory = clientHttpRequestFactory
    }

    @Test
    fun is_BanningRequestWorks_well() {
        val testUserId: String = "testUserId"
        mockServer.expect(
            ExpectedCount.min(1),
            MockRestRequestMatchers.requestTo("$serverAddress/api/v1/admin/user/${testUserId}?ban=true"),
        )
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andExpect(MockRestRequestMatchers.queryParam("ban", "true"))
            .andRespond(
                MockRestResponseCreators.withNoContent()
            )

        runCatching {
            serverModel.updateUser(testUserId, true)
        }.onFailure {
            println(it.stackTraceToString())
        }

        mockServer.verify()
    }

    @Test
    fun is_BanningRequest_catches_client_error_correct_body() {
        val testUserId: String = "testUserId"
        mockServer.expect(
            ExpectedCount.min(1),
            MockRestRequestMatchers.requestTo("$serverAddress/api/v1/admin/user/${testUserId}?ban=true"),
        )
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andExpect(MockRestRequestMatchers.queryParam("ban", "true"))
            .andRespond(
                MockRestResponseCreators.withBadRequest()
                    .body(
                        objectMapper.writeValueAsString(
                            ErrorResponse(
                                errorMessage = "Bad Request!"
                            )
                        )
                    )
            )

        runCatching {
            serverModel.updateUser(testUserId, true)
        }.onSuccess {
            fail("Seems like we are mocking a fail test but it succeed?")
        }.onFailure {
            assertThat(it.stackTraceToString()).contains("Server responded with: ")
        }

        mockServer.verify()
    }

    @Test
    fun is_BanningRequest_catches_client_error_wrong_body() {
        val testUserId: String = "testUserId"
        mockServer.expect(
            ExpectedCount.min(1),
            MockRestRequestMatchers.requestTo("$serverAddress/api/v1/admin/user/${testUserId}?ban=true"),
        )
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andExpect(MockRestRequestMatchers.queryParam("ban", "true"))
            .andRespond(
                MockRestResponseCreators.withBadRequest()
                    .body("")
            )

        runCatching {
            serverModel.updateUser(testUserId, true)
        }.onSuccess {
            fail("Seems like we are mocking a fail test but it succeed?")
        }.onFailure {
            assertThat(it.stackTraceToString()).contains("Exception Occurred!")
        }

        mockServer.verify()
    }

    @Test
    fun is_UNBanningRequestWorks_well() {
        val testUserId: String = "testUserId"
        mockServer.expect(
            ExpectedCount.min(1),
            MockRestRequestMatchers.requestTo("$serverAddress/api/v1/admin/user/${testUserId}?ban=true"),
        )
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andExpect(MockRestRequestMatchers.queryParam("ban", "false"))
            .andRespond(
                MockRestResponseCreators.withNoContent()
            )

        runCatching {
            serverModel.updateUser(testUserId, false)
        }.onFailure {
            println(it.stackTraceToString())
        }

        mockServer.verify()
    }

    @Test
    fun is_gettingUserReportList_works_well() {
        mockServer.expect(
            ExpectedCount.min(1),
            MockRestRequestMatchers.requestTo("$serverAddress/api/v1/admin/report"),
        )
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(
                MockRestResponseCreators.withSuccess().body(
                    objectMapper.writeValueAsString(
                        listOf(
                            ReportData(
                                reportUserId = "KangDroid",
                                reportContent = ReportRequest("ReportContent"),
                                isReportHandlingDone = false,
                                reportIdentifier = "SHA-512-somewhat"
                            ),
                            ReportData(
                                reportUserId = "KangDroid",
                                reportContent = ReportRequest("ReportContent"),
                                isReportHandlingDone = false,
                                reportIdentifier = "SHA-512-somewhat2"
                            )
                        )
                    )
                )
            )

        runCatching {
            serverModel.getUserReportList()
        }.onFailure {
            println(it.stackTraceToString())
            fail("Something went wrong!")
        }.onSuccess {
            assertThat(it.size).isEqualTo(2)
            it.forEach { eachData ->
                assertThat(eachData.reportUserId).isEqualTo("KangDroid")
            }
        }
    }

    @Test
    fun is_removeUserReport_works_well() {
        val testReportToken: String = "TestToken"
        mockServer.expect(
            ExpectedCount.min(1),
            MockRestRequestMatchers.requestTo("$serverAddress/api/v1/admin/report/${testReportToken}"),
        )
            .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
            .andRespond(
                MockRestResponseCreators.withNoContent()
            )

        runCatching {
            serverModel.removeUserReport(testReportToken)
        }.onFailure {
            println(it.stackTraceToString())
            fail("Something went wrong!")
        }
    }

    @Test
    fun is_getSealedUserList_works_well() {
        mockServer.expect(
            ExpectedCount.min(1),
            MockRestRequestMatchers.requestTo("$serverAddress/api/v1/admin/user"),
        )
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(
                MockRestResponseCreators.withSuccess().body(
                    objectMapper.writeValueAsString(
                        listOf(
                            SealedUser(
                                userId = "KangDroid",
                                userName = "KDR",
                                userPhoneNumber = "XXX-~",
                                leftTime = -1000,
                                totalStudyTime = -1000,
                                reservedSeatNumber = "10",
                                userState = UserState.BREAK,
                                userNonBanned = true
                            )
                        )
                    )
                )
            )

        runCatching {
            serverModel.getSealedUserList()
        }.onFailure {
            println(it.stackTraceToString())
            fail("Something went wrong!")
        }.onSuccess {
            assertThat(it.isNotEmpty()).isEqualTo(true)
            assertThat(it[0].userId).isEqualTo("KangDroid")
        }
    }
}