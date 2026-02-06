package com.sotti.kindergarten.exception

import java.util.UUID

class CenterNotFoundException(
    centerId: UUID,
) : RuntimeException("Center not found: $centerId")
