package com.creativeblisslabs.tiffinmanagement.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.generated.resources.Res
import com.creativeblisslabs.tiffinmanagement.generated.resources.app_logo

@Composable
fun TermsOfServiceScreen(
    onNavigateBack: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSection by remember { mutableStateOf("all") }
    var expandedSections by remember { mutableStateOf(setOf("section-1")) }

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
                    text = "Terms of Service",
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
                .padding(horizontal = 16.dp, vertical = 14.dp),
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
                            Text("FSSAI Active", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5B4139))
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color(0xFF6FFBBE))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("Verified Terms", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF002113))
                        }
                    }
                }

                Text(
                    text = "Terms of Service & Mess Charter",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191C1F)
                )

                Text(
                    text = "Clear standards of service for commercial tiffin operations, meal subscription logistics, and customer transparency across kitchen and delivery dispatches.",
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
                        Icon(Icons.Default.Gavel, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text(
                            text = "Transparent SaaS & Culinary Standards",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Transparent operational cutoffs, guaranteed fresh preparation, and fair customer billing.",
                            fontSize = 11.sp,
                            color = Color(0xFFD8DADE)
                        )
                    }
                }
            }

            // Filter Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(Icons.Default.Search, null, tint = Color(0xFF545F73), modifier = Modifier.size(20.dp))
                },
                placeholder = { Text("Filter terms (e.g. cutoff, refund, 08:00 AM, tier)...", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFAB2F00),
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                )
            )

            // Quick Anchor Navigation Horizontal Scroll
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "all" to "All Sections",
                    "section-1" to "1. Scope & Kitchen",
                    "section-2" to "2. Cutoffs & Pauses",
                    "section-3" to "3. Billing & Wallets",
                    "section-4" to "4. Food Safety",
                    "section-5" to "5. Termination",
                    "section-6" to "6. SaaS Tiers",
                    "section-7" to "7. Jurisdiction"
                ).forEach { (key, label) ->
                    val isSelected = selectedSection == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(if (isSelected) Color(0xFFAB2F00) else Color.White)
                            .clickable {
                                selectedSection = key
                                if (key != "all") expandedSections = expandedSections + key
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF191C1F)
                        )
                    }
                }
            }

            // Section 1: Kitchen & Delivery Services
            if ((selectedSection == "all" || selectedSection == "section-1") &&
                (searchQuery.isBlank() || "kitchen delivery scope fssai fresh preparation 01".contains(searchQuery.trim(), ignoreCase = true))) {
                TermsSectionCard(
                    id = "section-1",
                    sectionTag = "SECTION 01",
                    title = "Kitchen & Delivery Services",
                    icon = Icons.Default.Restaurant,
                    iconBg = Color(0xFFD5E0F8),
                    isExpanded = expandedSections.contains("section-1"),
                    onToggle = { toggleSection("section-1") }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Tiffin Manager connects certified cloud kitchen operators with recurring meal subscribers. By subscribing to meal services, you acknowledge that all culinary dispatches comply with local culinary standards and recurring route allocations.",
                            fontSize = 12.sp,
                            color = Color(0xFF5B4139),
                            lineHeight = 18.sp
                        )

                        TermsItemCard(
                            icon = Icons.Default.VerifiedUser,
                            iconColor = Color(0xFF006947),
                            title = "FSSAI Compliance Verification",
                            description = "Each commercial kitchen facility maintains active Food Safety & Standards Authority of India (FSSAI) licensure, with quarterly digital health registry audits."
                        )

                        TermsItemCard(
                            icon = Icons.Default.Schedule,
                            iconColor = Color(0xFFAB2F00),
                            title = "Fresh Preparation Window",
                            description = "Breakfast, Lunch, and Dinner dispatches are sealed in thermal-insulated containers within 45 minutes of stove departure."
                        )
                    }
                }
            }

            // Section 2: Order Cutoffs, Pauses & Cancellations
            if ((selectedSection == "all" || selectedSection == "section-2") &&
                (searchQuery.isBlank() || "order cutoffs pauses cancellations 08:00 vacation holds safety 02".contains(searchQuery.trim(), ignoreCase = true))) {
                TermsSectionCard(
                    id = "section-2",
                    sectionTag = "SECTION 02",
                    title = "Order Cutoffs & Pauses",
                    icon = Icons.Default.AlarmOn,
                    iconBg = Color(0xFFFFDBD1),
                    isExpanded = expandedSections.contains("section-2"),
                    onToggle = { toggleSection("section-2") }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "To maintain strict food procurement efficiency and minimize raw material waste, kitchen dispatches adhere strictly to immutable automation cutoffs.",
                            fontSize = 12.sp,
                            color = Color(0xFF5B4139),
                            lineHeight = 18.sp
                        )

                        // 08:00 AM Daily Rule Callout
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFDBD1).copy(alpha = 0.5f))
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.NotificationImportant, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                                    Text("08:00 AM Daily Rule", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                                }
                                Text(
                                    text = "Morning subscriptions, changes, pauses, and address shifts for that calendar day must be saved before 08:00 AM IST. Post-cutoff modifications will only apply to the next active day’s cycle.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF5B4139),
                                    lineHeight = 17.sp
                                )
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF2F4F8))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text("Vacation Holds", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("Up to 30 consecutive calendar days without losing slot seniority.", fontSize = 11.sp, color = Color(0xFF545F73), lineHeight = 15.sp)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF2F4F8))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text("Safety Threshold", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("A maximum overdraft safety credit limit of ₹330 per active subscriber profile.", fontSize = 11.sp, color = Color(0xFF545F73), lineHeight = 15.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Section 3: Billing & Prepaid Wallets
            if ((selectedSection == "all" || selectedSection == "section-3") &&
                (searchQuery.isBlank() || "billing prepaid wallets upi payment missed credits 03".contains(searchQuery.trim(), ignoreCase = true))) {
                TermsSectionCard(
                    id = "section-3",
                    sectionTag = "SECTION 03",
                    title = "Billing & Prepaid Wallets",
                    icon = Icons.Default.AccountBalanceWallet,
                    iconBg = Color(0xFF6FFBBE),
                    isExpanded = expandedSections.contains("section-3"),
                    onToggle = { toggleSection("section-3") }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Subscriptions operate under a dedicated pre-funded balance model. Wallets are debited per verified delivery receipt confirmation at dispatch point.",
                            fontSize = 12.sp,
                            color = Color(0xFF5B4139),
                            lineHeight = 18.sp
                        )

                        TermsItemCard(
                            icon = Icons.Default.Payments,
                            iconColor = Color(0xFF545F73),
                            title = "Payment Instruments",
                            description = "Supports UPI AutoPay, NetBanking, and RuPay/Visa/MasterCard corporate debit channels."
                        )

                        TermsItemCard(
                            icon = Icons.Default.CurrencyExchange,
                            iconColor = Color(0xFF006947),
                            title = "Missed Dispatch Credits",
                            description = "If a tiffin is undelivered due to kitchen logistical failure, 100% meal cost plus ₹50 convenience wallet credit is auto-reversed within 2 hours."
                        )
                    }
                }
            }

            // Section 4: Food Safety & Allergens
            if ((selectedSection == "all" || selectedSection == "section-4") &&
                (searchQuery.isBlank() || "food safety allergens jain ingredients dietary 04".contains(searchQuery.trim(), ignoreCase = true))) {
                TermsSectionCard(
                    id = "section-4",
                    sectionTag = "SECTION 04",
                    title = "Food Safety & Allergens",
                    icon = Icons.Default.HealthAndSafety,
                    iconBg = Color(0xFFFFDAD6),
                    isExpanded = expandedSections.contains("section-4"),
                    onToggle = { toggleSection("section-4") }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Customers are obligated to specify severe food allergies in their subscriber profile. Kitchen facilities separate Pure Jain / Swaminarayan cooking utensils with dedicated prep tables.",
                            fontSize = 12.sp,
                            color = Color(0xFF5B4139),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Section 5: Termination & Refunds
            if ((selectedSection == "all" || selectedSection == "section-5") &&
                (searchQuery.isBlank() || "termination refunds cancellation notice balance 05".contains(searchQuery.trim(), ignoreCase = true))) {
                TermsSectionCard(
                    id = "section-5",
                    sectionTag = "SECTION 05",
                    title = "Termination & Refunds",
                    icon = Icons.Default.Cancel,
                    iconBg = Color(0xFFECEEF2),
                    isExpanded = expandedSections.contains("section-5"),
                    onToggle = { toggleSection("section-5") }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Either party may cancel subscription packages with 24 hours notice prior to cutoff. Remaining unconsumed prepaid balance is refundable to source bank account within 3 business days.",
                            fontSize = 12.sp,
                            color = Color(0xFF5B4139),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Section 6: SaaS Operational Constraints & Tiers (Folder 10 Specs)
            if ((selectedSection == "all" || selectedSection == "section-6") &&
                (searchQuery.isBlank() || "saas tiers limits starter premium enterprise 06".contains(searchQuery.trim(), ignoreCase = true))) {
                TermsSectionCard(
                    id = "section-6",
                    sectionTag = "SECTION 06",
                    title = "SaaS Operational Constraints & Plan Tiers",
                    icon = Icons.Default.VerifiedUser,
                    iconBg = Color(0xFFFFF5F1),
                    isExpanded = expandedSections.contains("section-6"),
                    onToggle = { toggleSection("section-6") }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Merchants must maintain operations within their designated subscription plan caps:",
                            fontSize = 12.sp,
                            color = Color(0xFF5B4139),
                            lineHeight = 18.sp
                        )

                        TermsItemCard(
                            icon = Icons.Default.Restaurant,
                            iconColor = Color(0xFFAB2F00),
                            title = "Starter Tier Limits",
                            description = "Max 50 active recurring customers, 2 delivery routes, and 2 staff logins."
                        )

                        TermsItemCard(
                            icon = Icons.Default.Schedule,
                            iconColor = Color(0xFF006947),
                            title = "Premium Tier Limits",
                            description = "Max 500 active customers, 10 delivery routes, and 10 staff logins."
                        )

                        TermsItemCard(
                            icon = Icons.Default.AccountBalanceWallet,
                            iconColor = Color(0xFF545F73),
                            title = "Enterprise Tier",
                            description = "Unlimited customer capacity, customized webhooks, and unlimited driver consoles."
                        )
                    }
                }
            }

            // Section 7: Governing Jurisdiction (Patna, Bihar, India)
            if ((selectedSection == "all" || selectedSection == "section-7") &&
                (searchQuery.isBlank() || "governing jurisdiction legal patna bihar india court 07".contains(searchQuery.trim(), ignoreCase = true))) {
                TermsSectionCard(
                    id = "section-7",
                    sectionTag = "SECTION 07",
                    title = "Governing Jurisdiction & Amendments",
                    icon = Icons.Default.Gavel,
                    iconBg = Color(0xFFD5E0F8),
                    isExpanded = expandedSections.contains("section-7"),
                    onToggle = { toggleSection("section-7") }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "These Terms are governed by and construed in accordance with the laws of Patna, Bihar, India. Any dispute arising out of or related to these terms shall be subject to the exclusive jurisdiction of the courts of Patna, Bihar, India. We reserve the right to alter features, update tiers, and change pricing tables with a 30-day notice to active merchants.",
                            fontSize = 12.sp,
                            color = Color(0xFF5B4139),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TermsSectionCard(
    id: String,
    sectionTag: String,
    title: String,
    icon: ImageVector,
    iconBg: Color,
    isExpanded: Boolean,
    onToggle: () -> Unit,
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, null, tint = Color(0xFF191C1F), modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text(sectionTag, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00), letterSpacing = 1.sp)
                        Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                    }
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

@Composable
private fun TermsItemCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF2F4F8))
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(18.dp).padding(top = 1.dp))
            Column {
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                Spacer(modifier = Modifier.height(2.dp))
                Text(description, fontSize = 11.sp, color = Color(0xFF545F73), lineHeight = 16.sp)
            }
        }
    }
}
