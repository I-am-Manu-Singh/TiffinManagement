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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CarCrash
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneDisabled
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.SwapHorizontalCircle
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Whatshot
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
import androidx.compose.runtime.mutableStateListOf
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
fun RiderReportIncidentScreen(
    stopId: String,
    repository: TiffinRepository,
    onBack: () -> Unit,
    onSubmitSuccess: () -> Unit
) {
    val stops by repository.riderStops.collectAsState()
    val stop = stops.firstOrNull { it.id == stopId } ?: stops.firstOrNull { it.sequenceNumber == 19 } ?: stops.first()

    var selectedIssueType by remember { mutableStateOf("leak") }
    var photoAttached by remember { mutableStateOf(false) }
    val selectedTags = remember { mutableStateListOf<String>() }
    var supervisorNotes by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        // Top App Bar
        val branch by repository.currentBranch.collectAsState()
        val riderProfile by repository.riderProfile.collectAsState()
        com.example.myapplicationkmp.ui.components.TiffinTopHeader(
            branchName = branch.name,
            screenTitle = "Report Incident",
            userName = riderProfile.fullName.ifBlank { "Rider" },
            userRole = "Rider",
            subtitle = "Delivery Stop #${stop.sequenceNumber} • ${stop.customerName}",
            onNotificationClick = {},
            onProfileClick = onBack
        )

        // Subheader Dispatch Assistance
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorTokens.SurfaceContainerLow)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(ColorTokens.Primary))
                Text("INSTANT HUB DISPATCH ASSISTANCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
            }
            Text("Live Route Priority", fontSize = 11.sp, color = ColorTokens.Secondary)
        }

        // Scrollable Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Target Stop Context Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(ColorTokens.Primary.copy(alpha = 0.1f)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                Text("Stop #${stop.sequenceNumber}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                            }
                            Text(stop.subscriberIdCode, fontSize = 11.sp, color = ColorTokens.Secondary)
                        }
                        Row(
                            modifier = Modifier.clip(RoundedCornerShape(22.dp)).background(ColorTokens.SurfaceContainer).padding(horizontal = 8.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Whatshot, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(13.dp))
                            Text("Pod #12 (72°C)", fontSize = 10.sp, color = ColorTokens.OnSurface)
                        }
                    }

                    Text(stop.customerName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Apartment, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(14.dp))
                        Text("${stop.wingFlat}, ${stop.addressLine}", fontSize = 12.sp, color = ColorTokens.Secondary)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLow).padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(15.dp))
                            Text("Fri Lunch Window", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        }
                        Text("Target: 1:15 PM • 12m Left", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                    }
                }
            }

            // Issue Type Selection Matrix (5 items)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Select Issue Type", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                    Text("Required", fontSize = 11.sp, color = ColorTokens.Primary)
                }

                val issueTypes = listOf(
                    Triple("leak", "Tiffin Packaging Leak / Dal Spilled", "Transit damage, replacement meal dispatch needed"),
                    Triple("unreachable", "Customer Unreachable / Gate Blocked", "Security denying entry or customer phone not picking"),
                    Triple("wrong_bin", "Wrong Meal Bin Loaded", "Diet tag mismatch (Veg/Jain vs Non-Veg label)"),
                    Triple("breakdown", "Vehicle Breakdown / Puncture", "Immobilized bike, rescue rider required immediately"),
                    Triple("weather", "Severe Weather / Route Blocked", "Waterlogged avenue, tree fall or heavy water blockage")
                )

                issueTypes.forEach { (key, title, subtitle) ->
                    val isSelected = selectedIssueType == key
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { selectedIssueType = key },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) ColorTokens.Primary.copy(alpha = 0.08f) else ColorTokens.SurfaceContainerLowest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(if (key == "leak") ColorTokens.ErrorContainer else ColorTokens.SurfaceContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                val iconVector = when (key) {
                                    "leak" -> Icons.Default.SoupKitchen
                                    "unreachable" -> Icons.Default.PhoneDisabled
                                    "wrong_bin" -> Icons.Default.SwapHorizontalCircle
                                    "breakdown" -> Icons.Default.CarCrash
                                    else -> Icons.Default.Thunderstorm
                                }
                                Icon(iconVector, contentDescription = null, tint = if (key == "leak") ColorTokens.OnErrorContainer else ColorTokens.OnSurface, modifier = Modifier.size(20.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text(subtitle, fontSize = 11.sp, color = ColorTokens.Secondary)
                            }
                            if (isSelected) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = ColorTokens.Primary, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            // Photo Verification Dropzone & Tags
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Attach Verification Proof", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .clickable { photoAttached = !photoAttached }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!photoAttached) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(ColorTokens.Primary.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(22.dp))
                                }
                                Text("Attach Photo of Leak / Blocked Gate", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                                Text("Tap to launch camera (Instant Hub AI verification)", fontSize = 10.sp, color = ColorTokens.Secondary)
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainer).padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = ColorTokens.Tertiary, modifier = Modifier.size(18.dp))
                                    Text("spill_proof_stop${stop.sequenceNumber}.jpg", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = ColorTokens.OnSurface)
                                }
                                Text("Replace", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                            }
                        }
                    }

                    // Quick Incident Tags
                    Text("Quick Incident Tags", fontSize = 11.sp, color = ColorTokens.Secondary)
                    val sampleTags = listOf("Packaging lid opened in transit", "Customer not answering (3 calls)", "Bell rung twice - no response", "Gate guard refusing tiffin drop")
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        sampleTags.forEach { tag ->
                            val isTagSelected = selectedTags.contains(tag)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(if (isTagSelected) ColorTokens.Primary else ColorTokens.SurfaceContainer)
                                    .clickable {
                                        if (isTagSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(tag, fontSize = 11.sp, color = if (isTagSelected) ColorTokens.OnPrimary else ColorTokens.Secondary)
                            }
                        }
                    }

                    // Note for Supervisor
                    Text("Note for Kitchen Supervisor", fontSize = 11.sp, color = ColorTokens.Secondary)
                    OutlinedTextField(
                        value = supervisorNotes,
                        onValueChange = { supervisorNotes = it },
                        placeholder = { Text("Describe situation for immediate dispatch resolution...", fontSize = 12.sp, color = ColorTokens.Secondary) },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ColorTokens.SurfaceContainerLow,
                            unfocusedContainerColor = ColorTokens.SurfaceContainerLow,
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )
                }
            }

            // Hub Resolution SLAs
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLow),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(18.dp))
                        Text("Hub Resolution SLAs", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLowest).padding(10.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Moped, contentDescription = null, tint = ColorTokens.Tertiary, modifier = Modifier.size(16.dp))
                        Column {
                            Text("Rescue Rider Dispatch", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            Text("Nearest rider Sunil (1.2 km away) will auto-route to take over remaining 9 stops if bike is pinned.", fontSize = 11.sp, color = ColorTokens.Secondary)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(ColorTokens.SurfaceContainerLowest).padding(10.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(16.dp))
                        Column {
                            Text("Express Replacement Thali", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            Text("Hub warm line will fire fresh express meal pack within 12 minutes on verified photo report.", fontSize = 11.sp, color = ColorTokens.Secondary)
                        }
                    }
                }
            }

            // Urgent Action Line Buttons & Submit
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("URGENT ACTION LINE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Secondary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { /* Call Dispatch hotline */ },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainerLowest)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call Dispatch", fontSize = 12.sp, color = ColorTokens.OnSurface)
                    }

                    Button(
                        onClick = { /* SOS WhatsApp */ },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.TertiaryContainer)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = ColorTokens.OnTertiaryContainer, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SOS WhatsApp", fontSize = 12.sp, color = ColorTokens.OnTertiaryContainer)
                    }
                }

                Button(
                    onClick = {
                        isSubmitting = true
                        repository.submitRiderIncident(
                            stopSequence = stop.sequenceNumber,
                            subCode = stop.subscriberIdCode,
                            customerName = stop.customerName,
                            issueType = selectedIssueType,
                            tags = selectedTags.toList(),
                            note = supervisorNotes
                        )
                        onSubmitSuccess()
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = ColorTokens.OnPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Submit Incident Report", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnPrimary)
                }
            }
        }
    }
}
