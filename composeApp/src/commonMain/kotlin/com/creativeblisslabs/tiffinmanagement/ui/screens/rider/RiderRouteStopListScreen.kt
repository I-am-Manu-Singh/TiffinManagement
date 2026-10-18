package com.creativeblisslabs.tiffinmanagement.ui.screens.rider

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Moving
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHorizontalCircle
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.VpnKey
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
import com.creativeblisslabs.tiffinmanagement.data.models.RiderDeliveryStop
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun RiderRouteStopListScreen(
    repository: TiffinRepository,
    onNavigateToDropPhoto: (String) -> Unit,
    onNavigateToIncident: (String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val stops by repository.riderStops.collectAsState()
    val riderProfile by repository.riderProfile.collectAsState()
    val branch by repository.currentBranch.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf("all") } // all, pending, delivered, hold
    var selectedClusterFilter by remember { mutableStateOf("all") } // all, sunshine, hiranandani, green-glen

    val filteredStops = stops.filter { stop ->
        val matchesSearch = searchQuery.isBlank() ||
            stop.customerName.contains(searchQuery, ignoreCase = true) ||
            stop.wingFlat.contains(searchQuery, ignoreCase = true) ||
            stop.addressLine.contains(searchQuery, ignoreCase = true)

        val matchesStatus = when (selectedStatusFilter) {
            "pending" -> stop.status == "pending" || stop.status == "en_route"
            "delivered" -> stop.status == "delivered"
            "hold" -> stop.status == "hold" || stop.status == "skipped"
            else -> true
        }

        val matchesCluster = when (selectedClusterFilter) {
            "all" -> true
            else -> stop.societyCluster.equals(selectedClusterFilter, ignoreCase = true)
        }

        matchesSearch && matchesStatus && matchesCluster
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
       
   // Sticky Header: Status Pill + Temp + Actions
        com.example.myapplicationkmp.ui.components.TiffinTopHeader(
            branchName = branch.name,
            screenTitle = "Route Stops",
            userName = riderProfile.fullName.ifBlank { "Rider" },
            userRole = "Rider",
            subtitle = if (riderProfile.isOnDuty) "On Duty • ${riderProfile.carrierPodTempC}°C" else "Off Duty • ${riderProfile.carrierPodTempC}°C",
            onNotificationClick = onNavigateToNotifications,
            onProfileClick = onNavigateToProfile
        )
        // Top Filter Bar & Search
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search customer, flat, or wing...", fontSize = 13.sp, color = ColorTokens.Secondary) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = ColorTokens.Secondary, modifier = Modifier.size(20.dp))
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ColorTokens.SurfaceContainerLowest,
                    unfocusedContainerColor = ColorTokens.SurfaceContainerLow,
                    focusedBorderColor = ColorTokens.Primary,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            // Status Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val statusTabs = listOf(
                    Triple("all", "All", stops.size),
                    Triple("pending", "Pending", stops.count { it.status == "pending" || it.status == "en_route" }),
                    Triple("delivered", "Delivered", stops.count { it.status == "delivered" }),
                    Triple("hold", "Holds / Skipped", stops.count { it.status == "hold" || it.status == "skipped" })
                )

                statusTabs.forEach { (key, label, count) ->
                    val isSelected = selectedStatusFilter == key
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(if (isSelected) ColorTokens.Primary else ColorTokens.SurfaceContainer)
                            .clickable { selectedStatusFilter = key }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) ColorTokens.OnPrimary else ColorTokens.OnSurfaceVariant
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(if (isSelected) ColorTokens.PrimaryContainer.copy(alpha = 0.5f) else ColorTokens.SurfaceVariant)
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "$count",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) ColorTokens.OnPrimary else ColorTokens.OnSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Society Cluster Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val clusters = listOf(
                    "all" to "All Clusters",
                    "sunshine" to "Sunshine Residency (${stops.count { it.societyCluster == "sunshine" }})",
                    "hiranandani" to "Hiranandani Estate (${stops.count { it.societyCluster == "hiranandani" }})",
                    "green-glen" to "Green Glen Layout (${stops.count { it.societyCluster == "green-glen" }})"
                )

                clusters.forEach { (key, title) ->
                    val isSelected = selectedClusterFilter == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) ColorTokens.SecondaryContainer else ColorTokens.SurfaceContainerLow)
                            .clickable { selectedClusterFilter = key }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) ColorTokens.OnSecondaryContainer else ColorTokens.Secondary
                        )
                    }
                }
            }
        }

        // Live Sequence Strip Overview
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Moving,
                            contentDescription = null,
                            tint = ColorTokens.Primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Stop Sequence Flow",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.Primary
                        )
                    }
                    Text(
                        text = "Next: Sunshine Wing B",
                        fontSize = 11.sp,
                        color = ColorTokens.Secondary
                    )
                }

                // Micro Sequence Nodes
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val nodes = listOf(17 to "done", 18 to "done", 19 to "current", 20 to "next", 21 to "pending", 22 to "pending", 28 to "pending")
                    nodes.forEach { (nodeNum, state) ->
                        when (state) {
                            "done" -> {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.TertiaryFixed),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("$nodeNum", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnTertiaryFixed)
                                }
                                Box(modifier = Modifier.size(width = 10.dp, height = 2.dp).background(ColorTokens.Tertiary))
                            }
                            "current" -> {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.Primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("$nodeNum", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimary)
                                }
                                Box(modifier = Modifier.size(width = 10.dp, height = 2.dp).background(ColorTokens.SurfaceContainerHigh))
                            }
                            else -> {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.SurfaceContainerHigh),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("$nodeNum", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.Secondary)
                                }
                                if (nodeNum != 28) {
                                    Box(modifier = Modifier.size(width = 10.dp, height = 2.dp).background(ColorTokens.SurfaceContainerHigh))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Lazy List of Stop Cards
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredStops, key = { it.id }) { stop ->
                val isNextUp = stop.sequenceNumber == 19
                RiderStopCard(
                    stop = stop,
                    isNextUp = isNextUp,
                    onStartDrop = { onNavigateToDropPhoto(stop.id) },
                    onReportIncident = { onNavigateToIncident(stop.id) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun RiderStopCard(
    stop: RiderDeliveryStop,
    isNextUp: Boolean,
    onStartDrop: () -> Unit,
    onReportIncident: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isNextUp) 2.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Row: Badge + ETA
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
                            .clip(RoundedCornerShape(22.dp))
                            .background(if (isNextUp) ColorTokens.PrimaryFixed else ColorTokens.SurfaceContainerHigh)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "#${stop.sequenceNumber}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isNextUp) ColorTokens.OnPrimaryFixed else ColorTokens.OnSurface
                        )
                    }
                    if (isNextUp) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(ColorTokens.Primary.copy(alpha = 0.1f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "NEXT UP",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.Primary
                            )
                        }
                    }
                }
                Text(
                    text = "ETA ${stop.etaDisplay}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isNextUp) ColorTokens.Primary else ColorTokens.Secondary
                )
            }

            // Customer Name & Wing with Mini Map Thumbnail
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stop.customerName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Apartment,
                            contentDescription = null,
                            tint = if (isNextUp) ColorTokens.Primary else ColorTokens.Secondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "${stop.wingFlat} • ${stop.addressLine}",
                            fontSize = 12.sp,
                            color = ColorTokens.Secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Zero-cost Map Thumbnail
                com.example.myapplicationkmp.ui.components.TiffinMiniMapThumbnail(
                    latitude = stop.latitude,
                    longitude = stop.longitude,
                    label = stop.customerName,
                    sequenceNumber = stop.sequenceNumber,
                    isCompleted = stop.status == "delivered",
                    modifier = Modifier.size(width = 72.dp, height = 56.dp)
                )
            }

            // Meal Pack Info Chips
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(ColorTokens.SurfaceContainerLow)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
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
                                .size(14.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(ColorTokens.SurfaceContainerLowest),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ColorTokens.Tertiary))
                        }
                        Text(
                            text = stop.items.joinToString(" + "),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ColorTokens.OnSurface
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(ColorTokens.SurfaceContainerHigh)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${stop.items.size} Tiffins",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurfaceVariant
                        )
                    }
                }

                // Gate Access
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(ColorTokens.SecondaryFixed.copy(alpha = 0.5f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VpnKey,
                        contentDescription = null,
                        tint = ColorTokens.Primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Gate Access: Contactless Guard Code #${stop.contactlessOtp}",
                        fontSize = 11.sp,
                        color = ColorTokens.OnSecondaryFixedVariant
                    )
                }

                // Box Return Note if any
                if (stop.returnBoxesToCollect > 0) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ColorTokens.SecondaryFixed)
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHorizontalCircle,
                            contentDescription = null,
                            tint = ColorTokens.Primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Collect ${stop.returnBoxesToCollect} Empty Tiffin Box",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = ColorTokens.OnSecondaryFixed
                        )
                    }
                }
            }

            // Quick Actions: Call + WhatsApp + Directions + Start Drop
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ColorTokens.SurfaceContainer)
                            .clickable { /* Call customer */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Call", tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ColorTokens.SurfaceContainer)
                            .clickable { /* WhatsApp customer */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = "Chat", tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
                    Button(
                        onClick = {
                            com.example.myapplicationkmp.ui.components.openNativeTurnByTurnNavigation(
                                uriHandler = uriHandler,
                                latitude = stop.latitude,
                                longitude = stop.longitude,
                                label = stop.customerName
                            )
                        },
                        modifier = Modifier.height(36.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainerHigh)
                    ) {
                        Icon(Icons.Default.Navigation, contentDescription = null, tint = ColorTokens.OnSurface, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Directions", fontSize = 11.sp, color = ColorTokens.OnSurface)
                    }

                    Button(
                        onClick = onStartDrop,
                        modifier = Modifier.height(36.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isNextUp) ColorTokens.Primary else ColorTokens.PrimaryContainer)
                    ) {
                        Icon(Icons.Default.TaskAlt, contentDescription = null, tint = ColorTokens.OnPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Start Drop", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimary)
                    }
                }
            }
        }
    }
}
