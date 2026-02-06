package com.sotti.kindergarten.client

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kindergarten.api")
data class KindergartenApiProperties(
    val key: String,
    val baseUrl: String = "https://e-childschoolinfo.moe.go.kr/api/notice",
)
