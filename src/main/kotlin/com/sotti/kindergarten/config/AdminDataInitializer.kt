package com.sotti.kindergarten.config

import com.sotti.kindergarten.entity.Admin
import com.sotti.kindergarten.entity.AdminRole
import com.sotti.kindergarten.repository.AdminRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AdminDataInitializer(
    private val adminRepository: AdminRepository,
    private val passwordEncoder: PasswordEncoder,
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String?) {
        if (adminRepository.count() == 0L) {
            val admin =
                Admin(
                    email = "admin@kindergarten.com",
                    password = passwordEncoder.encode("admin1234!"),
                    name = "Super Admin",
                    role = AdminRole.SUPER_ADMIN,
                )
            adminRepository.save(admin)
            log.info("Initial SUPER_ADMIN account created: admin@kindergarten.com")
        }
    }
}
