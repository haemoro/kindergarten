package com.sotti.kindergarten.security

import com.sotti.kindergarten.entity.Admin
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class AdminUserDetails(
    private val admin: Admin,
) : UserDetails {
    val id: UUID get() = admin.id!!
    val email: String get() = admin.email
    val name: String get() = admin.name
    val role get() = admin.role

    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_${admin.role.name}"))

    override fun getPassword(): String = admin.password

    override fun getUsername(): String = admin.email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = admin.isActive
}
