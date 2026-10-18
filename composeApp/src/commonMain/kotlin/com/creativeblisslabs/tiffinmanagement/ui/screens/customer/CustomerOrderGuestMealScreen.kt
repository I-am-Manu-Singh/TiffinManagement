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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import kotlinx.coroutines.launch

/**
 * Customer / Subscriber Order Guest Meal Screen
 * Matches stitch_cross_platform_universal_ui_design 11/order_guest_meal
 * Features:
 * - Sub-bar with back button, Title "Add Guest Meal" & Wallet pill (₹1,420.00)
 * - Cutoff Notice Banner: Instant Wallet Auto-Deduction, Lunch locks 08:00 AM, Dinner locks 04:00 PM
 * - Choose Meal Session: Dinner Today (Locks in 1h 20m) vs Lunch Tomorrow (Pre-order window open)
 * - Dish Preview & Banner: Special Dal Makhani & Paneer Kulcha (Jeera Pulao, Gulab Jamun, Mint Chutney)
 * - Quantity Stepper (₹110.00/plate, Stepper - 2 +)
 * - Configure Individual Packs:
 *   - Guest Thali #1: Pure Veg / Jain Friendly / Medium Spicy
 *   - Guest Thali #2: Pure Veg / Jain (No Garlic/Onion) / Mild
 * - Packaging Containers: Standard Insulated Stainless Tiffin (Free) vs Eco Disposable Meal Box (+₹10 / pack)
 * - Delivery Preferences & Notes: Extra disposable cutlery checkbox & Kitchen Note text field
 * - Order Summary & Wallet calculation: Base cost, Free Doorstep Delivery privilege, Remaining After Order
 * - Confirm & Pay button with instant feedback
 */
@Composable
fun CustomerOrderGuestMealScreen(
    onNavigateBack: () -> Unit,
    onOrderSuccess: () -> Unit
) {
    val subscriberProfile by TiffinRepository.subscriberProfile.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedSession by remember { mutableStateOf("Dinner Today") }
    var quantity by remember { mutableStateOf(2) }
    var packagingType by remember { mutableStateOf("tiffin") } // "tiffin" or "disposable"
    var includeCutlery by remember { mutableStateOf(true) }
    var kitchenNote by remember { mutableStateOf("Please keep kulchas extra warm in foil") }

    val baseRate = 110.0
    val packagingSurcharge = if (packagingType == "disposable") (quantity * 10.0) else 0.0
    val totalPayable = (quantity * baseRate) + packagingSurcharge
    val remainingBalance = subscriberProfile.walletBalance - totalPayable

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Sticky Sub-Bar with Back, Title & Balance Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLowest)
                            .clickable { onNavigateBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = ColorTokens.OnSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(
                            text = "Add Guest Meal",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = "Extra plate for family or friends",
                            fontSize = 11.sp,
                            color = ColorTokens.Secondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(ColorTokens.SecondaryContainer)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = ColorTokens.Primary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "₹${subscriberProfile.walletBalance}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSecondaryFixed
                        )
                    }
                }
            }

            // 2. Cutoff Notice Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ColorTokens.PrimaryFixed)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    tint = ColorTokens.Primary,
                    modifier = Modifier.size(20.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Instant Wallet Auto-Deduction",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.Primary
                    )
                    Text(
                        text = "Lunch locks daily at 08:00 AM • Dinner locks at 04:00 PM. Meals are packed hot and dispatched together with your regular delivery.",
                        fontSize = 11.sp,
                        color = ColorTokens.OnPrimaryFixedVariant,
                        lineHeight = 15.sp
                    )
                }
            }

            // 3. Choose Meal Session
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Choose Meal Session",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.Primary)
                        )
                        Text(
                            text = "Live Kitchen",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.Primary
                        )
                    }
                }

                // Session Option 1: Dinner Today
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedSession = "Dinner Today" }
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
                                    .background(ColorTokens.PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Nightlife,
                                    contentDescription = null,
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Dinner Today",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(ColorTokens.SurfaceContainer)
                                            .padding(horizontal = 6.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "04 Sep",
                                            fontSize = 10.sp,
                                            color = ColorTokens.Secondary
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Alarm,
                                        contentDescription = null,
                                        tint = ColorTokens.Error,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = "Locks in 1h 20m",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.Error
                                    )
                                }
                            }
                        }

                        RadioButton(
                            selected = selectedSession == "Dinner Today",
                            onClick = { selectedSession = "Dinner Today" },
                            colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Primary)
                        )
                    }
                }

                // Session Option 2: Lunch Tomorrow
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedSession = "Lunch Tomorrow" }
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
                                    imageVector = Icons.Default.WbSunny,
                                    contentDescription = null,
                                    tint = ColorTokens.Secondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Lunch Tomorrow",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(ColorTokens.SurfaceContainer)
                                            .padding(horizontal = 6.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "05 Sep",
                                            fontSize = 10.sp,
                                            color = ColorTokens.Secondary
                                        )
                                    }
                                }

                                Text(
                                    text = "Pre-order window open",
                                    fontSize = 11.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }

                        RadioButton(
                            selected = selectedSession == "Lunch Tomorrow",
                            onClick = { selectedSession = "Lunch Tomorrow" },
                            colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Primary)
                        )
                    }
                }
            }

            // 4. Dish Preview & Stepper Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Meal Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF5A1C00), Color(0xFF1E0900))
                                )
                            )
                            .padding(12.dp)
                    ) {
                        // Top badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.Tertiary)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.9f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Special Chef Menu",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                            }
                        }

                        // Bottom dish title
                        Column(
                            modifier = Modifier.align(Alignment.BottomStart),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "Special Dal Makhani & Paneer Kulcha",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Accompanied by fragrant Jeera Pulao, Gulab Jamun & Mint Chutney",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    // Stepper row
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                Text(
                                    text = "Quantity of Thalis",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "₹110.00",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.Primary
                                    )
                                    Text(
                                        text = " / plate",
                                        fontSize = 11.sp,
                                        color = ColorTokens.Secondary
                                    )
                                }
                            }

                            // Stepper
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLowest)
                                    .padding(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .clickable { if (quantity > 1) quantity-- },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Decrease",
                                        tint = ColorTokens.OnSurface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Text(
                                    text = "$quantity",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface,
                                    modifier = Modifier.width(24.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ColorTokens.Primary)
                                        .clickable { if (quantity < 10) quantity++ },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Increase",
                                        tint = ColorTokens.OnPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        // Configure Individual Packs
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "CONFIGURE INDIVIDUAL PACKS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurfaceVariant,
                                    letterSpacing = 0.5.sp
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SoupKitchen,
                                        contentDescription = null,
                                        tint = ColorTokens.Tertiary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Custom Made",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.Tertiary
                                    )
                                }
                            }

                            // Pack 1
                            IndividualPackBox(
                                index = 1,
                                sizeLabel = "Regular Plan Size",
                                chips = listOf(
                                    PackChip("Pure Veg", isPrimary = true),
                                    PackChip("Jain Friendly", isPrimary = false),
                                    PackChip("Medium Spicy", isSpice = true)
                                )
                            )

                            // Pack 2
                            if (quantity >= 2) {
                                IndividualPackBox(
                                    index = 2,
                                    sizeLabel = "Special Dietary",
                                    chips = listOf(
                                        PackChip("Pure Veg", isPrimary = false),
                                        PackChip("Jain (No Garlic/Onion)", isPrimary = true),
                                        PackChip("Mild", isMild = true)
                                    )
                                )
                            }
                        }

                        // Packaging Containers
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Packaging Containers",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )

                            // Option 1: Stainless tiffin
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .clickable { packagingType = "tiffin" }
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    RadioButton(
                                        selected = packagingType == "tiffin",
                                        onClick = { packagingType = "tiffin" },
                                        colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Primary)
                                    )
                                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                        Text(
                                            text = "Standard Insulated Stainless Tiffin",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnSurface
                                        )
                                        Text(
                                            text = "Collected during next route collection",
                                            fontSize = 10.sp,
                                            color = ColorTokens.Secondary
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ColorTokens.SurfaceContainerLowest)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Free",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.Tertiary
                                    )
                                }
                            }

                            // Option 2: Eco Disposable
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .clickable { packagingType = "disposable" }
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    RadioButton(
                                        selected = packagingType == "disposable",
                                        onClick = { packagingType = "disposable" },
                                        colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Primary)
                                    )
                                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                        Text(
                                            text = "Eco Disposable Meal Box",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnSurface
                                        )
                                        Text(
                                            text = "100% Bagasse leakproof compartmental tray",
                                            fontSize = 10.sp,
                                            color = ColorTokens.Secondary
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ColorTokens.SurfaceContainerLowest)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "+₹10 / pack",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                }
                            }
                        }
                    }
                }

            
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Delivery Preferences & Notes",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )

                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.clickable { includeCutlery = !includeCutlery }
                        ) {
                            Checkbox(
                                checked = includeCutlery,
                                onCheckedChange = { includeCutlery = it },
                                colors = CheckboxDefaults.colors(checkedColor = ColorTokens.Primary)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                Text(
                                    text = "Include extra disposable wooden cutlery & tissue napkins",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = ColorTokens.OnSurface
                                )
                                Text(
                                    text = "Helps your guests dine without mess dishes",
                                    fontSize = 10.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Kitchen Note",
                                fontSize = 11.sp,
                                color = ColorTokens.Secondary
                            )
                            OutlinedTextField(
                                value = kitchenNote,
                                onValueChange = { kitchenNote = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Special kitchen cooking note") }
                            )
                        }
                    }
                

              
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Order Summary",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Text(
                                text = "Taxes & Surcharges Included",
                                fontSize = 10.sp,
                                color = ColorTokens.Secondary
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Base Meal Cost ($quantity × ₹110.00)",
                                fontSize = 12.sp,
                                color = ColorTokens.Secondary
                            )
                            Text(
                                text = "₹${quantity * baseRate}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Packaging & Delivery Surcharge",
                                fontSize = 12.sp,
                                color = ColorTokens.Secondary
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "₹$packagingSurcharge",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.Tertiary
                                )
                                Text(
                                    text = "₹30",
                                    fontSize = 11.sp,
                                    textDecoration = TextDecoration.LineThrough,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Active Subscription Privilege",
                                fontSize = 12.sp,
                                color = ColorTokens.Secondary
                            )
                            Text(
                                text = "Free Doorstep Delivery",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.Primary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(ColorTokens.SurfaceContainer)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Payable",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Text(
                                text = "₹$totalPayable",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.Primary
                            )
                        }

                        // Wallet calculation bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = ColorTokens.OnSurface,
                                    modifier = Modifier.size(16.dp)
                                )
                                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                    Text(
                                        text = "Prepaid Mess Balance",
                                        fontSize = 10.sp,
                                        color = ColorTokens.Secondary
                                    )
                                    Text(
                                        text = "₹${subscriberProfile.walletBalance}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Remaining After Order",
                                    fontSize = 10.sp,
                                    color = ColorTokens.Secondary
                                )
                                Text(
                                    text = "₹$remainingBalance",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (remainingBalance >= 0) ColorTokens.Tertiary else ColorTokens.Error
                                )
                            }
                        }
                    }
                

                // 7. CTA Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ColorTokens.Primary)
                        .clickable {
                            val success = TiffinRepository.submitGuestMealOrder(
                                sessionTitle = selectedSession,
                                sessionDate = if (selectedSession.contains("Today")) "04 Sep 2026" else "05 Sep 2026",
                                quantity = quantity,
                                packagingType = packagingType,
                                notes = kitchenNote
                            )
                            if (success) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Guest meal confirmed! Transmitted to kitchen chef desk.")
                                }
                                onOrderSuccess()
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Insufficient wallet balance. Please top-up to proceed.")
                                }
                            }
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = ColorTokens.OnPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Confirm & Pay ₹$totalPayable from Wallet",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnPrimary
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = ColorTokens.Tertiary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Instant notification will be sent to the kitchen chef desk",
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

private data class PackChip(
    val label: String,
    val isPrimary: Boolean = false,
    val isSpice: Boolean = false,
    val isMild: Boolean = false
)

@Composable
private fun IndividualPackBox(
    index: Int,
    sizeLabel: String,
    chips: List<PackChip>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(ColorTokens.SurfaceContainerLow)
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
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(ColorTokens.SecondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$index",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSecondaryFixed
                    )
                }
                Text(
                    text = "Guest Thali #$index",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTokens.OnSurface
                )
            }
            Text(
                text = sizeLabel,
                fontSize = 10.sp,
                color = ColorTokens.Secondary
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            chips.forEach { chip ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (chip.isPrimary) ColorTokens.TertiaryFixed
                            else ColorTokens.SurfaceContainerLowest
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (chip.isSpice) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = ColorTokens.Primary,
                                modifier = Modifier.size(12.dp)
                            )
                        } else if (chip.isMild) {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = null,
                                tint = ColorTokens.Tertiary,
                                modifier = Modifier.size(12.dp)
                            )
                        } else if (chip.isPrimary) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.Tertiary)
                            )
                        }
                        Text(
                            text = chip.label,
                            fontSize = 10.sp,
                            fontWeight = if (chip.isPrimary) FontWeight.Bold else FontWeight.Medium,
                            color = if (chip.isPrimary) ColorTokens.OnTertiaryFixed else ColorTokens.OnSurface
                        )
                    }
                }
            }
        }
    }
}
