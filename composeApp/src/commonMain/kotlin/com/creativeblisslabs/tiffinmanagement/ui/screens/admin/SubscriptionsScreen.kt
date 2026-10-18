package com.creativeblisslabs.tiffinmanagement.ui.screens.admin

import androidx.compose.foundation.background
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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

@Composable
fun SubscriptionsScreen(
    onCreateClick: () -> Unit = {},
    onSubscriptionClick: (String) -> Unit = {},
    onEditSubscription: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var dialogIsActive by remember { mutableStateOf(true) }

    var adjustingSubscription by remember { mutableStateOf<com.example.myapplicationkmp.data.models.SubscriptionModel?>(null) }
    var walletIsCredit by remember { mutableStateOf(true) }
    var walletAmountInput by remember { mutableStateOf("") }
    var walletRemarkInput by remember { mutableStateOf("") }
    var walletDialogError by remember { mutableStateOf<String?>(null) }

    var selectedPageSize by remember { mutableStateOf("15") }
    var pageSizeDropdownOpen by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var statusFilter by remember { mutableStateOf("all") } // 'all', 'active', 'paused', 'expired'
    var mealSlotFilter by remember { mutableStateOf("all") } // 'all', 'lunch', 'dinner'

    val subscriptionList by TiffinRepository.subscriptions.collectAsState()
    val filteredSubscriptions = remember(subscriptionList, searchQuery, statusFilter, mealSlotFilter) {
        subscriptionList.filter { sub ->
            val matchesSearch = searchQuery.isBlank() ||
                sub.customerName.contains(searchQuery, ignoreCase = true) ||
                sub.subscriptionCode.contains(searchQuery, ignoreCase = true)
            val matchesStatus = when (statusFilter) {
                "active" -> sub.isActive
                "paused" -> !sub.isActive
                "expired" -> sub.paymentStatus.equals("expired", ignoreCase = true)
                else -> true
            }
            val matchesSlot = when (mealSlotFilter) {
                "lunch" -> sub.packageName.contains("lunch", ignoreCase = true) || sub.instructions.contains("lunch", ignoreCase = true)
                "dinner" -> sub.packageName.contains("dinner", ignoreCase = true) || sub.instructions.contains("dinner", ignoreCase = true)
                else -> true
            }
            matchesSearch && matchesStatus && matchesSlot
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        

        // 2. Search & Toolbar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search customer name or phon...", fontSize = 12.sp, color = ColorTokens.TextSecondary) },
                leadingIcon = {
                    Icon(Icons.Default.Search, "Search", tint = ColorTokens.TextSecondary, modifier = Modifier.size(18.dp))
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ColorTokens.SurfaceContainerLowest,
                    unfocusedContainerColor = ColorTokens.SurfaceContainerLowest,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true
            )

            // Page Size Selector
            Box {
                Row(
                    modifier = Modifier
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ColorTokens.SurfaceContainerLowest)
                        .clickable { pageSizeDropdownOpen = true }
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(selectedPageSize, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    Icon(Icons.Default.UnfoldMore, "More", tint = ColorTokens.TextSecondary, modifier = Modifier.size(16.dp))
                }
                androidx.compose.material3.DropdownMenu(
                    expanded = pageSizeDropdownOpen,
                    onDismissRequest = { pageSizeDropdownOpen = false }
                ) {
                    listOf("15", "25", "50").forEach { sizeOpt ->
                        androidx.compose.material3.DropdownMenuItem(
                            text = { Text(sizeOpt, fontSize = 13.sp) },
                            onClick = {
                                selectedPageSize = sizeOpt
                                pageSizeDropdownOpen = false
                            }
                        )
                    }
                }
            }

            // Tune / Filter Icon
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (statusFilter != "all") ColorTokens.PrimaryContainer else ColorTokens.SurfaceContainerLowest)
                    .clickable { showFilterSheet = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Tune,
                    "Filter",
                    tint = if (statusFilter != "all") ColorTokens.PrimaryCTA else ColorTokens.OnSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
                 Button(
                onClick = onCreateClick,
                colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryCTA),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Add, "Create", modifier = Modifier.size(18.dp))
                    Text("Create", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // 3. Subscriptions List Container
        val limit = selectedPageSize.toIntOrNull() ?: 15
        val displayedSubscriptions = filteredSubscriptions.take(limit)

        displayedSubscriptions.forEach { sub ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    // Card Header: Customer Info & Status
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.CalendarMonth,
                                    "Calendar",
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = sub.customerName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = ColorTokens.OnSurface
                                )
                                Text(
                                    text = "ID: ${sub.subscriptionCode}",
                                    fontSize = 11.sp,
                                    color = ColorTokens.TextSecondary
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Veg Indicator Pill
                            if (sub.isVeg) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ColorTokens.VegBadgeBg)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Box(Modifier.size(6.dp).clip(CircleShape).background(ColorTokens.TertiaryMint))
                                    Text(
                                        text = "Veg",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.VegBadgeText
                                    )
                                }
                            }

                            // Interactive Status Switch for Subscription
                            Switch(
                                checked = sub.isActive,
                                onCheckedChange = { isChecked ->
                                    TiffinRepository.toggleSubscriptionStatus(sub.id, isChecked)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = ColorTokens.Primary,
                                    uncheckedThumbColor = ColorTokens.TextSecondary,
                                    uncheckedTrackColor = ColorTokens.SurfaceContainerHigh
                                )
                            )
                        }
                    }

                    // Package & Plan Details Band
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ColorTokens.SurfaceContainerLow.copy(alpha = 0.6f))
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
                                Icon(Icons.Default.Inventory2, "Package", tint = ColorTokens.Primary, modifier = Modifier.size(16.dp))
                                Text(
                                    text = sub.packageName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = ColorTokens.OnSurface
                                )
                            }
                            Text(
                                text = "Rate: ₹${sub.rate} / ${sub.ratePeriod}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = ColorTokens.TextSecondary
                            )
                        }

                        // Meal Components Row
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "MEAL COMPONENTS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.TextSecondary,
                                letterSpacing = 0.5.sp
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                sub.mealComponents.forEach { comp ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(ColorTokens.VegBadgeBg)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = comp,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.VegBadgeText
                                        )
                                    }
                                }
                            }
                        }

                        // Active Days
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Active Days:",
                                fontSize = 11.sp,
                                color = ColorTokens.TextSecondary
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                sub.activeDays.forEach { day ->
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(ColorTokens.PrimaryCTA),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = day,
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Financial & Logistics Details 2x2 Grid
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GridField(Modifier.weight(1f), "Start Date", sub.startDate)
                            GridField(Modifier.weight(1f), "Wallet Balance", "₹${sub.walletBalance}", isSuccess = true)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GridField(Modifier.weight(1f), "Allowed Skips", "${sub.skipsUsed} / ${sub.allowedSkips}")
                            GridField(Modifier.weight(1f), "Payment Status", sub.paymentStatus, isSuccess = true)
                        }
                    }

                    // Action Buttons Bar: Details, Edit, Wallet ±, Delete
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ColorTokens.SurfaceContainer)
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLowest)
                                .clickable { onSubscriptionClick(sub.id) }
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Visibility, "Details", tint = ColorTokens.Secondary, modifier = Modifier.size(15.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Details", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        }

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLowest)
                                .clickable { onEditSubscription(sub.id) }
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Edit, "Edit", tint = ColorTokens.Primary, modifier = Modifier.size(15.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Edit", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        }

                        Row(
                            modifier = Modifier
                                .weight(1.1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.TertiaryFixed)
                                .clickable {
                                    adjustingSubscription = sub
                                    walletAmountInput = ""
                                    walletRemarkInput = ""
                                    walletIsCredit = true
                                    walletDialogError = null
                                }
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Wallet ±", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnTertiaryFixed)
                        }

                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLowest)
                                .clickable { TiffinRepository.deleteSubscription(sub.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Delete, "Delete", tint = ColorTokens.Error, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Manual Wallet Adjustment Dialog for Subscriptions
        adjustingSubscription?.let { targetSub ->
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { adjustingSubscription = null },
                title = {
                    Column {
                        Text(
                            text = "Manual Wallet Adjustment",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = "${targetSub.customerName} (${targetSub.subscriptionCode})",
                            fontSize = 12.sp,
                            color = ColorTokens.TextSecondary
                        )
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { walletIsCredit = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (walletIsCredit) ColorTokens.Primary else ColorTokens.SurfaceContainerHigh
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "+ Credit",
                                    color = if (walletIsCredit) Color.White else ColorTokens.OnSurface,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(
                                onClick = { walletIsCredit = false },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!walletIsCredit) ColorTokens.Error else ColorTokens.SurfaceContainerHigh
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "- Debit",
                                    color = if (!walletIsCredit) Color.White else ColorTokens.OnSurface,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        OutlinedTextField(
                            value = walletAmountInput,
                            onValueChange = { walletAmountInput = it },
                            label = { Text("Amount (₹)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = walletRemarkInput,
                            onValueChange = { walletRemarkInput = it },
                            label = { Text("Reason / Remarks") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )

                        walletDialogError?.let {
                            Text(it, color = ColorTokens.Error, fontSize = 11.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amt = walletAmountInput.toDoubleOrNull()
                            if (amt == null || amt <= 0.0) {
                                walletDialogError = "Please enter a valid positive amount"
                                return@Button
                            }
                            TiffinRepository.applySubscriptionWalletAdjustment(
                                subscriptionId = targetSub.id,
                                amount = amt,
                                isCredit = walletIsCredit,
                                description = walletRemarkInput.ifBlank {
                                    if (walletIsCredit) "Manual Credit Adjustment" else "Manual Debit Adjustment"
                                }
                            )
                            adjustingSubscription = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryCTA),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Apply", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { adjustingSubscription = null }) {
                        Text("Cancel", color = ColorTokens.TextSecondary)
                    }
                }
            )
        }

        if (showFilterSheet) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showFilterSheet = false },
                title = { Text("Filter Subscriptions", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Status", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.TextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("all" to "All", "active" to "Active", "paused" to "Paused", "expired" to "Expired").forEach { (key, lbl) ->
                                val selected = statusFilter == key
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (selected) ColorTokens.PrimaryCTA else ColorTokens.SurfaceContainerLow)
                                        .clickable { statusFilter = key }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        lbl,
                                        fontSize = 11.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selected) Color.White else ColorTokens.OnSurface
                                    )
                                }
                            }
                        }

                        Text("Meal Slot", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.TextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("all" to "All", "lunch" to "Lunch", "dinner" to "Dinner").forEach { (key, lbl) ->
                                val selected = mealSlotFilter == key
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (selected) ColorTokens.PrimaryCTA else ColorTokens.SurfaceContainerLow)
                                        .clickable { mealSlotFilter = key }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        lbl,
                                        fontSize = 11.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selected) Color.White else ColorTokens.OnSurface
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showFilterSheet = false },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryCTA),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Apply", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = {
                        statusFilter = "all"
                        mealSlotFilter = "all"
                        showFilterSheet = false
                    }) {
                        Text("Reset", color = ColorTokens.TextSecondary)
                    }
                }
            )
        }

    }
}

@Composable
private fun GridField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isSuccess: Boolean = false
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ColorTokens.SurfaceContainerLow)
            .padding(8.dp)
    ) {
        Text(label, fontSize = 10.sp, color = ColorTokens.TextSecondary)
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSuccess) ColorTokens.Tertiary else ColorTokens.OnSurface
        )
    }
}
