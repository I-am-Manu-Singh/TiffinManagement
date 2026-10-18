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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CreditCardOff
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.ElectricMoped
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockPerson
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material.icons.filled.Store
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
fun PrivacyPolicyScreen(
    onNavigateBack: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var expandedSections by remember {
        mutableStateOf(setOf("section-1"))
    }

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
                    text = "Privacy Policy",
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
            // Hero / Meta Banner
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
                            Text("ISO 27001", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5B4139))
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color(0xFF6FFBBE))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("256-Bit", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF002113))
                        }
                    }
                }

                Text(
                    text = "Trust & Privacy at Tiffin Manager",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191C1F)
                )

                Text(
                    text = "Designed for cloud kitchens, meal subscription operators, and daily delivery fleets. Your culinary operations and customer confidentiality are fiercely protected.",
                    fontSize = 13.sp,
                    color = Color(0xFF545F73),
                    lineHeight = 18.sp
                )
            }

            // Zero Ads Graphic Callout Card
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
                            text = "Zero Third-Party Advertising Trackers. Ever.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Customer lists and order telemetry remain 100% private to your kitchen.",
                            fontSize = 11.sp,
                            color = Color(0xFFD8DADE)
                        )
                    }
                }
            }

            // Bento Grid: Privacy at a Glance
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.FactCheck, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                        Text("Privacy at a Glance", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                    }

                    Text(
                        text = "A transparent summary of how our operating system safeguards your kitchen ecosystem.",
                        fontSize = 12.sp,
                        color = Color(0xFF545F73)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Card 1
                        PrivacyBentoCard(
                            title = "What We Collect",
                            description = "Owner credentials, dropsite tags, dietary profiles & wallet logs.",
                            icon = Icons.Default.FolderShared,
                            badgeColor = Color(0xFFFFDBD1),
                            iconColor = Color(0xFFAB2F00),
                            modifier = Modifier.weight(1f)
                        )
                        // Card 2
                        PrivacyBentoCard(
                            title = "How We Use It",
                            description = "Batch cooking prep, 58mm labels & optimized driver routes.",
                            icon = Icons.Default.LocalShipping,
                            badgeColor = Color(0xFFD5E0F8),
                            iconColor = Color(0xFF545F73),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Card 3
                        PrivacyBentoCard(
                            title = "Who Sees It",
                            description = "Strictly authenticated kitchen staff & assigned dispatchers.",
                            icon = Icons.Default.LockPerson,
                            badgeColor = Color(0xFF6FFBBE),
                            iconColor = Color(0xFF002113),
                            modifier = Modifier.weight(1f)
                        )
                        // Card 4
                        PrivacyBentoCard(
                            title = "Your Rights",
                            description = "Instant ledger data exports, profile redactions & erasure.",
                            icon = Icons.Default.ManageAccounts,
                            badgeColor = Color(0xFFFFB59F),
                            iconColor = Color(0xFFAB2F00),
                            modifier = Modifier.weight(1f)
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
                placeholder = { Text("Filter policy clauses (e.g. thermal, UPI, route)...", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFAB2F00),
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                )
            )

            // Deep Dive Accordion Sections
            PolicyAccordionCard(
                id = "section-1",
                sectionNumber = "1. Information We Collect",
                icon = Icons.Default.Badge,
                iconBg = Color(0xFFD5E0F8),
                isExpanded = expandedSections.contains("section-1"),
                onToggle = { toggleSection("section-1") },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "To orchestrate complex multi-shift tiffin logistics, Tiffin Manager collects the minimum necessary operational data:",
                            fontSize = 12.sp,
                            color = Color(0xFF545F73)
                        )
                        PolicyDetailItem(Icons.Default.Store, "Kitchen Operator Credentials", "Owner legal name, verified mobile contact, FSSAI licensing ID, GSTIN, and kitchen billing parameters.")
                        PolicyDetailItem(Icons.Default.NearMe, "Subscriber Drop Points", "Delivery addresses, apartment wing/floor specifics, security gate instructions, and latitude-longitude pin drops.")
                        PolicyDetailItem(Icons.Default.RestaurantMenu, "Dietary Profiles & Allergen Tags", "Pure Jain compliance, diabetic preference, nut/dairy warnings, and spice tolerance notes saved per meal subscription.")
                    }
                }
            )

            PolicyAccordionCard(
                id = "section-2",
                sectionNumber = "2. Dispatch & Label Printing",
                icon = Icons.Default.Print,
                iconBg = Color(0xFFFFDBD1),
                isExpanded = expandedSections.contains("section-2"),
                onToggle = { toggleSection("section-2") },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Customer and meal metadata is processed exclusively to deliver warm, correct food packets on schedule:", fontSize = 12.sp, color = Color(0xFF545F73))
                        PolicyDetailItem(Icons.Default.ReceiptLong, "58mm Thermal Kitchen Stickers", "Customer names, lunchbox meal types (e.g. Standard Veg x1), and dietary tags are parsed to connected Bluetooth/ESC-POS thermal printers for physical lunchbox stickering. We do not print sensitive payment records on outer containers.")
                        PolicyDetailItem(Icons.Default.Chat, "WhatsApp Menu & Emergency SMS", "Customer phone numbers are utilized solely to broadcast daily kitchen rotations (sabzi / roti changes) or critical delay alerts via WhatsApp Cloud Business APIs. No telemarketing lists are rented or sold.")
                    }
                }
            )

            PolicyAccordionCard(
                id = "section-3",
                sectionNumber = "3. Payment & Wallet Security",
                icon = Icons.Default.Payments,
                iconBg = Color(0xFF6FFBBE),
                isExpanded = expandedSections.contains("section-3"),
                onToggle = { toggleSection("section-3") },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Tiffin Manager uses a zero-plain-text card architecture. All monetary transactions comply with regulatory payment guidelines:", fontSize = 12.sp, color = Color(0xFF545F73))
                        PolicyDetailItem(Icons.Default.AccountBalance, "UPI Deep-Linking", "Immediate settlement via NPCI-approved apps with no credential caching.")
                        PolicyDetailItem(Icons.Default.CreditCardOff, "PCI-DSS Compliant", "Card tokens managed exclusively by certified gateway processors.")
                        PolicyDetailItem(Icons.Default.HistoryEdu, "Encrypted Ledger", "Prepaid meal debits tracked using non-repudiation double-entry accounting.")
                    }
                }
            )

            PolicyAccordionCard(
                id = "section-4",
                sectionNumber = "4. Geolocation & Live Tracking",
                icon = Icons.Default.NearMe,
                iconBg = Color(0xFFD5E0F8),
                isExpanded = expandedSections.contains("section-4"),
                onToggle = { toggleSection("section-4") },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Live GPS coordinates are processed exclusively during active delivery shifts (e.g. 11:30 AM – 2:00 PM for lunch rounds):", fontSize = 12.sp, color = Color(0xFF545F73))
                        PolicyDetailItem(Icons.Default.ElectricMoped, "Fleet Worker Transparency", "Delivery riders are informed via background notification when route tracking is actively sharing breadcrumbs with the kitchen dashboard. Tracking terminates automatically upon finishing the dispatch loop.")
                    }
                }
            )

            PolicyAccordionCard(
                id = "section-5",
                sectionNumber = "5. Local Offline-First Cache",
                icon = Icons.Default.OfflinePin,
                iconBg = Color(0xFFECEEF2),
                isExpanded = expandedSections.contains("section-5"),
                onToggle = { toggleSection("section-5") },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Hot kitchens occasionally lose cellular connectivity. We store localized SQLite databases encrypted on your handheld terminal to facilitate uninterrupted delivery logging.", fontSize = 12.sp, color = Color(0xFF545F73))
                        Text("Data synchronizes over TLS 1.3 whenever Internet connectivity restores. Temporary local tokens are invalidated automatically after 48 hours of inactivity.", fontSize = 12.sp, color = Color(0xFF545F73))
                    }
                }
            )

            PolicyAccordionCard(
                id = "section-6",
                sectionNumber = "6. Data Portability & Erasure",
                icon = Icons.Default.DownloadForOffline,
                iconBg = Color(0xFFFFB59F),
                isExpanded = expandedSections.contains("section-6"),
                onToggle = { toggleSection("section-6") },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Kitchen operators have complete sovereign control over their customer and financial registers:", fontSize = 12.sp, color = Color(0xFF545F73))
                        Text("1-Click CSV/Excel exports for accounting audits, and permanent account erasure with complete dropsite wipe available upon verified operator sign-off.", fontSize = 12.sp, color = Color(0xFF545F73))
                    }
                }
            )

            PolicyAccordionCard(
                id = "section-7",
                sectionNumber = "7. Governing Jurisdiction & Legal Contact",
                icon = Icons.Default.Security,
                iconBg = Color(0xFFD5E0F8),
                isExpanded = expandedSections.contains("section-7"),
                onToggle = { toggleSection("section-7") },
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("This Privacy Policy is governed by and interpreted in accordance with the laws of Patna, Bihar, India. Any disputes are subject to the exclusive jurisdiction of the courts located in Patna, Bihar, India.", fontSize = 12.sp, color = Color(0xFF545F73))
                        Text("For privacy inquiries, merchant rights, or data erasure requests, please contact our Legal & Privacy Operations desk at support@tiffinmanager.com.", fontSize = 12.sp, color = Color(0xFF545F73))
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PrivacyBentoCard(
    title: String,
    description: String,
    icon: ImageVector,
    badgeColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF2F4F8))
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(16.dp))
            }
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
            Text(description, fontSize = 11.sp, color = Color(0xFF545F73), lineHeight = 15.sp)
        }
    }
}

@Composable
private fun PolicyAccordionCard(
    id: String,
    sectionNumber: String,
    icon: ImageVector,
    iconBg: Color,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, null, tint = Color(0xFF191C1F), modifier = Modifier.size(18.dp))
                    }
                    Text(
                        text = sectionNumber,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191C1F)
                    )
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
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun PolicyDetailItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF2F4F8))
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(16.dp).padding(top = 1.dp))
            Column {
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                Text(description, fontSize = 11.sp, color = Color(0xFF545F73), lineHeight = 16.sp)
            }
        }
    }
}
