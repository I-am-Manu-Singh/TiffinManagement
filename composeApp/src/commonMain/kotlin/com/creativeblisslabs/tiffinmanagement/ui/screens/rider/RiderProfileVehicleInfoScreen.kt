package com.creativeblisslabs.tiffinmanagement.ui.screens.rider

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
import androidx.compose.foundation.border
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun RiderProfileVehicleInfoScreen(
    repository: TiffinRepository,
    onBack: () -> Unit,
    onNavigateToAdmin: () -> Unit = {},
    onNavigateToCustomer: () -> Unit = {},
    onSignOut: () -> Unit = {}
) {
    val profile by repository.riderProfile.collectAsState()
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()
    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }

    var lunchToggle by remember(profile) { mutableStateOf(profile.isLunchShiftActive) }
    var dinnerToggle by remember(profile) { mutableStateOf(profile.isDinnerShiftActive) }
    var chimeToggle by remember(profile) { mutableStateOf(profile.isAlertChimeEnabled) }
    var selectedNavEngine by remember(profile) { mutableStateOf(profile.defaultNavEngine) }

    var showEditDialog by remember { mutableStateOf(false) }
    var editName by remember(profile) { mutableStateOf(profile.fullName) }
    var editPhone by remember(profile) { mutableStateOf(profile.phone ?: "") }
    var editVehicleModel by remember(profile) { mutableStateOf(profile.vehicleModel) }
    var editVehiclePlate by remember(profile) { mutableStateOf(profile.vehiclePlate) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        // Standard Universal Header
        val branch by repository.currentBranch.collectAsState()
        com.example.myapplicationkmp.ui.components.TiffinTopHeader(
            branchName = branch.name,
            screenTitle = "Rider Profile",
            userName = profile.fullName.ifBlank { "Rider" },
            userRole = "Rider",
            subtitle = if (profile.isOnDuty) "Verified Fleet • Driver ID: ${profile.driverIdCode}" else "Off Duty • Driver ID: ${profile.driverIdCode}",
            onNotificationClick = {},
            onProfileClick = {}
        )

        // Scrollable Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Rider Identity Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(64.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                val initials = profile.fullName
                                    .split(" ")
                                    .mapNotNull { it.firstOrNull()?.toString() }
                                    .take(2)
                                    .joinToString("")
                                    .ifBlank { "RK" }
                                Text(initials, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimaryFixed)
                            }
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.Tertiary)
                                    .border(2.dp, ColorTokens.SurfaceContainerLowest, CircleShape)
                                    .align(Alignment.BottomEnd)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(profile.fullName, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { showEditDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary),
                                    shape = RoundedCornerShape(22.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                        Text("Edit", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(ColorTokens.PrimaryFixed).padding(horizontal = 8.dp, vertical = 2.dp)) {
                                    Text(profile.tier, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimaryFixed)
                                }
                                Text("${profile.vehicleModel} • ${profile.vehiclePlate}", fontSize = 12.sp, color = ColorTokens.Secondary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(14.dp))
                                Text("${profile.rating}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text("(${profile.totalSafeDrops}+ safe drops)", fontSize = 11.sp, color = ColorTokens.Secondary)
                            }
                        }
                    }

                    // Hub & Supervisor Context
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Storefront, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(16.dp))
                            Text(profile.hubName, fontSize = 11.sp, color = ColorTokens.Secondary)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.SupervisorAccount, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(16.dp))
                                Text("Manager: ${profile.hubManagerName}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            }
                            Button(
                                onClick = { /* Call supervisor */ },
                                modifier = Modifier.height(30.dp),
                                shape = RoundedCornerShape(22.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainer)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call", fontSize = 10.sp, color = ColorTokens.OnSurface)
                            }
                        }
                    }
                }
            }

            // Contact & Personal Information Card (Matches Admin & Customer design)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PERSONAL & CONTACT INFORMATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.TextSecondary,
                        letterSpacing = 0.5.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Icon(Icons.Default.CheckCircle, null, tint = ColorTokens.Tertiary, modifier = Modifier.size(13.dp))
                        Text("KYC Verified", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Tertiary)
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Phone Item
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(
                                    modifier = Modifier.size(36.dp).clip(CircleShape).background(ColorTokens.SecondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Call, null, tint = ColorTokens.OnSecondaryContainer, modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Primary Mobile", fontSize = 10.sp, color = ColorTokens.Secondary)
                                    Text(profile.phone ?: "+91 —", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                }
                            }
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(22.dp)).background(ColorTokens.Tertiary.copy(alpha = 0.1f)).padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Verified", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Tertiary)
                            }
                        }

                        // Vehicle Registration Item
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(
                                    modifier = Modifier.size(36.dp).clip(CircleShape).background(ColorTokens.SecondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.TwoWheeler, null, tint = ColorTokens.OnSecondaryContainer, modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Registered Vehicle", fontSize = 10.sp, color = ColorTokens.Secondary)
                                    Text("${profile.vehicleModel} • ${profile.vehiclePlate}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                }
                            }
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(22.dp)).background(ColorTokens.Tertiary.copy(alpha = 0.1f)).padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Active", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Tertiary)
                            }
                        }
                    }
                }
            }

            // 2. Operational Metrics & Tier Overview
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(20.dp))
                            Text("Gold Courier Tier", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        }
                        Icon(Icons.Default.Info, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                    }
                    Text("₹${profile.baseDropRate.toInt()}.00 base drop + ₹${profile.boxRecoveryBonus.toInt()}.00 box recovery", fontSize = 12.sp, color = ColorTokens.Secondary)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column(modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${profile.totalSafeDrops}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            Text("Total Drops", fontSize = 10.sp, color = ColorTokens.Secondary)
                        }
                        Column(modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${profile.boxReturnRatePercent}%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Tertiary)
                            Text("Box Return", fontSize = 10.sp, color = ColorTokens.Secondary)
                        }
                        Column(modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${profile.onTimeRatePercent}%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                            Text("On-Time", fontSize = 10.sp, color = ColorTokens.Secondary)
                        }
                    }

                    // Linked UPI
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(ColorTokens.SecondaryContainer), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.AccountBalance, contentDescription = null, tint = ColorTokens.OnSecondaryContainer, modifier = Modifier.size(16.dp))
                            }
                            Column {
                                Text("HDFC Bank •••• ${profile.bankAccountTail}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text("UPI: ${profile.upiId}", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ColorTokens.Tertiary, modifier = Modifier.size(15.dp))
                            Text("Linked", fontSize = 11.sp, color = ColorTokens.Tertiary)
                        }
                    }
                }
            }

            // 3. Shift & Gear Assignment
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Shift & Gear Assignment", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)

                    // Lunch Shift Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.WbSunny, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(18.dp))
                            Column {
                                Text("Lunch Dispatch Slot", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text("11:00 AM – 02:00 PM", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                        }
                        Switch(
                            checked = lunchToggle,
                            onCheckedChange = {
                                lunchToggle = it
                                repository.updateRiderProfile(profile.copy(isLunchShiftActive = it))
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = ColorTokens.OnPrimary, checkedTrackColor = ColorTokens.Primary)
                        )
                    }

                    // Dinner Shift Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.DarkMode, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                            Column {
                                Text("Dinner Dispatch Slot", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text("06:30 PM – 09:30 PM", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                        }
                        Switch(
                            checked = dinnerToggle,
                            onCheckedChange = {
                                dinnerToggle = it
                                repository.updateRiderProfile(profile.copy(isDinnerShiftActive = it))
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = ColorTokens.OnPrimary, checkedTrackColor = ColorTokens.Primary)
                        )
                    }

                    // Thermal Pod Telemetry Tile
                    Column(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerHigh).padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Inventory2, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(18.dp))
                                Column {
                                    Text("Aero-Insulated 36-Box Heated Pod #12", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                    Text("Sensor ID: #SNS-8891", fontSize = 10.sp, color = ColorTokens.Secondary)
                                }
                            }
                            Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(ColorTokens.Tertiary.copy(alpha = 0.1f)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                Text("Inspected", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Tertiary)
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Passed morning thermal seal audit", fontSize = 11.sp, color = ColorTokens.Secondary)
                            Text("Today, 10:45 AM", fontSize = 11.sp, color = ColorTokens.OnSurface)
                        }
                    }

                    // Document Compliance
                    Text("DOCUMENT COMPLIANCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Secondary)
                    val docs = listOf(
                        Triple("Driving License", "Valid till 2029", Icons.Default.Badge),
                        Triple("Commercial Insurance", "Active Coverage", Icons.Default.VerifiedUser),
                        Triple("PUC Certificate", "Verified (BS-VI)", Icons.Default.Eco)
                    )
                    docs.forEach { (title, subtitle, icon) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(icon, contentDescription = null, tint = ColorTokens.Tertiary, modifier = Modifier.size(15.dp))
                                Text(title, fontSize = 11.sp, color = ColorTokens.OnSurface)
                            }
                            Text(subtitle, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = ColorTokens.Tertiary)
                        }
                    }
                }
            }

            // 4. Settings & Dispatch Safety
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Settings & Dispatch Safety", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)

                    // Navigation engine switch
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Default Navigation Route Engine", fontSize = 11.sp, color = ColorTokens.Secondary)
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (selectedNavEngine == "Google Maps") ColorTokens.SurfaceContainerLowest else Color.Transparent)
                                    .clickable {
                                        selectedNavEngine = "Google Maps"
                                        repository.updateRiderProfile(profile.copy(defaultNavEngine = "Google Maps"))
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Google Maps", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selectedNavEngine == "Google Maps") ColorTokens.Primary else ColorTokens.Secondary)
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (selectedNavEngine == "Waze") ColorTokens.SurfaceContainerLowest else Color.Transparent)
                                    .clickable {
                                        selectedNavEngine = "Waze"
                                        repository.updateRiderProfile(profile.copy(defaultNavEngine = "Waze"))
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Waze Navigation", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selectedNavEngine == "Waze") ColorTokens.Primary else ColorTokens.Secondary)
                            }
                        }
                    }

                    // Alert Sound Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                            Column {
                                Text("Route Re-assignment Alerts", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text("Loud haptic chime for new stops", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                        }
                        Switch(
                            checked = chimeToggle,
                            onCheckedChange = {
                                chimeToggle = it
                                repository.updateRiderProfile(profile.copy(isAlertChimeEnabled = it))
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = ColorTokens.OnPrimary, checkedTrackColor = ColorTokens.Primary)
                        )
                    }

                    // Cash limit & Deposit
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column(modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp)) {
                            Text("Cash Handled Limit", fontSize = 10.sp, color = ColorTokens.Secondary)
                            Text("₹4,500", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            Text("₹0 collected today", fontSize = 10.sp, color = ColorTokens.Tertiary)
                        }
                        Column(modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp)) {
                            Text("Carrier Deposit", fontSize = 10.sp, color = ColorTokens.Secondary)
                            Text("₹2,000", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            Text("Security Held", fontSize = 10.sp, color = ColorTokens.Tertiary)
                        }
                    }
                }
            }

            // 5. Actions & Escalations
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { showEditDialog = true },
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = ColorTokens.OnPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Edit Profile & Vehicle", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimary)
                }

                Button(
                    onClick = { /* Contact supervisor */ },
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainerLowest)
                ) {
                    Icon(Icons.Default.SupportAgent, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Contact Hub Supervisor", fontSize = 13.sp, color = ColorTokens.OnSurface)
                }

                Button(
                    onClick = { repository.toggleRiderDuty(!profile.isOnDuty) },
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.ErrorContainer.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Default.PowerSettingsNew, contentDescription = null, tint = ColorTokens.Error, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (profile.isOnDuty) "Request Break / Go Off-Duty" else "Resume Shift / Go On-Duty",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.Error
                    )
                }


                // Logout
                Button(
                    onClick = { onSignOut() },
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.ErrorContainer)
                ) {
                    Text("Log Out from Rider Account", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnErrorContainer)
                }
            }
        }
    }

    if (showEditDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text("Edit Rider Profile & Vehicle", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    androidx.compose.material3.OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    androidx.compose.material3.OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    androidx.compose.material3.OutlinedTextField(
                        value = editVehicleModel,
                        onValueChange = { editVehicleModel = it },
                        label = { Text("Vehicle Model (e.g. Hero Splendor Plus)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    androidx.compose.material3.OutlinedTextField(
                        value = editVehiclePlate,
                        onValueChange = { editVehiclePlate = it },
                        label = { Text("Registration Plate (e.g. MH 04 EZ 8821)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = profile.copy(
                            fullName = editName.trim(),
                            phone = editPhone.trim(),
                            vehicleModel = editVehicleModel.trim(),
                            vehiclePlate = editVehiclePlate.trim().uppercase()
                        )
                        repository.updateRiderProfile(updated)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary)
                ) {
                    Text("Save", color = ColorTokens.OnPrimary)
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = ColorTokens.Secondary)
                }
            }
        )
    }
}
