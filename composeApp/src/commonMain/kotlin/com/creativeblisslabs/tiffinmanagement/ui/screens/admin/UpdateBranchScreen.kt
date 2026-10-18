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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.models.BranchModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinTopHeader

@Composable
fun UpdateBranchScreen(
    onNavigateBack: () -> Unit = {}
) {
    val currentBranch by TiffinRepository.currentBranch.collectAsState()

    var branchName by remember(currentBranch) { mutableStateOf(currentBranch.name) }
    var branchAddress by remember(currentBranch) { mutableStateOf(currentBranch.address ?: "") }
    var latitude by remember(currentBranch) { mutableStateOf(currentBranch.latitude ?: "18.5204") }
    var longitude by remember(currentBranch) { mutableStateOf(currentBranch.longitude ?: "73.8567") }
    var isBranchActive by remember(currentBranch) { mutableStateOf(currentBranch.isActive) }

    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        // Main Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Operational Branch Badge Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ColorTokens.PrimaryFixed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Store, null, tint = ColorTokens.Primary, modifier = Modifier.size(20.dp))
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = currentBranch.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFECFDF5))
                                        .border(1.dp, Color(0xFFA7F3D0), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text("Operational", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                                }
                            }
                            Text(
                                text = "Coverage: ${currentBranch.coverageRadiusKm} km radius • Daily Lunches",
                                fontSize = 11.sp,
                                color = ColorTokens.TextSecondary
                            )
                        }
                    }

                    Text(
                        text = "Switch",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ColorTokens.Primary
                    )
                }
            }

            // Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Branch Name
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Branch Name *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        OutlinedTextField(
                            value = branchName,
                            onValueChange = { branchName = it },
                            trailingIcon = {
                                Icon(Icons.Default.Check, null, tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ColorTokens.SurfaceContainerLow,
                                unfocusedContainerColor = ColorTokens.SurfaceContainerLow,
                                focusedBorderColor = ColorTokens.Primary,
                                unfocusedBorderColor = ColorTokens.BorderSubtle
                            )
                        )
                    }

                    // Address Details
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Address Details", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        OutlinedTextField(
                            value = branchAddress,
                            onValueChange = { branchAddress = it },
                            placeholder = { Text("Branch location address...", fontSize = 12.sp, color = ColorTokens.TextSecondary) },
                            minLines = 3,
                            maxLines = 3,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ColorTokens.SurfaceContainerLow,
                                unfocusedContainerColor = ColorTokens.SurfaceContainerLow,
                                focusedBorderColor = ColorTokens.Primary,
                                unfocusedBorderColor = ColorTokens.BorderSubtle
                            )
                        )
                    }

                    // Latitude & Longitude
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Latitude", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            OutlinedTextField(
                                value = latitude,
                                onValueChange = { latitude = it },
                                placeholder = { Text("e.g. 18.5204", fontSize = 12.sp, color = ColorTokens.TextSecondary) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = ColorTokens.SurfaceContainerLow,
                                    unfocusedContainerColor = ColorTokens.SurfaceContainerLow,
                                    focusedBorderColor = ColorTokens.Primary,
                                    unfocusedBorderColor = ColorTokens.BorderSubtle
                                )
                            )
                        }

                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Longitude", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            OutlinedTextField(
                                value = longitude,
                                onValueChange = { longitude = it },
                                placeholder = { Text("e.g. 73.8567", fontSize = 12.sp, color = ColorTokens.TextSecondary) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = ColorTokens.SurfaceContainerLow,
                                    unfocusedContainerColor = ColorTokens.SurfaceContainerLow,
                                    focusedBorderColor = ColorTokens.Primary,
                                    unfocusedBorderColor = ColorTokens.BorderSubtle
                                )
                            )
                        }
                    }

                    // Choose Location on Map Button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ColorTokens.Primary)
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Map, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Text("Choose Location on Map", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                    }

                    // Status Radio Selection
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Status", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { isBranchActive = true }
                            ) {
                                RadioButton(
                                    selected = isBranchActive,
                                    onClick = { isBranchActive = true },
                                    colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Primary)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF10B981)))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Active", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { isBranchActive = false }
                            ) {
                                RadioButton(
                                    selected = !isBranchActive,
                                    onClick = { isBranchActive = false },
                                    colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Primary)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Inactive", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = ColorTokens.Secondary)
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 12.sp)
                    }

                    if (successMessage != null) {
                        Text(successMessage ?: "", color = Color(0xFF16A34A), fontSize = 12.sp)
                    }

                    // Save Branch CTA Button
                    Button(
                        onClick = {
                            errorMessage = null
                            successMessage = null
                            if (branchName.isBlank()) {
                                errorMessage = "Branch Name cannot be empty"
                                return@Button
                            }
                            val latVal = latitude.trim().toDoubleOrNull()
                            val lngVal = longitude.trim().toDoubleOrNull()
                            if (latitude.isNotBlank() && latVal == null) {
                                errorMessage = "Latitude must be a valid number (e.g. 18.5204)"
                                return@Button
                            }
                            if (longitude.isNotBlank() && lngVal == null) {
                                errorMessage = "Longitude must be a valid number (e.g. 73.8567)"
                                return@Button
                            }
                            val updated = currentBranch.copy(
                                name = branchName.trim(),
                                address = branchAddress.trim().ifBlank { null },
                                latitude = latitude.trim().ifBlank { null },
                                longitude = longitude.trim().ifBlank { null },
                                isActive = isBranchActive
                            )
                            TiffinRepository.updateBranch(updated)
                            successMessage = "Branch saved successfully"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Save Branch", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            // Mini Hint
            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "Need to configure delivery zones? Head to Delivery Matrix.",
                    fontSize = 11.sp,
                    color = ColorTokens.TextSecondary
                )
            }
        }
    }
}
