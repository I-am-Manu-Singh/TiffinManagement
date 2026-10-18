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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHorizontalCircle
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Whatshot
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import androidx.compose.runtime.remember
import kotlinx.coroutines.launch

@Composable
fun RiderEarningsBoxLedgerScreen(
    repository: TiffinRepository,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val earnings by repository.riderEarnings.collectAsState()
    val riderProfile by repository.riderProfile.collectAsState()
    val branch by repository.currentBranch.collectAsState()

    val custodyState by repository.boxCustody.collectAsState()
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()
    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorTokens.Surface)
        ) {
              // Sticky Header: Status Pill + Temp + Actions
            com.example.myapplicationkmp.ui.components.TiffinTopHeader(
                branchName = branch.name,
                screenTitle = "Earnings Ledger",
                userName = riderProfile.fullName.ifBlank { "Rider" },
                userRole = "Rider",
                subtitle = if (riderProfile.isOnDuty) "On Duty • ${riderProfile.carrierPodTempC}°C" else "Off Duty • ${riderProfile.carrierPodTempC}°C",
                onNotificationClick = onNavigateToNotifications,
                onProfileClick = onNavigateToProfile
            )

            // Scrollable Body
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
            // Partner Profile Header
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.PrimaryFixed),
                            contentAlignment = Alignment.Center
                        ) {
                            val initials = riderProfile.fullName
                                .split(" ")
                                .filter { it.isNotBlank() }
                                .mapNotNull { it.firstOrNull()?.toString() }
                                .take(2)
                                .joinToString("")
                                .ifBlank { "" }
                            Text(initials, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimaryFixed)
                        }
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(riderProfile.fullName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Box(modifier = Modifier.clip(RoundedCornerShape(22.dp)).background(ColorTokens.SecondaryContainer).padding(horizontal = 6.dp, vertical = 1.dp)) {
                                    Text(riderProfile.driverIdCode, fontSize = 10.sp, color = ColorTokens.OnSecondaryContainer)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(13.dp))
                                Text("${riderProfile.rating}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                                Text("• ${riderProfile.totalSafeDrops}+ safe drops", fontSize = 11.sp, color = ColorTokens.Secondary)
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(ColorTokens.TertiaryFixed)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = ColorTokens.OnTertiaryFixed, modifier = Modifier.size(13.dp))
                            Text("Verified Fleet", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnTertiaryFixed)
                        }
                    }
                }
            }

            // Today's Earnings Breakdown
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Today's Earnings", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            Text("Lunch Shift • Active", fontSize = 11.sp, color = ColorTokens.Secondary)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("₹${earnings.todayTotal.toInt()}.00", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                            Text("3 Sources", fontSize = 10.sp, color = ColorTokens.Tertiary)
                        }
                    }

                    // Item 1: Completed Drops
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(ColorTokens.SecondaryContainer), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Moped, contentDescription = null, tint = ColorTokens.OnSecondaryContainer, modifier = Modifier.size(16.dp))
                            }
                            Column {
                                Text("${earnings.completedDropsCount} Completed Drops", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                                Text("₹25 per drop standard base", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                        }
                        Text("₹${earnings.completedDropsPayout.toInt()}.00", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                    }

                    // Item 2: Box Collections
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(ColorTokens.TertiaryFixed), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Recycling, contentDescription = null, tint = ColorTokens.OnTertiaryFixed, modifier = Modifier.size(16.dp))
                            }
                            Column {
                                Text("${earnings.boxCollectionsCount} Box Collections", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                                Text("₹10 carrier recovery bonus", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                        }
                        Text("₹${earnings.boxCollectionsPayout.toInt()}.00", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                    }

                    // Item 3: Peak Rush
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(ColorTokens.PrimaryFixed), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Whatshot, contentDescription = null, tint = ColorTokens.OnPrimaryFixed, modifier = Modifier.size(16.dp))
                            }
                            Column {
                                Text("Peak Lunch Rush Spike", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                                Text("12:30 PM - 02:00 PM On-Time", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                        }
                        Text("₹${earnings.peakRushSpikePayout.toInt()}.00", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                    }
                }
            }

            // Tiffin Box Ledger Section
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.LunchDining, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(20.dp))
                            Text("Tiffin Ledger & Carrier", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        }
                        Box(modifier = Modifier.clip(RoundedCornerShape(22.dp)).background(ColorTokens.PrimaryFixed).padding(horizontal = 8.dp, vertical = 2.dp)) {
                            Text("Live Route", fontSize = 10.sp, color = ColorTokens.OnPrimaryFixed)
                        }
                    }

                    // Inventory Visual Metrics 2x2 Bento
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column(modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Kitchen Loaded", fontSize = 10.sp, color = ColorTokens.Secondary)
                                Icon(Icons.Default.Kitchen, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(14.dp))
                            }
                            Text("${custodyState.totalLoaded} Boxes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            Text("Morning dispatch", fontSize = 10.sp, color = ColorTokens.OnSurfaceVariant)
                        }
                        Column(modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Delivered", fontSize = 10.sp, color = ColorTokens.Tertiary)
                                Icon(Icons.Default.Done, contentDescription = null, tint = ColorTokens.Tertiary, modifier = Modifier.size(14.dp))
                            }
                            Text("${earnings.completedDropsCount} Drops", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            Text("${(custodyState.totalLoaded - earnings.completedDropsCount).coerceAtLeast(0)} remaining on run", fontSize = 10.sp, color = ColorTokens.Tertiary)
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column(modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp)).background(ColorTokens.SurfaceContainerLow).padding(10.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Empty Recovered", fontSize = 10.sp, color = ColorTokens.Tertiary)
                                Icon(Icons.Default.SwapHorizontalCircle, contentDescription = null, tint = ColorTokens.Tertiary, modifier = Modifier.size(14.dp))
                            }
                            Text("${custodyState.recoveredCount} Empty", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            Text("Verified & tagged", fontSize = 10.sp, color = ColorTokens.Tertiary)
                        }
                        Column(modifier = Modifier.weight(1f).clip(RoundedCornerShape(10.dp)).background(ColorTokens.ErrorContainer).padding(10.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("With Customers", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnErrorContainer)
                                Icon(Icons.Default.ReportProblem, contentDescription = null, tint = ColorTokens.OnErrorContainer, modifier = Modifier.size(14.dp))
                            }
                            Text("${custodyState.deficitDue.toString().padStart(2, '0')} Deficit", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnErrorContainer)
                            Text("Needs collection", fontSize = 10.sp, color = ColorTokens.OnErrorContainer)
                        }
                    }

                    // In Bike Carrier Right Now
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainer)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(ColorTokens.SurfaceContainerHighest), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = ColorTokens.OnSurface, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text("In Bike Carrier Right Now", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text("${custodyState.currentInPod} Pod Custody in Vehicle", fontSize = 10.sp, color = ColorTokens.OnSurfaceVariant)
                            }
                        }
                        Box(
                            modifier = Modifier.size(32.dp).clip(CircleShape).background(ColorTokens.TertiaryFixed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${custodyState.currentInPod}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnTertiaryFixed)
                        }
                    }

                    // Outstanding Boxes List
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Pending Steel Return (${custodyState.deficitsList.size} Custodians)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            Text("1-Tap WhatsApp Ping", fontSize = 11.sp, color = ColorTokens.Primary)
                        }

                        custodyState.deficitsList.forEach { deficit ->
                            val initials = deficit.customerName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(ColorTokens.SurfaceContainerHigh), contentAlignment = Alignment.Center) {
                                        Text(initials, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurfaceVariant)
                                    }
                                    Column {
                                        Text("${deficit.customerName} (${deficit.flatAddress})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                        Text("${deficit.setCount} Stainless Sets • ${deficit.daysDue} Days Due", fontSize = 10.sp, color = ColorTokens.Secondary)
                                    }
                                }
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Dispatched WhatsApp return reminder to ${deficit.customerName}")
                                        }
                                    },
                                    modifier = Modifier.height(30.dp),
                                    shape = RoundedCornerShape(22.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Tertiary)
                                ) {
                                    Icon(Icons.Default.Chat, contentDescription = null, tint = ColorTokens.OnTertiary, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.size(4.dp))
                                    Text("Remind", fontSize = 10.sp, color = ColorTokens.OnTertiary)
                                }
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            androidx.compose.material3.SnackbarHost(
                hostState = snackbarHostState
            )
        }
    }
}
}
