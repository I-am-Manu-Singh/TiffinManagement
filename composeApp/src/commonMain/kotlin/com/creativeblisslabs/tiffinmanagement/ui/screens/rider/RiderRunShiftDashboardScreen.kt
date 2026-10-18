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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun RiderRunShiftDashboardScreen(
    repository: TiffinRepository,
    onNavigateToStops: () -> Unit,
    onNavigateToDropPhoto: (String) -> Unit,
    onNavigateToIncident: (String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val riderProfile by repository.riderProfile.collectAsState()
    val branch by repository.currentBranch.collectAsState()
    val stops by repository.riderStops.collectAsState()
    val currentStop = stops.firstOrNull { it.sequenceNumber == 19 } ?: stops.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        // Sticky Header: Status Pill + Temp + Actions
        com.example.myapplicationkmp.ui.components.TiffinTopHeader(
            branchName = branch.name,
            screenTitle = "Delivery Run",
            userName = riderProfile.fullName.ifBlank { "Rider" },
            userRole = "Rider",
            subtitle = if (riderProfile.isOnDuty) "On Duty • ${riderProfile.carrierPodTempC}°C" else "Off Duty • ${riderProfile.carrierPodTempC}°C",
            onNotificationClick = onNavigateToNotifications,
            onProfileClick = onNavigateToProfile
        )

        // Scrollable Run Dashboard Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 0. Live Delivery Route & Zero-Cost Vector Map Card
            val originLat = branch.latitude?.toDoubleOrNull() ?: 19.1663
            val originLng = branch.longitude?.toDoubleOrNull() ?: 72.9986
            val activeIdx = stops.indexOfFirst { it.id == currentStop?.id }.let { if (it >= 0) it else 0 }

            com.example.myapplicationkmp.ui.components.TiffinDeliveryMapCard(
                originTitle = branch.name,
                originLat = originLat,
                originLng = originLng,
                stops = stops,
                riderName = riderProfile.fullName.ifBlank { "Rider Partner" },
                activeStopIndex = activeIdx,
                showControls = true,
                onRefreshRoute = {
                    repository.refreshTodayRoutes()
                }
            )

            // 1. Driver Header Profile & Fleet Metadata Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
                            Text(
                                text = "RK",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = ColorTokens.OnPrimaryFixed
                            )
                        }
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = riderProfile.fullName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(22.dp))
                                        .background(ColorTokens.SecondaryFixed)
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star",
                                        tint = ColorTokens.Primary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "${riderProfile.rating}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSecondaryFixed
                                    )
                                }
                            }
                            Text(
                                text = "${riderProfile.vehicleModel} • ${riderProfile.vehiclePlate}",
                                fontSize = 13.sp,
                                color = ColorTokens.Secondary
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(ColorTokens.PrimaryFixed)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = riderProfile.currentShiftName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnPrimaryFixed
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = riderProfile.shiftWindow,
                            fontSize = 11.sp,
                            color = ColorTokens.Secondary
                        )
                    }
                }
            }

            // 2. Thermal Integrity & Bag Status Hero Card
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
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Whatshot,
                                    contentDescription = "Heated Pod",
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Heated Tiffin ${riderProfile.carrierPodId}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                                Text(
                                    text = "Aero-Insulated • Sensor Active",
                                    fontSize = 11.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.TertiaryFixed)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${riderProfile.carrierPodTempC}°C Sealed • Optimal",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnTertiaryFixed
                            )
                        }
                    }

                    // Shift Progress Pipeline
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Route Completion",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ColorTokens.OnSurface
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${riderProfile.completedDrops}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.Primary
                                )
                                Text(
                                    text = " / ${riderProfile.totalDrops} drops",
                                    fontSize = 13.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }

                        // Progress Multi-state Indicator Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(ColorTokens.SurfaceContainer)
                                .padding(2.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            val completedWeight = riderProfile.completedDrops.toFloat() / riderProfile.totalDrops.coerceAtLeast(1)
                            val enRouteWeight = riderProfile.enRouteDrops.toFloat() / riderProfile.totalDrops.coerceAtLeast(1)
                            val remainingWeight = (1f - completedWeight - enRouteWeight).coerceAtLeast(0.05f)

                            Box(
                                modifier = Modifier
                                    .weight(completedWeight.coerceAtLeast(0.05f))
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(ColorTokens.Tertiary)
                                    .fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .weight(enRouteWeight.coerceAtLeast(0.05f))
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(ColorTokens.PrimaryContainer)
                                    .fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .weight(remainingWeight)
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(ColorTokens.SurfaceDim)
                                    .fillMaxSize()
                            )
                        }

                        // Pill Breakdown Telemetry
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Done
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.Tertiary)
                                )
                                Column {
                                    Text(
                                        text = "${riderProfile.completedDrops} Done",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "Served hot",
                                        fontSize = 10.sp,
                                        color = ColorTokens.Secondary
                                    )
                                }
                            }
                            // En-Route
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.PrimaryContainer)
                                )
                                Column {
                                    Text(
                                        text = "${riderProfile.enRouteDrops} En-Route",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "Active transit",
                                        fontSize = 10.sp,
                                        color = ColorTokens.Secondary
                                    )
                                }
                            }
                            // Returns
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.Secondary)
                                )
                                Column {
                                    Text(
                                        text = "${riderProfile.returnBoxesInPod} Returns",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "Steel carriers",
                                        fontSize = 10.sp,
                                        color = ColorTokens.Secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Current High-Priority Drop Target Card
            if (currentStop != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Top Sequence Row
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
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ColorTokens.Primary)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "NEXT STOP #${currentStop.sequenceNumber}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnPrimary
                                    )
                                }
                                Text(
                                    text = currentStop.subscriberIdCode,
                                    fontSize = 11.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(ColorTokens.PrimaryFixed)
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "ETA",
                                    tint = ColorTokens.PrimaryContainer,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${currentStop.etaDisplay} (~12m)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.PrimaryContainer
                                )
                            }
                        }

                        // Customer Details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentStop.customerName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ColorTokens.SecondaryFixed)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Tower Entry: #4021",
                                    fontSize = 11.sp,
                                    color = ColorTokens.OnSecondaryFixed
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Address",
                                tint = ColorTokens.Primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "${currentStop.wingFlat}, ${currentStop.addressLine}",
                                fontSize = 13.sp,
                                color = ColorTokens.OnSurface
                            )
                        }

                        // Special Delivery Note
                        if (currentStop.specialNote.isNotBlank()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainer)
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "Note: \"${currentStop.specialNote}\"",
                                    fontSize = 12.sp,
                                    color = ColorTokens.OnSurface
                                )
                            }
                        }

                        // Dietary Items List
                        currentStop.items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Veg Dot Box
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(ColorTokens.SurfaceContainerLowest),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(ColorTokens.Tertiary)
                                        )
                                    }
                                    Text(
                                        text = item,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ColorTokens.OnSurface
                                    )
                                }
                                Text(
                                    text = currentStop.podBinSlot,
                                    fontSize = 11.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }

                        // Primary Quick Actions Grid (Navigate, Call, Drop Photo)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
                            Button(
                                onClick = {
                                    com.example.myapplicationkmp.ui.components.openNativeTurnByTurnNavigation(
                                        uriHandler = uriHandler,
                                        latitude = currentStop.latitude,
                                        longitude = currentStop.longitude,
                                        label = currentStop.customerName
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainerHigh)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.NearMe,
                                        contentDescription = "Navigate",
                                        tint = ColorTokens.Primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Navigate",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                }
                            }

                            Button(
                                onClick = { /* Direct Call masked partner dialer */ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainerHigh)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Call",
                                        tint = ColorTokens.Secondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Call Customer",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                }
                            }

                            Button(
                                onClick = { onNavigateToDropPhoto(currentStop.id) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoCamera,
                                        contentDescription = "Drop Photo",
                                        tint = ColorTokens.OnPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Drop Photo",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Shift Performance Mosaic
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // On-Time
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("On-Time", fontSize = 11.sp, color = ColorTokens.Secondary)
                            Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = ColorTokens.Tertiary, modifier = Modifier.size(14.dp))
                        }
                        Text("${riderProfile.onTimeRatePercent}%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        Text("+2.1% bonus tier", fontSize = 9.sp, color = ColorTokens.Tertiary)
                    }
                }

                // Distance
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Distance", fontSize = 11.sp, color = ColorTokens.Secondary)
                            Icon(Icons.Default.NearMe, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(14.dp))
                        }
                        Text("14.2 km", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        Text("8.4 km to hub", fontSize = 9.sp, color = ColorTokens.Secondary)
                    }
                }

                // Shift Window
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Shift Window", fontSize = 11.sp, color = ColorTokens.Secondary)
                            Icon(Icons.Default.Timer, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(14.dp))
                        }
                        Text("1h 15m", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        Text("45m left", fontSize = 9.sp, color = ColorTokens.Secondary)
                    }
                }
            }

            // 5. Next in Carrier Queue Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Next in Carrier (${riderProfile.carrierPodId})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                    Text(
                        text = "Full Route View",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ColorTokens.Primary,
                        modifier = Modifier.clickable { onNavigateToStops() }
                    )
                }

                stops.filter { it.sequenceNumber > 19 }.take(2).forEach { stop ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ColorTokens.SurfaceContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${stop.sequenceNumber}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.Secondary
                                    )
                                }
                                Column {
                                    Text(
                                        text = "${stop.customerName} (${stop.items.firstOrNull() ?: "Meal"})",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ColorTokens.OnSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${stop.wingFlat} • ETA ${stop.etaDisplay}",
                                        fontSize = 11.sp,
                                        color = ColorTokens.Secondary
                                    )
                                }
                            }

                            if (stop.returnBoxesToCollect > 0) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ColorTokens.TertiaryFixed)
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "+${stop.returnBoxesToCollect} Return Box",
                                        fontSize = 10.sp,
                                        color = ColorTokens.OnTertiaryFixed
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ColorTokens.SurfaceContainer)
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "Prepaid OTP",
                                        fontSize = 10.sp,
                                        color = ColorTokens.Secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 6. Fleet Assistance & Sync Controls
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Fleet Assistance & Sync",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.CloudDone, contentDescription = null, tint = ColorTokens.Tertiary, modifier = Modifier.size(16.dp))
                            Text("All synced", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.Tertiary)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Kitchen Dispatch Hotline
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .clickable { /* Dial Hub hotline */ }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.ErrorContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.SupportAgent, contentDescription = null, tint = ColorTokens.OnErrorContainer, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text("Kitchen Dispatch", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text("Koramangala Hub", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                        }

                        // Report Issue
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .clickable { onNavigateToIncident(currentStop?.id ?: "stop-19") }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.SecondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ReportProblem, contentDescription = null, tint = ColorTokens.OnSecondaryContainer, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text("Report Issue", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text("Spill / Bike Fault", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                        }
                    }
                }
            }
        }
    }
}
