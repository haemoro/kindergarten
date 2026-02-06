package com.sotti.kindergarten.exception

class FavoriteNotFoundException(
    message: String = "Favorite not found",
) : RuntimeException(message)
