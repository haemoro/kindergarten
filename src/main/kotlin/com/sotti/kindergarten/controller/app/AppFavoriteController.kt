package com.sotti.kindergarten.controller.app

import com.sotti.kindergarten.dto.FavoriteRequest
import com.sotti.kindergarten.dto.FavoriteResponse
import com.sotti.kindergarten.dto.PageResponse
import com.sotti.kindergarten.service.FavoriteService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/app/favorites")
@Validated
class AppFavoriteController(
    private val favoriteService: FavoriteService,
) {
    @GetMapping
    fun getFavorites(
        @RequestParam deviceId: String,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "20") size: Int,
    ): PageResponse<FavoriteResponse> =
        favoriteService.getFavorites(
            deviceId = deviceId,
            page = page,
            size = size,
        )

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addFavorite(
        @RequestBody @Valid request: FavoriteRequest,
    ): FavoriteResponse = favoriteService.addFavorite(request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeFavorite(
        @PathVariable id: UUID,
        @RequestParam deviceId: String,
    ) {
        favoriteService.removeFavorite(id, deviceId)
    }
}
