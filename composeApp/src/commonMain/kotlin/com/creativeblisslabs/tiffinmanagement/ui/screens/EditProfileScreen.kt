package com.creativeblisslabs.tiffinmanagement.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Desk
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Work
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository

/**
 * Unified Single Edit Profile Screen
 * Seamlessly adapts to any user role:
 * - Customer: Personal info, Delivery Flat/Wing, Landmark, City/Pin, GPS Pin, Delivery Instructions & Drop Note
 * - Driver / Rider: Personal info, Vehicle Model, Registration Plate, DL status
 * - Admin / Staff: Personal info, Business Display Title, Central Kitchen Branch, Operating Timezone, Language
 */
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit = {}
) {
    val authState by TiffinRepository.authState.collectAsState()
    val userProfile by TiffinRepository.userProfile.collectAsState()
    val subscriberProfile by TiffinRepository.subscriberProfile.collectAsState()
    val riderProfile by TiffinRepository.riderProfile.collectAsState()

    val currentRole = (authState.currentUser?.role ?: "owner").lowercase()
    val isCustomer = currentRole == "customer" || currentRole == "subscriber"
    val isRider = currentRole == "rider" || currentRole == "driver"
    val isStaffOrOwner = !isCustomer && !isRider

    // Universal fields initialized by role
    val initialName = when {
        isCustomer -> subscriberProfile.fullName.ifBlank { authState.currentUser?.fullName ?: "" }
        isRider -> riderProfile.fullName.ifBlank { authState.currentUser?.fullName ?: "" }
        else -> userProfile.fullName.ifBlank { authState.currentUser?.fullName ?: "" }
    }
    val initialPhone = when {
        isCustomer -> subscriberProfile.phone.replace("+91 ", "").trim()
        isRider -> (riderProfile.phone ?: "").replace("+91 ", "").trim()
        else -> userProfile.phone.replace("+91 ", "").trim()
    }
    val initialEmail = when {
        isCustomer -> subscriberProfile.email.ifBlank { authState.currentUser?.email ?: "" }
        isRider -> authState.currentUser?.email ?: ""
        else -> userProfile.email.ifBlank { authState.currentUser?.email ?: "" }
    }

    var fullName by remember(initialName) { mutableStateOf(initialName) }
    var primaryPhone by remember(initialPhone) { mutableStateOf(initialPhone) }
    var emailAddr by remember(initialEmail) { mutableStateOf(initialEmail) }
    var secPhone by remember(userProfile, subscriberProfile) {
        mutableStateOf(if (isCustomer) subscriberProfile.emergencyContact.replace("+91 ", "").trim() else (userProfile.secondaryPhone ?: ""))
    }

    // Owner / Staff specific state
    var jobTitle by remember(userProfile) { mutableStateOf(userProfile.displayJobTitle) }
    var selectedBranch by remember(userProfile) { mutableStateOf(userProfile.branchName) }
    var isBranchDropdownOpen by remember { mutableStateOf(false) }
    var selectedTz by remember { mutableStateOf("Asia/Kolkata (IST +5:30)") }
    var isTzDropdownOpen by remember { mutableStateOf(false) }
    var selectedLang by remember { mutableStateOf("English (UK / India)") }
    var isLangDropdownOpen by remember { mutableStateOf(false) }

    // Rider specific state
    var vehicleModel by remember(riderProfile) { mutableStateOf(riderProfile.vehicleModel) }
    var vehiclePlate by remember(riderProfile) { mutableStateOf(riderProfile.vehiclePlate) }

    // Customer specific state
    var locationType by remember(subscriberProfile) { mutableStateOf(subscriberProfile.locationType.ifBlank { "apartment" }) }
    var flatDetails by remember(subscriberProfile) { mutableStateOf(subscriberProfile.flatDetails) }
    var landmark by remember(subscriberProfile) { mutableStateOf(subscriberProfile.landmark) }
    var cityPincode by remember(subscriberProfile) { mutableStateOf(subscriberProfile.cityPincode) }
    var gpsCoordinates by remember(subscriberProfile) { mutableStateOf(subscriberProfile.gpsCoordinates.ifBlank { "19.2458° N, 72.9734° E" }) }
    var driverDropNote by remember(subscriberProfile) { mutableStateOf(subscriberProfile.driverDropNote) }

    // Security / Password Change
    var isSecurityOpen by remember { mutableStateOf(false) }
    var currPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confPass by remember { mutableStateOf("") }
    var isCurrPassVisible by remember { mutableStateOf(false) }
    var isNewPassVisible by remember { mutableStateOf(false) }
    var isConfPassVisible by remember { mutableStateOf(false) }

    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FD))
    ) {
        // Universal Top Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF0F172A)
                    )
                }
                Column {
                    Text(
                        text = "Edit Profile",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = when {
                            isCustomer -> "Customer & Delivery Address Settings"
                            isRider -> "Rider & Delivery Vehicle Profile"
                            else -> "Kitchen Admin & Operations Profile"
                        },
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFFFF4EF))
                    .border(1.dp, Color(0xFFFFB59F), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = when {
                        isCustomer -> "Customer"
                        isRider -> "Rider"
                        currentRole == "kitchen_staff" -> "Staff"
                        else -> "Owner"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFAB2F00)
                )
            }
        }

        // Scrollable Form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Profile Photo Uploader Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF4EF))
                                .border(2.dp, Color(0xFFFFB59F), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = fullName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").ifBlank { "U" },
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFAB2F00)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFAB2F00))
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PhotoCamera, null, tint = Color.White, modifier = Modifier.size(15.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Change Photo", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFAB2F00))
                        Text("•", fontSize = 12.sp, color = Color(0xFF94A3B8))
                        Text("Remove", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFFDC2626))
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "JPG or PNG under 5MB. Recommended square 1:1.",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            // 1. Personal Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Badge, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                        Text("Personal Information", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                    }

                    // Full Name
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Full Name", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp)) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC),
                                focusedBorderColor = Color(0xFFAB2F00),
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )
                    }

                    // Primary Phone Number
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Primary Phone Number", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Default.Call, null, modifier = Modifier.size(14.dp), tint = Color(0xFFF95721))
                                    Text("+91", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                }
                            }

                            OutlinedTextField(
                                value = primaryPhone,
                                onValueChange = { primaryPhone = it },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF8FAFC),
                                    unfocusedContainerColor = Color(0xFFF8FAFC),
                                    focusedBorderColor = Color(0xFFAB2F00),
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                )
                            )
                        }
                    }

                    // Email Address
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Email Address", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                        OutlinedTextField(
                            value = emailAddr,
                            onValueChange = { emailAddr = it },
                            leadingIcon = { Icon(Icons.Default.Mail, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC),
                                focusedBorderColor = Color(0xFFAB2F00),
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )
                    }

                    // Secondary / Emergency Phone
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text = if (isCustomer) "Emergency / Roommate Contact" else "Secondary / Emergency Phone",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF475569)
                            )
                            Text("Optional", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }
                        OutlinedTextField(
                            value = secPhone,
                            onValueChange = { secPhone = it },
                            placeholder = { Text("e.g. 98123 45678", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                            leadingIcon = { Icon(Icons.Default.Call, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC),
                                focusedBorderColor = Color(0xFFAB2F00),
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )
                    }
                }
            }

            // 2. Role-Specific Section: Customer Delivery Drop Point
            if (isCustomer) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.LocationOn, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                            Text("Delivery Drop Point", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        }

                        // Location Type Selector
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Location Classification", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(
                                    Triple("apartment", "Apartment", Icons.Default.Apartment),
                                    Triple("hostel", "Hostel / PG", Icons.Default.Hotel),
                                    Triple("office", "Office", Icons.Default.Desk),
                                    Triple("other", "Other", Icons.Default.MoreHoriz)
                                ).forEach { item ->
                                    val key = item.first
                                    val label = item.second
                                    val icon = item.third
                                    val isSelected = locationType == key
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) Color(0xFFAB2F00) else Color(0xFFF1F5F9))
                                            .clickable { locationType = key }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = null,
                                                tint = if (isSelected) Color.White else Color(0xFF475569),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Text(
                                                text = label,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else Color(0xFF475569)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Flat / House details
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Flat / Unit & Building Details", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                            OutlinedTextField(
                                value = flatDetails,
                                onValueChange = { flatDetails = it },
                                placeholder = { Text("e.g. Flat 402, Wing B, Sunshine Residency", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF8FAFC),
                                    unfocusedContainerColor = Color(0xFFF8FAFC),
                                    focusedBorderColor = Color(0xFFAB2F00),
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                )
                            )
                        }

                        // Landmark
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Landmark & Sub-Area", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                            OutlinedTextField(
                                value = landmark,
                                onValueChange = { landmark = it },
                                placeholder = { Text("e.g. Near Hiranandani Tech Gate 2", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF8FAFC),
                                    unfocusedContainerColor = Color(0xFFF8FAFC),
                                    focusedBorderColor = Color(0xFFAB2F00),
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                )
                            )
                        }

                        // City & Pincode
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("City & Postal Pincode", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                            OutlinedTextField(
                                value = cityPincode,
                                onValueChange = { cityPincode = it },
                                placeholder = { Text("e.g. Mumbai - 400076", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF8FAFC),
                                    unfocusedContainerColor = Color(0xFFF8FAFC),
                                    focusedBorderColor = Color(0xFFAB2F00),
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                )
                            )
                        }

                        // GPS Pin Card
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("GPS Drop Location", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF006947), modifier = Modifier.size(13.dp))
                                    Text("High Accuracy (3m)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006947))
                                }
                            }
                            OutlinedTextField(
                                value = gpsCoordinates,
                                onValueChange = { gpsCoordinates = it },
                                leadingIcon = { Icon(Icons.Default.MyLocation, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp)) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF8FAFC),
                                    unfocusedContainerColor = Color(0xFFF8FAFC),
                                    focusedBorderColor = Color(0xFFAB2F00),
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                )
                            )
                        }

                        // Driver Drop Note
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Driver Drop Instructions", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                            OutlinedTextField(
                                value = driverDropNote,
                                onValueChange = { driverDropNote = it },
                                placeholder = { Text("e.g. Leave tiffin on doorstep shoe rack, ring bell once.", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 4,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF8FAFC),
                                    unfocusedContainerColor = Color(0xFFF8FAFC),
                                    focusedBorderColor = Color(0xFFAB2F00),
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                )
                            )
                        }
                    }
                }
            }

            // 2. Role-Specific Section: Rider Vehicle Information
            if (isRider) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.TwoWheeler, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                            Text("Delivery Vehicle & License", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        }

                        // Vehicle Model
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Vehicle Model", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                            OutlinedTextField(
                                value = vehicleModel,
                                onValueChange = { vehicleModel = it },
                                placeholder = { Text("e.g. Hero Splendor Plus / Honda Activa 6G", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                                leadingIcon = { Icon(Icons.Default.DirectionsCar, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp)) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF8FAFC),
                                    unfocusedContainerColor = Color(0xFFF8FAFC),
                                    focusedBorderColor = Color(0xFFAB2F00),
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                )
                            )
                        }

                        // Registration Plate Number
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Registration Number Plate", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                            OutlinedTextField(
                                value = vehiclePlate,
                                onValueChange = { vehiclePlate = it },
                                placeholder = { Text("e.g. MH 04 EZ 8821", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF8FAFC),
                                    unfocusedContainerColor = Color(0xFFF8FAFC),
                                    focusedBorderColor = Color(0xFFAB2F00),
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                )
                            )
                        }
                    }
                }
            }

            // 2. Role-Specific Section: Kitchen Admin / Staff Preferences
            if (isStaffOrOwner) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Storefront, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                            Text("Kitchen & Preferences", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        }

                        // Business Display Title
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Business Display Title", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                            OutlinedTextField(
                                value = jobTitle,
                                onValueChange = { jobTitle = it },
                                leadingIcon = { Icon(Icons.Default.Work, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp)) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFFF8FAFC),
                                    unfocusedContainerColor = Color(0xFFF8FAFC),
                                    focusedBorderColor = Color(0xFFAB2F00),
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                )
                            )
                        }

                        // Default Branch Dropdown
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Default Branch", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                                    .clickable { isBranchDropdownOpen = !isBranchDropdownOpen }
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(Icons.Default.LocationOn, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                                        Text(selectedBranch, fontSize = 13.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.Medium)
                                    }
                                    Icon(Icons.Default.ExpandMore, null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                                }

                                DropdownMenu(expanded = isBranchDropdownOpen, onDismissRequest = { isBranchDropdownOpen = false }) {
                                    listOf("Main Branch", "Sector 18 Cloud Hub", "South Campus Express").forEach { br ->
                                        DropdownMenuItem(text = { Text(br, fontSize = 13.sp) }, onClick = {
                                            selectedBranch = br
                                            isBranchDropdownOpen = false
                                        })
                                    }
                                }
                            }
                        }

                        // Operational Timezone
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Operational Timezone", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                                    .clickable { isTzDropdownOpen = !isTzDropdownOpen }
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(Icons.Default.Schedule, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                                        Text(selectedTz, fontSize = 13.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.Medium)
                                    }
                                    Icon(Icons.Default.ExpandMore, null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                                }

                                DropdownMenu(expanded = isTzDropdownOpen, onDismissRequest = { isTzDropdownOpen = false }) {
                                    listOf("Asia/Kolkata (IST +5:30)", "Asia/Dubai (GST +4:00)", "Europe/London (GMT / BST)").forEach { tz ->
                                        DropdownMenuItem(text = { Text(tz, fontSize = 13.sp) }, onClick = {
                                            selectedTz = tz
                                            isTzDropdownOpen = false
                                        })
                                    }
                                }
                            }
                        }

                        // App Language
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("App Language", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                                    .clickable { isLangDropdownOpen = !isLangDropdownOpen }
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(Icons.Default.Translate, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                                        Text(selectedLang, fontSize = 13.sp, color = Color(0xFF0F172A), fontWeight = FontWeight.Medium)
                                    }
                                    Icon(Icons.Default.ExpandMore, null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                                }

                                DropdownMenu(expanded = isLangDropdownOpen, onDismissRequest = { isLangDropdownOpen = false }) {
                                    listOf("English (UK / India)", "Hindi (हिन्दी)", "Punjabi (ਪੰਜਾਬੀ)").forEach { lang ->
                                        DropdownMenuItem(text = { Text(lang, fontSize = 13.sp) }, onClick = {
                                            selectedLang = lang
                                            isLangDropdownOpen = false
                                        })
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. Security & Password Change Section (Collapsible)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isSecurityOpen = !isSecurityOpen }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(Icons.Default.LockReset, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(22.dp))
                            Column {
                                Text("Security & Password", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Text("Update your account login password", fontSize = 11.sp, color = Color(0xFF64748B))
                            }
                        }
                        Icon(Icons.Default.ExpandMore, null, tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                    }

                    AnimatedVisibility(visible = isSecurityOpen) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Current Password
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Current Password", fontSize = 12.sp, color = Color(0xFF475569))
                                OutlinedTextField(
                                    value = currPass,
                                    onValueChange = { currPass = it },
                                    visualTransformation = if (isCurrPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    trailingIcon = {
                                        IconButton(onClick = { isCurrPassVisible = !isCurrPassVisible }) {
                                            Icon(if (isCurrPassVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color(0xFF94A3B8))
                                        }
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFF8FAFC),
                                        unfocusedContainerColor = Color(0xFFF8FAFC)
                                    )
                                )
                            }

                            // New Password
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("New Password", fontSize = 12.sp, color = Color(0xFF475569))
                                OutlinedTextField(
                                    value = newPass,
                                    onValueChange = { newPass = it },
                                    visualTransformation = if (isNewPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    trailingIcon = {
                                        IconButton(onClick = { isNewPassVisible = !isNewPassVisible }) {
                                            Icon(if (isNewPassVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color(0xFF94A3B8))
                                        }
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFF8FAFC),
                                        unfocusedContainerColor = Color(0xFFF8FAFC)
                                    )
                                )
                                Text("Minimum 8 characters with numbers", fontSize = 10.sp, color = Color(0xFF94A3B8))
                            }

                            // Confirm New Password
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Confirm New Password", fontSize = 12.sp, color = Color(0xFF475569))
                                OutlinedTextField(
                                    value = confPass,
                                    onValueChange = { confPass = it },
                                    visualTransformation = if (isConfPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    trailingIcon = {
                                        IconButton(onClick = { isConfPassVisible = !isConfPassVisible }) {
                                            Icon(if (isConfPassVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color(0xFF94A3B8))
                                        }
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFF8FAFC),
                                        unfocusedContainerColor = Color(0xFFF8FAFC)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }

            if (errorMessage != null) {
                Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 12.sp)
            }

            if (successMessage != null) {
                Text(successMessage ?: "", color = Color(0xFF16A34A), fontSize = 12.sp)
            }

            // Bottom Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onNavigateBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECEEF2)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).height(46.dp)
                ) {
                    Text("Cancel", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                }

                Button(
                    onClick = {
                        errorMessage = null
                        successMessage = null
                        if (fullName.isBlank()) {
                            errorMessage = "Full name cannot be empty"
                            return@Button
                        }

                        // Validate password update if requested
                        if (newPass.isNotBlank()) {
                            if (newPass != confPass) {
                                errorMessage = "New passwords do not match"
                                return@Button
                            }
                            if (newPass.length < 6) {
                                errorMessage = "Password must be at least 6 characters"
                                return@Button
                            }
                        }

                        // Dispatch role-specific profile updates
                        if (isCustomer) {
                            val updatedCustomer = subscriberProfile.copy(
                                fullName = fullName.trim(),
                                phone = "+91 ${primaryPhone.trim()}",
                                email = emailAddr.trim(),
                                emergencyContact = if (secPhone.isNotBlank()) "+91 ${secPhone.trim()}" else "",
                                locationType = locationType,
                                flatDetails = flatDetails.trim(),
                                landmark = landmark.trim(),
                                cityPincode = cityPincode.trim(),
                                gpsCoordinates = gpsCoordinates.trim(),
                                driverDropNote = driverDropNote.trim()
                            )
                            TiffinRepository.updateSubscriberProfile(updatedCustomer)
                        } else if (isRider) {
                            val updatedRider = riderProfile.copy(
                                fullName = fullName.trim(),
                                phone = "+91 ${primaryPhone.trim()}",
                                vehicleModel = vehicleModel.trim(),
                                vehiclePlate = vehiclePlate.trim().uppercase()
                            )
                            TiffinRepository.updateRiderProfile(updatedRider)
                        } else {
                            val updatedUser = userProfile.copy(
                                fullName = fullName.trim(),
                                displayJobTitle = jobTitle.trim(),
                                phone = "+91 ${primaryPhone.trim()}",
                                email = emailAddr.trim(),
                                secondaryPhone = secPhone.trim().ifBlank { null },
                                branchName = selectedBranch
                            )
                            TiffinRepository.updateUserProfile(updatedUser)
                        }

                        successMessage = "Profile changes saved successfully!"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.5f).height(46.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Text("Save Changes", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
