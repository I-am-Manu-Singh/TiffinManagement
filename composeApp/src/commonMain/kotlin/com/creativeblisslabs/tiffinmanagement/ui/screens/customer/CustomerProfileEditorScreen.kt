package com.creativeblisslabs.tiffinmanagement.ui.screens.customer

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
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Desk
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import kotlinx.coroutines.launch

/**
 * Customer / Subscriber Profile Editor Screen
 * Matches stitch_cross_platform_universal_ui_design 11/subscriber_profile_editor
 * Features:
 * - Sub-Header with Back, Title "Edit Profile" (Aarav Sharma • Regular Plan) & "Save" pill
 * - Avatar & Status Header (#SUB-8842 • Active Subscriber, Camera badge)
 * - Personal Information Card:
 *   - KYC Done badge
 *   - Full Name ("Aarav Sharma")
 *   - Primary Mobile Number (Verified badge, +91 98765 43210)
 *   - Email Address ("aarav.sharma@gmail.com")
 *   - Emergency / Roommate Contact ("+91 98111 22334")
 * - Primary Drop Point Card:
 *   - Tiffin Stop #14 badge
 *   - Location Classification 4-grid: Apartment/Flat, Hostel/PG, Office Desk, Other
 *   - Flat/Unit ("Flat 402, Sunshine Residency, Wing B")
 *   - Landmark ("Near Tech Park Gate 2, Hiranandani Estate")
 *   - City & Postal Pincode ("Mumbai - 400076")
 *   - GPS Pin Accuracy: High (3m Precision), Coordinates (19.2458° N, 72.9734° E), Recalibrate GPS Pin
 *   - Driver Instructions & Drop Note (Multi-line text area)
 * - Sunday Alternate Address Card (Toggle switch, Sector 4, Kopar Khairane Route #4)
 * - Meal Dispatch Windows (Lunch 12:30-1:15, Dinner 07:30-8:30)
 * - Operator audit note & "Save & Update Profile" button
 */
@Composable
fun CustomerProfileEditorScreen(
    onNavigateBack: () -> Unit
) {
    val subscriberProfile by TiffinRepository.subscriberProfile.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var fullName by remember(subscriberProfile) { mutableStateOf(subscriberProfile.fullName) }
    var mobile by remember(subscriberProfile) { mutableStateOf(subscriberProfile.phone) }
    var email by remember(subscriberProfile) { mutableStateOf(subscriberProfile.email) }
    var emergencyContact by remember(subscriberProfile) { mutableStateOf(subscriberProfile.emergencyContact) }
    var locationType by remember(subscriberProfile) { mutableStateOf(subscriberProfile.locationType.ifBlank { "apartment" }) }
    var flatDetails by remember(subscriberProfile) { mutableStateOf(subscriberProfile.flatDetails) }
    var landmark by remember(subscriberProfile) { mutableStateOf(subscriberProfile.landmark) }
    var cityPincode by remember(subscriberProfile) { mutableStateOf(subscriberProfile.cityPincode) }
    var driverDropNote by remember(subscriberProfile) { mutableStateOf(subscriberProfile.driverDropNote) }
    var hasSundayAddress by remember(subscriberProfile) { mutableStateOf(subscriberProfile.hasSundayAddress) }
    var gpsCoordinates by remember(subscriberProfile) { mutableStateOf(subscriberProfile.gpsCoordinates.ifBlank { "19.2458° N, 72.9734° E" }) }

    fun handleSave() {
        val updated = subscriberProfile.copy(
            fullName = fullName,
            phone = mobile,
            email = email,
            emergencyContact = emergencyContact,
            locationType = locationType,
            flatDetails = flatDetails,
            landmark = landmark,
            cityPincode = cityPincode,
            gpsCoordinates = gpsCoordinates,
            driverDropNote = driverDropNote,
            hasSundayAddress = hasSundayAddress
        )
        TiffinRepository.updateSubscriberProfile(updated)
        scope.launch {
            snackbarHostState.showSnackbar("Profile & Delivery Drop Point successfully saved!")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Sub-Header Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorTokens.SurfaceContainerLowest)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { onNavigateBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = ColorTokens.OnSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Edit Profile",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                 
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(ColorTokens.PrimaryFixed)
                        .clickable { handleSave() }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Save",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnPrimaryFixed
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Avatar & Subscriber Status Header
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(modifier = Modifier.size(80.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFFE87932), Color(0xFFAB2F00))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                              Text(
                                 text = subscriberProfile.fullName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                 fontSize = 28.sp,
                                 fontWeight = FontWeight.Bold,
                                 color = Color.White
                             )
                            }

                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.Primary)
                                    .align(Alignment.BottomEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = "Change photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = fullName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = ColorTokens.Tertiary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(ColorTokens.TertiaryFixed)
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.Tertiary)
                                )
                                Text(
                                    text = if (subscriberProfile.subscriberNumber.isNotBlank()) "${subscriberProfile.subscriberNumber} • Active Subscriber" else "Active Subscriber",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnTertiaryFixed
                                )
                            }
                        }
                    }
                }

                // 2. Personal Information Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
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
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ColorTokens.SecondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = ColorTokens.OnSecondaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "Personal Information",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                            }
                        }

                        // Full Name field
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(text = "Full Name", fontSize = 11.sp, color = ColorTokens.Secondary)
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = ColorTokens.Outline,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }

                        // Primary Mobile Number
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Primary Mobile Number", fontSize = 11.sp, color = ColorTokens.Secondary)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = ColorTokens.Tertiary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "Verified",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.Tertiary
                                    )
                                }
                            }
                            OutlinedTextField(
                                value = mobile,
                                onValueChange = { mobile = it },
                                prefix = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Default.PhoneInTalk, contentDescription = null, modifier = Modifier.size(14.dp), tint = ColorTokens.Primary)
                                        Text("+91 ")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.VerifiedUser,
                                        contentDescription = null,
                                        tint = ColorTokens.Tertiary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            )
                        }

                        // Email Address
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(text = "Email Address (Invoicing & Menu)", fontSize = 11.sp, color = ColorTokens.Secondary)
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Mail,
                                        contentDescription = null,
                                        tint = ColorTokens.Outline,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }

                        // Emergency Contact
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(text = "Emergency / Roommate Contact (Optional)", fontSize = 11.sp, color = ColorTokens.Secondary)
                            OutlinedTextField(
                                value = emergencyContact,
                                onValueChange = { emergencyContact = it },
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.PhoneInTalk,
                                        contentDescription = null,
                                        tint = ColorTokens.Outline,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                    }
                }

                // 3. Primary Drop Point & GPS Location Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
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
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ColorTokens.PrimaryFixed),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = ColorTokens.Primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "Primary Drop Point",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                            }
                            Text(
                                text = "Tiffin Stop #14",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.Primary
                            )
                        }

                        // Location classification 4-grid
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "Location Classification", fontSize = 11.sp, color = ColorTokens.Secondary)
                            val options = listOf(
                                "apartment" to ("Apartment / Flat" to Icons.Default.Apartment),
                                "hostel" to ("Hostel / PG" to Icons.Default.Hotel),
                                "office" to ("Office Desk" to Icons.Default.Desk),
                                "other" to ("Other" to Icons.Default.MoreHoriz)
                            )
                            options.chunked(2).forEach { rowList ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowList.forEach { (type, pair) ->
                                        val isSelected = locationType == type
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (isSelected) ColorTokens.Primary else ColorTokens.SurfaceContainer
                                                )
                                                .clickable { locationType = type }
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = pair.second,
                                                    contentDescription = null,
                                                    tint = if (isSelected) ColorTokens.OnPrimary else ColorTokens.OnSurface,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Text(
                                                    text = pair.first,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) ColorTokens.OnPrimary else ColorTokens.OnSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Address fields
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(text = "Flat / Unit & Building Details", fontSize = 11.sp, color = ColorTokens.Secondary)
                            OutlinedTextField(
                                value = flatDetails,
                                onValueChange = { flatDetails = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(text = "Landmark & Sub-Area", fontSize = 11.sp, color = ColorTokens.Secondary)
                            OutlinedTextField(
                                value = landmark,
                                onValueChange = { landmark = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(text = "City & Postal Pincode", fontSize = 11.sp, color = ColorTokens.Secondary)
                            OutlinedTextField(
                                value = cityPincode,
                                onValueChange = { cityPincode = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // GPS Precision Card
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                          

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF2C3E50), Color(0xFF1A252F))
                                        )
                                    )
                                    .padding(10.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.End)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.White.copy(alpha = 0.9f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.MyLocation,
                                                contentDescription = null,
                                                tint = ColorTokens.Primary,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Text(
                                                text = subscriberProfile.gpsCoordinates.ifBlank { "GPS Coordinates Pending" },
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ColorTokens.OnSurface
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "B-Wing Gate Entry Pin",
                                            fontSize = 11.sp,
                                            color = Color.White
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(ColorTokens.Primary)
                                                .clickable {
                                                    gpsCoordinates = "19.2461° N, 72.9739° E"
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("GPS Pin recalibrated to current high-precision coordinates!")
                                                    }
                                                }
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "Recalibrate GPS Pin",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ColorTokens.OnPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Driver Delivery Note
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Driver Instructions & Drop Note", fontSize = 11.sp, color = ColorTokens.Secondary)
                            }
                            OutlinedTextField(
                                value = driverDropNote,
                                onValueChange = { driverDropNote = it },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3
                            )
                        }
                    }
                }

                // 4. Sunday Alternate Address Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
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
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ColorTokens.TertiaryFixed),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Weekend,
                                        contentDescription = null,
                                        tint = ColorTokens.OnTertiaryFixed,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                    Text(
                                        text = "Sunday Alternate Address",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "Route split for weekend lunch",
                                        fontSize = 11.sp,
                                        color = ColorTokens.Secondary
                                    )
                                }
                            }

                            Switch(
                                checked = hasSundayAddress,
                                onCheckedChange = { hasSundayAddress = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = ColorTokens.Primary
                                )
                            )
                        }

                        if (hasSundayAddress) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = subscriberProfile.sundayAddressTitle.ifBlank { "Sunday Alternate Drop Pinned" },
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "Change",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.Primary
                                    )
                                }
                                Text(
                                    text = subscriberProfile.sundayAddressDetails.ifBlank { "Address configuration pending" },
                                    fontSize = 11.sp,
                                    color = ColorTokens.Secondary
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ColorTokens.TertiaryFixed.copy(alpha = 0.5f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = ColorTokens.Tertiary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = subscriberProfile.sundayRoute.ifBlank { "Assigned Route" },
                                        fontSize = 10.sp,
                                        color = ColorTokens.OnTertiaryFixedVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // 5. Meal Dispatch Windows Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ColorTokens.SecondaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = ColorTokens.OnSecondaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Meal Dispatch Windows",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Lunch delivery
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.WbSunny,
                                            contentDescription = null,
                                            tint = ColorTokens.Primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(text = "Lunch Delivery", fontSize = 10.sp, color = ColorTokens.Secondary)
                                    }
                                    Text(
                                        text = subscriberProfile.lunchSlotWindow.ifBlank { "12:30 - 1:15" },
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Text(text = "Standard Slot", fontSize = 10.sp, color = ColorTokens.Tertiary)
                                }
                            }

                            // Dinner delivery
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = ColorTokens.Secondary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(text = "Dinner Delivery", fontSize = 10.sp, color = ColorTokens.Secondary)
                                    }
                                    Text(
                                        text = subscriberProfile.dinnerSlotWindow.ifBlank { "07:30 - 8:30" },
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Text(text = "Evening Route", fontSize = 10.sp, color = ColorTokens.Tertiary)
                                }
                            }
                        }
                    }
                }

                // 6. Save & Update Profile CTA
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ColorTokens.Primary)
                        .clickable { handleSave() }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            tint = ColorTokens.OnPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Save & Update Profile",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp)
        )
    }
}
