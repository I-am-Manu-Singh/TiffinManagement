package com.creativeblisslabs.tiffinmanagement.ui.screens.customer

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinCustomerHeader
import kotlinx.coroutines.launch

/**
 * Customer / Subscriber Wallet & Invoices Screen
 * Matches stitch_cross_platform_universal_ui_design 11/wallet_invoices
 * Features:
 * - Live Prepaid Balance Card (Warm gradient, available balance ₹1,420.00, health bar 14 days remaining, ₹330 overdraft buffer, Top-up CTA)
 * - Quick Refill Suggestions horizontal scroll: ₹500 (5 Meals Buffer), ₹1,500 (15 Days Refill), ₹3,000 (Popular 1 Month Renewal with 5% cashback)
 * - Zero Fee UPI Auto-Confirm banner
 * - Wallet Ledger Section with filter tabs (All, Debits, Credits)
 * - Real item transactions (Lunch Thali Delivered, Dinner Thali Delivered, Vacation Hold Refund, UPI Recharge)
 * - Tax Invoices & Statements (August 2026 ₹3,250, July 2026 ₹3,120) with PDF download simulation
 * - Compliance banner: Annapurna Daily Kitchens LLP, FSSAI & GSTIN details
 * - Instant Top-Up dialog
 */
@Composable
fun CustomerWalletInvoicesScreen(
    onNavigateToProfile: () -> Unit
) {
    val subscriberProfile by TiffinRepository.subscriberProfile.collectAsState()
    val transactions by TiffinRepository.subscriberWalletTransactions.collectAsState()
    val currentBranch by TiffinRepository.currentBranch.collectAsState()
    val userProfile by TiffinRepository.userProfile.collectAsState()
    val kitchenName = currentBranch.name.ifBlank { "Your Kitchen" }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedFilter by remember { mutableStateOf("all") } // "all", "debit", "credit"
    var showTopUpDialog by remember { mutableStateOf(false) }
    var topUpAmountText by remember { mutableStateOf("1500") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Screen Header
            TiffinCustomerHeader(
                title = "Wallet",
                isActive = true,
                subtitle = "TIFFIN MANAGER",
                onNotificationClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Prepaid escrow balance is protected & reconciled daily.")
                    }
                },
                onProfileClick = onNavigateToProfile
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Live Prepaid Balance Card (Culinary Gradient)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFFAB2F00), Color(0xFFD53E02), Color(0xFF862300))
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Top Row: Live tag + Escrow badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.Black.copy(alpha = 0.25f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(ColorTokens.TertiaryFixed)
                                        )
                                        Text(
                                            text = "Live Prepaid Wallet",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VerifiedUser,
                                        contentDescription = null,
                                        tint = ColorTokens.PrimaryFixed,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text(
                                        text = "Escrow Protected",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = ColorTokens.PrimaryFixed
                                    )
                                }
                            }

                            // Available Balance
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "AVAILABLE BALANCE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.PrimaryFixed.copy(alpha = 0.9f),
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "₹${subscriberProfile.walletBalance}",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            // Health Bar & Forecast
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Black.copy(alpha = 0.22f))
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Eco,
                                            contentDescription = null,
                                            tint = ColorTokens.TertiaryFixed,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "~14 Days remaining",
                                            fontSize = 11.sp,
                                            color = Color.White
                                        )
                                    }
                                    Text(
                                        text = "Based on 2 meals/day",
                                        fontSize = 11.sp,
                                        color = ColorTokens.PrimaryFixed
                                    )
                                }

                                LinearProgressIndicator(
                                    progress = { 0.68f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = ColorTokens.TertiaryFixed,
                                    trackColor = Color.White.copy(alpha = 0.25f)
                                )
                            }

                            // Overdraft Buffer & Top-up CTA
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
                                        imageVector = Icons.Default.HealthAndSafety,
                                        contentDescription = null,
                                        tint = ColorTokens.PrimaryFixed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                        Text(
                                            text = "₹${subscriberProfile.overdraftBuffer} buffer allowed",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "No food stopped on zero",
                                            fontSize = 9.sp,
                                            color = ColorTokens.PrimaryFixed.copy(alpha = 0.8f)
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(ColorTokens.SurfaceContainerLowest)
                                        .clickable { showTopUpDialog = true }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Bolt,
                                            contentDescription = null,
                                            tint = ColorTokens.Primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "Top-up / Renew",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.Primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. Quick Refill Packs Horizontal Scroller
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quick Top-up Packs",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = null,
                                tint = ColorTokens.Tertiary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Instant activation",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ColorTokens.Tertiary
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Pack 1: ₹500
                        QuickTopUpPackCard(
                            amount = "₹500",
                            title = "5 Meals Buffer",
                            subtitle = "Emergency refill for busy workweeks",
                            icon = Icons.Default.LunchDining,
                            isPopular = false,
                            onSelect = {
                                topUpAmountText = "500"
                                showTopUpDialog = true
                            }
                        )

                        // Pack 2: ₹1,500
                        QuickTopUpPackCard(
                            amount = "₹1,500",
                            title = "15 Days Refill",
                            subtitle = "Covers lunch + dinner through mid-month",
                            icon = Icons.Default.CalendarToday,
                            isPopular = false,
                            onSelect = {
                                topUpAmountText = "1500"
                                showTopUpDialog = true
                            }
                        )

                        // Pack 3: ₹3,000 (Popular)
                        QuickTopUpPackCard(
                            amount = "₹3,000",
                            title = "Full 1 Month Renewal",
                            subtitle = "Includes 5% cashback (+₹150 credit)",
                            icon = Icons.Default.Stars,
                            isPopular = true,
                            onSelect = {
                                topUpAmountText = "3000"
                                showTopUpDialog = true
                            }
                        )
                    }

                    // Payment channels banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = ColorTokens.Tertiary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "UPI Auto-Confirm: GPay • PhonePe • Paytm",
                                fontSize = 11.sp,
                                color = ColorTokens.Secondary
                            )
                        }
                        Text(
                            text = "Zero Fee",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurfaceVariant
                        )
                    }
                }

                // 3. Wallet Ledger Section with Filter Tabs
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Wallet Ledger",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Cryptographically secure",
                                tint = ColorTokens.Tertiary,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Text(
                            text = "Updated 12m ago",
                            fontSize = 11.sp,
                            color = ColorTokens.Secondary
                        )
                    }

                    // Filter tabs row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(
                            "all" to "All",
                            "debit" to "Debits (Meals)",
                            "credit" to "Credits (Holds/UPI)"
                        ).forEach { (filterKey, label) ->
                            val isSelected = selectedFilter == filterKey
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (isSelected) ColorTokens.SurfaceContainerLowest else Color.Transparent
                                    )
                                    .clickable { selectedFilter = filterKey }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) ColorTokens.Primary else ColorTokens.Secondary
                                )
                            }
                        }
                    }

                    // Ledger items
                    val filteredList = when (selectedFilter) {
                        "debit" -> transactions.filter { !it.isCredit }
                        "credit" -> transactions.filter { it.isCredit }
                        else -> transactions
                    }

                    filteredList.forEach { tx ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (tx.isCredit) ColorTokens.TertiaryFixed else ColorTokens.SecondaryContainer
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (tx.isCredit) Icons.Default.PauseCircle else Icons.Default.Restaurant,
                                            contentDescription = null,
                                            tint = if (tx.isCredit) ColorTokens.OnTertiaryFixed else ColorTokens.OnSecondaryContainer,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            if (!tx.isCredit) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(6.dp)
                                                        .clip(CircleShape)
                                                        .background(ColorTokens.Tertiary)
                                                )
                                            }
                                            Text(
                                                text = tx.title,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ColorTokens.OnSurface,
                                                maxLines = 1
                                            )
                                        }

                                        Text(
                                            text = "${tx.timestamp} • ${tx.subtext}",
                                            fontSize = 10.sp,
                                            color = ColorTokens.Secondary,
                                            maxLines = 1
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = (if (tx.isCredit) "+₹" else "-₹") + tx.amount,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (tx.isCredit) ColorTokens.Tertiary else ColorTokens.OnSurface
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (tx.isCredit) ColorTokens.TertiaryFixed.copy(alpha = 0.5f)
                                                else ColorTokens.SurfaceContainer
                                            )
                                            .padding(horizontal = 6.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = tx.tag,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (tx.isCredit) ColorTokens.OnTertiaryFixed else ColorTokens.Secondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. Tax Invoices & Statements
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(
                            text = "Tax Invoices & Statements",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = "Download monthly breakdowns for employer meal reimbursement",
                            fontSize = 11.sp,
                            color = ColorTokens.Secondary
                        )
                    }

                    // August 2026 Statement
                    InvoiceDownloadCard(
                        title = "August 2026 Statement",
                        subtitle = "₹3,250.00 • 50 Meals consumed",
                        onDownload = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Downloading August 2026 Statement PDF...")
                            }
                        }
                    )

                    // July 2026 Statement
                    InvoiceDownloadCard(
                        title = "July 2026 Statement",
                        subtitle = "₹3,120.00 • 48 Meals consumed",
                        onDownload = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Downloading July 2026 Statement PDF...")
                            }
                        }
                    )
                }

                // 5. Compliance & Regulatory Footer Card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLow),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = ColorTokens.Tertiary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (currentBranch.fssaiLicenseNumber.isNotBlank())
                                    "$kitchenName • FSSAI Lic #${currentBranch.fssaiLicenseNumber}"
                                else kitchenName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                        }
                        val gstinText = if (userProfile.businessGstin.isNotBlank()) "GSTIN: ${userProfile.businessGstin} • " else ""
                        Text(
                            text = "${gstinText}5% GST inclusive on prepaid catering meal vouchers. All meal holds credited automatically based on cutoff time (4 hours prior).",
                            fontSize = 10.sp,
                            color = ColorTokens.Secondary,
                            lineHeight = 14.sp
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

        // Instant Top-Up Dialog
        if (showTopUpDialog) {
            AlertDialog(
                onDismissRequest = { showTopUpDialog = false },
                title = {
                    Text(
                        text = "Instant Wallet Top-up",
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Enter amount or confirm selected pack:",
                            fontSize = 12.sp,
                            color = ColorTokens.Secondary
                        )
                        OutlinedTextField(
                            value = topUpAmountText,
                            onValueChange = { topUpAmountText = it },
                            prefix = { Text("₹", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Instant refund guarantee on canceled schedules. Escrow protected by Razorpay & UPI Auto-Pay.",
                            fontSize = 10.sp,
                            color = ColorTokens.Secondary,
                            lineHeight = 14.sp
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val amt = topUpAmountText.toDoubleOrNull() ?: 1500.0
                            TiffinRepository.topUpWallet(amt)
                            showTopUpDialog = false
                            scope.launch {
                                snackbarHostState.showSnackbar("Recharge of ₹$amt successful! Wallet balance updated.")
                            }
                        }
                    ) {
                        Text("Pay via UPI", color = ColorTokens.Primary, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTopUpDialog = false }) {
                        Text("Cancel", color = ColorTokens.Secondary)
                    }
                }
            )
        }
    }
}

@Composable
private fun QuickTopUpPackCard(
    amount: String,
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPopular: Boolean,
    onSelect: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPopular) ColorTokens.PrimaryFixed else ColorTokens.SurfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .width(170.dp)
            .clickable { onSelect() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = amount,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPopular) ColorTokens.OnPrimaryFixed else ColorTokens.OnSurface
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isPopular) ColorTokens.Primary else ColorTokens.Secondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPopular) ColorTokens.OnPrimaryFixedVariant else ColorTokens.Primary
            )

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = if (isPopular) ColorTokens.OnSurfaceVariant else ColorTokens.Secondary,
                lineHeight = 13.sp,
                minLines = 2
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isPopular) ColorTokens.Primary else ColorTokens.SurfaceContainer)
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isPopular) "Claim Cashback" else "Select Pack",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isPopular) ColorTokens.OnPrimary else ColorTokens.OnSurface
                )
            }
        }
    }
}

@Composable
private fun InvoiceDownloadCard(
    title: String,
    subtitle: String,
    onDownload: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ColorTokens.SurfaceContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = ColorTokens.Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = ColorTokens.Secondary
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorTokens.SurfaceContainerLow)
                    .clickable { onDownload() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = ColorTokens.Primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "PDF",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                }
            }
        }
    }
}
