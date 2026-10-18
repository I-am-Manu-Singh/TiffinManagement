package com.creativeblisslabs.tiffinmanagement.ui.screens.rider

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.models.RiderNotificationAlert
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun RiderNotificationsAlertsScreen(
    repository: TiffinRepository,
    onBack: () -> Unit,
    onNavigateToStops: () -> Unit,
    onNavigateToPayouts: () -> Unit
) {
    val notifications by repository.riderNotifications.collectAsState()
    val riderProfile by repository.riderProfile.collectAsState()

    var selectedFilter by remember { mutableStateOf("all") } // all, route, kitchen, payouts
    val unreadCount = notifications.count { it.isUnread }

    val filteredNotifications = notifications.filter {
        selectedFilter == "all" || it.category == selectedFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        // Sticky Header
        val branch by repository.currentBranch.collectAsState()
        com.example.myapplicationkmp.ui.components.TiffinTopHeader(
            branchName = branch.name,
            screenTitle = "Rider Notifications",
            userName = riderProfile.fullName.ifBlank { "Rider" },
            userRole = "Rider",
            subtitle = if (unreadCount > 0) "$unreadCount New Alerts" else "All Alerts Read",
            onNotificationClick = {},
            onProfileClick = onBack
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorTokens.SurfaceContainerLowest)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mark all as read",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = ColorTokens.Primary,
                modifier = Modifier.clickable { repository.markAllRiderNotificationsRead() }
            )
        }

        // Rider Identity Sub-strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorTokens.SurfaceContainerLow)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(ColorTokens.PrimaryFixed),
                    contentAlignment = Alignment.Center
                ) {
                    Text("RK", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimaryFixed)
                }
                Column {
                    Text(riderProfile.fullName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                    Text("Pod Carrier ${riderProfile.carrierPodId} • East Route", fontSize = 10.sp, color = ColorTokens.Secondary)
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(22.dp))
                    .background(ColorTokens.TertiaryContainer.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ColorTokens.Tertiary))
                    Text("Live Sync", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Tertiary)
                }
            }
        }

        // Filter Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val pills = listOf(
                "all" to "All (${notifications.size})",
                "route" to "Route & Stops (${notifications.count { it.category == "route" }})",
                "kitchen" to "Kitchen & Bag (${notifications.count { it.category == "kitchen" }})",
                "payouts" to "Payouts (${notifications.count { it.category == "payouts" }})"
            )

            pills.forEach { (key, label) ->
                val isSelected = selectedFilter == key
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(if (isSelected) ColorTokens.OnSurface else ColorTokens.SurfaceContainer)
                        .clickable { selectedFilter = key }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) ColorTokens.Surface else ColorTokens.Secondary
                    )
                }
            }
        }

        // Lazy Notifications Feed
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredNotifications, key = { it.id }) { notif ->
                RiderNotificationCard(
                    notification = notif,
                    onActionClick = {
                        when (notif.actionType) {
                            "view_sequence" -> onNavigateToStops()
                            "view_payout" -> onNavigateToPayouts()
                            "box_return" -> onNavigateToStops()
                            else -> {}
                        }
                    }
                )
            }

            // Standard Hygiene Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Pod Hygiene Standard", fontSize = 11.sp, color = ColorTokens.Secondary)
                            Text("98.4% On-Time Carrier Dispatch Score", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        }
                        Icon(Icons.Default.Stars, contentDescription = null, tint = ColorTokens.Tertiary, modifier = Modifier.size(20.dp))
                    }
                }
            }

            // Footer Link
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Notification Sound & Vibration Settings", fontSize = 12.sp, color = ColorTokens.Secondary)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun RiderNotificationCard(
    notification: RiderNotificationAlert,
    onActionClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Icon Box
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            when (notification.category) {
                                "route" -> ColorTokens.Primary.copy(alpha = 0.1f)
                                "kitchen" -> ColorTokens.SecondaryContainer
                                "payouts" -> ColorTokens.TertiaryContainer.copy(alpha = 0.2f)
                                else -> ColorTokens.SurfaceContainer
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val iconVector = when (notification.category) {
                        "route" -> if (notification.priority == "urgent") Icons.Default.AltRoute else Icons.Default.Inventory2
                        "kitchen" -> Icons.Default.Thermostat
                        "payouts" -> Icons.Default.AccountBalanceWallet
                        else -> Icons.Default.Info
                    }
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = when (notification.category) {
                            "route" -> ColorTokens.Primary
                            "kitchen" -> ColorTokens.OnSecondaryContainer
                            "payouts" -> ColorTokens.Tertiary
                            else -> ColorTokens.Secondary
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (notification.isUnread) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ColorTokens.Primary))
                            }
                            Text(
                                text = if (notification.priority == "urgent") "URGENT PRIORITY" else notification.category.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (notification.priority == "urgent") ColorTokens.Primary else ColorTokens.Tertiary
                            )
                        }
                        Text(notification.timestampDisplay, fontSize = 10.sp, color = ColorTokens.Secondary)
                    }

                    Text(
                        text = notification.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                }
            }

            Text(
                text = notification.body,
                fontSize = 12.sp,
                color = ColorTokens.Secondary,
                modifier = Modifier.padding(start = 48.dp)
            )

            // Optional Action Buttons
            if (notification.actionType != null) {
                Row(
                    modifier = Modifier.padding(start = 48.dp, top = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (notification.actionType) {
                        "view_sequence" -> {
                            Button(
                                onClick = onActionClick,
                                modifier = Modifier.height(34.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary)
                            ) {
                                Icon(Icons.Default.Navigation, contentDescription = null, tint = ColorTokens.OnPrimary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("View New Sequence", fontSize = 11.sp, color = ColorTokens.OnPrimary)
                            }
                        }
                        "view_payout" -> {
                            Button(
                                onClick = onActionClick,
                                modifier = Modifier.height(34.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainerHigh)
                            ) {
                                Text("View Payout", fontSize = 11.sp, color = ColorTokens.OnSurface)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = ColorTokens.OnSurface, modifier = Modifier.size(14.dp))
                            }
                        }
                        "box_return" -> {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("Floor 4, Flat 402 • Green Bag", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                        }
                    }
                }
            }
        }
    }
}
