package com.creativeblisslabs.tiffinmanagement.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinTopHeader

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onBranchOperations: () -> Unit = {},
    onNotificationSettings: () -> Unit = {},
    onNavigateToCustomer: () -> Unit = {},
    onNavigateToRider: () -> Unit = {},
    onNavigateToAppInfo: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateToLegal: () -> Unit = {},
    onSignOut: () -> Unit = {}
) {
    val authState by TiffinRepository.authState.collectAsState()
    val activeUser = authState.currentUser
    val userProfile by TiffinRepository.userProfile.collectAsState()
    val subscriberProfile by TiffinRepository.subscriberProfile.collectAsState()
    val riderProfile by TiffinRepository.riderProfile.collectAsState()

    val role = (activeUser?.role ?: "admin").lowercase()
    val isCustomer = role == "customer" || role == "subscriber"
    val isRider = role == "rider" || role == "driver"

    var isCopiedGstin by remember { mutableStateOf(false) }
    var isSignOutConfirming by remember { mutableStateOf(false) }

    val displayName = when {
        isCustomer -> subscriberProfile.fullName.ifBlank { activeUser?.fullName ?: "Customer" }
        isRider -> riderProfile.fullName.ifBlank { activeUser?.fullName ?: "Delivery Partner" }
        else -> userProfile.fullName.ifBlank { activeUser?.fullName ?: "Admin" }
    }

    val displayEmail = when {
        isCustomer -> subscriberProfile.email.ifBlank { activeUser?.email ?: "" }
        isRider -> activeUser?.email ?: ""
        else -> userProfile.email.ifBlank { activeUser?.email ?: "" }
    }

    val displayPhone = when {
        isCustomer -> subscriberProfile.phone.ifBlank { activeUser?.phone ?: "+91 98765 43210" }
        isRider -> riderProfile.phone?.ifBlank { activeUser?.phone ?: "+91 98765 43210" } ?: (activeUser?.phone ?: "+91 98765 43210")
        else -> userProfile.phone.ifBlank { activeUser?.phone ?: "+91 98765 43210" }
    }

    val roleLabel = when {
        isCustomer -> "Subscriber / Customer"
        isRider -> "Rider / Delivery Partner"
        else -> "Owner / Admin"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FD))
    ) {
        // Scrollable Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Profile Summary Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar & Role Badges
                    Box(contentAlignment = Alignment.BottomCenter) {
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF4EF))
                                .border(2.dp, Color(0xFFFFB59F), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = displayName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFAB2F00)
                            )
                        }
                        Spacer(modifier = Modifier.height(26.dp))

                        // Verified Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color(0xFFD53E02))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                Icon(Icons.Default.Verified, null, tint = Color.White, modifier = Modifier.size(11.dp))
                                Text(roleLabel, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = displayName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.width(12.dp))

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
                    }

                    Text(
                        text = when {
                            isCustomer -> "Subscriber ID: ${subscriberProfile.subscriberNumber.ifBlank { subscriberProfile.id }}"
                            isRider -> "Rider ID: ${riderProfile.driverIdCode.ifBlank { riderProfile.id }} • ${riderProfile.vehiclePlate}"
                            else -> userProfile.displayJobTitle
                        },
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Active Business / Kitchen Tag
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color(0xFFECEEF2))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF006947)))
                        Text(
                            text = when {
                                isCustomer -> "Active Meal Drop • ${subscriberProfile.flatDetails.ifBlank { "Home Drop" }}"
                                isRider -> "Assigned Route • ${riderProfile.currentShiftName.ifBlank { "Day Shift" }}"
                                else -> "Tiffin Manager • ${userProfile.branchName}"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF191C1F)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Metrics Ribbon (3 cols)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (isCustomer) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Plan", fontSize = 11.sp, color = Color(0xFF64748B))
                                Text(subscriberProfile.planTitle.ifBlank { "Active Meal" }, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Text(subscriberProfile.scheduleSummary.ifBlank { "Daily Regular" }, fontSize = 9.sp, color = Color(0xFF006947), fontWeight = FontWeight.SemiBold)
                            }
                            Box(modifier = Modifier.width(1.dp).height(36.dp).background(Color(0xFFE2E8F0)))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Wallet", fontSize = 11.sp, color = Color(0xFF64748B))
                                Text("₹${subscriberProfile.walletBalance.toInt()}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00))
                                Text("Available balance", fontSize = 9.sp, color = Color(0xFF94A3B8))
                            }
                            Box(modifier = Modifier.width(1.dp).height(36.dp).background(Color(0xFFE2E8F0)))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Buffer", fontSize = 11.sp, color = Color(0xFF64748B))
                                Text("₹${subscriberProfile.overdraftBuffer.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Text(subscriberProfile.lunchSlotWindow.ifBlank { "Regular Slot" }, fontSize = 9.sp, color = Color(0xFF94A3B8))
                            }
                        } else if (isRider) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Today Drops", fontSize = 11.sp, color = Color(0xFF64748B))
                                Text("${riderProfile.completedDrops}/${riderProfile.totalDrops}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Text("Boxes done", fontSize = 9.sp, color = Color(0xFF006947), fontWeight = FontWeight.SemiBold)
                            }
                            Box(modifier = Modifier.width(1.dp).height(36.dp).background(Color(0xFFE2E8F0)))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Rating", fontSize = 11.sp, color = Color(0xFF64748B))
                                Text("★ ${riderProfile.rating}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00))
                                Text("${riderProfile.totalSafeDrops} safe drops", fontSize = 9.sp, color = Color(0xFF94A3B8))
                            }
                            Box(modifier = Modifier.width(1.dp).height(36.dp).background(Color(0xFFE2E8F0)))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Base Rate", fontSize = 11.sp, color = Color(0xFF64748B))
                                Text("₹${riderProfile.baseDropRate.toInt()}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Text("Per drop payout", fontSize = 9.sp, color = Color(0xFF94A3B8))
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Branches", fontSize = 11.sp, color = Color(0xFF64748B))
                                Text(userProfile.totalBranchesCount.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Text("Central Kitchen", fontSize = 9.sp, color = Color(0xFF006947), fontWeight = FontWeight.SemiBold)
                            }
                            Box(modifier = Modifier.width(1.dp).height(36.dp).background(Color(0xFFE2E8F0)))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Subscribers", fontSize = 11.sp, color = Color(0xFF64748B))
                                Text(userProfile.activeSubscribersCount.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00))
                                Text("Active daily", fontSize = 9.sp, color = Color(0xFF94A3B8))
                            }
                            Box(modifier = Modifier.width(1.dp).height(36.dp).background(Color(0xFFE2E8F0)))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Operating", fontSize = 11.sp, color = Color(0xFF64748B))
                                Text(userProfile.operatingSince, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Text("Since Jan", fontSize = 9.sp, color = Color(0xFF94A3B8))
                            }
                        }
                    }
                }
            }

            // 2. Personal & Contact Information
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isCustomer) "DELIVERY DETAILS & CONTACT" else if (isRider) "RIDER DETAILS & CONTACT" else "BUSINESS CREDENTIALS & CONTACT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        letterSpacing = 0.5.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF006947), modifier = Modifier.size(13.dp))
                        Text("KYC Verified", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006947))
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Phone Item
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(
                                    modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFD5E0F8)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Call, null, tint = Color(0xFF111C2D), modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Primary Mobile", fontSize = 10.sp, color = Color(0xFF64748B))
                                    Text(displayPhone, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                }
                            }
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(22.dp)).background(Color(0xFFECFDF5)).padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Verified", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006947))
                            }
                        }

                        // Email Item
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(
                                    modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFD5E0F8)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Mail, null, tint = Color(0xFF111C2D), modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Billing / Account Email", fontSize = 10.sp, color = Color(0xFF64748B))
                                    Text(displayEmail, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                }
                            }
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(22.dp)).background(Color(0xFFECFDF5)).padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Primary", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006947))
                            }
                        }

                        // Role / Drop Item
                        if (isCustomer) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Box(
                                        modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFE2E8F0)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.LocationOn, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                                    }
                                    Column {
                                        Text("Primary Delivery Drop", fontSize = 10.sp, color = Color(0xFF64748B))
                                        Text("${subscriberProfile.flatDetails.ifBlank { "Doorstep Drop" }}, ${subscriberProfile.landmark}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                    }
                                }
                                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF006947), modifier = Modifier.size(18.dp))
                            }
                        } else if (isRider) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Box(
                                        modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFE2E8F0)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.TwoWheeler, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                                    }
                                    Column {
                                        Text("Vehicle & Badge", fontSize = 10.sp, color = Color(0xFF64748B))
                                        Text("${riderProfile.vehicleModel} (${riderProfile.vehiclePlate})", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                    }
                                }
                                Text("DL Valid", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006947))
                            }
                        } else {
                            // GSTIN / Tax Item for Admin
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Box(
                                        modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFD5E0F8)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.ReceiptLong, null, tint = Color(0xFF111C2D), modifier = Modifier.size(18.dp))
                                    }
                                    Column {
                                        Text("Business GSTIN", fontSize = 10.sp, color = Color(0xFF64748B))
                                        Text(
                                            text = userProfile.businessGstin,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            color = Color(0xFF0F172A)
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .clickable { isCopiedGstin = true },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isCopiedGstin) Icons.Default.CheckCircle else Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = if (isCopiedGstin) Color(0xFF006947) else Color(0xFF64748B),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Fast Navigation / Account Actions
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "ACCOUNT & PREFERENCES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        AccountActionRow(
                            icon = Icons.Default.ManageAccounts,
                            title = "Edit Profile & Address Details",
                            subtitle = "Update profile info, phone number & security",
                            onClick = onEditProfile
                        )
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))

                        if (!isCustomer && !isRider) {
                            AccountActionRow(
                                icon = Icons.Default.Storefront,
                                title = "Branch Profile & Operations",
                                subtitle = "Kitchen timings, prep cut-offs, dispatch radii",
                                onClick = onBranchOperations
                            )
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))
                        }

                        AccountActionRow(
                            icon = Icons.Default.NotificationsActive,
                            title = "Notification Preferences",
                            subtitle = "SMS, WhatsApp alerts & daily meal schedule updates",
                            onClick = onNotificationSettings
                        )
                    }
                }
            }

            // 4. App Info & Support Cards (Requested at bottom of Profile Screen)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "HELP, LEGAL & APP INFORMATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        AccountActionRow(
                            icon = Icons.Default.Info,
                            title = "App Info & Diagnostics",
                            subtitle = "Version v2.4.0 • Enterprise Cloud Kitchen build info",
                            onClick = onNavigateToAppInfo
                        )
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))
                        AccountActionRow(
                            icon = Icons.Default.SupportAgent,
                            title = "Support Desk & FAQs",
                            subtitle = "Need help? Chat with kitchen dispatch or submit ticket",
                            onClick = onNavigateToSupport
                        )
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))
                        AccountActionRow(
                            icon = Icons.Default.Policy,
                            title = "Legal Center & Meal Policies",
                            subtitle = "Privacy, Terms of Service & refund/cancellation rules",
                            onClick = onNavigateToLegal
                        )
                    }
                }
            }

            // 5. Sign Out Button & App Version Footer
            Button(
                onClick = {
                    if (isSignOutConfirming) {
                        onSignOut()
                    } else {
                        isSignOutConfirming = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFDAD6)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color(0xFFBA1A1A), modifier = Modifier.size(18.dp))
                    Text(
                        text = if (isSignOutConfirming) "Tap again to Confirm Sign Out" else "Sign Out from Account",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFBA1A1A)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.SoupKitchen, null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                    Text("Tiffin Manager Enterprise", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                }
                Text("v2.4.0 (Build 2026.09) • Made for Cloud Kitchens", fontSize = 10.sp, color = Color(0xFF94A3B8))
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun AccountActionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color(0xFF334155), modifier = Modifier.size(18.dp))
            }

            Column {
                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Text(subtitle, fontSize = 11.sp, color = Color(0xFF64748B))
            }
        }

        Icon(Icons.Default.ArrowForward, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
    }
}
