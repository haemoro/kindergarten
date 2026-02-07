package com.sotti.kindergarten.service

import com.sotti.kindergarten.dto.admin.AdminAccountResponse
import com.sotti.kindergarten.dto.admin.CreateAdminRequest
import com.sotti.kindergarten.dto.admin.UpdateAdminRequest
import com.sotti.kindergarten.entity.Admin
import com.sotti.kindergarten.exception.BusinessException
import com.sotti.kindergarten.exception.ErrorCode
import com.sotti.kindergarten.repository.AdminRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class AdminAccountService(
    private val adminRepository: AdminRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun listAdmins(): List<AdminAccountResponse> = adminRepository.findAll().map { it.toResponse() }

    fun getAdmin(id: UUID): AdminAccountResponse {
        val admin =
            adminRepository
                .findById(id)
                .orElseThrow { BusinessException(ErrorCode.ADMIN_NOT_FOUND) }
        return admin.toResponse()
    }

    @Transactional
    fun createAdmin(request: CreateAdminRequest): AdminAccountResponse {
        if (adminRepository.existsByEmail(request.email)) {
            throw BusinessException(ErrorCode.DUPLICATE_EMAIL)
        }

        val admin =
            Admin(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                name = request.name,
                role = request.role,
            )
        return adminRepository.save(admin).toResponse()
    }

    @Transactional
    fun updateAdmin(
        id: UUID,
        request: UpdateAdminRequest,
    ): AdminAccountResponse {
        val admin =
            adminRepository
                .findById(id)
                .orElseThrow { BusinessException(ErrorCode.ADMIN_NOT_FOUND) }

        request.name?.let { admin.name = it }
        request.role?.let { admin.role = it }
        request.isActive?.let { admin.isActive = it }

        return adminRepository.save(admin).toResponse()
    }

    @Transactional
    fun deleteAdmin(
        id: UUID,
        currentAdminId: UUID,
    ) {
        if (id == currentAdminId) {
            throw BusinessException(ErrorCode.CANNOT_DELETE_SELF)
        }

        val admin =
            adminRepository
                .findById(id)
                .orElseThrow { BusinessException(ErrorCode.ADMIN_NOT_FOUND) }
        adminRepository.delete(admin)
    }

    private fun Admin.toResponse() =
        AdminAccountResponse(
            id = id!!,
            email = email,
            name = name,
            role = role,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}
