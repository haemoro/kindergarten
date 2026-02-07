package com.sotti.kindergarten.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.sotti.kindergarten.dto.ErrorResponse
import com.sotti.kindergarten.security.JwtAuthenticationFilter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@ConfigurationPropertiesScan("com.sotti.kindergarten.security")
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val objectMapper: ObjectMapper,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/app/**")
                    .permitAll()
                    .requestMatchers("/api/v1/**")
                    .permitAll()
                    .requestMatchers("/api/admin/auth/login")
                    .permitAll()
                    .requestMatchers("/api/admin/**")
                    .authenticated()
                    .anyRequest()
                    .permitAll()
            }.exceptionHandling { ex ->
                ex.authenticationEntryPoint { request, response, _ ->
                    writeErrorResponse(response, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Authentication is required")
                }
                ex.accessDeniedHandler { request, response, _ ->
                    writeErrorResponse(response, HttpStatus.FORBIDDEN, "FORBIDDEN", "Access denied")
                }
            }.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    private fun writeErrorResponse(
        response: HttpServletResponse,
        status: HttpStatus,
        code: String,
        message: String,
    ) {
        response.status = status.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        objectMapper.writeValue(
            response.outputStream,
            ErrorResponse(status = status.value(), code = code, message = message),
        )
    }
}
