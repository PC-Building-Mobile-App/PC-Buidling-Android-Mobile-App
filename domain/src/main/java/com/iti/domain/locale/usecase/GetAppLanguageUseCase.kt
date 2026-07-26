package com.iti.domain.locale.usecase

import com.iti.domain.locale.repository.LocaleRepository
import javax.inject.Inject

class GetAppLanguageUseCase @Inject constructor(
    private val repository: LocaleRepository,
) {
    operator fun invoke(): String = repository.getLanguageTag()
}