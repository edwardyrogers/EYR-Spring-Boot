package com.eyr.demo.common.data.repos.dance

import jakarta.persistence.*

@Entity
data class DanceModel(
    @Id
    @GeneratedValue
    private val id: Long? = null,

    @Column
    @Enumerated(EnumType.ORDINAL)
    val type: DanceUtil.DanceType = DanceUtil.DanceType.NO_TYPE,

    @Column
    val name: String = "",
)