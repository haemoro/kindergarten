package com.sotti.kindergarten.exception

import com.sotti.kindergarten.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(ex.errorCode.httpStatus)
            .body(
                ErrorResponse(
                    status = ex.errorCode.httpStatus.value(),
                    code = ex.errorCode.name,
                    message = ex.message,
                ),
            )

    @ExceptionHandler(CenterNotFoundException::class)
    fun handleCenterNotFound(ex: CenterNotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    status = HttpStatus.NOT_FOUND.value(),
                    code = "CENTER_NOT_FOUND",
                    message = ex.message ?: "Center not found",
                ),
            )

    @ExceptionHandler(InvalidCompareRequestException::class)
    fun handleInvalidCompareRequest(ex: InvalidCompareRequestException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    code = "INVALID_COMPARE_REQUEST",
                    message = ex.message ?: "Invalid comparison request",
                ),
            )

    @ExceptionHandler(FavoriteNotFoundException::class)
    fun handleFavoriteNotFound(ex: FavoriteNotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    status = HttpStatus.NOT_FOUND.value(),
                    code = "FAVORITE_NOT_FOUND",
                    message = ex.message ?: "Favorite not found",
                ),
            )

    @ExceptionHandler(DuplicateFavoriteException::class)
    fun handleDuplicateFavorite(ex: DuplicateFavoriteException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    status = HttpStatus.CONFLICT.value(),
                    code = "DUPLICATE_FAVORITE",
                    message = ex.message ?: "Favorite already exists",
                ),
            )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors =
            ex.bindingResult.fieldErrors
                .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    status = HttpStatus.BAD_REQUEST.value(),
                    code = "VALIDATION_ERROR",
                    message = errors.ifEmpty { "Validation failed" },
                ),
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    code = "INTERNAL_SERVER_ERROR",
                    message = ex.message ?: "An unexpected error occurred",
                ),
            )
}
