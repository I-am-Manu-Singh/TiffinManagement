package com.creativeblisslabs.tiffinmanagement.ui.screens.customer

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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.NoMeals
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinCustomerHeader
import kotlinx.coroutines.launch

/**
 * Customer / Subscriber Dashboard Screen
 * Matches stitch_cross_platform_universal_ui_design 11/customer_dashboard
 * Features:
 * - Interactive Live Status Stepper: Cutoff -> Prepped -> On Way -> Drop-off
 * - Assigned Valet Info Card (Rajesh Kumar ★4.9 with Call & Map tracking)
 * - Today's Curated Lunch Thali breakdown (Paneer Butter Masala, Dal Tadka, 4 Phulkas, Jeera Rice, Raita, Gulab Jamun)
 * - Dinner 04:00 PM cutoff notice
 * - Quick Actions: Skip Dinner (+₹120 Refund), Drop Note (Door Handle edit dialog), + Guest Meal (+₹110)
 * - Active Drop Note Pill Bar
 * - Upcoming Week 42 Menu preview (Wed, Thu, Fri dishes)
 * - Cloud Kitchen Verified card with FSSAI & WhatsApp/Call support
 */
@Composable
fun CustomerDashboardScreen(
    onNavigateToGuestMeal: () -> Unit,
    onNavigateToSchedule: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onEditDropNote: () -> Unit,
    onNavigateToLiveTracker: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToMealRating: () -> Unit = {},
    onNavigateToReportIssue: () -> Unit = {}
) {
    val subscriberProfile by TiffinRepository.subscriberProfile.collectAsState()
    val riderProfile by TiffinRepository.riderProfile.collectAsState()
    val riderStops by TiffinRepository.riderStops.collectAsState()
    val currentBranch by TiffinRepository.currentBranch.collectAsState()

    val currentStop = riderStops.firstOrNull { it.customerName.equals(subscriberProfile.fullName, ignoreCase = true) || it.subscriberIdCode == subscriberProfile.subscriberNumber }
        ?: riderStops.firstOrNull { it.sequenceNumber == 19 }
        ?: riderStops.first()
    val isDelivered = currentStop.status == "delivered"
    val deliveryStatusText = if (isDelivered) "Delivered ✓" else "Out for Delivery"

    val driverName = riderProfile.fullName.ifBlank { "Your Delivery Partner" }
    val driverPhone = riderProfile.phone?.ifBlank { null }
    val kitchenName = currentBranch.name.ifBlank { "Your Kitchen" }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showSkipDialog by remember { mutableStateOf(false) }
    var showDropNoteDialog by remember { mutableStateOf(false) }
    var editedDropNote by remember { mutableStateOf(subscriberProfile.driverDropNote) }
    var isDinnerSkipped by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = ColorTokens.Surface
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorTokens.Surface)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Screen Header
                TiffinCustomerHeader(
                    title = "Today",
                    isActive = true,
                    subtitle = "TIFFIN MANAGER",
                    onNotificationClick = onNavigateToNotifications,
                    onProfileClick = onNavigateToProfile
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 1. Interactive Live Delivery Tracker Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onNavigateToLiveTracker)
                    ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Header & ETA
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(ColorTokens.PrimaryFixed)
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isDelivered) ColorTokens.Tertiary else ColorTokens.Primary)
                                            )
                                            Text(
                                                text = deliveryStatusText,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ColorTokens.OnPrimaryFixed
                                            )
                                        }
                                    }

                                    Text(
                                        text = "• Tiffin #${currentStop.sequenceNumber + 380}",
                                        fontSize = 12.sp,
                                        color = ColorTokens.Secondary
                                    )
                                }

                                Text(
                                    text = "Lunch Express Dispatch",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (isDelivered) "Delivered" else "Estimated Slot",
                                    fontSize = 11.sp,
                                    color = ColorTokens.Secondary
                                )
                                Text(
                                    text = if (isDelivered) "Completed ✓" else subscriberProfile.lunchSlotWindow,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDelivered) ColorTokens.Tertiary else ColorTokens.Primary
                                )
                            }
                        }

                        // Live Route Mini Map Preview
                        com.example.myapplicationkmp.ui.components.TiffinMiniMapThumbnail(
                            latitude = currentStop.latitude,
                            longitude = currentStop.longitude,
                            label = subscriberProfile.fullName.ifBlank { "Delivery Address" },
                            sequenceNumber = currentStop.sequenceNumber,
                            isCompleted = isDelivered,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                            onClickNavigation = onNavigateToLiveTracker
                        )

                        StepperTracker(isDelivered = isDelivered)

                        // Assigned Valet Info Box
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.SecondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "RK",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = ColorTokens.OnSecondaryContainer
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = driverName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = ColorTokens.OnSurface
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = ColorTokens.Tertiary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "4.9",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = ColorTokens.Tertiary
                                        )
                                    }

                                    Text(
                                        text = "Route: HSR Sector 2 • 2 stops left",
                                        fontSize = 11.sp,
                                        color = ColorTokens.Secondary
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.SurfaceContainer)
                                        .clickable {
                                             scope.launch {
                                                val callMsg = if (driverPhone != null) "Connecting to $driverName ($driverPhone)..." else "Connecting to $driverName..."
                                                snackbarHostState.showSnackbar(callMsg)
                                             }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Call Driver",
                                        tint = ColorTokens.OnSurface,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(ColorTokens.Primary)
                                        .clickable {
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Live GPS: Valet is 650m away near 14th Main Cross.")
                                            }
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.NearMe,
                                            contentDescription = null,
                                            tint = ColorTokens.OnPrimary,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Text(
                                            text = "Map",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. Today's Curated Lunch Thali Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Dish Visual Gradient Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF6B2700), Color(0xFF281005))
                                    )
                                )
                                .padding(14.dp)
                        ) {
                            // Top Badges
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(ColorTokens.SurfaceContainerLowest.copy(alpha = 0.92f))
                                        .padding(horizontal = 9.dp, vertical = 3.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(7.dp)
                                                .clip(CircleShape)
                                                .background(ColorTokens.Tertiary)
                                        )
                                        Text(
                                            text = "Pure Veg Thali",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnSurface
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.Black.copy(alpha = 0.45f))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "Regular Plan",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                }
                            }

                            // Bottom Dish Headline
                            Column(
                                modifier = Modifier.align(Alignment.BottomStart),
                                verticalArrangement = Arrangement.spacedBy(1.dp)
                            ) {
                                Text(
                                    text = "TODAY'S CURATED LUNCH",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.8f),
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Shahi Paneer Deluxe Meal",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Dish items breakdown
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Menu Breakdown",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.Secondary
                                )
                                Text(
                                    text = "6 Items Included",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.Primary
                                )
                            }

                            val dishes = listOf(
                                "Paneer Butter Masala",
                                "Dal Tadka (Garlic Temper)",
                                "4 Phulka Rotis (Desi Ghee)",
                                "Jeera Basmati Rice",
                                "Cucumber Boondi Raita"
                            )

                            // Dish chips row
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                dishes.chunked(2).forEach { rowItems ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        rowItems.forEach { item ->
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(ColorTokens.SurfaceContainerLow)
                                                    .padding(horizontal = 8.dp, vertical = 7.dp)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(5.dp)
                                                            .clip(CircleShape)
                                                            .background(ColorTokens.Tertiary)
                                                    )
                                                    Text(
                                                        text = item,
                                                        fontSize = 11.sp,
                                                        color = ColorTokens.OnSurface,
                                                        fontWeight = FontWeight.Medium,
                                                        maxLines = 1
                                                    )
                                                }
                                            }
                                        }
                                        if (rowItems.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }

                                // Dessert item
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ColorTokens.PrimaryFixed.copy(alpha = 0.5f))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Restaurant,
                                            contentDescription = null,
                                            tint = ColorTokens.OnPrimaryFixed,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = "Gulab Jamun (1 pc)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnPrimaryFixed
                                        )
                                    }
                                }
                            }

                            // Dinner cutoff notice
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SecondaryContainer.copy(alpha = 0.45f))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = ColorTokens.Secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                    Text(
                                        text = "Dinner changes lock today at 04:00 PM",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "Any skip requests or add-on orders after this cutoff will roll over to the next billing cycle.",
                                        fontSize = 11.sp,
                                        color = ColorTokens.Secondary,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Quick Emergency Self-Service Actions
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quick Actions",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = "Instant updates",
                            fontSize = 11.sp,
                            color = ColorTokens.Secondary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Action 1: Skip Dinner
                        QuickActionButton(
                            title = if (isDinnerSkipped) "Skipped" else "Skip Dinner",
                            subtitle = if (isDinnerSkipped) "Refund Done" else "+₹120 Refund",
                            icon = Icons.Default.NoMeals,
                            iconBg = ColorTokens.ErrorContainer,
                            iconTint = ColorTokens.Error,
                            subtitleColor = ColorTokens.Tertiary,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (!isDinnerSkipped) {
                                    showSkipDialog = true
                                }
                            }
                        )

                        // Action 2: Change Drop Note
                        QuickActionButton(
                            title = "Drop Note",
                            subtitle = "Door Handle",
                            icon = Icons.Default.EditNote,
                            iconBg = ColorTokens.SurfaceContainerHigh,
                            iconTint = ColorTokens.OnSurface,
                            subtitleColor = ColorTokens.Secondary,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                editedDropNote = subscriberProfile.driverDropNote
                                showDropNoteDialog = true
                            }
                        )

                        // Action 3: Add Guest Meal
                        QuickActionButton(
                            title = "+ Guest Meal",
                            subtitle = "+₹110",
                            icon = Icons.Default.AddCircle,
                            iconBg = ColorTokens.TertiaryFixed,
                            iconTint = ColorTokens.Tertiary,
                            subtitleColor = ColorTokens.Primary,
                            modifier = Modifier.weight(1f),
                            onClick = onNavigateToGuestMeal
                        )
                    }

                    // Active Drop Note Pill Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLowest)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PinDrop,
                                contentDescription = null,
                                tint = ColorTokens.Primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Delivery note: ${subscriberProfile.driverDropNote}",
                                fontSize = 12.sp,
                                color = ColorTokens.OnSurface,
                                maxLines = 1
                            )
                        }

                        Text(
                            text = "Edit",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.Primary,
                            modifier = Modifier
                                .clickable {
                                    editedDropNote = subscriberProfile.driverDropNote
                                    showDropNoteDialog = true
                                }
                                .padding(start = 6.dp)
                        )
                    }
                }

                // 4. Upcoming Week 42 Menu Sneak Peek
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Upcoming Menu",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ColorTokens.SurfaceContainerHigh)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Week 42",
                                    fontSize = 10.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onNavigateToSchedule() }
                        ) {
                            Text(
                                text = "Full Month",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.Primary
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = ColorTokens.Primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Wednesday
                    UpcomingMenuRow(
                        day = "Wed",
                        date = "23",
                        title = "Rajma Masala & Jeera Chawal",
                        subtitle = "Aloo Gobi Adraki • 3 Tawa Rotis • Boondi Raita",
                        badge = "Scheduled"
                    )

                    // Thursday
                    UpcomingMenuRow(
                        day = "Thu",
                        date = "24",
                        title = "Dal Makhani & Mix Vegetable",
                        subtitle = "Steamed Rice • 4 Phulkas • Fresh Green Salad",
                        badge = "Scheduled"
                    )

                    // Friday
                    UpcomingMenuRow(
                        day = "Fri",
                        date = "25",
                        title = "Mutter Mushroom & Veg Pulao",
                        subtitle = "Yellow Moong Dal • Laccha Paratha • Kheer",
                        badge = "Special"
                    )
                }

                // 5. Kitchen Details & Operational Compliance
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(ColorTokens.Primary.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Storefront,
                                        contentDescription = null,
                                        tint = ColorTokens.Primary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = kitchenName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "Branch 01 • Koramangala Hub",
                                        fontSize = 11.sp,
                                        color = ColorTokens.Secondary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ColorTokens.TertiaryFixed)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "Verified",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnTertiaryFixed
                                )
                            }
                        }

                        // License metadata
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = ColorTokens.Tertiary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = if (currentBranch.fssaiLicenseNumber.isNotBlank()) "FSSAI Lic #${currentBranch.fssaiLicenseNumber}" else "FSSAI Licensed",
                                    fontSize = 11.sp,
                                    color = ColorTokens.Secondary
                                )
                            }

                            Text(
                                text = "Hygiene Grade A+",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.Tertiary
                            )
                        }

                        // Quick Support Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF25D366).copy(alpha = 0.12f))
                                    .clickable {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Opening WhatsApp chat with Kitchen Master...")
                                        }
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Chat,
                                        contentDescription = null,
                                        tint = Color(0xFF075E54),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "WhatsApp Mess",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF075E54)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainer)
                                    .clickable {
                                     scope.launch {
                                         val kitchenContact = if (currentBranch.phone.isNotBlank()) "Calling Kitchen Helpdesk..." else "Calling Kitchen Helpdesk..."
                                         snackbarHostState.showSnackbar(kitchenContact)
                                     }
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = null,
                                        tint = ColorTokens.OnSurface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Call Kitchen",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Skip Dinner Confirmation Modal
        if (showSkipDialog) {
            AlertDialog(
                onDismissRequest = { showSkipDialog = false },
                title = {
                    Text(
                        text = "Skip Tonight's Dinner?",
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                },
                text = {
                    Text(
                        text = "Skip tonight's dinner before 04:00 PM cutoff? ₹120.00 will be refunded instantly to your prepaid mess wallet.",
                        color = ColorTokens.Secondary,
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val refund = TiffinRepository.skipDinnerWithRefund()
                            isDinnerSkipped = true
                            showSkipDialog = false
                            scope.launch {
                                snackbarHostState.showSnackbar("Dinner skipped! ₹$refund credited to Mess Wallet.")
                            }
                        }
                    ) {
                        Text("Confirm Skip", color = ColorTokens.Primary, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSkipDialog = false }) {
                        Text("Keep Dinner", color = ColorTokens.Secondary)
                    }
                }
            )
        }

        // Drop Note Edit Modal
        if (showDropNoteDialog) {
            AlertDialog(
                onDismissRequest = { showDropNoteDialog = false },
                title = {
                    Text(
                        text = "Update Driver Drop Note",
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Special drop instructions for your delivery partner:",
                            fontSize = 12.sp,
                            color = ColorTokens.Secondary
                        )
                        OutlinedTextField(
                            value = editedDropNote,
                            onValueChange = { editedDropNote = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g. Leave bag on handle of door 402") }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            TiffinRepository.updateDropNote(editedDropNote.trim())
                            showDropNoteDialog = false
                            scope.launch {
                                snackbarHostState.showSnackbar("Drop note updated for delivery partner.")
                            }
                        }
                    ) {
                        Text("Save Note", color = ColorTokens.Primary, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDropNoteDialog = false }) {
                        Text("Cancel", color = ColorTokens.Secondary)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun TrackerStepItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isActive: Boolean,
    isDone: Boolean,
    isCurrent: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    if (isCurrent) ColorTokens.Primary
                    else if (isDone) ColorTokens.Primary
                    else ColorTokens.SurfaceContainerHigh
                )
                .then(
                    if (isCurrent) Modifier.border(3.dp, ColorTokens.PrimaryFixed, CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isActive || isDone || isCurrent) ColorTokens.OnPrimary else ColorTokens.Secondary,
                modifier = Modifier.size(15.dp)
            )
        }

        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
            color = if (isCurrent) ColorTokens.Primary else ColorTokens.OnSurface
        )

        Text(
            text = subtitle,
            fontSize = 9.sp,
            color = if (isCurrent) ColorTokens.Primary else ColorTokens.Secondary
        )
    }
}

@Composable
private fun QuickActionButton(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    subtitleColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTokens.OnSurface
            )

            Text(
                text = subtitle,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = subtitleColor
            )
        }
    }
}

@Composable
private fun UpcomingMenuRow(
    day: String,
    date: String,
    title: String,
    subtitle: String,
    badge: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ColorTokens.SurfaceContainerLow),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        Text(
                            text = day.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.Secondary
                        )
                        Text(
                            text = date,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface,
                            maxLines = 1
                        )
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.Tertiary)
                        )
                    }
                    Text(
                        text = subtitle,
                        fontSize = 10.sp,
                        color = ColorTokens.Secondary,
                        maxLines = 1
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(ColorTokens.SurfaceContainer)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = badge,
                    fontSize = 10.sp,
                    color = ColorTokens.Secondary
                )
            }
        }
    }
}

@Composable
fun StepperTracker(isDelivered: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Step 1: Cutoff
            TrackerStepNode(
                icon = Icons.Default.Check,
                isActive = true,
                isDone = true,
                isCurrent = false
            )

            // Line 1 -> 2
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(3.dp)
                    .background(ColorTokens.Primary)
            )

            // Step 2: Prepped
            TrackerStepNode(
                icon = Icons.Default.SoupKitchen,
                isActive = true,
                isDone = true,
                isCurrent = false
            )

            // Line 2 -> 3
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(3.dp)
                    .background(ColorTokens.Primary)
            )

            // Step 3: On Way
            TrackerStepNode(
                icon = Icons.Default.TwoWheeler,
                isActive = true,
                isDone = isDelivered,
                isCurrent = !isDelivered
            )

            // Line 3 -> 4
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(3.dp)
                    .background(if (isDelivered) ColorTokens.Primary else ColorTokens.SurfaceContainerHigh)
            )

            // Step 4: Drop-off
            TrackerStepNode(
                icon = Icons.Default.Home,
                isActive = isDelivered,
                isDone = isDelivered,
                isCurrent = isDelivered
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Step Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TrackerStepLabel(title = "Cutoff", subtitle = "08:00 AM", isCurrent = false, alignment = Alignment.Start)
            TrackerStepLabel(title = "Prepped", subtitle = "11:45 AM", isCurrent = false, alignment = Alignment.CenterHorizontally)
            TrackerStepLabel(title = "On Way", subtitle = if (isDelivered) "Completed" else "In Transit", isCurrent = !isDelivered, alignment = Alignment.CenterHorizontally)
            TrackerStepLabel(title = "Drop-off", subtitle = if (isDelivered) "Delivered ✓" else "~01:05 PM", isCurrent = isDelivered, alignment = Alignment.End)
        }
    }
}

@Composable
private fun TrackerStepNode(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    isDone: Boolean,
    isCurrent: Boolean
) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(
                if (isCurrent || isDone) ColorTokens.Primary
                else ColorTokens.SurfaceContainerHigh
            )
            .then(
                if (isCurrent) Modifier.border(3.dp, ColorTokens.PrimaryFixed, CircleShape)
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive || isDone || isCurrent) ColorTokens.OnPrimary else ColorTokens.Secondary,
            modifier = Modifier.size(15.dp)
        )
    }
}

@Composable
private fun TrackerStepLabel(
    title: String,
    subtitle: String,
    isCurrent: Boolean,
    alignment: Alignment.Horizontal
) {
    Column(horizontalAlignment = alignment) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
            color = if (isCurrent) ColorTokens.Primary else ColorTokens.OnSurface
        )
        Text(
            text = subtitle,
            fontSize = 9.sp,
            color = if (isCurrent) ColorTokens.Primary else ColorTokens.Secondary
        )
    }
}