package com.eyr.demo.common.data.repositories.dance

import jakarta.persistence.*

@Entity
data class DanceModel(
    @Id
    @GeneratedValue
    private val id: Long? = null,

    @Column
    @Enumerated(EnumType.ORDINAL)
    val type: DanceHelper.DanceType? = null,

    @Column
    val name: String = "",
)