package com.eyr.demo.common.data.repos.dance

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DanceRepository: JpaRepository<DanceModel, Long?>