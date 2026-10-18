package com.creativeblisslabs.tiffinmanagement.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.generated.resources.Res
import com.creativeblisslabs.tiffinmanagement.generated.resources.app_logo
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import org.jetbrains.compose.resources.painterResource

/**
 * Universal Header for Subscriber / Customer screens matching stitch_cross_platform_universal_ui_design 11:
 * - Brand logo (Res.drawable.app_logo)
 * - Screen Title with "Active" badge
 * - "TIFFIN MANAGER" subtitle
 * - Notification bell with red badge
 * - Subscriber avatar circle
 */
@Composable
fun TiffinCustomerHeader(
    title: String,
    isActive: Boolean = true,
    subtitle: String = "TIFFIN MANAGER",
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSettingClick: () -> Unit = {}
) {
    val subscriberProfile by com.example.myapplicationkmp.data.repository.TiffinRepository.subscriberProfile.collectAsState()
    val branch by com.example.myapplicationkmp.data.repository.TiffinRepository.currentBranch.collectAsState()
    val displayName = subscriberProfile.fullName.ifBlank { "Subscriber" }

    TiffinTopHeader(
        branchName = branch.name,
        screenTitle = title,
        userName = displayName,
        userRole = "Customer",
        subtitle = subtitle,
        onNotificationClick = onNotificationClick,
        onProfileClick = onProfileClick,
        onSettingClick = onSettingClick
    )
}
