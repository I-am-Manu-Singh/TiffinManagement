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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinTopHeader

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
    onNavigateToPrivacy: () -> Unit = {}
) {
    val authState by TiffinRepository.authState.collectAsState()
    val activeUser = authState.currentUser
    val role = (activeUser?.role ?: "admin").lowercase()
    val isCustomer = role == "customer" || role == "subscriber"
    val isRider = role == "rider" || role == "driver"

    val settings by TiffinRepository.systemSettings.collectAsState()
    val subscriberProfile by TiffinRepository.subscriberProfile.collectAsState()
    val riderProfile by TiffinRepository.riderProfile.collectAsState()

    var autoLock by remember(settings) { mutableStateOf(settings.autoLockDailyOrders) }
    var dietaryMode by remember(settings) { mutableStateOf(settings.defaultDietaryMode) } // pure_veg, mix
    var kdsAutoRefresh by remember(settings) { mutableStateOf(settings.kdsAutoRefresh) }
    var printLabels by remember(settings) { mutableStateOf(settings.printStickerLabels) }
    var soundAlerts by remember(settings) { mutableStateOf(settings.soundChimesAndAlerts) }

    // Customer specific settings
    var customerMorningMenuAlert by remember(subscriberProfile) { mutableStateOf(subscriberProfile.notifyMorningMenu) }
    var customerDispatchAlert by remember(subscriberProfile) { mutableStateOf(subscriberProfile.notifyDriverDispatch) }
    var customerProofAlert by remember(subscriberProfile) { mutableStateOf(subscriberProfile.notifyDropPhoto) }

    // Rider specific settings
    var riderSoundAlerts by remember(riderProfile) { mutableStateOf(riderProfile.isAlertChimeEnabled) }
    var riderOfflineCaching by remember(riderProfile) { mutableStateOf(riderProfile.isOnDuty) }

    var isSyncing by remember { mutableStateOf(false) }
    var cacheClearedMessage by remember { mutableStateOf<String?>(null) }
    var savedSuccessMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FD))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Role Banner Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFAB2F00)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isCustomer) Icons.Default.RestaurantMenu else if (isRider) Icons.Default.TwoWheeler else Icons.Default.Tune,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = if (isCustomer) "Customer App Preferences" else if (isRider) "Rider Navigation & Alert Preferences" else "Kitchen & Enterprise System Settings",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (isCustomer) "Active profile: ${subscriberProfile.fullName.ifBlank { activeUser?.fullName ?: "Customer" }}" else if (isRider) "Active vehicle: ${riderProfile.vehiclePlate.ifBlank { "Rider Plate" }}" else "Managing central kitchen preferences",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            if (isCustomer) {
                // Customer Notification & Delivery Preferences
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.RestaurantMenu, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(16.dp))
                        Text("MEAL & NOTIFICATION PREFERENCES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), letterSpacing = 0.5.sp)
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Morning Menu Preview SMS", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                    Text("Send daily menu at 08:30 AM via WhatsApp & SMS", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                                Switch(
                                    checked = customerMorningMenuAlert,
                                    onCheckedChange = { customerMorningMenuAlert = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFAB2F00))
                                )
                            }
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Driver Out for Delivery & Live ETA", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                    Text("Real-time notifications when your tiffin is out", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                                Switch(
                                    checked = customerDispatchAlert,
                                    onCheckedChange = { customerDispatchAlert = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFAB2F00))
                                )
                            }
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Doorstep Drop Photo Verification", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                    Text("Receive verified photo when box is left at door", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                                Switch(
                                    checked = customerProofAlert,
                                    onCheckedChange = { customerProofAlert = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFAB2F00))
                                )
                            }
                        }
                    }
                }
            } else if (isRider) {
                // Rider Specific Navigation & Alert Settings
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.TwoWheeler, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(16.dp))
                        Text("RIDER RUN & AUDIO CHIMES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), letterSpacing = 0.5.sp)
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Loud Drop Chimes & Voice Guidance", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                    Text("Audio alert on reaching geo-fence stop", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                                Switch(
                                    checked = riderSoundAlerts,
                                    onCheckedChange = { riderSoundAlerts = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFAB2F00))
                                )
                            }
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Offline Manifest Caching", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                    Text("Keep stop list cached in basement/low network areas", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                                Switch(
                                    checked = riderOfflineCaching,
                                    onCheckedChange = { riderOfflineCaching = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFAB2F00))
                                )
                            }
                        }
                    }
                }
            }

            // Section 1: Kitchen & Operations (Always visible for admin, relevant summary for others)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.SoupKitchen, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(16.dp))
                    Text("KITCHEN & OPERATIONS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), letterSpacing = 0.5.sp)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column {
                        // Auto-Lock Daily Orders
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFD5E0F8)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.LockClock, null, tint = Color(0xFF111C2D), modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Auto-Lock Daily Orders", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                    Text("Daily cutoff locks at 08:00 AM sharp", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                            }
                            Switch(
                                checked = autoLock,
                                onCheckedChange = { autoLock = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFAB2F00))
                            )
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                        // Default Dietary Mode
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8FAFC))
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.RestaurantMenu, null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                                    Text("Default Dietary Mode", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                }
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color(0xFF006947)))
                                    Text("Kitchen Default", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006947))
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                // Pure Veg Only
                                val isVeg = (dietaryMode == "pure_veg")
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White)
                                        .border(1.dp, if (isVeg) Color(0xFF006947) else Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                        .clickable { dietaryMode = "pure_veg" }
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF006947)))
                                        Text("Pure Veg Only", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                    }
                                    RadioButton(selected = isVeg, onClick = { dietaryMode = "pure_veg" }, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFAB2F00)))
                                }

                                // Mix (Veg + Non)
                                val isMix = (dietaryMode == "mix")
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF1F5F9))
                                        .border(1.dp, if (isMix) Color(0xFFDC2626) else Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                        .clickable { dietaryMode = "mix" }
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFDC2626)))
                                        Text("Mix (Veg+Non)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                                    }
                                    RadioButton(selected = isMix, onClick = { dietaryMode = "mix" }, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFAB2F00)))
                                }
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                        // KDS Auto-Refresh
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFD5E0F8)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Sync, null, tint = Color(0xFF111C2D), modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("KDS Auto-Refresh", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                    Text("Syncs active tickets every 30 seconds", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                            }
                            Switch(
                                checked = kdsAutoRefresh,
                                onCheckedChange = { kdsAutoRefresh = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFAB2F00))
                            )
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                        // Print Sticker Labels
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFD5E0F8)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Label, null, tint = Color(0xFF111C2D), modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Print Sticker Labels", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                    Text("Auto-trigger thermal sticker on meal dispatch", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                            }
                            Switch(
                                checked = printLabels,
                                onCheckedChange = { printLabels = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFAB2F00))
                            )
                        }
                    }
                }
            }

            // Section 2: Hardware & Devices
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Print, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(16.dp))
                    Text("HARDWARE & DEVICES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), letterSpacing = 0.5.sp)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Bluetooth Thermal Printer
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(
                                    modifier = Modifier.size(42.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFF6FFBBE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Print, null, tint = Color(0xFF002113), modifier = Modifier.size(20.dp))
                                }
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text("Bluetooth 58mm Thermal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF006947)))
                                    }
                                    Text("Model: KDS-P1 (Paired & Ready)", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF1F5F9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Tune, null, tint = Color(0xFF475569), modifier = Modifier.size(16.dp))
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                        // Digital Portion Scale
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(
                                    modifier = Modifier.size(42.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFECEEF2)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Scale, null, tint = Color(0xFF5B4139), modifier = Modifier.size(20.dp))
                                }
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text("Digital Portion Scale", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF94A3B8)))
                                    }
                                    Text("Not configured", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .clickable { }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Pair", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            }
                        }
                    }
                }
            }

            // Section 3: Billing, Tax & Currency
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Payments, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(16.dp))
                    Text("BILLING, TAX & CURRENCY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), letterSpacing = 0.5.sp)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Operating Currency
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Operating Currency", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                Text("Symbol and exchange base", fontSize = 11.sp, color = Color(0xFF64748B))
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(settings.currencyName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                    Icon(Icons.Default.ArrowForward, null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                                }
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                        // Default GST / Tax Rate
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Default GST / Tax Rate", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                Text("Composite food service rate", fontSize = 11.sp, color = Color(0xFF64748B))
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("${settings.gstRatePercent.toInt()}% Composite", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                    Icon(Icons.Default.ExpandMore, null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                                }
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                        // Customer Trust Limit
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Customer Trust Limit", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                Text("Allow delivery on negative balance", fontSize = 11.sp, color = Color(0xFF64748B))
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFFFDBD1))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("₹${"%.2f".format(settings.customerTrustLimit)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00))
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                        // Thermal Receipt Footer
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Thermal Receipt Footer", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Text("Edit Template", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00))
                            }
                            Text(
                                text = settings.thermalReceiptFooterText,
                                fontSize = 11.sp,
                                fontStyle = FontStyle.Italic,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
            }

            // Section 4: App Experience & Localization
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Tune, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(16.dp))
                    Text("APP EXPERIENCE & LOCALIZATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), letterSpacing = 0.5.sp)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Appearance
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFD5E0F8)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.LightMode, null, tint = Color(0xFF111C2D), modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Appearance", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                    Text("Light Theme (Active)", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("Change", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                        // Kitchen Language
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFD5E0F8)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Language, null, tint = Color(0xFF111C2D), modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Kitchen Language", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                    Text(settings.appLanguage, fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                            }
                            Icon(Icons.Default.ArrowForward, null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                        // Sound Chimes & Alerts
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFD5E0F8)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.VolumeUp, null, tint = Color(0xFF111C2D), modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Sound Chimes & Alerts", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                    Text("Loud chime when meal plans modify", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                            }
                            Switch(
                                checked = soundAlerts,
                                onCheckedChange = { soundAlerts = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFFAB2F00))
                            )
                        }
                    }
                }
            }

            // Section 5: Data & Backup
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.CloudSync, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(16.dp))
                    Text("DATA & BACKUP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), letterSpacing = 0.5.sp)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(modifier = Modifier.size(38.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFF6FFBBE)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.CloudDone, null, tint = Color(0xFF002113), modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Cloud Database Backup", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                    Text("Last synced: Today at 06:30 AM", fontSize = 11.sp, color = Color(0xFF006947), fontWeight = FontWeight.Medium)
                                }
                            }

                            Button(
                                onClick = { isSyncing = !isSyncing },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00855B)),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Default.Sync, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Text(if (isSyncing) "Synced" else "Sync", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Local App Cache", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                Text(cacheClearedMessage ?: "14.2 MB temporary images & slips", fontSize = 11.sp, color = Color(0xFF64748B))
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFECEEF2))
                                    .clickable { cacheClearedMessage = "0.0 MB (Cache cleared)" }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("Clear Cache", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBA1A1A))
                            }
                        }
                    }
                }
            }

            // Section 6: Legal & Compliance
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.VerifiedUser, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(16.dp))
                    Text("LEGAL & COMPLIANCE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B), letterSpacing = 0.5.sp)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToTerms() },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Terms of Service", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                Text("FSSAI standards, delivery cutoffs & wallet rules", fontSize = 11.sp, color = Color(0xFF64748B))
                            }
                            Icon(Icons.Default.ArrowForward, null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToPrivacy() },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Privacy Policy", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                                Text("Data portability, thermal printing & zero-ad telemetry", fontSize = 11.sp, color = Color(0xFF64748B))
                            }
                            Icon(Icons.Default.ArrowForward, null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

        

            if (savedSuccessMessage != null) {
                Text(savedSuccessMessage ?: "", color = Color(0xFF16A34A), fontSize = 12.sp)
            }

            // Save Preferences Sticky Action
            Button(
                onClick = {
                    if (isCustomer) {
                        val updatedSub = subscriberProfile.copy(
                            notifyMorningMenu = customerMorningMenuAlert,
                            notifyDriverDispatch = customerDispatchAlert,
                            notifyDropPhoto = customerProofAlert
                        )
                        TiffinRepository.updateSubscriberProfile(updatedSub)
                        savedSuccessMessage = "Subscriber preferences saved successfully!"
                    } else if (isRider) {
                        val updatedRider = riderProfile.copy(
                            isAlertChimeEnabled = riderSoundAlerts,
                            isOnDuty = riderOfflineCaching
                        )
                        TiffinRepository.updateRiderProfile(updatedRider)
                        savedSuccessMessage = "Rider delivery preferences saved successfully!"
                    } else {
                        val updated = settings.copy(
                            autoLockDailyOrders = autoLock,
                            defaultDietaryMode = dietaryMode,
                            kdsAutoRefresh = kdsAutoRefresh,
                            printStickerLabels = printLabels,
                            soundChimesAndAlerts = soundAlerts
                        )
                        TiffinRepository.updateSystemSettings(updated)
                        savedSuccessMessage = "System preferences saved successfully!"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Save, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Text("Save Preferences", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
