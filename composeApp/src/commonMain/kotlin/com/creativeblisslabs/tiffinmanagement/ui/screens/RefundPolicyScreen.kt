package com.creativeblisslabs.tiffinmanagement.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.generated.resources.Res
import com.creativeblisslabs.tiffinmanagement.generated.resources.app_logo

@Composable
fun RefundPolicyScreen(
    onNavigateBack: () -> Unit,
    onContactBilling: () -> Unit = {},
    onViewCancellation: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var searchQuery by remember { mutableStateOf("") }
    var expandedSections by remember { mutableStateOf(setOf("1", "2")) }

    fun toggleSection(step: String) {
        expandedSections = if (expandedSections.contains(step)) {
            expandedSections - step
        } else {
            expandedSections + step
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
                    text = "Refund Policy",
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
                        Text("Effective: Sep 2026", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF586377))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color(0xFFE6E8EC))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("ISO Billing", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5B4139))
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color(0xFF6FFBBE))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Fast Reversal", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF002113))
                        }
                    }
                }

                Text(
                    text = "Refund Policy & Wallet Reversals",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191C1F)
                )

                Text(
                    text = "At Tiffin Manager, we provide transparent and automated billing. This Refund Policy describes the terms regarding subscription pauses, unused credits, and wallet adjustments.",
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
                        Icon(Icons.Default.Payments, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text(
                            text = "Automated Unused Meal Rollover",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Credits from paused or skipped meals automatically credit back to your customer wallet.",
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
                placeholder = { Text("Filter refund terms (e.g. trial, duplicate, upgrade, tax)...", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFAB2F00),
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                )
            )

            // Section 1: Try Before You Pay
            if (searchQuery.isBlank() || "trial test evaluation demo free 1".contains(searchQuery.trim(), ignoreCase = true)) {
                RefundSectionCard(
                    step = "1",
                    title = "Try Before You Pay",
                    description = "We believe in full transparency. TiffinManager offers a free trial period (configured dynamically by plan tier, with no credit card required) for all new merchants. During the trial period, you can configure your kitchen, import customers, create menus, and test delivery tracking completely free of charge. We highly encourage evaluating our platform thoroughly during this trial period before making a payment decision.",
                    isExpanded = expandedSections.contains("1"),
                    onToggle = { toggleSection("1") }
                )
            }

            // Section 2: No Refund Policy (Strict Highlight Alert)
            if (searchQuery.isBlank() || "strict final non-refundable final payments no refund 2".contains(searchQuery.trim(), ignoreCase = true)) {
                val isExp = expandedSections.contains("2")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFFFFF7ED), Color(0xFFFFEDD5).copy(alpha = 0.6f))
                                ),
                                RoundedCornerShape(16.dp)
                            )
                            .border(1.dp, Color(0xFFFED7AA), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { toggleSection("2") },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFF95721)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("2", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text("No Refund Policy", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFFFFEDD5))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("STRICT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC2410C))
                                        }
                                    }
                                }

                                Icon(
                                    imageVector = Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = Color(0xFF545F73),
                                    modifier = Modifier
                                        .size(22.dp)
                                        .rotate(if (isExp) 180f else 0f)
                                )
                            }

                            AnimatedVisibility(visible = isExp) {
                                Column(
                                    modifier = Modifier.padding(top = 12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Once a payment has been processed (whether for a monthly billing cycle, upgrade, or annual commitment), all payments are final and non-refundable. This is because:",
                                        fontSize = 12.sp,
                                        color = Color(0xFF334155),
                                        lineHeight = 17.sp
                                    )

                                    // Bullet Points
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.White.copy(alpha = 0.9f))
                                            .border(1.dp, Color(0xFFFFEDD5), RoundedCornerShape(12.dp))
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        BulletPoint(text = "Merchants are given free trial access to verify the software's suitability prior to making any payment.")
                                        BulletPoint(text = "Resource allocations, database partitions, and API integrations are initiated immediately upon subscription activation.")
                                        BulletPoint(text = "We incur upfront infrastructure and payment processing costs.")
                                    }

                                    // Info callout
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFFFE4D6))
                                            .padding(10.dp)
                                    ) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Info, null, tint = Color(0xFFEA580C), modifier = Modifier.size(16.dp))
                                            Text(
                                                text = "We do not offer prorated refunds for mid-month cancellations or unused portions of any active billing cycle.",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color(0xFF9A3412),
                                                lineHeight = 15.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 3: Exceptions & Billing Discrepancies
            if (searchQuery.isBlank() || "exceptions billing discrepancies duplicate charges error 3".contains(searchQuery.trim(), ignoreCase = true)) {
                val isExp3 = expandedSections.contains("3")
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
                                .clickable { toggleSection("3") },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFFFF5F1))
                                        .border(1.dp, Color(0xFFFFD0C2), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("3", color = Color(0xFFF95721), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Text("Exceptions & Billing Discrepancies", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            }

                            Icon(
                                imageVector = Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = Color(0xFF545F73),
                                modifier = Modifier
                                    .size(22.dp)
                                    .rotate(if (isExp3) 180f else 0f)
                            )
                        }

                        AnimatedVisibility(visible = isExp3) {
                            Column(modifier = Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = "We want to ensure fair billing. We will investigate and issue refunds to the original payment source within 5 to 7 business days under the following circumstances:",
                                    fontSize = 12.sp,
                                    color = Color(0xFF475569),
                                    lineHeight = 17.sp
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    DiscrepancyItem(
                                        title = "Duplicate Charges",
                                        desc = "If you were accidentally charged twice for the same billing transaction."
                                    )
                                    DiscrepancyItem(
                                        title = "Billing Error",
                                        desc = "If we charged you an incorrect amount that deviates from your selected plan or invoice."
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFFEF3C7).copy(alpha = 0.6f))
                                        .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(10.dp))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = "Discrepancies must be reported to billing@tiffinmanager.com within 30 days of the transaction date. If confirmed, the refund will be processed back to the original payment method.",
                                        fontSize = 11.sp,
                                        color = Color(0xFF78350F),
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section 4: Deductions and Exclusions
            if (searchQuery.isBlank() || "deductions exclusions gst vat tax 4".contains(searchQuery.trim(), ignoreCase = true)) {
                RefundSectionCard(
                    step = "4",
                    title = "Deductions and Exclusions",
                    description = "If a refund is authorized due to billing discrepancies, please note that third-party payment gateway transaction fees and government taxes (GST/VAT) collected on behalf of the tax authorities are non-refundable and will be deducted from the final refund amount.",
                    isExpanded = expandedSections.contains("4"),
                    onToggle = { toggleSection("4") }
                )
            }

            // Section 5: Downgrades and Plan Upgrades
            if (searchQuery.isBlank() || "downgrades plan upgrades cycle prorated 5".contains(searchQuery.trim(), ignoreCase = true)) {
                val isExp5 = expandedSections.contains("5")
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
                                .clickable { toggleSection("5") },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFFFF5F1))
                                        .border(1.dp, Color(0xFFFFD0C2), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("5", color = Color(0xFFF95721), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Text("Downgrades and Plan Upgrades", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            }

                            Icon(
                                imageVector = Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = Color(0xFF545F73),
                                modifier = Modifier
                                    .size(22.dp)
                                    .rotate(if (isExp5) 180f else 0f)
                            )
                        }

                        AnimatedVisibility(visible = isExp5) {
                            Column(modifier = Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("If you change your plan:", fontSize = 12.sp, color = Color(0xFF64748B))

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFF95721)), RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                                        .padding(start = 8.dp, top = 2.dp, bottom = 4.dp)
                                ) {
                                    Text("Upgrades:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                    Text("You will be charged the prorated difference for the current billing cycle and upgraded instantly.", fontSize = 11.sp, color = Color(0xFF475569))
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFCBD5E1)), RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                                        .padding(start = 8.dp, top = 2.dp, bottom = 4.dp)
                                ) {
                                    Text("Downgrades:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                    Text("Downgrades take effect starting with your next billing cycle. We do not provide prorated refunds for the remaining duration of your current higher-tier cycle.", fontSize = 11.sp, color = Color(0xFF475569))
                                }
                            }
                        }
                    }
                }
            }

            // Section 6: Contact Us
            if (searchQuery.isBlank() || "contact support email billing 6".contains(searchQuery.trim(), ignoreCase = true)) {
                RefundSectionCard(
                    step = "6",
                    title = "Contact Us",
                    description = "For questions or assistance regarding subscriptions or billing details, please contact us at support@tiffinmanager.com.",
                    isExpanded = expandedSections.contains("6"),
                    onToggle = { toggleSection("6") }
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }

        // Fixed Bottom Actions
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
                    onClick = onContactBilling,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF95721))
                ) {
                    Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Contact Billing Support", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                OutlinedButton(
                    onClick = onViewCancellation,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF334155))
                ) {
                    Text("View Cancellation Policy", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun RefundSectionCard(
    step: String,
    title: String,
    description: String,
    isExpanded: Boolean = true,
    onToggle: () -> Unit = {}
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
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFFF5F1))
                            .border(1.dp, Color(0xFFFFD0C2), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(step, color = Color(0xFFF95721), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
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
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(description, fontSize = 12.sp, color = Color(0xFF475569), lineHeight = 17.sp)
                }
            }
        }
    }
}

@Composable
private fun BulletPoint(text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(Color(0xFFF95721))
        )
        Text(text, fontSize = 12.sp, color = Color(0xFF334155), lineHeight = 17.sp)
    }
}

@Composable
private fun DiscrepancyItem(title: String, desc: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFFF95721), modifier = Modifier.size(15.dp))
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        }
        Text(desc, fontSize = 11.sp, color = Color(0xFF64748B), modifier = Modifier.padding(start = 21.dp, top = 2.dp))
    }
}
