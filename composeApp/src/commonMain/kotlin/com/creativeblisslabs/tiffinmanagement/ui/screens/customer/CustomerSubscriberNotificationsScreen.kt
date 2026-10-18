package com.creativeblisslabs.tiffinmanagement.ui.screens.customer

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.models.SubscriberNotification
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinCustomerHeader
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import kotlinx.coroutines.launch

@Composable
fun CustomerSubscriberNotificationsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToLiveTracker: () -> Unit,
    onNavigateToFeedback: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToSchedule: () -> Unit,
    onNavigateToGuestMeal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val notifications: List<SubscriberNotification> by TiffinRepository.notifications.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedFilter by remember { mutableStateOf("all") }

    val unreadCount = notifications.count { it.isUnread }

    val filteredNotifications: List<SubscriberNotification> = remember(notifications, selectedFilter) {
        when (selectedFilter) {
            "delivery" -> notifications.filter { it.category == "delivery" }
            "wallet" -> notifications.filter { it.category == "wallet" }
            "announcement" -> notifications.filter { it.category == "announcement" }
            else -> notifications
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = ColorTokens.Surface,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorTokens.Surface)
        ) {
            // Header
            TiffinCustomerHeader(
                title = "Notifications",
                subtitle = "TIFFIN MANAGER",
                isActive = true,
                onNotificationClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("You have $unreadCount new delivery alerts.")
                    }
                },
                onProfileClick = onNavigateToProfile
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Screen sub-header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.SurfaceContainer)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Go Back",
                                tint = ColorTokens.OnSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "Notifications",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ColorTokens.Primary)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "$unreadCount New",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnPrimary
                                )
                            }
                        }
                    }

                    TextButton(
                        onClick = {
                            TiffinRepository.markAllNotificationsRead()
                            scope.launch {
                                snackbarHostState.showSnackbar("All notifications marked as read.")
                            }
                        }
                    ) {
                        Text("Mark all as read", style = MaterialTheme.typography.labelMedium, color = ColorTokens.Primary)
                    }
                }

                // Filter Tabs (All, Meals & Delivery, Wallet & Billing, Kitchen Announcements)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val tabs = listOf(
                        "all" to "All (${notifications.size})",
                        "delivery" to "Meals (${notifications.count { it.category == "delivery" }})",
                        "wallet" to "Wallet (${notifications.count { it.category == "wallet" }})",
                        "announcement" to "Announce (${notifications.count { it.category == "announcement" }})"
                    )

                    tabs.forEach { (key, label) ->
                        val isSelected = selectedFilter == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) ColorTokens.OnSecondaryFixed else ColorTokens.SurfaceContainerLowest)
                                .clickable { selectedFilter = key }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) ColorTokens.OnSecondary else ColorTokens.Secondary
                            )
                        }
                    }
                }

                // Notification Feed
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    filteredNotifications.forEach { notif ->
                        NotificationCard(
                            notif = notif,
                            onPrimaryClick = {
                                when (notif.actionPrimaryTitle) {
                                    "Track Driver (Live)" -> onNavigateToLiveTracker()
                                    "Skip Dinner" -> {
                                        TiffinRepository.skipDinnerWithRefund()
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Tonight's dinner skipped. ₹120 credited to Wallet.")
                                        }
                                    }
                                    "View Balance Ledger" -> onNavigateToWallet()
                                    "Rate Dish (5/5)" -> onNavigateToFeedback()
                                    "Recharge Now" -> onNavigateToWallet()
                                    else -> {}
                                }
                            },
                            onSecondaryClick = {
                                when (notif.actionSecondaryTitle) {
                                    "Call Driver" -> {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Calling your delivery partner...")
                                        }
                                    }
                                    "Add Guest Meal" -> onNavigateToGuestMeal()
                                    else -> {}
                                }
                            }
                        )
                    }
                }

               

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notif: SubscriberNotification,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notif.isUnread) 3.dp else 1.dp),
        border = if (notif.isUnread) androidx.compose.foundation.BorderStroke(1.dp, ColorTokens.PrimaryFixed) else null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Category Icon with unread badge
            Box(modifier = Modifier.size(40.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            when (notif.category) {
                                "delivery" -> ColorTokens.PrimaryFixed
                                "wallet" -> ColorTokens.TertiaryFixed
                                "announcement" -> ColorTokens.SecondaryContainer
                                else -> ColorTokens.SurfaceContainer
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (notif.category) {
                            "delivery" -> Icons.Default.TwoWheeler
                            "wallet" -> Icons.Default.AccountBalanceWallet
                            "announcement" -> Icons.Default.SoupKitchen
                            else -> Icons.Default.Notifications
                        },
                        contentDescription = null,
                        tint = when (notif.category) {
                            "delivery" -> ColorTokens.Primary
                            "wallet" -> ColorTokens.OnTertiaryFixed
                            "announcement" -> ColorTokens.OnSecondaryContainer
                            else -> ColorTokens.OnSurface
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (notif.isUnread) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(ColorTokens.Primary)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = notif.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (notif.isUrgent) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ColorTokens.ErrorContainer)
                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                            ) {
                                Text("Urgent", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnErrorContainer)
                            }
                        }
                    }

                    Text(
                        text = notif.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = if (notif.isUnread) ColorTokens.Primary else ColorTokens.Secondary
                    )
                }

                Text(
                    text = notif.body,
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorTokens.OnSurfaceVariant
                )

                // Action Buttons
                if (notif.actionPrimaryTitle != null || notif.actionSecondaryTitle != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        if (notif.actionPrimaryTitle != null) {
                            Button(
                                onClick = onPrimaryClick,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ColorTokens.Primary,
                                    contentColor = ColorTokens.OnPrimary
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text(notif.actionPrimaryTitle, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (notif.actionSecondaryTitle != null) {
                            OutlinedButton(
                                onClick = onSecondaryClick,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = ColorTokens.SurfaceContainerLow,
                                    contentColor = ColorTokens.OnSurface
                                ),
                                border = null,
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text(notif.actionSecondaryTitle, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
