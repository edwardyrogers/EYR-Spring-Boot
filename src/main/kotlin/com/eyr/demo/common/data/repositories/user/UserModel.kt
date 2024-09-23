package com.eyr.demo.common.data.repositories.user

import com.eyr.demo.common.data.repositories.user.UserHelper.UserRole
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "_user")
data class UserModel(
    @Id
    @GeneratedValue
    private val id: Long? = null,

    @Column
    private val username: String = "",

    @Column
    private val password: String = "",

    @Column
    @Enumerated(EnumType.ORDINAL)
    val role: UserRole = UserRole.REGULAR,
) : UserDetails {
    fun getUserID(): Long? = id

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = role.authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}