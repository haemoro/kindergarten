package com.sotti.kindergarten.repository

import com.sotti.kindergarten.entity.Center
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CenterRepository :
    JpaRepository<Center, UUID>,
    CenterRepositoryCustom {
    fun findByKinderCode(kinderCode: String): Center?

    fun findAllByKinderCodeIn(kinderCodes: List<String>): List<Center>
}
