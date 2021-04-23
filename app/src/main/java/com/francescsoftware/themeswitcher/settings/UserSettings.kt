package com.francescsoftware.themeswitcher.settings

import kotlinx.coroutines.flow.StateFlow

enum class AppTheme {
    MODE_DAY,
    MODE_NIGHT,
    MODE_AUTO;

    companion object {
        fun fromOrdinal(ordinal: Int) = values()[ordinal]
    }
}

interface UserSettings {
    val themeStream: StateFlow<AppTheme>
    var theme: AppTheme
}
