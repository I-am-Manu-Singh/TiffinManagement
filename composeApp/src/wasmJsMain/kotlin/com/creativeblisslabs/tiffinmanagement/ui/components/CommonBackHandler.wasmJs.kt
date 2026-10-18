package com.creativeblisslabs.tiffinmanagement.ui.components

import androidx.compose.runtime.Composable

@Composable
actual fun CommonBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No-op on WasmJs
}
