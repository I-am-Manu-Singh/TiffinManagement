package com.creativeblisslabs.tiffinmanagement.ui.screens.customer

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinCustomerHeader
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CustomerLiveDeliveryTrackerScreen(
    repository: TiffinRepository,
    onNavigateBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToFeedback: () -> Unit,
    onNavigateToReportIssue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by repository.subscriberProfile.collectAsState()
    val riderProfile by repository.riderProfile.collectAsState()
    val riderStops by repository.riderStops.collectAsState()
    // Find customer stop if mapped, or fallback to stop 19 (Aarav Sharma)
    val currentStop = riderStops.firstOrNull { it.customerName.equals(profile.fullName, ignoreCase = true) || it.subscriberIdCode == profile.subscriberNumber }
        ?: riderStops.firstOrNull { it.sequenceNumber == 19 }
        ?: riderStops.first()
    val isDelivered = currentStop.status == "delivered"
    val isOtpDone = currentStop.isOtpVerified
    val orderCode = profile.subscriberNumber.ifBlank { "#SUB-8842" }

    val driverName = riderProfile.fullName.ifBlank { "Your Delivery Partner" }
    val driverPhone = riderProfile.phone?.ifBlank { null }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var copiedOtp by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = ColorTokens.Surface,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorTokens.Surface)
        ) {
            // Screen Header
            TiffinCustomerHeader(
                title = "Live Delivery",
                subtitle = "TIFFIN MANAGER",
                isActive = true,
                onNotificationClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Live ETA: Arriving by 12:45 PM.")
                    }
                },
                onProfileClick = onNavigateToProfile
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp)
            ) {
                // Screen Level Sub-header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Column(modifier = Modifier.weight(1f, fill = false)) {
                            Text(
                                text = "Live Delivery Status",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Order $orderCode • ${profile.planTitle.ifBlank { "Standard North Veg Meal" }}",
                                style = MaterialTheme.typography.labelSmall,
                                color = ColorTokens.Secondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Delivery tracking link copied to clipboard!")
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.SurfaceContainer)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Route",
                                tint = ColorTokens.OnSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Connecting to Kitchen Dispatch Support...")
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.ErrorContainer)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SupportAgent,
                                contentDescription = "Call Kitchen Hub",
                                tint = ColorTokens.OnErrorContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Interactive Map Simulation Canvas
                val branch by repository.currentBranch.collectAsState()
                val originLat = branch.latitude?.toDoubleOrNull() ?: 19.1663
                val originLng = branch.longitude?.toDoubleOrNull() ?: 72.9986
                val activeIdx = riderStops.indexOfFirst { it.id == currentStop.id }.let { if (it >= 0) it else 0 }

                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    com.example.myapplicationkmp.ui.components.TiffinDeliveryMapCard(
                        originTitle = branch.name,
                        originLat = originLat,
                        originLng = originLng,
                        stops = riderStops,
                        riderName = driverName,
                        activeStopIndex = activeIdx,
                        showControls = true,
                        onRefreshRoute = {
                            repository.refreshTodayRoutes()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Live Status & ETA Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(ColorTokens.Tertiary)
                                    )
                                    Text(
                                        text = "OUT FOR DELIVERY",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.Tertiary,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "12:45 PM",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "(in ~12 mins)",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ColorTokens.Primary
                                    )
                                }
                                Text(
                                    text = "Driver is approaching the 80ft Road signal junction.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ColorTokens.Secondary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ColorTokens.SecondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = ColorTokens.OnSecondaryContainer,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "LIVE",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSecondaryContainer
                                    )
                                }
                            }
                        }

                        Divider(color = ColorTokens.SurfaceContainerHigh)

                        // 4-Step Vertical Timeline
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            // Step 1: Prepped
                            TimelineItem(
                                title = "Order Hot-Packed & Sealed",
                                time = "11:45 AM",
                                isDone = true,
                                isCurrent = false,
                                isLast = false,
                                activeColor = ColorTokens.Tertiary
                            )

                            // Step 2: Dispatched
                            TimelineItem(
                                title = "Dispatched Koramangala Hub",
                                time = "12:15 PM",
                                isDone = true,
                                isCurrent = false,
                                isLast = false,
                                activeColor = ColorTokens.Tertiary
                            )

                            // Step 3: On Route
                            TimelineItem(
                                title = "On Delivery Route",
                                time = if (isDelivered) "Delivered" else "En Route",
                                isDone = isDelivered,
                                isCurrent = !isDelivered,
                                isLast = false,
                                activeColor = if (isDelivered) ColorTokens.Tertiary else ColorTokens.Primary
                            )

                            // Step 4: Drop Off
                            TimelineItem(
                                title = "Drop-off at ${profile.flatDetails.take(20).ifBlank { "Doorstep 402" }}",
                                time = if (isDelivered) "Completed ✓" else "Est. ${currentStop.etaDisplay}",
                                isDone = isDelivered,
                                isCurrent = isDelivered,
                                isLast = true,
                                activeColor = if (isDelivered) ColorTokens.Tertiary else ColorTokens.Secondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Driver Details & Communication Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.PrimaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Driver Avatar",
                                    tint = ColorTokens.OnPrimaryContainer,
                                    modifier = Modifier.size(32.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.Tertiary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = ColorTokens.OnTertiary,
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = driverName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(ColorTokens.SurfaceContainerLow)
                                            .padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = ColorTokens.Primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "${riderProfile.rating}",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnSurface
                                        )
                                    }
                                }
                                Text(
                                    text = "${riderProfile.totalSafeDrops}+ safe tiffin drops • Verified Partner",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ColorTokens.Secondary
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Moped,
                                        contentDescription = null,
                                        tint = ColorTokens.Secondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "${riderProfile.vehicleModel} (${riderProfile.vehiclePlate})",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 11.sp,
                                        color = ColorTokens.Secondary
                                    )
                                }
                            }
                        }

                        // Thermal Bag Badge
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Heated Insulated Pod ${riderProfile.carrierPodId}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ColorTokens.OnSurfaceVariant
                                )
                            }
                            Text(
                                text = "${riderProfile.carrierPodTempC}°C Insulated",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.Tertiary
                            )
                        }

                        // Driver Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    scope.launch {
                                        val callMsg = if (driverPhone != null) "Calling $driverName: $driverPhone" else "Calling $driverName..."
                                        snackbarHostState.showSnackbar(callMsg)
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = ColorTokens.SurfaceContainer,
                                    contentColor = ColorTokens.OnSurface
                                ),
                                border = null
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Call Driver", style = MaterialTheme.typography.labelMedium)
                            }

                            Button(
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Opening WhatsApp chat with $driverName")
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ColorTokens.Tertiary,
                                    contentColor = ColorTokens.OnTertiary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("WhatsApp", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Secure Drop-off Credentials & House Instructions
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
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
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Pin,
                                    contentDescription = null,
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Drop-Off Passcode",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                            }
                            Text(
                                text = "Share if requested",
                                style = MaterialTheme.typography.labelSmall,
                                color = ColorTokens.Secondary
                            )
                        }

                        // OTP Box
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.PrimaryFixed.copy(alpha = 0.4f))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "CONTACTLESS OTP",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnPrimaryFixedVariant
                                )
                                Text(
                                    text = "Verify with driver or gate guard",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ColorTokens.OnSurfaceVariant
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLowest)
                                    .clickable {
                                        copiedOtp = true
                                        scope.launch {
                                            snackbarHostState.showSnackbar("OTP ${profile.deliveryOtp} copied to clipboard!")
                                            delay(2000)
                                            copiedOtp = false
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = profile.deliveryOtp,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 4.sp,
                                    color = ColorTokens.Primary
                                )
                                Icon(
                                    imageVector = if (copiedOtp) Icons.Default.Check else Icons.Default.ContentCopy,
                                    contentDescription = "Copy OTP",
                                    tint = if (copiedOtp) ColorTokens.Tertiary else ColorTokens.Secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Drop Note
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ReceiptLong,
                                    contentDescription = null,
                                    tint = ColorTokens.Secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Subscriber Drop Note",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ColorTokens.OnSurface
                                )
                            }
                            Text(
                                text = "\"${profile.driverDropNote}\"",
                                style = MaterialTheme.typography.bodySmall,
                                color = ColorTokens.OnSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Delivery Proof Preview Section
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = ColorTokens.Tertiary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Photo Proof of Delivery",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                            }
                            Text(
                                text = "Active Mode",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.Tertiary
                            )
                        }

                        Text(
                            text = "Driver captures an instant geo-tagged timestamp photo at door 402 upon placement to guarantee safe contactless drop-off.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ColorTokens.Secondary
                        )

                        if (isDelivered || currentStop.photoProofUrl != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ColorTokens.TertiaryFixed.copy(alpha = 0.2f))
                                    .border(1.dp, ColorTokens.Tertiary, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(ColorTokens.TertiaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = ColorTokens.Tertiary,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                    Text(
                                        text = "Doorstep Drop Verified ✓",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "Geo-tagged photo captured at door ${profile.flatDetails.take(20).ifBlank { "402" }}. Box custody logged.",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        color = ColorTokens.Secondary,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ColorTokens.SurfaceContainerLowest.copy(alpha = 0.9f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Lat: 19.2458° N, Long: 72.9734° E",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        color = ColorTokens.Tertiary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ColorTokens.SurfaceContainer)
                                    .border(1.dp, ColorTokens.OutlineVariant.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(ColorTokens.SurfaceContainerLowest),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PhotoCamera,
                                            contentDescription = null,
                                            tint = ColorTokens.Primary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Text(
                                        text = "Waiting for Drop-off Snap",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "Photo proof with GPS coordinates will appear automatically once dropped.",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        color = ColorTokens.Secondary,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ColorTokens.SurfaceContainerLowest.copy(alpha = 0.85f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Geo: 12.9352° N, 77.6245° E",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        color = ColorTokens.Secondary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Post Delivery Action Shortcuts
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateToFeedback,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ColorTokens.Primary
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ColorTokens.Primary)
                    ) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Rate Today's Meal", style = MaterialTheme.typography.labelMedium)
                    }

                    Button(
                        onClick = onNavigateToReportIssue,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorTokens.ErrorContainer,
                            contentColor = ColorTokens.OnErrorContainer
                        )
                    ) {
                        Icon(imageVector = Icons.Default.ReportProblem, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Report Issue", style = MaterialTheme.typography.labelMedium)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Support Assurance Footer
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.HelpCenter,
                            contentDescription = null,
                            tint = ColorTokens.Secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Tiffin delayed, spill detected, or query?",
                            style = MaterialTheme.typography.bodySmall,
                            color = ColorTokens.Secondary
                        )
                    }

                    TextButton(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Connecting directly to Kitchen Manager...")
                            }
                        }
                    ) {
                        Text(
                            text = "Contact Kitchen Manager Directly",
                            style = MaterialTheme.typography.labelMedium,
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

                    Text(
                        text = "Daily Mess Assurance • 100% On-Time Guarantee",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        color = ColorTokens.Secondary
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineItem(
    title: String,
    time: String,
    isDone: Boolean,
    isCurrent: Boolean,
    isLast: Boolean,
    activeColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isDone -> activeColor
                            isCurrent -> ColorTokens.Primary
                            else -> ColorTokens.SurfaceContainerHigh
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        isDone -> Icons.Default.Check
                        isCurrent -> Icons.Default.LocalShipping
                        else -> Icons.Default.DoorFront
                    },
                    contentDescription = null,
                    tint = if (isDone || isCurrent) Color.White else ColorTokens.Secondary,
                    modifier = Modifier.size(13.dp)
                )
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(28.dp)
                        .background(
                            when {
                                isDone -> activeColor
                                isCurrent -> ColorTokens.SurfaceContainerHighest
                                else -> ColorTokens.SurfaceContainerHigh
                            }
                        )
                )
            }
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isCurrent) ColorTokens.Primary else ColorTokens.OnSurface
                )
                if (isCurrent) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ColorTokens.PrimaryFixed)
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "Current",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            color = ColorTokens.OnPrimaryFixed
                        )
                    }
                }
            }

            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                color = if (isCurrent) ColorTokens.Primary else ColorTokens.Secondary
            )
        }
    }
}
