package com.eyr.demo.datasource.user

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserRepoEntity, Long>, JpaSpecificationExecutor<UserRepoEntity> {
    fun <T> findAllBy(pageable: Pageable, type: Class<T>): Page<T>
    fun <T> findAllByStatus(pageable: Pageable, status: UserRepoEntity.UserStatus, type: Class<T>): Page<T>
}