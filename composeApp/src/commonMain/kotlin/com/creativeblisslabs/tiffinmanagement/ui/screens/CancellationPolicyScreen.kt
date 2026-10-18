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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.generated.resources.Res
import com.creativeblisslabs.tiffinmanagement.generated.resources.app_logo

@Composable
fun CancellationPolicyScreen(
    onNavigateBack: () -> Unit = {},
    onContactSupport: () -> Unit = {},
    onViewRefundPolicy: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var expandedSections by remember { mutableStateOf(setOf("1", "2")) }

    fun toggleSection(id: String) {
        expandedSections = if (expandedSections.contains(id)) {
            expandedSections - id
        } else {
            expandedSections + id
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FD))
    ) {
        // Legal Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "Cancellation Policy",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }
        }
        androidx.compose.material3.HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

        // Scrollable Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            
            // Hero / Meta Banner matching Trust & Privacy style
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(Color(0xFFD5E0F8))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.VerifiedUser, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(14.dp))
                        Text("Effective: Sep 2026", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF586377))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color(0xFFE6E8EC))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("No Lock-In", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5B4139))
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color(0xFF6FFBBE))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("1-Click Pause", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF002113))
                        }
                    }
                }

                Text(
                    text = "Cancellation Policy & Pause Rules",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191C1F)
                )

                Text(
                    text = "At Tiffin Manager, we prioritize operational flexibility. Manage, pause, or cancel subscriptions without hidden penalties or lock-in periods.",
                    fontSize = 13.sp,
                    color = Color(0xFF545F73),
                    lineHeight = 18.sp
                )
            }

            // Graphic Callout Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF191C1F)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFDBD1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Cancel, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text(
                            text = "Zero Lock-In Commitment",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Cancel or pause your meal subscription plan at any time before cutoff hours.",
                            fontSize = 11.sp,
                            color = Color(0xFFD8DADE)
                        )
                    }
                }
            }

            // Filter Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(Icons.Default.Search, null, tint = Color(0xFF545F73), modifier = Modifier.size(20.dp))
                },
                placeholder = { Text("Filter cancellation clauses (e.g. self-service, notice, renewal)...", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFAB2F00),
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                )
            )

            // Section 1: Your Right to Cancel
            if (searchQuery.isBlank() || "right to cancel registered merchant fees lock-in 1".contains(searchQuery.trim(), ignoreCase = true)) {
                PolicyArticleCard(
                    stepNumber = "1",
                    title = "Your Right to Cancel",
                    isExpanded = expandedSections.contains("1"),
                    onToggle = { toggleSection("1") }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "As a registered merchant on TiffinManager, you have full control over your account status. You can cancel your subscription at any time without termination fees, cancellation penalties, or hidden lock-in periods.",
                            fontSize = 12.sp,
                            color = Color(0xFF334155),
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // Section 2: How to Process a Cancellation
            if (searchQuery.isBlank() || "process self-service support assisted steps 2".contains(searchQuery.trim(), ignoreCase = true)) {
                PolicyArticleCard(
                    stepNumber = "2",
                    title = "How to Process a Cancellation",
                    isExpanded = expandedSections.contains("2"),
                    onToggle = { toggleSection("2") }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("2.1 Self-Service Cancellation", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(Color(0xFFFFDBD1))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("INSTANT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF862300))
                            }
                        }

                        Text("Cancel your renewal directly through your dashboard in 4 steps:", fontSize = 11.sp, color = Color(0xFF64748B))

                        listOf(
                            "Log in to your TiffinManager Vendor portal.",
                            "Navigate to Settings → Billing & Subscription.",
                            "Tap on 'Cancel Auto-Renewal' or 'Cancel Subscription'.",
                            "Confirm cancellation to receive an automated email receipt confirming the request."
                        ).forEachIndexed { index, stepText ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE2E8F0)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${index + 1}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                                }
                                Text(stepText, fontSize = 12.sp, color = Color(0xFF475569), lineHeight = 16.sp)
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFE2E8F0)))

                        Text("2.2 Support-Assisted Cancellation", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        Text(
                            "If you face any issues or prefer our team to handle the request, email us at:",
                            fontSize = 12.sp,
                            color = Color(0xFF475569)
                        )

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFF5F1))
                                .border(1.dp, Color(0xFFFFD3C4), RoundedCornerShape(8.dp))
                                .clickable { onContactSupport() }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Mail, null, tint = Color(0xFFF95721), modifier = Modifier.size(14.dp))
                            Text("support@tiffinmanager.com", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF95721))
                        }

                        Text(
                            "Include your Account ID. Processed within 24 business hours. Please submit at least 24 hours prior to renewal.",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            // Section 3: Effect of Cancellation & Active Period Access
            if (searchQuery.isBlank() || "effect active period access expiration preserve 3".contains(searchQuery.trim(), ignoreCase = true)) {
                PolicyArticleCard(
                    stepNumber = "3",
                    title = "Effect of Cancellation & Period Access",
                    isExpanded = expandedSections.contains("3"),
                    onToggle = { toggleSection("3") }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "When you cancel, your access does not shut down immediately:",
                            fontSize = 12.sp,
                            color = Color(0xFF475569)
                        )
                        listOf(
                            "Full access to order dispatch, printing, and customer ledger remains active until the end of your current paid billing period.",
                            "No further automatic debits will occur after the expiration date.",
                            "Your kitchen menu, customer directory, and route records are safely preserved for 90 days in case you wish to resume operations."
                        ).forEach { effectText ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF059669), modifier = Modifier.size(16.dp).padding(top = 1.dp))
                                Text(effectText, fontSize = 12.sp, color = Color(0xFF334155), lineHeight = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PolicyArticleCard(
    stepNumber: String,
    title: String,
    isExpanded: Boolean = true,
    onToggle: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFFF5F1))
                            .border(1.dp, Color(0xFFFFD3C4), RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stepNumber, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF95721))
                    }
                    Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                }

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Color(0xFF545F73),
                    modifier = Modifier
                        .size(22.dp)
                        .rotate(if (isExpanded) 180f else 0f)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    content()
                }
            }
        }
    }
}
