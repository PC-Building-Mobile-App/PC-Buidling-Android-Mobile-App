package com.iti.presentation.categorybuilds

import com.iti.presentation.categorybuilds.model.BuildUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildSelectionManager @Inject constructor() {
    private val _selectedBuilds = MutableStateFlow<List<BuildUiModel>>(emptyList())
    val selectedBuilds: StateFlow<List<BuildUiModel>> = _selectedBuilds.asStateFlow()

    private val _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode: StateFlow<Boolean> = _isSelectionMode.asStateFlow()

    fun toggleSelectionMode() {
        _isSelectionMode.update { !it }
        if (!_isSelectionMode.value) {
            clearSelection()
        }
    }

    fun toggleBuildSelection(build: BuildUiModel) {
        _selectedBuilds.update { current ->
            if (current.any { it.id == build.id }) {
                current.filterNot { it.id == build.id }
            } else {
                if (current.size < 2) {
                    current + build
                } else {
                    current
                }
            }
        }
    }

    fun clearSelection() {
        _selectedBuilds.value = emptyList()
        _isSelectionMode.value = false
    }
}
