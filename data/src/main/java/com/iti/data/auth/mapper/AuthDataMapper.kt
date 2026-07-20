package com.iti.data.auth.mapper

import com.iti.data.auth.model.AuthResponseDto
import com.iti.domain.auth.model.AuthUser

fun AuthResponseDto.toDomain(): AuthUser =
    AuthUser(
        id = data.user.id,
        name = data.user.name,
        email = data.user.email,
        token = data.token,
        tokenType = data.tokenType,
    )