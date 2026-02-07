package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "region")
class Region(
    @Column(name = "sido_code", nullable = false)
    val sidoCode: String,
    @Column(name = "sido_name", nullable = false)
    val sidoName: String,
    @Id
    @Column(name = "sgg_code", nullable = false)
    val sggCode: String,
    @Column(name = "sgg_name", nullable = false)
    val sggName: String,
)
