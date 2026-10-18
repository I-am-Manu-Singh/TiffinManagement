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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
fun DataProcessingAddendumScreen(
    onNavigateBack: () -> Unit,
    onAcknowledgeDpa: () -> Unit = {},
    onDownloadPdf: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var isAcknowledged by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var expandedSections by remember { mutableStateOf(setOf("dpa-1")) }

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
            .background(Color(0xFFF9FAFB))
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
                    text = "Data Processing Addendum",
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
                        Text("Version 2.4 - Jul 2026", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF586377))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color(0xFFE6E8EC))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("GDPR/DPDP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5B4139))
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color(0xFF6FFBBE))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("B2B Standard", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF002113))
                        }
                    }
                }

                Text(
                    text = "Data Processing Addendum (DPA)",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191C1F)
                )

                Text(
                    text = "Defines the contractual data processing relationship between your Kitchen (Data Controller) and Tiffin Manager (Data Processor) under data privacy standards.",
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
                            text = "Contractual Controller-to-Processor Terms",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Customer data is processed solely under Merchant instructions for batch cooking and meal deliveries.",
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
                placeholder = { Text("Filter DPA terms (e.g. processor, subprocessor, breach)...", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFAB2F00),
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                )
            )

            // Section 1: Scope and Applicability
            if (searchQuery.isBlank() || "scope applicability controller processor parties".contains(searchQuery.trim(), ignoreCase = true)) {
                DpaAccordionCard(
                    id = "dpa-1",
                    step = "1",
                    title = "Scope and Applicability",
                    icon = Icons.Default.Policy,
                    isExpanded = expandedSections.contains("dpa-1"),
                    onToggle = { toggleSection("dpa-1") }
                ) {
                    Text(
                        text = "This DPA applies to the processing of personal data submitted by Kitchen Merchants or their end-customers through Tiffin Manager. The Merchant acts as Data Controller, and Tiffin Manager acts as Data Processor.",
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563),
                        lineHeight = 17.sp
                    )
                }
            }

            // Section 2: Processor Obligations
            if (searchQuery.isBlank() || "processor obligations instructions confidentiality security audit".contains(searchQuery.trim(), ignoreCase = true)) {
                DpaAccordionCard(
                    id = "dpa-2",
                    step = "2",
                    title = "Processor Obligations",
                    icon = Icons.Default.AssignmentTurnedIn,
                    isExpanded = expandedSections.contains("dpa-2"),
                    onToggle = { toggleSection("dpa-2") }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        DpaBullet("Process Customer Data only on documented instructions from the Merchant.")
                        DpaBullet("Ensure that persons authorized to process Customer Data are committed to confidentiality.")
                        DpaBullet("Implement appropriate technical and organizational measures to ensure security.")
                        DpaBullet("Assist the Merchant in responding to data subjects' requests to exercise their rights.")
                    }
                }
            }

            // Section 3: Sub-processors
            if (searchQuery.isBlank() || "subprocessors cloud hosting payment gateway aws twilio".contains(searchQuery.trim(), ignoreCase = true)) {
                DpaAccordionCard(
                    id = "dpa-3",
                    step = "3",
                    title = "Sub-processors",
                    icon = Icons.Default.Shield,
                    isExpanded = expandedSections.contains("dpa-3"),
                    onToggle = { toggleSection("dpa-3") }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Merchant grants general authorization to Tiffin Manager to engage sub-processors (e.g. cloud hosting, SMS gateways, payment infrastructure) to provide the service. We maintain an up-to-date list of sub-processors upon request.",
                            fontSize = 12.sp,
                            color = Color(0xFF4B5563),
                            lineHeight = 17.sp
                        )
                        DpaBullet("Cloud Infrastructure: Certified SOC-2 / ISO-27001 data center partners.")
                        DpaBullet("Messaging Gateways: WhatsApp Business Cloud & transactional SMS providers.")
                    }
                }
            }

            // Section 4: Data Subject Rights & Deletion
            if (searchQuery.isBlank() || "data subject rights deletion erasure export exit".contains(searchQuery.trim(), ignoreCase = true)) {
                DpaAccordionCard(
                    id = "dpa-4",
                    step = "4",
                    title = "Data Subject Rights & Deletion",
                    icon = Icons.Default.LockReset,
                    isExpanded = expandedSections.contains("dpa-4"),
                    onToggle = { toggleSection("dpa-4") }
                ) {
                    Text(
                        text = "Upon termination of services, Tiffin Manager will, at the choice of the Merchant, delete or return all Customer Data, unless required by applicable law to retain copies.",
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563),
                        lineHeight = 17.sp
                    )
                }
            }

            // Section 5: Security Incident Notification
            if (searchQuery.isBlank() || "security incident notification breach 48 hours alert".contains(searchQuery.trim(), ignoreCase = true)) {
                DpaAccordionCard(
                    id = "dpa-5",
                    step = "5",
                    title = "Security Incident Notification",
                    icon = Icons.Default.NotificationImportant,
                    isExpanded = expandedSections.contains("dpa-5"),
                    onToggle = { toggleSection("dpa-5") }
                ) {
                    Text(
                        text = "In the event of a security incident affecting Merchant or customer data partitions, we will notify the affected Merchant within 48 hours of confirmation, providing detailed reports on the data segments involved.",
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563),
                        lineHeight = 17.sp
                    )
                }
            }

            // Section 6: Governing Jurisdiction
            if (searchQuery.isBlank() || "governing jurisdiction law patna bihar courts".contains(searchQuery.trim(), ignoreCase = true)) {
                DpaAccordionCard(
                    id = "dpa-6",
                    step = "6",
                    title = "Governing Jurisdiction",
                    icon = Icons.Default.Gavel,
                    isExpanded = expandedSections.contains("dpa-6"),
                    onToggle = { toggleSection("dpa-6") }
                ) {
                    Text(
                        text = "This DPA and all associated processing operations are governed by the laws of Patna, Bihar, India. Courts of Patna, Bihar hold exclusive jurisdiction.",
                        fontSize = 12.sp,
                        color = Color(0xFF4B5563),
                        lineHeight = 17.sp
                    )
                }
            }

            // Questions Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Questions about legal matters?", fontSize = 11.sp, color = Color(0xFF6B7280))
                    Text("support@tiffinmanager.com", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF95721))
                }
            }

            Spacer(modifier = Modifier.height(70.dp))
        }

        // Sticky Bottom Actions
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        isAcknowledged = true
                        onAcknowledgeDpa()
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAcknowledged) Color(0xFF10B981) else Color(0xFFF95721)
                    )
                ) {
                    Icon(
                        if (isAcknowledged) Icons.Default.CheckCircle else Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (isAcknowledged) "DPA Acknowledged" else "Acknowledge DPA",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                OutlinedButton(
                    onClick = onDownloadPdf,
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF374151))
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Download Executed DPA (PDF)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun DpaAccordionCard(
    id: String,
    step: String,
    title: String,
    icon: ImageVector,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFFF5F1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(step, color = Color(0xFFF95721), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                }

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Color(0xFF6B7280),
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(if (isExpanded) 180f else 0f)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp, start = 32.dp)) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun DpaBullet(text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(5.dp)
                .clip(CircleShape)
                .background(Color(0xFFF95721))
        )
        Text(text, fontSize = 12.sp, color = Color(0xFF4B5563), lineHeight = 16.sp)
    }
}
