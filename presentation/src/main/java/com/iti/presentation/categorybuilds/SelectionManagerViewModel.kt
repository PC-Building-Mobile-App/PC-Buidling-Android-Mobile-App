package com.iti.presentation.categorybuilds

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectionManagerViewModel @Inject constructor(
    val manager: BuildSelectionManager
) : ViewModel()
