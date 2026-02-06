package com.sotti.kindergarten.exception

class InvalidCompareRequestException(
    message: String = "Center comparison requires 2 to 4 centers",
) : RuntimeException(message)
