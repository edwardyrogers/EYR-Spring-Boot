package cc.worldline.customermanagement.v2.datasource.user

import cc.worldline.customermanagement.v2.business.user.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.*

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun <T> findAllByCustomerNumber(pageable: Pageable, customerNumber: String, type: Class<T>): Page<T>
    fun <T> findAllByIc(pageable: Pageable, ic: String, type: Class<T>): Page<T>
    fun <T> findAllBy(pageable: Pageable, type: Class<T>): Page<T>

    fun <T> findByUsernameIgnoreCase(username: String, type: Class<T>): Optional<T>
    fun <T> findById(id: BigInteger, type: Class<T>): Optional<T>
    fun <T> findByIc(ic: String, type: Class<T>): Optional<T>
    fun <T> findByCustomerNumber(customerNumber: String, type: Class<T>): Optional<T>
    fun <T> findByUsername(username: String, type: Class<T>): Optional<T>

    fun existsByUsernameAndPasswordModifiedDateBefore(name: String, passwordModifiedDate: LocalDateTime): Boolean
    fun existsByUsername(name: String): Boolean
    fun existsById(id: BigInteger): Boolean
}
