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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SecurityPolicyScreen(
    onNavigateBack: () -> Unit,
    onNavigatePolicy: (String) -> Unit = {},
    onContactSecurity: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var searchQuery by remember { mutableStateOf("") }
    var expandedSections by remember { mutableStateOf(setOf("sec-1")) }

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
            .background(Color(0xFFF8FAFC))
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
                    text = "Security Policy",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }
        }
        HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

        // Scrollable Body
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 16.dp),
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
                        Text("Effective: Jul 2026", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF586377))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color(0xFFE6E8EC))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("SOC 2 Ready", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5B4139))
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color(0xFF6FFBBE))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("AES-256", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF002113))
                        }
                    }
                }

                Text(
                    text = "Security Policy & Cryptographic Guarantees",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191C1F)
                )

                Text(
                    text = "End-to-end security architecture protecting kitchen routes, customer delivery drops, staff identities, and financial ledgers.",
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
                        Icon(Icons.Default.Security, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text(
                            text = "Zero-Trust Encryption In Transit & At Rest",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "All database entries and transit channels utilize TLS 1.3 and authenticated AES-256 encryption.",
                            fontSize = 11.sp,
                            color = Color(0xFFD8DADE)
                        )
                    }
                }
            }

            // Search / Filter Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(Icons.Default.Search, null, tint = Color(0xFF545F73), modifier = Modifier.size(20.dp))
                },
                placeholder = { Text("Filter security protocols (e.g. TLS, AES, audit)...", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFAB2F00),
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                )
            )

            // Section 1: Data Encryption
            if (searchQuery.isBlank() || "data encryption tls aes transit rest".contains(searchQuery.trim(), ignoreCase = true)) {
                SecurityAccordionCard(
                    id = "sec-1",
                    step = "1",
                    title = "Data Encryption",
                    subtitle = "Cryptographic protection across all states",
                    icon = Icons.Default.Lock,
                    isExpanded = expandedSections.contains("sec-1"),
                    onToggle = { toggleSection("sec-1") }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF8FAFC))
                                .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text("A. In Transit:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Text("All HTTP connections are encrypted via TLS 1.3. Any data sent from customer portals or merchant dashboards is protected during transmission.", fontSize = 11.sp, color = Color(0xFF475569))
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF8FAFC))
                                .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text("B. At Rest:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                Text("Databases and file backups are encrypted utilizing AES-256 standards on cloud servers.", fontSize = 11.sp, color = Color(0xFF475569))
                            }
                        }
                    }
                }
            }

            // Section 2: Access Controls & Authentication
            if (searchQuery.isBlank() || "access control authentication acl role staff".contains(searchQuery.trim(), ignoreCase = true)) {
                SecurityAccordionCard(
                    id = "sec-2",
                    step = "2",
                    title = "Access Controls & Authentication",
                    subtitle = "Role-based granular staff isolation",
                    icon = Icons.Default.Shield,
                    isExpanded = expandedSections.contains("sec-2"),
                    onToggle = { toggleSection("sec-2") }
                ) {
                    Text(
                        text = "We enforce strict authorization layers (ACL) across our application. Staff accounts created by Merchants are limited to specific permissions (e.g. kitchen_view, route_update) to prevent unauthorized dashboard exports or profile changes.",
                        fontSize = 12.sp,
                        color = Color(0xFF475569),
                        lineHeight = 17.sp
                    )
                }
            }

            // Section 3: Hosting and Datacenter Security
            if (searchQuery.isBlank() || "hosting datacenter cloud firewall soc 2 ddos".contains(searchQuery.trim(), ignoreCase = true)) {
                SecurityAccordionCard(
                    id = "sec-3",
                    step = "3",
                    title = "Hosting and Datacenter Security",
                    subtitle = "Tier-4 cloud infrastructure resilience",
                    icon = Icons.Default.CloudDone,
                    isExpanded = expandedSections.contains("sec-3"),
                    onToggle = { toggleSection("sec-3") }
                ) {
                    Text(
                        text = "TiffinManager code runs on secured cloud instances equipped with firewalls, DDoS mitigation, and automatic vulnerability scanning systems. Physical datacenters maintain SOC 2 compliance.",
                        fontSize = 12.sp,
                        color = Color(0xFF475569),
                        lineHeight = 17.sp
                    )
                }
            }

            // Section 4: Vulnerability Disclosure
            if (searchQuery.isBlank() || "vulnerability disclosure bounty bug security reporting".contains(searchQuery.trim(), ignoreCase = true)) {
                SecurityAccordionCard(
                    id = "sec-4",
                    step = "4",
                    title = "Vulnerability Disclosure",
                    subtitle = "Responsible bug bounty & reporting",
                    icon = Icons.Default.BugReport,
                    isExpanded = expandedSections.contains("sec-4"),
                    onToggle = { toggleSection("sec-4") }
                ) {
                    Text(
                        text = "We welcome reports from security researchers. If you identify a security issue in our routing, payment callbacks, or file upload systems, please email us directly at security@tiffinmanager.com. We investigate all valid submissions.",
                        fontSize = 12.sp,
                        color = Color(0xFF475569),
                        lineHeight = 17.sp
                    )
                }
            }

            // Section 5: Office and Server Audits
            if (searchQuery.isBlank() || "audits office server reviews posture injection".contains(searchQuery.trim(), ignoreCase = true)) {
                SecurityAccordionCard(
                    id = "sec-5",
                    step = "5",
                    title = "Office and Server Audits",
                    subtitle = "Scheduled continuous posture reviews",
                    icon = Icons.Default.FactCheck,
                    isExpanded = expandedSections.contains("sec-5"),
                    onToggle = { toggleSection("sec-5") }
                ) {
                    Text(
                        text = "Our engineering team at Patna, Bihar, India conducts quarterly code refactoring audits to check for session leaks, database injection vulnerabilities, and server updates.",
                        fontSize = 12.sp,
                        color = Color(0xFF475569),
                        lineHeight = 17.sp
                    )
                }
            }

            // Contact Security Desk Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5F1)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD0C2))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF95721)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Email, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text("Security Operations Desk", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text("Dedicated incident response team", fontSize = 11.sp, color = Color(0xFF64748B))
                        }
                    }

                    Button(
                        onClick = onContactSecurity,
                        modifier = Modifier.fillMaxWidth().height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF95721))
                    ) {
                        Text("Report Vulnerability / Inquiry", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SecurityAccordionCard(
    id: String,
    step: String,
    title: String,
    subtitle: String,
    icon: ImageVector,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFFF5F1))
                            .border(1.dp, Color(0xFFFFD0C2), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(step, color = Color(0xFFF95721), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Column {
                        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                        Text(subtitle, fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                }

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Color(0xFF545F73),
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(if (isExpanded) 180f else 0f)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp, start = 44.dp)) {
                    content()
                }
            }
        }
    }
}
