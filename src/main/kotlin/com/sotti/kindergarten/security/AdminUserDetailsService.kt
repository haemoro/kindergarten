package com.sotti.kindergarten.security

import com.sotti.kindergarten.repository.AdminRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AdminUserDetailsService(
    private val adminRepository: AdminRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val admin =
            adminRepository.findByEmail(username)
                ?: throw UsernameNotFoundException("Admin not found with email: $username")
        return AdminUserDetails(admin)
    }
}
