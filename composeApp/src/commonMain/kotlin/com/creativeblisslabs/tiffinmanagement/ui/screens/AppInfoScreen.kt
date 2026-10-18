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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.generated.resources.Res
import com.creativeblisslabs.tiffinmanagement.generated.resources.app_logo

@Composable
fun AppInfoScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToPrivacy: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
    onNavigateToContact: () -> Unit = {}
) {
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
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Identity Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Brand Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(Color(0xFFF2F4F8))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.RestaurantMenu, null, modifier = Modifier.size(14.dp), tint = Color(0xFFAB2F00))
                            Text("ABOUT US", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00), letterSpacing = 0.5.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // App Logo with pulse indicator
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFFF0EC)),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.foundation.Image(
                                painter = org.jetbrains.compose.resources.painterResource(Res.drawable.app_logo),
                                contentDescription = "Tiffin Manager Brand Logo",
                                modifier = Modifier.fillMaxSize().padding(4.dp),
                                contentScale = androidx.compose.ui.layout.ContentScale.Fit
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF006947))
                                .border(2.dp, Color.White, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Built Around Real Tiffin Workflows",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191C1F),
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color(0xFFE6E8EC))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text("v2.4.8 (Build 2026.09)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF5B4139))
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color(0xFF6FFBBE))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text("Production Stable", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF002113))
                        }
                    }

                    Text(
                        text = "We build software that helps tiffin operators run their daily business with less stress — focused on delivery dispatch, kitchen planning, and transparent customer billing.",
                        fontSize = 13.sp,
                        color = Color(0xFF5B4139),
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp,
                        modifier = Modifier.padding(top = 10.dp, bottom = 14.dp)
                    )

                    // Quick Action Counter Chips (500+ Kitchens, 99.9% Dispatched, ₹0 Disputes)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF2F4F8))
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("500+", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00))
                            Text("Kitchens", fontSize = 11.sp, color = Color(0xFF545F73))
                        }
                        Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color(0xFFD8DADE)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("99.9%", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006947))
                            Text("Dispatched", fontSize = 11.sp, color = Color(0xFF545F73))
                        }
                        Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color(0xFFD8DADE)))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("₹0", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                            Text("Disputes", fontSize = 11.sp, color = Color(0xFF545F73))
                        }
                    }
                }
            }

            // Section: Why We Built This
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFDBD1)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Lightbulb, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text("Why We Built This", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                            Text("Solving daily morning dispatch pressure", fontSize = 11.sp, color = Color(0xFF545F73))
                        }
                    }

                    Text(
                        text = "Tiffin service operations are operationally intense. Every morning starts with figuring out how many meals to cook, which customers paused, who changed quantity, and which route the driver should follow. Every month ends with billing reconciliation that can take days.",
                        fontSize = 12.sp,
                        color = Color(0xFF5B4139),
                        lineHeight = 18.sp
                    )

                    // Problem vs Approach Box
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF2F4F8))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Cancel, null, tint = Color(0xFFBA1A1A), modifier = Modifier.size(16.dp).padding(top = 1.dp))
                            Text(
                                text = "The Problem: WhatsApp groups, handwritten dairies, and Excel sheets make manual updates slip through during peak kitchen prep.",
                                fontSize = 12.sp,
                                color = Color(0xFF5B4139),
                                lineHeight = 17.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF006947), modifier = Modifier.size(16.dp).padding(top = 1.dp))
                            Text(
                                text = "Our Approach: Hard cutoff locks protect the cook stove, combined with real-time wallet ledgers that customers trust unconditionally.",
                                fontSize = 12.sp,
                                color = Color(0xFF5B4139),
                                lineHeight = 17.sp
                            )
                        }
                    }

                    Text("What We Focus On", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))

                    listOf(
                        "Simple screens that kitchen and delivery staff learn in 10 minutes",
                        "Transparent ledger wallet: verified balance with daily delivery audit",
                        "Strict cutoff rules protecting prep from late changes",
                        "Zero app installation required for staff or clients"
                    ).forEach { focusItem ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFAB2F00)))
                            Text(focusItem, fontSize = 12.sp, color = Color(0xFF5B4139))
                        }
                    }
                }
            }

            // Section: How We Think About Software (Our Principles)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(Color(0xFFE6E8EC))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF5B4139), modifier = Modifier.size(12.dp))
                            Text("OUR PRINCIPLES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5B4139))
                        }
                    }
                    Text("How We Think About Software", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F), modifier = Modifier.padding(top = 4.dp))
                }

                // Principle 1
                PrincipleCard(
                    icon = Icons.Default.Storefront,
                    bgColor = Color(0xFFFFDBD1),
                    tintColor = Color(0xFFAB2F00),
                    title = "Operation-First Design",
                    description = "Designed for the real daily rhythm: 6 AM morning prep, noon dispatch, and month-end settlement."
                )

                // Principle 2
                PrincipleCard(
                    icon = Icons.Default.VerifiedUser,
                    bgColor = Color(0xFF6FFBBE),
                    tintColor = Color(0xFF005236),
                    title = "No Destructive Records",
                    description = "Deliveries and ledger entries are permanently audited. No record ever disappears mysteriously."
                )

                // Principle 3
                PrincipleCard(
                    icon = Icons.Default.Groups,
                    bgColor = Color(0xFFD5E0F8),
                    tintColor = Color(0xFF586377),
                    title = "Simple for Non-Technical Staff",
                    description = "Intuitive color coding, large tap targets, and no confusing technical software jargon."
                )

                // Principle 4
                PrincipleCard(
                    icon = Icons.Default.Smartphone,
                    bgColor = Color(0xFFFFDBD1).copy(alpha = 0.5f),
                    tintColor = Color(0xFFD53E02),
                    title = "Mobile-Ready Always",
                    description = "Optimized for low-end mobile devices on 4G networks right from the browser or app."
                )
            }

            // Section: Designed for These Operations (6 Kitchen Types)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(Color(0xFFE6E8EC))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF5B4139), modifier = Modifier.size(12.dp))
                            Text("WHO THIS IS FOR", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5B4139))
                        }
                    }
                    Text("Designed for These Operations", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F), modifier = Modifier.padding(top = 4.dp))
                }

                val operations = listOf(
                    Triple(Icons.Default.Storefront, "Home Kitchen", "Small batch home subscriptions for office workers."),
                    Triple(Icons.Default.RestaurantMenu, "Tiffin Service", "Multi-zone citywide breakfast, lunch & dinner routes."),
                    Triple(Icons.Default.Storefront, "Corporate Meals", "Bulk lunches delivered directly to corporate tech parks."),
                    Triple(Icons.Default.Groups, "Hostel / PG Mess", "Daily student attendance logs with meal opt-in/out."),
                    Triple(Icons.Default.CheckCircle, "Meal Subs", "Diet, keto, or fixed monthly calendar tiffins."),
                    Triple(Icons.Default.Storefront, "Cloud Kitchen", "Centralized high-volume food prep with batch tagging.")
                )

                for (pair in operations.chunked(2)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for ((icon, title, desc) in pair) {
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFF2F4F8)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(icon, contentDescription = title, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                                    }
                                    Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F), modifier = Modifier.padding(top = 6.dp))
                                    Text(desc, fontSize = 11.sp, color = Color(0xFF5B4139), lineHeight = 15.sp, modifier = Modifier.padding(top = 2.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Section: Connect & Socials
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Connect & Socials", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                            Text("Join operator discussions and updates", fontSize = 11.sp, color = Color(0xFF545F73))
                        }
                        Icon(Icons.Default.Forum, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(20.dp))
                    }

                    // Channels List
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SocialRow(icon = Icons.Default.Chat, iconBg = Color(0xFF6FFBBE), iconTint = Color(0xFF005236), title = "WhatsApp Helpdesk", handle = "+91 76548 43346")
                        SocialRow(icon = Icons.Default.PlayCircle, iconBg = Color(0xFFFFDAD6), iconTint = Color(0xFFBA1A1A), title = "YouTube Guides", handle = "@TiffinManagerHQ")
                        SocialRow(icon = Icons.Default.PhotoCamera, iconBg = Color(0xFFFFDBD1), iconTint = Color(0xFFAB2F00), title = "Instagram", handle = "@tiffinmanager")
                        SocialRow(icon = Icons.Default.Share, iconBg = Color(0xFFD5E0F8), iconTint = Color(0xFF545F73), title = "LinkedIn & X", handle = "@TiffinManager")
                    }

                    Button(
                        onClick = { onNavigateToContact() },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.SupportAgent, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Text("Chat with Operations Team", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Legal Compliance & Policies (2-Column Grid)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F8)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("LEGAL & COMPLIANCE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F), letterSpacing = 0.5.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .clickable { onNavigateToTerms() }
                                .padding(10.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Terms of Service", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF191C1F))
                                Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF545F73), modifier = Modifier.size(16.dp))
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .clickable { onNavigateToPrivacy() }
                                .padding(10.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Privacy Policy", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF191C1F))
                                Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF545F73), modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .clickable { onNavigateToTerms() }
                                .padding(10.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("Refund Policy", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF191C1F))
                                Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF545F73), modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            // Headquarters & Copyright Footer
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.LocationOn, null, tint = Color(0xFF545F73), modifier = Modifier.size(16.dp))
                    Text("© 2026 Tiffin Manager.", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                }
                Text(" • Serving kitchens pan-India", fontSize = 11.sp, color = Color(0xFF5B4139))
            }
        }
    }
}

@Composable
fun PrincipleCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bgColor: Color,
    tintColor: Color,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = tintColor, modifier = Modifier.size(22.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                Text(description, fontSize = 12.sp, color = Color(0xFF5B4139), lineHeight = 17.sp, modifier = Modifier.padding(top = 2.dp))
            }
        }
    }
}

@Composable
fun SocialRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    handle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
            Column {
                Text(title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                Text(handle, fontSize = 11.sp, color = Color(0xFF545F73))
            }
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color(0xFF545F73), modifier = Modifier.size(16.dp))
    }
}
