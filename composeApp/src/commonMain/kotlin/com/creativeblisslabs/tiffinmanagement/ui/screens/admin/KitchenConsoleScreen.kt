package com.creativeblisslabs.tiffinmanagement.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.runtime.collectAsState
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun KitchenConsoleScreen(
    onPrintStickersClick: () -> Unit = {},
    onStationViewClick: () -> Unit = {},
    onManifestClick: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var isSyncing by remember { mutableStateOf(false) }
    var showStationDialog by remember { mutableStateOf(false) }
    var showManifestDialog by remember { mutableStateOf(false) }
    var selectedSlotIndex by remember { mutableStateOf(0) }
    var selectedSubTabIndex by remember { mutableStateOf(0) }

    val batch by TiffinRepository.kitchenBatch.collectAsState()
    val riderProfile by TiffinRepository.riderProfile.collectAsState()
    val deliveryRoutes by TiffinRepository.deliveryRoutes.collectAsState()
    val activeRoute = deliveryRoutes.firstOrNull { it.isActive } ?: deliveryRoutes.firstOrNull()
    val subscriptions by TiffinRepository.subscriptions.collectAsState()
    val systemSettings by TiffinRepository.systemSettings.collectAsState()
    val breakfastCount = subscriptions.count { it.isActive &&
        it.mealComponents.any { c -> c.contains("Breakfast", ignoreCase = true) } ||
        it.packageName.contains("Breakfast", ignoreCase = true) }
    val lunchCount = subscriptions.count { it.isActive &&
        (it.packageName.contains("Lunch", ignoreCase = true) ||
         it.mealComponents.any { c -> c.contains("Lunch", ignoreCase = true) }) }
    val dinnerCount = subscriptions.count { it.isActive &&
        (it.packageName.contains("Dinner", ignoreCase = true) ||
         it.mealComponents.any { c -> c.contains("Dinner", ignoreCase = true) }) }
    // Use empty string when no rider assigned — don't fall back to hardcoded name
    val dispatchedDriverName = riderProfile.fullName.ifBlank {
        activeRoute?.assignedDriverName?.ifBlank { "Unassigned" } ?: "Unassigned"
    }
    val totalTiffinCount = subscriptions.count { it.isActive }
    val mealSlots = listOf("Breakfast ($breakfastCount)", "Lunch ($lunchCount)", "Dinner ($dinnerCount)")
    val subTabs = listOf("Cooking Plan (Prep)", "Packing List", "Pick Up Console")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        

        // 2. Meal Slot Tabs (Breakfast, Lunch, Dinner)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(ColorTokens.SurfaceContainerLow)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            mealSlots.forEachIndexed { index, slot ->
                val isSelected = selectedSlotIndex == index
                val icon = when (index) {
                    0 -> Icons.Default.WbTwilight
                    1 -> Icons.Default.LightMode
                    else -> Icons.Default.DarkMode
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) ColorTokens.PrimaryCTA else Color.Transparent)
                        .clickable { selectedSlotIndex = index }
                        .padding(vertical = 10.dp, horizontal = 6.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = slot,
                        tint = if (isSelected) Color.White else ColorTokens.Secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = slot,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else ColorTokens.Secondary
                    )
                }
            }
        }

        // 3. Select Production Date Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.CalendarMonth, "Calendar", tint = ColorTokens.Primary, modifier = Modifier.size(18.dp))
                        Text(
                            text = "Select Production Date",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = ColorTokens.OnSurface
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(ColorTokens.SecondaryContainer)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Live Sync",
                            color = ColorTokens.OnSecondaryContainer,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Date Input Display Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.CalendarToday, "Date", tint = ColorTokens.Secondary, modifier = Modifier.size(16.dp))
                            Text(batch.dateFormatted, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = ColorTokens.OnSurface)
                        }

                        Icon(Icons.Default.EditCalendar, "Edit", tint = ColorTokens.Secondary, modifier = Modifier.size(16.dp))
                    }

                    Button(
                        onClick = {
                            isSyncing = true
                            TiffinRepository.refreshKitchenBatch()
                            coroutineScope.launch {
                                kotlinx.coroutines.delay(600)
                                isSyncing = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryCTA),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(44.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Sync, "Sync", modifier = Modifier.size(16.dp))
                            Text(if (isSyncing) "Syncing..." else "Sync", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 4. Sub-Navigation Tabs: Cooking Plan (Prep), Packing List, Pick Up Console
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            subTabs.forEachIndexed { index, tab ->
                val isSelected = selectedSubTabIndex == index
                val icon = when (index) {
                    0 -> Icons.Default.SoupKitchen
                    1 -> Icons.Default.Inventory2
                    else -> Icons.Default.DirectionsWalk
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) ColorTokens.OnSurface else ColorTokens.SurfaceContainer)
                        .clickable { selectedSubTabIndex = index }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = tab,
                        tint = if (isSelected) ColorTokens.PrimaryFixed else ColorTokens.Secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = tab,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected) Color.White else ColorTokens.Secondary
                    )
                }
            }
        }

        // 5. Distinct Sub-Tab Content Switcher
        when (selectedSubTabIndex) {
            0 -> {
                // TAB 0: Cooking Plan (Prep)
                // 5a. Aggregate Preparation Quantities Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Aggregate Preparation Quantities",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = ColorTokens.OnSurface
                            )
                            Box(Modifier.size(8.dp).clip(CircleShape).background(ColorTokens.Primary))
                        }

                        Text(
                            text = "Total counts calculated from active subscriptions scheduled for ${batch.dateFormatted}.",
                            fontSize = 12.sp,
                            color = ColorTokens.Secondary
                        )

                        // Empty / Idle State Box
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(ColorTokens.SurfaceContainerLow.copy(alpha = 0.7f))
                                .padding(vertical = 28.dp, horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(ColorTokens.SurfaceContainerLowest),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.SoupKitchen, "Empty", tint = ColorTokens.Primary, modifier = Modifier.size(32.dp))
                            }

                            Text(
                                text = "No items to prepare for this category and date.",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = ColorTokens.OnSurface,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Text(
                                text = "Active subscriptions will automatically populate ingredients and meal portions here.",
                                fontSize = 11.sp,
                                color = ColorTokens.Secondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(ColorTokens.SurfaceContainer)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Verified, "Verified", tint = ColorTokens.Tertiary, modifier = Modifier.size(14.dp))
                                Text(
                                    text = "Kitchen idle • Zero batch tickets pending",
                                    fontSize = 11.sp,
                                    color = ColorTokens.OnSurfaceVariant
                                )
                            }
                        }

                        // 3 Quantities Counters (Rotis, Sabzi, Dal)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            KitchenUnitCard(Modifier.weight(1f), "Rotis/Breads", batch.rotisCount.toString(), "units")
                            KitchenUnitCard(Modifier.weight(1f), "Sabzi / Mains", "${batch.sabziKg}", "kg")
                            KitchenUnitCard(Modifier.weight(1f), "Dal / Curries", "${batch.dalLiters}", "liters")
                        }
                    }
                }

                // 6. Kitchen Station Display (KDS) Card (Dark Mode Interior)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ColorTokens.SecondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.DesktopWindows, "KDS", tint = ColorTokens.OnSecondaryContainer, modifier = Modifier.size(18.dp))
                                }

                                Column {
                                    Text(
                                        text = "Kitchen Station Display (KDS)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = ColorTokens.OnSurface
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Box(Modifier.size(5.dp).clip(CircleShape).background(ColorTokens.Tertiary))
                                        Text("Active KDS Feed", fontSize = 11.sp, color = ColorTokens.Tertiary, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainer)
                                    .clickable {
                                        onStationViewClick()
                                        showStationDialog = true
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.OpenInNew, "Station", tint = ColorTokens.Primary, modifier = Modifier.size(14.dp))
                                Text("Station View", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            }
                        }

                        // Dark KDS Interior Console
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(ColorTokens.KdsBackground)
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(ColorTokens.TertiaryContainer)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("STATION 01", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                    Text("Main Prep & Roti", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.White)
                                }

                                Text("Shift: 07:00 - 09:30", fontSize = 10.sp, color = Color.LightGray)
                            }

                            // 3 Stat Panels: Pending, In-Prep, Dispatched
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                KdsMiniBox(Modifier.weight(1f), "Pending", "${batch.pendingCount}", Color.White)
                                KdsMiniBox(Modifier.weight(1f), "In-Prep", "${batch.inPrepCount}", ColorTokens.TertiaryFixedDim)
                                KdsMiniBox(Modifier.weight(1f), "Dispatched", "${batch.dispatchedCount}", Color.White)
                            }

                            // Status Message Strip
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x1AFFFFFF))
                                    .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Sensors, "Sensors", tint = ColorTokens.PrimaryFixed, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "Awaiting next meal batch dispatch. Connected directly to chef prep tablet & kitchen wall display.",
                                    fontSize = 11.sp,
                                    color = Color(0xFFD1D5DB),
                                    lineHeight = 15.sp
                                )
                            }

                            // Footer KDS Controls
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Icon(Icons.Default.Autorenew, "Refresh", tint = ColorTokens.TertiaryFixedDim, modifier = Modifier.size(12.dp))
                                        Text("Auto-Refresh (${systemSettings.kdsRefreshSeconds}s)", fontSize = 10.sp, color = Color.LightGray)
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Icon(Icons.Default.VolumeUp, "Audio", tint = ColorTokens.PrimaryFixed, modifier = Modifier.size(12.dp))
                                        Text("Chime ON", fontSize = 10.sp, color = Color.LightGray)
                                    }
                                }

                                Text(
                                    text = "Manifest",
                                    fontSize = 10.sp,
                                    color = ColorTokens.PrimaryFixed,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        onManifestClick()
                                        showManifestDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            }

            1 -> {
                // TAB 1: Packing List
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        val totalPacked = batch.dispatchedCount
                        val pureVegTrays = subscriptions.count { it.isActive && !it.packageName.contains("Non", ignoreCase = true) && !it.packageName.contains("Jain", ignoreCase = true) }
                        val jainTrays = subscriptions.count { it.isActive && it.packageName.contains("Jain", ignoreCase = true) }
                        val nonVegTrays = subscriptions.count { it.isActive && it.packageName.contains("Non", ignoreCase = true) }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Tiffin Container Packing Checklist",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = ColorTokens.OnSurface
                                )
                                Text(
                                    text = "Meal containers sealed, thermal bagged & dietary-coded",
                                    fontSize = 12.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(ColorTokens.TertiaryFixed)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("$totalPacked Packed", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnTertiaryFixed)
                            }
                        }

                        // Dietary distribution pills
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFE8F5E9))
                                    .padding(12.dp)
                            ) {
                                Text("PURE VEG", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                                Text("$pureVegTrays Trays", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1B5E20))
                                Text("Standard Thali", fontSize = 11.sp, color = Color(0xFF388E3C))
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFFF3E0))
                                    .padding(12.dp)
                            ) {
                                Text("JAIN / SPECIAL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                                Text("$jainTrays Trays", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFBF360C))
                                Text("No Onion/Garlic", fontSize = 11.sp, color = Color(0xFFD84315))
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFFFEBEE))
                                    .padding(12.dp)
                            ) {
                                Text("NON-VEG", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                                Text("$nonVegTrays Trays", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB71C1C))
                                Text("Special Protein", fontSize = 11.sp, color = Color(0xFFC62828))
                            }
                        }

                        // Packing Verification Flow Checklist
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("PACKING LINE STATUS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Verified, null, tint = ColorTokens.Tertiary, modifier = Modifier.size(16.dp))
                                Text("Barcode thermal stickers printed & aligned with customer IDs", fontSize = 12.sp, color = ColorTokens.OnSurface)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Verified, null, tint = ColorTokens.Tertiary, modifier = Modifier.size(16.dp))
                                Text("Silicon spill-proof lids verified on hot curries & gravies", fontSize = 12.sp, color = ColorTokens.OnSurface)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Verified, null, tint = ColorTokens.Tertiary, modifier = Modifier.size(16.dp))
                                Text("Assigned to insulated carrier pod bins in sequence", fontSize = 12.sp, color = ColorTokens.OnSurface)
                            }
                        }
                    }
                }
            }

            2 -> {
                // TAB 2: Pick Up Console
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Rider Dispatch & Counter Pickups",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = ColorTokens.OnSurface
                                )
                                Text(
                                    text = "Handover bay for fleet delivery riders & takeaway walk-ins",
                                    fontSize = 12.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(ColorTokens.PrimaryFixed)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Bay 01 Active", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimaryFixed)
                            }
                        }

                        // Dispatch Queue Overview Cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Riders Dispatched", fontSize = 11.sp, color = ColorTokens.Secondary)
                                Text("1", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = ColorTokens.OnSurface)
                                Text("$dispatchedDriverName ${if (riderProfile.id.isNotBlank()) "(Active)" else "(Idle)"}", fontSize = 10.sp, color = ColorTokens.Tertiary, fontWeight = FontWeight.Bold)
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Handover Pending", fontSize = 11.sp, color = ColorTokens.Secondary)
                                Text("0", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = ColorTokens.Primary)
                                Text("Ready for Handover", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Takeaway Walk-ins", fontSize = 11.sp, color = ColorTokens.Secondary)
                                Text("0", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = ColorTokens.OnSurface)
                                Text("Counter Slips", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                        }

                        // Active Handover Queue Strip
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(ColorTokens.SurfaceContainer)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Default.DirectionsWalk, null, tint = ColorTokens.Primary, modifier = Modifier.size(20.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(activeRoute?.name ?: "Doorstep Route", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text("Driver: $dispatchedDriverName • ${activeRoute?.stopsCount ?: 0} Stops • Pod Carrier Temp: ${riderProfile.carrierPodTempC}°C", fontSize = 11.sp, color = ColorTokens.Secondary)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.TertiaryFixed)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("En Route", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnTertiaryFixed)
                            }
                        }
                    }
                }
            }
        }

        // 7. Sticky Floating Bar: Print Sticker Labels
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.InverseSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x33FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Label, "Labels", tint = ColorTokens.PrimaryFixed, modifier = Modifier.size(20.dp))
                    }

                    Column {
                        Text("Print Sticker Labels", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                        Text("$totalTiffinCount tiffin containers ready", fontSize = 11.sp, color = Color.LightGray)
                    }
                }

                Button(
                    onClick = onPrintStickersClick,
                    colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryCTA),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Print, "Batch", modifier = Modifier.size(16.dp))
                        Text("Batch Print", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (showStationDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showStationDialog = false },
                title = { Text("KDS Station View", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Station 01: Main Prep & Roti Counter", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text("Current live batch: ${batch.pendingCount} pending orders, ${batch.inPrepCount} in prep.", fontSize = 12.sp)
                        Text("Kitchen displays are active and synced with live dispatcher feed.", fontSize = 12.sp, color = ColorTokens.Secondary)
                    }
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = { showStationDialog = false }) {
                        Text("Close", color = ColorTokens.PrimaryCTA)
                    }
                }
            )
        }

        if (showManifestDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showManifestDialog = false },
                title = { Text("Kitchen Production Manifest", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Date: ${batch.dateFormatted}", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Text("Rotis Required: ${batch.rotisCount}", fontSize = 12.sp)
                        Text("Sabzi: ${batch.sabziKg} kg | Dal: ${batch.dalLiters} L", fontSize = 12.sp)
                        Text("Active Route: ${activeRoute?.name ?: "Main Central Route"} (${activeRoute?.assignedDriverName ?: "Amit Kumar"})", fontSize = 12.sp)
                    }
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = { showManifestDialog = false }) {
                        Text("Done", color = ColorTokens.PrimaryCTA)
                    }
                }
            )
        }
    }
}

@Composable
private fun KitchenUnitCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(ColorTokens.SurfaceContainerLow)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(title, fontSize = 11.sp, color = ColorTokens.Secondary)
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = ColorTokens.OnSurface)
        Text(unit, fontSize = 11.sp, color = ColorTokens.Secondary)
    }
}

@Composable
private fun KdsMiniBox(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x14FFFFFF))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, fontSize = 10.sp, color = Color.LightGray)
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
