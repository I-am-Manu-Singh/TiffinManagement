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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.creativeblisslabs.tiffinmanagement.data.models.DeliveryRouteModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinTopHeader

@Composable
fun UpdateDeliveryRouteScreen(
    routeId: String = "route-1",
    onNavigateBack: () -> Unit = {}
) {
    val deliveryRoutes by TiffinRepository.deliveryRoutes.collectAsState()
    val route = deliveryRoutes.find { it.id == routeId } ?: deliveryRoutes.firstOrNull()

    var routeName by remember(route) { mutableStateOf(route?.name ?: "Main Route") }
    var selectedDriver by remember(route) { mutableStateOf(route?.assignedDriverName ?: "Unassigned") }
    var isDriverDropdownOpen by remember { mutableStateOf(false) }

    var originBranch by remember(route) { mutableStateOf(route?.originBranchName ?: "Main Branch") }
    var isBranchDropdownOpen by remember { mutableStateOf(false) }

    var isRouteActive by remember(route) { mutableStateOf(route?.isActive != false) }

    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val staffMembers by TiffinRepository.staffMembers.collectAsState()
    val branches by TiffinRepository.branches.collectAsState()
    val driverOptions = listOf("Unassigned") + staffMembers
        .filter { it.role.contains("driver", ignoreCase = true) || it.role.contains("delivery", ignoreCase = true) || it.role.contains("rider", ignoreCase = true) }
        .map { "${it.fullName} (${it.role})" }
        .ifEmpty { listOf("No drivers available") }
    val branchOptions = branches.map { it.name }.ifEmpty { listOf("Main Branch") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        // Scrollable Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Card 1: Route Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ColorTokens.Primary))
                            Text("ROUTE INFORMATION", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                        }
                        Text("Step 1 of 2", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                    }

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorTokens.BorderSubtle))

                    // Route Name
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Route Name *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        OutlinedTextField(
                            value = routeName,
                            onValueChange = { routeName = it },
                            placeholder = { Text("e.g. North Zone Morning", fontSize = 12.sp, color = ColorTokens.TextSecondary) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ColorTokens.SurfaceContainerLow,
                                unfocusedContainerColor = ColorTokens.SurfaceContainerLow,
                                focusedBorderColor = ColorTokens.Primary,
                                unfocusedBorderColor = ColorTokens.BorderSubtle
                            )
                        )
                    }

                    // Assigned Driver / Staff
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Assigned Driver / Staff", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp))
                                .clickable { isDriverDropdownOpen = !isDriverDropdownOpen }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(selectedDriver, fontSize = 13.sp, color = ColorTokens.OnSurface, fontWeight = FontWeight.Medium)
                                Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                            }

                            DropdownMenu(
                                expanded = isDriverDropdownOpen,
                                onDismissRequest = { isDriverDropdownOpen = false }
                            ) {
                                driverOptions.forEach { drv ->
                                    DropdownMenuItem(
                                        text = { Text(drv, fontSize = 13.sp) },
                                        onClick = {
                                            selectedDriver = drv
                                            isDriverDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Start Location (Origin Branch)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Start Location (Origin Branch) *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp))
                                .clickable { isBranchDropdownOpen = !isBranchDropdownOpen }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(originBranch, fontSize = 13.sp, color = ColorTokens.OnSurface, fontWeight = FontWeight.Medium)
                                Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                            }

                            DropdownMenu(
                                expanded = isBranchDropdownOpen,
                                onDismissRequest = { isBranchDropdownOpen = false }
                            ) {
                                branchOptions.forEach { br ->
                                    DropdownMenuItem(
                                        text = { Text(br, fontSize = 13.sp) },
                                        onClick = {
                                            originBranch = br
                                            isBranchDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Status
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Status", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { isRouteActive = true }
                            ) {
                                RadioButton(
                                    selected = isRouteActive,
                                    onClick = { isRouteActive = true },
                                    colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.TertiaryMint)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ColorTokens.TertiaryMint))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Active", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { isRouteActive = false }
                            ) {
                                RadioButton(
                                    selected = !isRouteActive,
                                    onClick = { isRouteActive = false },
                                    colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Secondary)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Inactive", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = ColorTokens.Secondary)
                            }
                        }
                    }
                }
            }

            // Card 2: Route Stop Sequence Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("ROUTE STOP SEQUENCE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                            Text("Drag & Drop to Sort sequence", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(22.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("${route?.stopsCount ?: 0} Stops", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Secondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val stops by TiffinRepository.riderStops.collectAsState()
                    val branchLat = (branches.find { it.name == originBranch }?.latitude?.toDoubleOrNull()) ?: 19.1663
                    val branchLng = (branches.find { it.name == originBranch }?.longitude?.toDoubleOrNull()) ?: 72.9986

                    // Live Interactive Route Map Card
                    com.example.myapplicationkmp.ui.components.TiffinDeliveryMapCard(
                        originTitle = originBranch,
                        originLat = branchLat,
                        originLng = branchLng,
                        stops = stops,
                        riderName = selectedDriver,
                        activeStopIndex = 0,
                        showControls = false
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Route Action Toolbar (Auto-Optimize + Refresh Stops)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val optimized = TiffinRepository.generateAndSaveTodayRoute(routeId)
                                successMessage = "Route optimized (${optimized.stopsCount} stops, ${optimized.totalDistanceKm} km, ~${optimized.totalDurationMinutes} mins)"
                            },
                            modifier = Modifier.weight(1f).height(42.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary)
                        ) {
                            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Auto-Optimize (TSP)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Button(
                            onClick = {
                                TiffinRepository.refreshTodayRoutes()
                                successMessage = "Refreshed stops and active subscriptions from DB"
                            },
                            modifier = Modifier.weight(1f).height(42.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainerHigh)
                        ) {
                            Text("Refresh from DB", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Stop Sequence List
                    stops.forEach { stop ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (stop.status == "delivered") ColorTokens.Tertiary else ColorTokens.Primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${stop.sequenceNumber}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Column {
                                    Text(stop.customerName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                                    Text("${stop.wingFlat} • ${stop.addressLine}", fontSize = 11.sp, color = ColorTokens.Secondary)
                                }
                            }
                            Text("ETA ${stop.etaDisplay}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                        }
                    }
                }
            }

            // Card 3: Unassigned Customers Panel
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("UNASSIGNED CUSTOMERS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                    Text("Drag into the sequence panel to assign", fontSize = 11.sp, color = ColorTokens.TextSecondary)

                    Spacer(modifier = Modifier.height(14.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFECFDF5))
                                .border(1.dp, Color(0xFFA7F3D0), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Check, null, tint = Color(0xFF059669), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("All active customers are already assigned.", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text("New subscribers will appear here automatically.", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                    }
                }
            }

            if (errorMessage != null) {
                Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 12.sp)
            }

            if (successMessage != null) {
                Text(successMessage ?: "", color = Color(0xFF16A34A), fontSize = 12.sp)
            }

            // Save Route Button
            Button(
                onClick = {
                    errorMessage = null
                    successMessage = null
                    if (routeName.isBlank()) {
                        errorMessage = "Route Name cannot be empty"
                        return@Button
                    }
                    val updated = DeliveryRouteModel(
                        id = route?.id ?: "route-1",
                        name = routeName.trim(),
                        originBranchId = "b-1",
                        originBranchName = originBranch,
                        assignedDriverId = if (selectedDriver == "Unassigned") null else "drv-1",
                        assignedDriverName = selectedDriver,
                        isActive = isRouteActive,
                        stopsCount = route?.stopsCount ?: 0
                    )
                    TiffinRepository.updateDeliveryRoute(updated)
                    successMessage = "Route saved successfully"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Text("Save Route", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
