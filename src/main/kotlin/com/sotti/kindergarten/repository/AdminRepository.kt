package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.Admin
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AdminRepository : JpaRepository<Admin, UUID> {
    fun findByEmail(email: String): Admin?

    fun existsByEmail(email: String): Boolean
}
