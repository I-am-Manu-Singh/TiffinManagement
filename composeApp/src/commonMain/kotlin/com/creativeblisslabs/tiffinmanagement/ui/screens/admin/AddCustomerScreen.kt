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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.models.CustomerModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun AddCustomerScreen(
    onNavigateBack: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var selectedBranch by remember { mutableStateOf("") }
    var branchDropdownOpen by remember { mutableStateOf(false) }

    var selectedRoute by remember { mutableStateOf("") }
    var routeDropdownOpen by remember { mutableStateOf(false) }

    var deliveryAddress by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var trustLimit by remember { mutableStateOf("0") }
    var statusActive by remember { mutableStateOf(true) }
    var isVeg by remember { mutableStateOf(true) }
    var saveError by remember { mutableStateOf<String?>(null) }

    val liveBranches by TiffinRepository.branches.collectAsState()
    val liveRoutes by TiffinRepository.deliveryRoutes.collectAsState()
    val branchOptions = remember(liveBranches) { liveBranches.map { it.name } }
    val routeOptions = remember(liveRoutes) { liveRoutes.map { it.name } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
       

        // 2. Main Form Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Section Title Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CUSTOMER PROFILE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.TextSecondary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "* Required fields",
                        fontSize = 11.sp,
                        color = Color(0xFFF43F5E),
                        fontWeight = FontWeight.Medium
                    )
                }

                // Name
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row {
                        Text("Name", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text(" *", fontSize = 13.sp, color = Color(0xFFF43F5E))
                    }
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = { Text("Full Name", fontSize = 13.sp, color = ColorTokens.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = ColorTokens.BorderSubtle
                        ),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                // Phone Number with Country Flag prefix
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row {
                        Text("Phone Number", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text(" *", fontSize = 13.sp, color = Color(0xFFF43F5E))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 10.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Call, null, tint = ColorTokens.Primary, modifier = Modifier.size(16.dp))
                            Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.TextSecondary, modifier = Modifier.size(16.dp))
                        }
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(52.dp)
                                .background(ColorTokens.BorderSubtle)
                        )
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            placeholder = { Text("081234 56789", fontSize = 13.sp, color = ColorTokens.TextSecondary) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true
                        )
                    }
                }

                // Email Address
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Email Address", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    OutlinedTextField(
                        value = emailAddress,
                        onValueChange = { emailAddress = it },
                        placeholder = { Text("Email Address", fontSize = 13.sp, color = ColorTokens.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = ColorTokens.BorderSubtle
                        ),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )
                }

                // Password
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row {
                        Text("Password", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text(" *", fontSize = 13.sp, color = Color(0xFFF43F5E))
                    }
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Portal Login Password", fontSize = 13.sp, color = ColorTokens.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = ColorTokens.BorderSubtle
                        ),
                        shape = RoundedCornerShape(10.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    tint = ColorTokens.TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        singleLine = true
                    )
                }

                // Confirm Password
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row {
                        Text("Confirm Password", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text(" *", fontSize = 13.sp, color = Color(0xFFF43F5E))
                    }
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = { Text("Confirm Password", fontSize = 13.sp, color = ColorTokens.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = ColorTokens.BorderSubtle
                        ),
                        shape = RoundedCornerShape(10.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true
                    )
                }

                // Branch
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Branch", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp))
                                .clickable { branchDropdownOpen = true }
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(selectedBranch, fontSize = 13.sp, color = ColorTokens.OnSurface)
                            Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.TextSecondary, modifier = Modifier.size(18.dp))
                        }
                        DropdownMenu(
                            expanded = branchDropdownOpen,
                            onDismissRequest = { branchDropdownOpen = false }
                        ) {
                            branchOptions.forEach { b ->
                                DropdownMenuItem(
                                    text = { Text(b, fontSize = 13.sp) },
                                    onClick = {
                                        selectedBranch = b
                                        branchDropdownOpen = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Delivery Route
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Delivery Route", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp))
                                .clickable { routeDropdownOpen = true }
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(selectedRoute, fontSize = 13.sp, color = ColorTokens.OnSurface)
                            Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.TextSecondary, modifier = Modifier.size(18.dp))
                        }
                        DropdownMenu(
                            expanded = routeDropdownOpen,
                            onDismissRequest = { routeDropdownOpen = false }
                        ) {
                            routeOptions.forEach { r ->
                                DropdownMenuItem(
                                    text = { Text(r, fontSize = 13.sp) },
                                    onClick = {
                                        selectedRoute = r
                                        routeDropdownOpen = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Delivery Address
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Delivery Address", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    OutlinedTextField(
                        value = deliveryAddress,
                        onValueChange = { deliveryAddress = it },
                        placeholder = { Text("House No, Building, Street, Landmark...", fontSize = 13.sp, color = ColorTokens.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = ColorTokens.BorderSubtle
                        ),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 3
                    )
                }

                // Latitude & Longitude Coordinates
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Latitude", fontSize = 12.sp, color = ColorTokens.TextSecondary)
                        OutlinedTextField(
                            value = latitude,
                            onValueChange = { latitude = it },
                            placeholder = { Text("e.g. 18.5204", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ColorTokens.Primary,
                                unfocusedBorderColor = ColorTokens.BorderSubtle
                            ),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Longitude", fontSize = 12.sp, color = ColorTokens.TextSecondary)
                        OutlinedTextField(
                            value = longitude,
                            onValueChange = { longitude = it },
                            placeholder = { Text("e.g. 73.8567", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ColorTokens.Primary,
                                unfocusedBorderColor = ColorTokens.BorderSubtle
                            ),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                }

                // Choose Location on Map Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, ColorTokens.PrimarySpice, RoundedCornerShape(10.dp))
                        .clickable { }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Map, null, tint = ColorTokens.PrimarySpice, modifier = Modifier.size(16.dp))
                        Text(
                            text = "Choose Location on Map",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ColorTokens.PrimarySpice
                        )
                    }
                }

                // Max Allowed Due (Trust Limit)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row {
                        Text("Max Allowed Due (Trust Limit)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text(" *", fontSize = 13.sp, color = Color(0xFFF43F5E))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 12.dp, vertical = 14.dp)
                        ) {
                            Text("₹", fontSize = 13.sp, color = ColorTokens.TextSecondary, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(48.dp)
                                .background(ColorTokens.BorderSubtle)
                        )
                        OutlinedTextField(
                            value = trustLimit,
                            onValueChange = { trustLimit = it },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                    Text(
                        text = "Allowed overdue/debt limit for postpaid transactions. Enter 0 for prepaid only.",
                        fontSize = 11.sp,
                        color = ColorTokens.TextSecondary
                    )
                }

                // Status (Active / Inactive Radio Group)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Status", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { statusActive = true }
                        ) {
                            RadioButton(
                                selected = statusActive,
                                onClick = { statusActive = true },
                                colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.TertiaryMint)
                            )
                            Text("Active", fontSize = 13.sp, color = ColorTokens.OnSurface)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { statusActive = false }
                        ) {
                            RadioButton(
                                selected = !statusActive,
                                onClick = { statusActive = false },
                                colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Secondary)
                            )
                            Text("Inactive", fontSize = 13.sp, color = ColorTokens.OnSurfaceVariant)
                        }
                    }
                }

                // Diet Preference Radio Group
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Diet Preference", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { isVeg = true }
                        ) {
                            RadioButton(
                                selected = isVeg,
                                onClick = { isVeg = true },
                                colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.TertiaryMint)
                            )
                            Text("Veg", fontSize = 13.sp, color = ColorTokens.OnSurface)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { isVeg = false }
                        ) {
                            RadioButton(
                                selected = !isVeg,
                                onClick = { isVeg = false },
                                colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.PrimarySpice)
                            )
                            Text("Non-Veg", fontSize = 13.sp, color = ColorTokens.OnSurfaceVariant)
                        }
                    }
                }

                // Validation Error Display
                if (saveError != null) {
                    Text(
                        text = saveError ?: "",
                        color = Color(0xFFDC2626),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // 3. Save Customer CTA Button
        Button(
            onClick = {
                if (fullName.isBlank()) { saveError = "Name is required"; return@Button }
                val rawPhone = phoneNumber.trim().removePrefix("+91").replace(" ", "")
                if (rawPhone.length < 10) { saveError = "Phone must be at least 10 digits"; return@Button }
                val latVal = latitude.trim().toDoubleOrNull()
                val lngVal = longitude.trim().toDoubleOrNull()
                if (latitude.isNotBlank() && latVal == null) { saveError = "Latitude must be a valid number"; return@Button }
                if (longitude.isNotBlank() && lngVal == null) { saveError = "Longitude must be a valid number"; return@Button }
                saveError = null
                TiffinRepository.addCustomer(
                    CustomerModel(
                        id = "c-${System.currentTimeMillis()}",
                        name = fullName.trim(),
                        phone = phoneNumber.trim(),
                        email = if (emailAddress.isNotBlank()) emailAddress.trim().lowercase() else null,
                        address = if (deliveryAddress.isNotBlank()) deliveryAddress.trim() else null,
                        branchName = selectedBranch.ifBlank { liveBranches.firstOrNull()?.name ?: "" },
                        deliveryRoute = selectedRoute.ifBlank { null },
                        latitude = latitude.trim().ifBlank { null },
                        longitude = longitude.trim().ifBlank { null },
                        isActive = statusActive,
                        isVeg = isVeg,
                        walletBalance = 0.0,
                        dueLimit = trustLimit.toDoubleOrNull() ?: 0.0
                    )
                )
                onNavigateBack()
            },
            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimarySpice),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(
                text = "Save Customer",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
