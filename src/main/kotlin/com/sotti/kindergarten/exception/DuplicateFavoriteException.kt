package com.sotti.kindergarten.exception

class DuplicateFavoriteException(
    message: String = "Favorite already exists",
) : RuntimeException(message)
