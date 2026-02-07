package com.sotti.kindergarten.service

import com.sotti.kindergarten.dto.admin.AdminMeResponse
import com.sotti.kindergarten.dto.admin.ChangePasswordRequest
import com.sotti.kindergarten.dto.admin.LoginRequest
import com.sotti.kindergarten.dto.admin.LoginResponse
import com.sotti.kindergarten.exception.BusinessException
import com.sotti.kindergarten.exception.ErrorCode
import com.sotti.kindergarten.repository.AdminRepository
import com.sotti.kindergarten.security.AdminUserDetails
import com.sotti.kindergarten.security.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AdminAuthService(
    private val adminRepository: AdminRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
) {
    fun login(request: LoginRequest): LoginResponse {
        val admin =
            adminRepository.findByEmail(request.email)
                ?: throw BusinessException(ErrorCode.INVALID_CREDENTIALS)

        if (!passwordEncoder.matches(request.password, admin.password)) {
            throw BusinessException(ErrorCode.INVALID_CREDENTIALS)
        }

        if (!admin.isActive) {
            throw BusinessException(ErrorCode.ADMIN_INACTIVE)
        }

        val token = jwtTokenProvider.generateToken(admin.email)
        return LoginResponse(
            accessToken = token,
            expiresIn = jwtTokenProvider.getExpirationMs() / 1000,
        )
    }

    fun getMe(userDetails: AdminUserDetails): AdminMeResponse =
        AdminMeResponse(
            id = userDetails.id,
            email = userDetails.email,
            name = userDetails.name,
            role = userDetails.role,
            createdAt = adminRepository.findByEmail(userDetails.email)!!.createdAt,
        )

    @Transactional
    fun changePassword(
        userDetails: AdminUserDetails,
        request: ChangePasswordRequest,
    ) {
        val admin =
            adminRepository.findByEmail(userDetails.email)
                ?: throw BusinessException(ErrorCode.ADMIN_NOT_FOUND)

        if (!passwordEncoder.matches(request.currentPassword, admin.password)) {
            throw BusinessException(ErrorCode.INVALID_PASSWORD)
        }

        admin.password = passwordEncoder.encode(request.newPassword)
        adminRepository.save(admin)
    }
}
