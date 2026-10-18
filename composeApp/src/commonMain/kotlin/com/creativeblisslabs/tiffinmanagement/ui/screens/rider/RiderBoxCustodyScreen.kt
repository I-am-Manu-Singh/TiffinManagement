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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun RiderBoxCustodyScreen(
    repository: TiffinRepository,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToDropPhoto: (String) -> Unit
) {
    val custodyState by repository.boxCustody.collectAsState()
    val riderProfile by repository.riderProfile.collectAsState()
    val branch by repository.currentBranch.collectAsState()
    val routeStops by repository.riderStops.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        // Sticky Header: Standard TiffinTopHeader
        com.example.myapplicationkmp.ui.components.TiffinTopHeader(
            branchName = branch.name,
            screenTitle = "Box Custody",
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
            // Screen Title & Shift Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Steel Box Custody & Returns", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                    Text("Real-time pod balance & reverse logistics", fontSize = 12.sp, color = ColorTokens.Secondary)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(ColorTokens.Primary.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(custodyState.shiftCode, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                }
            }

            // Primary Metric Hero Card (Dispatched, In Pod, Deficit)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerHigh),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Inventory2, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                            }
                            Text("Carrier Pod Status", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(99.dp))
                                .background(ColorTokens.Tertiary.copy(alpha = 0.1f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("QR Seal Verified", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.Tertiary)
                        }
                    }

                    // 3 Counters Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Dispatched
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Dispatched", fontSize = 11.sp, color = ColorTokens.Secondary)
                                Icon(Icons.Default.LocalShipping, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(14.dp))
                            }
                            Text("${custodyState.totalLoaded}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            Text("3-Tier Steel", fontSize = 10.sp, color = ColorTokens.Secondary)
                        }

                        // In Pod #12
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SecondaryContainer.copy(alpha = 0.4f))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("In Pod #12", fontSize = 11.sp, color = ColorTokens.Secondary)
                                Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(14.dp))
                            }
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("${custodyState.currentInPod}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text(" / ${custodyState.totalCapacity}", fontSize = 11.sp, color = ColorTokens.Secondary)
                            }
                            Text("22 Cln + 2 Hot", fontSize = 10.sp, color = ColorTokens.Secondary)
                        }

                        // Deficit (With Customers)
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.Primary.copy(alpha = 0.1f))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Deficit", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                                Icon(Icons.Default.PendingActions, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(14.dp))
                            }
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("${custodyState.deficitDue}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                                Text(" due", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                            }
                            Text("To Collect", fontSize = 10.sp, color = ColorTokens.Primary.copy(alpha = 0.8f))
                        }
                    }

                    // Pod Capacity Visual Heatmap Bar
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Pod #12 Physical Capacity", fontSize = 11.sp, color = ColorTokens.Secondary)
                            Text("${custodyState.freeSlots} Free Slots", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(ColorTokens.SurfaceContainerHigh)
                        ) {
                            val recoveredRatio = custodyState.recoveredCount.toFloat() / custodyState.totalCapacity
                            val hotRatio = custodyState.undeliveredCount.toFloat() / custodyState.totalCapacity
                            val remainingRatio = (1f - recoveredRatio - hotRatio).coerceAtLeast(0.1f)

                            Box(modifier = Modifier.weight(recoveredRatio).fillMaxSize().background(ColorTokens.Tertiary))
                            Box(modifier = Modifier.weight(hotRatio).fillMaxSize().background(ColorTokens.Primary))
                            Box(modifier = Modifier.weight(remainingRatio).fillMaxSize().background(Color.Transparent))
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ColorTokens.Tertiary))
                                Text("22 Recovered", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ColorTokens.Primary))
                                Text("2 Undelivered", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ColorTokens.SurfaceContainerHighest))
                                Text("12 Slots Left", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                        }
                    }
                }
            }

            // Quick Action Scanner Triggers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        repository.scanBoxReturn("TIF-3092", "stop-20")
                    },
                    modifier = Modifier.weight(1.4f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary)
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = ColorTokens.OnPrimary)
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Scan Tiffin Lid QR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimary)
                }

                Button(
                    onClick = { /* Open manual box id input */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainerLowest)
                ) {
                    Icon(Icons.Default.Dialpad, contentDescription = null, tint = ColorTokens.Secondary)
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Manual Box ID", fontSize = 12.sp, color = ColorTokens.OnSurface)
                }
            }

            // Route Active Checklist Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Route Active Checklist", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                    Text("Zone Indiranagar-B", fontSize = 12.sp, color = ColorTokens.Secondary)
                }

                // Dynamic Route Active Checklist
                if (routeStops.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ColorTokens.SurfaceContainerLowest)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No active route stops scheduled for current shift.", fontSize = 12.sp, color = ColorTokens.Secondary)
                    }
                } else {
                    routeStops.forEach { stop ->
                        val isDelivered = stop.status == "delivered"
                        val hasReturnBox = stop.returnBoxesToCollect > 0 || !stop.returnBoxScannedCode.isNullOrBlank()
                        
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (!isDelivered) 2.dp else 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isDelivered) ColorTokens.Tertiary.copy(alpha = 0.12f)
                                                    else ColorTokens.Primary
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                "${stop.sequenceNumber}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isDelivered) ColorTokens.Tertiary else ColorTokens.OnPrimary
                                            )
                                        }
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Text(stop.customerName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(22.dp))
                                                        .background(
                                                            if (isDelivered) ColorTokens.Tertiary.copy(alpha = 0.1f)
                                                            else ColorTokens.Primary.copy(alpha = 0.1f)
                                                        )
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        if (isDelivered) "Delivered" else if (hasReturnBox) "Collect ${stop.returnBoxesToCollect} Box" else "Drop Only",
                                                        fontSize = 10.sp,
                                                        color = if (isDelivered) ColorTokens.Tertiary else ColorTokens.Primary
                                                    )
                                                }
                                            }
                                            Text("${stop.wingFlat} • ${stop.addressLine}", fontSize = 11.sp, color = ColorTokens.Secondary)
                                        }
                                    }

                                    if (isDelivered) {
                                        Icon(Icons.Default.Verified, contentDescription = null, tint = ColorTokens.Tertiary, modifier = Modifier.size(20.dp))
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(ColorTokens.Primary.copy(alpha = 0.1f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("ETA ${stop.etaDisplay}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                                        }
                                    }
                                }

                                if (!isDelivered) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(ColorTokens.SurfaceContainerLow)
                                            .padding(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        if (stop.specialNote.isNotBlank()) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Icon(Icons.Default.DoorFront, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(14.dp))
                                                Text(stop.specialNote, fontSize = 11.sp, color = ColorTokens.Secondary)
                                            }
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Button(
                                                onClick = { onNavigateToDropPhoto(stop.id) },
                                                modifier = Modifier.weight(1f).height(36.dp),
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryContainer)
                                            ) {
                                                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = ColorTokens.OnPrimaryContainer, modifier = Modifier.size(15.dp))
                                                Spacer(modifier = Modifier.size(4.dp))
                                                Text("Scan at Door", fontSize = 11.sp, color = ColorTokens.OnPrimaryContainer)
                                            }
                                        }
                                    }
                                } else if (!stop.returnBoxScannedCode.isNullOrBlank()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(ColorTokens.SurfaceContainerLow)
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Scanned #${stop.returnBoxScannedCode}", fontSize = 11.sp, color = ColorTokens.OnSurface)
                                        Text("1 Set Secured ✓", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Tertiary)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Past Due Deficits Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.HistoryToggleOff, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(16.dp))
                        Text("Past Due Recoveries", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(22.dp)).background(ColorTokens.Primary.copy(alpha = 0.1f)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                        Text("3 Sets Behind", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                    }
                }

                custodyState.deficitsList.forEach { deficit ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(deficit.customerName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                        Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(ColorTokens.ErrorContainer).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                            Text("${deficit.daysDue} Days Due", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnErrorContainer)
                                        }
                                    }
                                    Text(deficit.flatAddress, fontSize = 11.sp, color = ColorTokens.Secondary)
                                }
                                Text("${deficit.setCount} Sets", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Deposit Held: ₹${deficit.depositHeld.toInt()}", fontSize = 11.sp, color = ColorTokens.Secondary)
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Button(
                                        onClick = { /* WhatsApp Reminder */ },
                                        modifier = Modifier.height(30.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Tertiary.copy(alpha = 0.15f))
                                    ) {
                                        Icon(Icons.Default.Send, contentDescription = null, tint = ColorTokens.Tertiary, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.size(4.dp))
                                        Text("WhatsApp Reminder", fontSize = 10.sp, color = ColorTokens.Tertiary, fontWeight = FontWeight.SemiBold)
                                    }
                                    Box(
                                        modifier = Modifier.size(30.dp).clip(RoundedCornerShape(6.dp)).background(ColorTokens.SurfaceContainer).clickable { /* Call */ },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Call, contentDescription = "Call", tint = ColorTokens.Secondary, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
