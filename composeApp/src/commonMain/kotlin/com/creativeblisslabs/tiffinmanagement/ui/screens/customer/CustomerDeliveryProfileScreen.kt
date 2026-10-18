package com.creativeblisslabs.tiffinmanagement.ui.screens.customer

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EditLocationAlt
import androidx.compose.material.icons.filled.HolidayVillage
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PublishedWithChanges
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinCustomerHeader
import kotlinx.coroutines.launch

/**
 * Customer / Subscriber Delivery Drop Profile Screen
 * Matches stitch_cross_platform_universal_ui_design 11/delivery_drop_profile
 * Features:
 * - Subscriber Profile Card: Aarav Sharma #SUB-8842, active plan & member tenure
 * - Primary Delivery Drop Point: GPS pinned banner (19.2458° N, 72.9734° E, 3m precision), full flat address, driver drop note & "Edit Address & GPS Location" button
 * - Weekend / Alternate Address toggle module: Home Address active for Sunday feasts (Sector 4, Kopar Khairane)
 * - WhatsApp & SMS Live Alerts 4-toggle matrix: Daily Morning Menu, Driver Dispatch & ETA, Dropped Photo, Low Wallet Balance
 * - Policies & Support quick links: Mess Rules & Meal Timings, Cancellation & Refund Policy, Privacy & Security Policy
 * - "Log Out from Subscriber Account" button
 */
@Composable
fun CustomerDeliveryProfileScreen(
    onEditProfile: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToRules: () -> Unit,
    onNavigateToRefund: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToAdmin: () -> Unit = {},
    onNavigateToRider: () -> Unit = {},
    onSignOut: () -> Unit
) {
    val subscriberProfile by TiffinRepository.subscriberProfile.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Screen Header
            TiffinCustomerHeader(
                title = "Profile",
                isActive = true,
                subtitle = "TIFFIN MANAGER",
                onNotificationClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Notifications preferences are actively synchronized.")
                    }
                },
                onSettingClick = onNavigateToSettings,
                onProfileClick = onNavigateToProfile
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. Subscriber Profile Overview Card
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
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Avatar with online status
                            Box(modifier = Modifier.size(64.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                listOf(Color(0xFFE87932), Color(0xFFAB2F00))
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val initials = subscriberProfile.fullName
                                        .split(" ")
                                        .mapNotNull { it.firstOrNull()?.toString() }
                                        .take(2)
                                        .joinToString("")
                                        .ifBlank { "" }
                                    Text(
                                        text = initials,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.Tertiary)
                                        .border(2.dp, ColorTokens.SurfaceContainerLowest, CircleShape)
                                        .align(Alignment.BottomEnd)
                                )
                            }

                            Column(
                                modifier = Modifier,
                                verticalArrangement = Arrangement.spacedBy(0.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = subscriberProfile.fullName,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )

                     Button(
                onClick = onEditProfile,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00)),
                shape = RoundedCornerShape(22.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Text("Edit", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(ColorTokens.PrimaryFixed)
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = subscriberProfile.subscriberNumber,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnPrimaryFixed
                                        )
                                    }


                                }

                                Text(
                                    text = subscriberProfile.phone,
                                    fontSize = 12.sp,
                                    color = ColorTokens.Secondary
                                )
                                Text(
                                    text = subscriberProfile.email,
                                    fontSize = 12.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }

                        // Subscription Details strip
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Active Subscription Plan",
                                    fontSize = 11.sp,
                                    color = ColorTokens.Secondary
                                )
                                Text(
                                    text = "Member Since ${subscriberProfile.memberSince}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = ColorTokens.OnSurfaceVariant
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.Primary)
                                )
                                Text(
                                    text = subscriberProfile.planTitle,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = ColorTokens.Tertiary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = subscriberProfile.scheduleSummary,
                                    fontSize = 12.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }
                    }
                }

                // 2. Primary Delivery Drop Point Card
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
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Primary Delivery Drop",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ColorTokens.SecondaryContainer)
                                    .padding(horizontal = 9.dp, vertical = 3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Apartment,
                                        contentDescription = null,
                                        tint = ColorTokens.OnSecondaryContainer,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "Hostel / Flat",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSecondaryContainer
                                    )
                                }
                            }
                        }

                        // Map Graphic Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF2A4365), Color(0xFF1A202C))
                                    )
                                )
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.align(Alignment.BottomStart),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NearMe,
                                    contentDescription = null,
                                    tint = ColorTokens.PrimaryFixed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "GPS Coordinates Pinned (${subscriberProfile.gpsAccuracy})",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }

                        // Full Address Text
                        Text(
                            text = "${subscriberProfile.flatDetails}, ${subscriberProfile.landmark}, ${subscriberProfile.cityPincode}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = ColorTokens.OnSurface,
                            lineHeight = 18.sp
                        )

                        // Driver Drop Note Callout
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.PrimaryFixed.copy(alpha = 0.35f))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.StickyNote2,
                                contentDescription = null,
                                tint = ColorTokens.Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "DRIVER DROP NOTE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnPrimaryFixedVariant,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "“${subscriberProfile.driverDropNote}”",
                                    fontSize = 12.sp,
                                    color = ColorTokens.OnSurface,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        // Edit Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerHigh)
                                .clickable { onEditProfile() }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EditLocationAlt,
                                    contentDescription = null,
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Edit Address & GPS Location",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                            }
                        }
                    }
                }

                // 3. Weekend / Alternate Address Module
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
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HolidayVillage,
                                    contentDescription = null,
                                    tint = ColorTokens.Secondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Weekend / Alternate Address",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                            }

                            Switch(
                                checked = subscriberProfile.hasSundayAddress,
                                onCheckedChange = { TiffinRepository.toggleSundayAddress(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = ColorTokens.Primary
                                )
                            )
                        }

                        if (subscriberProfile.hasSundayAddress) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.SecondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = null,
                                        tint = ColorTokens.OnSecondaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = subscriberProfile.sundayAddressTitle,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnSurface
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(ColorTokens.TertiaryFixed)
                                                .padding(horizontal = 6.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "Active for Sunday",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ColorTokens.OnTertiaryFixed
                                            )
                                        }
                                    }

                                    Text(
                                        text = "${subscriberProfile.sundayAddressDetails} (Assigned automatically for special Sunday feasts)",
                                        fontSize = 11.sp,
                                        color = ColorTokens.Secondary,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // 4. WhatsApp & SMS Live Alerts Matrix
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
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = ColorTokens.Tertiary,
                                modifier = Modifier.size(22.dp)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                Text(
                                    text = "WhatsApp & SMS Live Alerts",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                                Text(
                                    text = "Instant meal updates delivered to WhatsApp",
                                    fontSize = 11.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }

                        // Toggle 1: Morning Menu
                        NotificationToggleRow(
                            icon = Icons.Default.SoupKitchen,
                            iconTint = ColorTokens.Primary,
                            title = "Daily Morning Menu & Cooking Update",
                            subtitle = "Sent around 08:30 AM",
                            isChecked = subscriberProfile.notifyMorningMenu,
                            onToggle = { TiffinRepository.toggleNotification("morning", it) }
                        )

                        // Toggle 2: Driver Dispatch
                        NotificationToggleRow(
                            icon = Icons.Default.TwoWheeler,
                            iconTint = ColorTokens.Primary,
                            title = "Driver Dispatch & ETA Alert",
                            subtitle = "Sent around 12:00 PM & 07:30 PM",
                            isChecked = subscriberProfile.notifyDriverDispatch,
                            onToggle = { TiffinRepository.toggleNotification("dispatch", it) }
                        )

                        // Toggle 3: Photo confirmation
                        NotificationToggleRow(
                            icon = Icons.Default.PhotoCamera,
                            iconTint = ColorTokens.Tertiary,
                            title = "Tiffin Dropped Photo Confirmation",
                            subtitle = "Direct photo proof sent upon delivery",
                            isChecked = subscriberProfile.notifyDropPhoto,
                            onToggle = { TiffinRepository.toggleNotification("photo", it) }
                        )

                        // Toggle 4: Low wallet balance
                        NotificationToggleRow(
                            icon = Icons.Default.AccountBalanceWallet,
                            iconTint = ColorTokens.Error,
                            title = "Low Wallet Balance Alert",
                            subtitle = "Triggered when balance falls below ₹300",
                            isChecked = subscriberProfile.notifyLowBalance,
                            onToggle = { TiffinRepository.toggleNotification("low_balance", it) }
                        )
                    }
                }

                // 5. Policies & Subscriber Support Links
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Policies & Subscriber Support",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )

                        SupportPolicyRow(
                            icon = Icons.Default.MenuBook,
                            title = "Mess Rules & Meal Timings",
                            onClick = onNavigateToRules
                        )

                        SupportPolicyRow(
                            icon = Icons.Default.PublishedWithChanges,
                            title = "Cancellation & Refund Policy",
                            onClick = onNavigateToRefund
                        )

                        SupportPolicyRow(
                            icon = Icons.Default.Security,
                            title = "Privacy & Security Policy",
                            onClick = onNavigateToPrivacy
                        )

                        Spacer(modifier = Modifier.height(4.dp))


                        // Logout button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.ErrorContainer.copy(alpha = 0.5f))
                                .clickable { onSignOut() }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = null,
                                    tint = ColorTokens.Error,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Log Out from Subscriber Account",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnErrorContainer
                                )
                            }
                        }
                    }
                }

                // Footer label
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tiffin Manager v4.8 • Cloud Kitchen Dispatch Engine",
                        fontSize = 11.sp,
                        color = ColorTokens.Secondary
                    )
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

@Composable
private fun NotificationToggleRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = 2.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = ColorTokens.OnSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = ColorTokens.Secondary
                )
            }
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ColorTokens.Primary
            )
        )
    }
}

@Composable
private fun SupportPolicyRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
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
                    .clip(CircleShape)
                    .background(ColorTokens.SecondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = ColorTokens.OnSecondaryContainer,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = ColorTokens.OnSurface
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = ColorTokens.Secondary,
            modifier = Modifier.size(18.dp)
        )
    }
}
