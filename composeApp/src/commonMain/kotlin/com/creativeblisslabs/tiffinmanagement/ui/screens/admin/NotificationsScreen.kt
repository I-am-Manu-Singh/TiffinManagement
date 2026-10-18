package com.creativeblisslabs.tiffinmanagement.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.models.ActivityNotificationItemModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinTopHeader

@Composable
fun NotificationsScreen(
    onNavigateBack: () -> Unit = {},
    onManageSettings: () -> Unit = {},
    onViewRoute: (String) -> Unit = {}
) {
    val authState by TiffinRepository.authState.collectAsState()
    val activeUser = authState.currentUser
    val role = (activeUser?.role ?: "admin").lowercase()
    val isCustomer = role == "customer" || role == "subscriber"
    val isRider = role == "rider" || role == "driver"

    val notifications by TiffinRepository.activityNotifications.collectAsState()
    var activeFilter by remember { mutableStateOf("all") } // 'all', 'unread', 'orders', 'holds', 'system'

    val unreadCount = notifications.count { it.isUnread }

    val filteredNotifications = remember(notifications, activeFilter, role) {
        val baseList = when {
            isCustomer -> notifications.filter { it.type == "dispatch" || it.type == "wallet" || it.type == "payment" || it.type == "hold" }
            isRider -> notifications.filter { it.type == "dispatch" || it.type == "cooking" }
            else -> notifications
        }
        when (activeFilter) {
            "unread" -> baseList.filter { it.isUnread }
            "orders" -> baseList.filter { it.type == "dispatch" || it.type == "cooking" }
            "holds" -> baseList.filter { it.type == "hold" }
            "system" -> baseList.filter { it.type == "wallet" || it.type == "payment" }
            else -> baseList
        }
    }

    val todayItems = filteredNotifications.filter { it.isToday }
    val yesterdayItems = filteredNotifications.filter { !it.isToday }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FD))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("All Notifications", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                if (unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color(0xFFAB2F00))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("$unreadCount New", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Text(
                text = "Mark all as read",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFAB2F00),
                modifier = Modifier.clickable { TiffinRepository.markAllNotificationsAsRead() }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            

            // Filter Chips Bar
            androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item{
                     Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFFF1F5F9))
                        .clickable { TiffinRepository.markAllNotificationsAsRead() }
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.DoneAll, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(15.dp))
                        Text("Mark all read", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
                    }
                }
                }
                item {
                    FilterChip(
                        label = "All",
                        count = notifications.size.toString(),
                        isSelected = activeFilter == "all",
                        onClick = { activeFilter = "all" }
                    )
                }
                item {
                    FilterChip(
                        label = "Unread",
                        count = unreadCount.toString(),
                        isSelected = activeFilter == "unread",
                        hasDot = unreadCount > 0,
                        onClick = { activeFilter = "unread" }
                    )
                }
                item {
                    FilterChip(
                        label = "Orders & Plans",
                        isSelected = activeFilter == "orders",
                        onClick = { activeFilter = "orders" }
                    )
                }
                item {
                    FilterChip(
                        label = "Kitchen Holds",
                        isSelected = activeFilter == "holds",
                        onClick = { activeFilter = "holds" }
                    )
                }
                item {
                    FilterChip(
                        label = "System & Pay",
                        isSelected = activeFilter == "system",
                        onClick = { activeFilter = "system" }
                    )
                }
            }
        }

        // Scrollable Notifications List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Today Group
            if (todayItems.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TODAY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "${todayItems.count { it.isUnread }} pending action",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }

                todayItems.forEach { item ->
                    NotificationCardItem(
                        item = item,
                        onAction = {
                            if (item.type == "hold") {
                                TiffinRepository.approveNotificationHold(item.id)
                            } else if (item.type == "dispatch") {
                                onViewRoute(item.routeCode ?: "route-1")
                            }
                        }
                    )
                }
            }

            // Yesterday Group
            if (yesterdayItems.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "YESTERDAY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Archived activity",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }

                yesterdayItems.forEach { item ->
                    NotificationCardItem(
                        item = item,
                        onAction = { }
                    )
                }
            }

            // "You're all caught up!" Footer Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.VerifiedUser, null, tint = Color(0xFF006947), modifier = Modifier.size(24.dp))
                    }

                    Text("You're all caught up!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    Text(
                        "No earlier notifications needing attention. Older kitchen logs are preserved in your reporting archive.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color.White)
                            .clickable { onManageSettings() }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Tune, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(15.dp))
                            Text("Manage Notification Settings & Channels", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFAB2F00))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    count: String? = null,
    isSelected: Boolean,
    hasDot: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(if (isSelected) Color(0xFFAB2F00) else Color(0xFFF1F5F9))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF475569)
            )
            if (hasDot) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(if (isSelected) Color.White else Color(0xFFAB2F00)))
            }
            if (count != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(if (isSelected) Color.White.copy(alpha = 0.25f) else Color(0xFFE2E8F0))
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = count,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else Color(0xFF334155)
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationCardItem(
    item: ActivityNotificationItemModel,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Category Icon Badge
            val (bgColor, iconColor, icon) = when (item.type) {
                "dispatch" -> Triple(Color(0xFFFFDBD1), Color(0xFFAB2F00), Icons.Default.TwoWheeler)
                "hold" -> Triple(Color(0xFFD8E3FB), Color(0xFF111C2D), Icons.Default.PauseCircle)
                "wallet" -> Triple(Color(0xFFFFDAD6), Color(0xFFBA1A1A), Icons.Default.AccountBalanceWallet)
                "payment" -> Triple(Color(0xFF6FFBBE), Color(0xFF002113), Icons.Default.Payments)
                else -> Triple(Color(0xFFD5E0F8), Color(0xFF545F73), Icons.Default.SoupKitchen)
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A),
                        modifier = Modifier.weight(1f)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (item.isUnread) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFAB2F00)))
                        }
                        Text(
                            text = item.timeAgoFormatted,
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    fontSize = 12.sp,
                    color = Color(0xFF475569)
                )

                // Contextual Footer & Action buttons
                Spacer(modifier = Modifier.height(8.dp))
                when (item.type) {
                    "dispatch" -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(22.dp))
                                        .background(Color(0xFFF1F5F9))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(item.routeCode ?: "Route #A-4", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                                }
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF006947), modifier = Modifier.size(12.dp))
                                    Text("On Schedule", fontSize = 10.sp, color = Color(0xFF006947), fontWeight = FontWeight.Medium)
                                }
                            }

                            Button(
                                onClick = onAction,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00)),
                                shape = RoundedCornerShape(22.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("View Route", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(12.dp))
                                }
                            }
                        }
                    }
                    "hold" -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = onAction,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006947)),
                                shape = RoundedCornerShape(22.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 5.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(13.dp))
                                    Text(item.actionLabel ?: "Approve Hold", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                    "wallet" -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Color(0xFFFFDAD6))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Auto-dispatch paused", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBA1A1A))
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .clickable { onAction() }
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Default.Send, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(12.dp))
                                    Text("Send Reminder", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00))
                                }
                            }
                        }
                    }
                    "payment" -> {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("Txn: ${item.txnCode ?: "#UPI88291"}", fontSize = 10.sp, color = Color(0xFF475569))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Icon(Icons.Default.ReceiptLong, null, tint = Color(0xFF006947), modifier = Modifier.size(12.dp))
                                Text("Auto-receipt Sent", fontSize = 10.sp, color = Color(0xFF006947), fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                    "cooking" -> {
                        if (item.chefName != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(item.chefName, fontSize = 10.sp, color = Color(0xFF475569))
                            }
                        }
                    }
                }
            }
        }
    }
}
