package com.sotti.kindergarten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "favorite",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["device_id", "center_id"]),
    ],
)
class Favorite(
    @Column(nullable = false, length = 255)
    val deviceId: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    val center: Center,
) : BaseEntity()
