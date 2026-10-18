package com.creativeblisslabs.tiffinmanagement.ui.screens.rider

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricMoped
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NoMeetingRoom
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import kotlinx.coroutines.launch

@Composable
fun RiderDoorstepDropProofScreen(
    stopId: String,
    repository: TiffinRepository,
    onBack: () -> Unit,
    onConfirmed: () -> Unit
) {
    val stops by repository.riderStops.collectAsState()
    val stop = stops.firstOrNull { it.id == stopId } ?: stops.firstOrNull { it.sequenceNumber == 19 } ?: stops.first()

    var enteredOtpDigits by remember { mutableStateOf(listOf("", "", "")) }
    var activeOtpIndex by remember { mutableStateOf(0) }
    var isGateDropVerified by remember { mutableStateOf(false) }
    var carrierStatusOption by remember { mutableStateOf("found") } // "found" or "no_box"
    var isPhotoCaptured by remember { mutableStateOf(true) }
    var isBoxQrScanned by remember { mutableStateOf(true) }
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()
    val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }

    androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorTokens.Surface)
        ) {
        val branch by repository.currentBranch.collectAsState()
        val riderProfile by repository.riderProfile.collectAsState()
        com.example.myapplicationkmp.ui.components.TiffinTopHeader(
            branchName = branch.name,
            screenTitle = "Drop Proof",
            userName = riderProfile.fullName.ifBlank { "Rider" },
            userRole = "Rider",
            subtitle = "Active Navigation • Stop #${stop.sequenceNumber}",
            onNotificationClick = {},
            onProfileClick = onBack
        )

        // Subheader Stop Progress
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorTokens.SurfaceContainerLow)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                            .clip(RoundedCornerShape(22.dp))
                            .background(ColorTokens.PrimaryContainer)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Stop #${stop.sequenceNumber} of 28",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnPrimaryContainer
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricMoped,
                            contentDescription = null,
                            tint = ColorTokens.Tertiary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "On Schedule",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ColorTokens.Tertiary
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ColorTokens.SurfaceContainerHighest)
                            .clickable {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Calling ${stop.customerName} (${stop.customerPhone.ifBlank { "+91 98765 43210" }})...")
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Call", tint = ColorTokens.OnSurface, modifier = Modifier.size(16.dp))
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ColorTokens.TertiaryContainer)
                            .clickable {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Opening WhatsApp chat with ${stop.customerName}...")
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = ColorTokens.OnTertiaryContainer, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // Customer Name & Sub Code
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stop.customerName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(ColorTokens.SecondaryContainer)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = stop.subscriberIdCode,
                                fontSize = 10.sp,
                                color = ColorTokens.OnSecondaryContainer
                            )
                        }
                    }
                    Text(
                        text = "${stop.mealSlot} Delivery Run",
                        fontSize = 11.sp,
                        color = ColorTokens.Secondary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ColorTokens.PrimaryFixed),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stop.customerName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnPrimaryFixed
                    )
                }
            }
        }

        // Scrollable Verification Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Customer & Drop Details Bento Card
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
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.SurfaceContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(20.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("DROP ADDRESS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Secondary)
                                Text(stop.floorNote.ifBlank { "Doorstep Access" }, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.Primary)
                            }
                            Text(stop.wingFlat, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            Text(stop.addressLine, fontSize = 12.sp, color = ColorTokens.Secondary)
                        }
                    }

                    // Items to unload
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
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Items to Unload (${stop.items.size} Boxes)", fontSize = 11.sp, color = ColorTokens.Secondary)
                            Text(stop.podBinSlot.ifBlank { "Carrier Bin A-1" }, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        }

                        stop.items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLowest)
                                    .padding(8.dp),
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
                                    Text(item, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(22.dp))
                                        .background(ColorTokens.SurfaceContainerHigh)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("1x Medium", fontSize = 10.sp, color = ColorTokens.OnSurface)
                                }
                            }
                        }
                    }

                    // Instruction Note Callout
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ColorTokens.PrimaryFixed.copy(alpha = 0.3f))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Default.StickyNote2, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(18.dp))
                        Column {
                            Text("Customer Gate & Drop Note", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                            Text("\"${stop.specialNote.ifEmpty { "Ring bell twice, leave on handle of door 402 if not opened in 2 mins." }}\"", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                        }
                    }
                }
            }

            // Step 1: OTP Confirmation
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.Primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("1", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimary)
                            }
                            Text("Contactless Customer OTP", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(ColorTokens.TertiaryFixed)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Quick Verify", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnTertiaryFixed)
                        }
                    }
                    Text("Ask Aarav or door attendant for the 3-digit verification code sent via SMS.", fontSize = 12.sp, color = ColorTokens.Secondary)

                    // Digit Boxes UI
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            enteredOtpDigits.forEachIndexed { index, digit ->
                                val isActive = index == activeOtpIndex
                                Box(
                                    modifier = Modifier
                                        .size(width = 56.dp, height = 64.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isActive) ColorTokens.PrimaryFixed.copy(alpha = 0.4f) else ColorTokens.SurfaceContainerLow)
                                        .border(
                                            width = if (isActive) 1.5.dp else 1.dp,
                                            color = if (isActive) ColorTokens.Primary else androidx.compose.ui.graphics.Color.Transparent,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { activeOtpIndex = index },
                                    contentAlignment = Alignment.Center
                                ) {
                                    androidx.compose.foundation.text.BasicTextField(
                                        value = digit,
                                        onValueChange = { input ->
                                            val filtered = input.filter { it.isDigit() }
                                            val newDigits = enteredOtpDigits.toMutableList()
                                            if (filtered.isNotEmpty()) {
                                                newDigits[index] = filtered.last().toString()
                                                enteredOtpDigits = newDigits
                                                if (index < 2) activeOtpIndex = index + 1
                                            } else {
                                                newDigits[index] = ""
                                                enteredOtpDigits = newDigits
                                                if (index > 0) activeOtpIndex = index - 1
                                            }
                                        },
                                        textStyle = androidx.compose.ui.text.TextStyle(
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.Primary,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        ),
                                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
                                            imeAction = if (index == 2) androidx.compose.ui.text.input.ImeAction.Done else androidx.compose.ui.text.input.ImeAction.Next
                                        ),
                                        singleLine = true,
                                        decorationBox = { innerTextField ->
                                            Box(contentAlignment = Alignment.Center) {
                                                if (digit.isEmpty() && !isActive) {
                                                    Text("•", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Outline)
                                                }
                                                innerTextField()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.clickable {
                                isGateDropVerified = true
                            }
                        ) {
                            Icon(
                                Icons.Default.NoMeetingRoom,
                                contentDescription = null,
                                tint = if (isGateDropVerified) ColorTokens.Tertiary else ColorTokens.Primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                if (isGateDropVerified) "Dropped at Gate ✓" else "Drop at Gate / Handle",
                                fontSize = 11.sp,
                                color = if (isGateDropVerified) ColorTokens.Tertiary else ColorTokens.Primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.clickable {
                                enteredOtpDigits = listOf("", "", "")
                                activeOtpIndex = 0
                            }
                        ) {
                            Icon(Icons.Default.Sync, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(14.dp))
                            Text("Clear / Resend", fontSize = 11.sp, color = ColorTokens.Secondary)
                        }
                    }
                }
            }

            // Step 2: Geo-Tagged Proof Photo
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.Primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("2", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimary)
                            }
                            Text("Geo-Tagged Delivery Photo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(ColorTokens.TertiaryContainer)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Captured", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnTertiaryContainer)
                        }
                    }
                    Text("Clear snap showing the packed tiffins securely placed at apartment entrance 402.", fontSize = 12.sp, color = ColorTokens.Secondary)

                    // Simulated Camera Frame with Telemetry Watermark
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ColorTokens.InverseSurface)
                    ) {
                     
                      
                            // Watermark Info
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ColorTokens.InverseSurface.copy(alpha = 0.85f))
                                    .padding(8.dp)
                            ) {
                              
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Lat: 19.2458° N, Long: 72.9734° E", fontSize = 10.sp, color = ColorTokens.SurfaceDim)
                                    Text("04 Sep 2026 • 12:48 PM", fontSize = 10.sp, color = ColorTokens.SurfaceDim)
                                }
                            }
                        
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { /* Retake camera */ },
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainer)
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = ColorTokens.OnSurface, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Retake Photo", fontSize = 11.sp, color = ColorTokens.OnSurface)
                        }
                        Button(
                            onClick = { /* Preview photo */ },
                            modifier = Modifier.height(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainer)
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Preview", fontSize = 11.sp, color = ColorTokens.Secondary)
                        }
                    }
                }
            }

            // Step 3: Empty Tiffin Carrier Pickup
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.Primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("3", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimary)
                            }
                            Text("Empty Steel Box Collection", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(ColorTokens.SurfaceContainerHigh)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Daily Eco-Cycle", fontSize = 10.sp, color = ColorTokens.Secondary)
                        }
                    }

                    // Radio 1: Box Found
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (carrierStatusOption == "found") ColorTokens.TertiaryFixed.copy(alpha = 0.3f) else ColorTokens.SurfaceContainerLow)
                            .clickable { carrierStatusOption = "found" }
                            .padding(10.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(
                            selected = carrierStatusOption == "found",
                            onClick = { carrierStatusOption = "found" },
                            colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Tertiary)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("1 Steel Carrier Received from Customer", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ColorTokens.Tertiary, modifier = Modifier.size(18.dp))
                            }
                            Text("Placed outside door in green return bag. Ready for scanning.", fontSize = 11.sp, color = ColorTokens.Secondary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        repository.scanBoxReturn("TIF-3091", stop.id)
                                        isBoxQrScanned = true
                                    },
                                    modifier = Modifier.height(32.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainerLowest)
                                ) {
                                    Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Scan Lid QR (#TIF-3091)", fontSize = 10.sp, color = ColorTokens.OnSurface)
                                }
                                if (isBoxQrScanned) {
                                    Text("Scanned ✓", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Tertiary)
                                }
                            }
                        }
                    }

                    // Radio 2: No Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (carrierStatusOption == "no_box") ColorTokens.PrimaryFixed.copy(alpha = 0.3f) else ColorTokens.SurfaceContainerLow)
                            .clickable { carrierStatusOption = "no_box" }
                            .padding(10.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(
                            selected = carrierStatusOption == "no_box",
                            onClick = { carrierStatusOption = "no_box" },
                            colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Primary)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("No Box Outside", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ColorTokens.SurfaceContainerHigh)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("Skip", fontSize = 10.sp, color = ColorTokens.Secondary)
                                }
                            }
                            Text("Sends an automated WhatsApp gentle reminder for tomorrow's pickup slot.", fontSize = 11.sp, color = ColorTokens.Secondary)
                        }
                    }
                }
            }

            // Summary Audit Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ColorTokens.SurfaceContainer)
                    .padding(12.dp),
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
                            .clip(CircleShape)
                            .background(ColorTokens.TertiaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.TaskAlt, contentDescription = null, tint = ColorTokens.OnTertiaryContainer, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Text("Delivery Audit Complete", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        Text("Photo, Geo & Bag Logged", fontSize = 10.sp, color = ColorTokens.Secondary)
                    }
                }
                Text("Ready to Disembark", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
            }

            // Confirm Drop-off Button
            val isOtpComplete = enteredOtpDigits.all { it.isNotBlank() }
            val canConfirm = isOtpComplete || isGateDropVerified

            Button(
                onClick = {
                    repository.completeStopDrop(stop.id)
                    onConfirmed()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = canConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorTokens.Primary,
                    disabledContainerColor = ColorTokens.SurfaceContainerHigh
                )
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
                        Icon(Icons.Default.Verified, contentDescription = null, tint = ColorTokens.OnPrimary)
                        Text("Confirm Drop-off", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimary)
                    }
                    Text("✓ Stop #${stop.sequenceNumber}", fontSize = 13.sp, color = ColorTokens.OnPrimary)
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