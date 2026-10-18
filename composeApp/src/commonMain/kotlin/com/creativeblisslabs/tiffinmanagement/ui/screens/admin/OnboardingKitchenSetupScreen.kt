package com.creativeblisslabs.tiffinmanagement.ui.screens.admin

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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.UploadFile
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.models.KitchenOnboardingStateModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository

@Composable
fun OnboardingKitchenSetupScreen(
    onNavigateBack: () -> Unit = {},
    onLaunchDashboard: () -> Unit = {},
    onSkip: () -> Unit = {}
) {
    val onboardingState by TiffinRepository.onboardingState.collectAsState()

    var brandName by remember { mutableStateOf(onboardingState.brandName) }
    var breakfastSlot by remember { mutableStateOf(onboardingState.breakfastSlotEnabled) }
    var lunchSlot by remember { mutableStateOf(onboardingState.lunchSlotEnabled) }
    var dinnerSlot by remember { mutableStateOf(onboardingState.dinnerSlotEnabled) }
    var dietaryMode by remember { mutableStateOf(onboardingState.dietarySpecialization) } // 'pure-veg', 'mix', 'jain'

    var showCsvImportDialog by remember { mutableStateOf(false) }
    var csvInputText by remember { mutableStateOf("Rohit Mehra, +91 98231 44552, Phase 1 Hinjewadi, Pune\nAnanya Sen, +91 99882 11223, Viman Nagar, Pune") }
    var importFeedbackMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FD))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Progress Tracker & Step Indicator (Step 3 of 3 • Final Step)
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFDBD1))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.TaskAlt, null, tint = Color(0xFF862300), modifier = Modifier.size(14.dp))
                                Text(
                                    "STEP 3 OF 3 • FINAL STEP",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF862300),
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                        Text("95% complete", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF545F73))
                    }

                    // 3-Segment Progress Bar
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(22.dp)).background(Color(0xFFAB2F00)))
                        Box(modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(22.dp)).background(Color(0xFFAB2F00)))
                        Box(modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(22.dp)).background(Color(0xFFAB2F00)))
                    }
                }

                // Warm Header Framing
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Configure Your Kitchen in Under 2 Minutes",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF191C1F),
                            letterSpacing = (-0.4).sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tell us a bit about your tiffin service to personalize your daily dispatch & batch prep console.",
                            fontSize = 13.sp,
                            color = Color(0xFF5B4139),
                            lineHeight = 18.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFD5E0F8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.RestaurantMenu, contentDescription = null, tint = Color(0xFF1E293B), modifier = Modifier.size(28.dp))
                    }
                }

                // 1. Kitchen Brand Name Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Storefront, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                                Text("Kitchen / Brand Name", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                            }
                            Text("Required", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFAB2F00))
                        }

                        OutlinedTextField(
                            value = brandName,
                            onValueChange = { brandName = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            trailingIcon = {
                                if (brandName.isNotBlank()) {
                                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF006947), modifier = Modifier.size(20.dp))
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color(0xFFF2F4F8),
                                focusedBorderColor = Color(0xFFAB2F00),
                                unfocusedBorderColor = Color.Transparent
                            )
                        )

                        Text(
                            "This name will appear on printed customer meal tags and delivery slips.",
                            fontSize = 11.sp,
                            color = Color(0xFF545F73)
                        )
                    }
                }

                // 2. Daily Meal Offerings
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.RestaurantMenu, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                                Text("Daily Meal Offerings", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                            }
                            Text("Tap to toggle", fontSize = 11.sp, color = Color(0xFF545F73))
                        }

                        // Breakfast Slot
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F8)),
                            modifier = Modifier.fillMaxWidth().clickable { breakfastSlot = !breakfastSlot }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(if (breakfastSlot) Color(0xFFAB2F00) else Color(0xFFE0E3E6)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            if (breakfastSlot) Icons.Default.Check else Icons.Default.Add,
                                            null,
                                            tint = if (breakfastSlot) Color.White else Color(0xFF545F73),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Column {
                                        Text("Breakfast Dispatch", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                        Text("07:00 AM – 09:30 AM", fontSize = 11.sp, color = Color(0xFF545F73))
                                    }
                                }
                                Card(
                                    shape = RoundedCornerShape(22.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD5E0F8))
                                ) {
                                    Text(
                                        "Morning Shift",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF3C475A),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        // Lunch Slot
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F8)),
                            modifier = Modifier.fillMaxWidth().clickable { lunchSlot = !lunchSlot }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(if (lunchSlot) Color(0xFFAB2F00) else Color(0xFFE0E3E6)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            if (lunchSlot) Icons.Default.Check else Icons.Default.Add,
                                            null,
                                            tint = if (lunchSlot) Color.White else Color(0xFF545F73),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Column {
                                        Text("Lunch Service (Peak)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                        Text("12:00 PM – 02:30 PM", fontSize = 11.sp, color = Color(0xFF545F73))
                                    }
                                }
                                Card(
                                    shape = RoundedCornerShape(22.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFDBD1))
                                ) {
                                    Text(
                                        "High Volume",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF862300),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        // Dinner Slot
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F8)),
                            modifier = Modifier.fillMaxWidth().clickable { dinnerSlot = !dinnerSlot }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(if (dinnerSlot) Color(0xFFAB2F00) else Color(0xFFE0E3E6)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            if (dinnerSlot) Icons.Default.Check else Icons.Default.Add,
                                            null,
                                            tint = if (dinnerSlot) Color.White else Color(0xFF545F73),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Column {
                                        Text("Dinner Dispatch", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                        Text("07:00 PM – 09:30 PM", fontSize = 11.sp, color = Color(0xFF545F73))
                                    }
                                }
                                Card(
                                    shape = RoundedCornerShape(22.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD5E0F8))
                                ) {
                                    Text(
                                        "Evening Shift",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF3C475A),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. Primary Dietary Specialization
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Eco, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                                Text("Dietary Specialization", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                            }
                            Text("Select Primary", fontSize = 11.sp, color = Color(0xFF545F73))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Pure Veg
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { dietaryMode = "pure-veg" }
                                    .then(
                                        if (dietaryMode == "pure-veg") Modifier.border(1.5.dp, Color(0xFFAB2F00), RoundedCornerShape(8.dp))
                                        else Modifier
                                    ),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (dietaryMode == "pure-veg") Color.White else Color(0xFFF2F4F8)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(Color.White)
                                            .border(1.dp, Color(0xFF006947), RoundedCornerShape(3.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF006947)))
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Pure Veg", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                                    Text("100% Green", fontSize = 10.sp, color = Color(0xFF545F73))
                                }
                            }

                            // Mix Kitchen
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { dietaryMode = "mix" }
                                    .then(
                                        if (dietaryMode == "mix") Modifier.border(1.5.dp, Color(0xFFAB2F00), RoundedCornerShape(8.dp))
                                        else Modifier
                                    ),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (dietaryMode == "mix") Color.White else Color(0xFFF2F4F8)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(Color.White)
                                                .border(1.dp, Color(0xFF006947), RoundedCornerShape(3.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF006947)))
                                        }
                                        Box(
                                            modifier = Modifier
                                                .size(14.dp)
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(Color.White)
                                                .border(1.dp, Color(0xFFBA1A1A), RoundedCornerShape(3.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFBA1A1A)))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Mix Kitchen", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                                    Text("Veg & Non-Veg", fontSize = 10.sp, color = Color(0xFF545F73))
                                }
                            }

                            // Jain Friendly
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { dietaryMode = "jain" }
                                    .then(
                                        if (dietaryMode == "jain") Modifier.border(1.5.dp, Color(0xFFAB2F00), RoundedCornerShape(8.dp))
                                        else Modifier
                                    ),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (dietaryMode == "jain") Color.White else Color(0xFFF2F4F8)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier.size(16.dp).clip(CircleShape).background(Color(0xFFF5FFF6)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Spa, null, tint = Color(0xFF00855B), modifier = Modifier.size(13.dp))
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Jain Friendly", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                                    Text("No Root/Garlic", fontSize = 10.sp, color = Color(0xFF545F73))
                                }
                            }
                        }
                    }
                }

                // 4. Billing Defaults & Currency
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.AccountBalanceWallet, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                                Text("Billing Defaults & Currency", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                            }
                            Card(
                                shape = RoundedCornerShape(22.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFD5E0F8))
                            ) {
                                Text(
                                    "Auto-Managed",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF3C475A),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F8))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Operational Currency", fontSize = 11.sp, color = Color(0xFF545F73))
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text("₹ INR", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                                        Text("(Rupee)", fontSize = 11.sp, color = Color(0xFF545F73))
                                    }
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F8))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Debit Trust Limit", fontSize = 11.sp, color = Color(0xFF545F73))
                                        Icon(Icons.Default.Info, null, tint = Color(0xFF545F73), modifier = Modifier.size(13.dp))
                                    }
                                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text("₹330", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00))
                                        Text("/ user", fontSize = 11.sp, color = Color(0xFF545F73))
                                    }
                                }
                            }
                        }
                    }
                }

                // Compliance Ribbon
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F8))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.VerifiedUser, null, tint = Color(0xFF006947), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "FSSAI Compliant • Offline-first sync enabled",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF191C1F)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF00855B)))
                    }
                }

                // Sticky Bottom CTA Section
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            TiffinRepository.updateOnboardingState(
                                KitchenOnboardingStateModel(
                                    brandName = brandName,
                                    breakfastSlotEnabled = breakfastSlot,
                                    lunchSlotEnabled = lunchSlot,
                                    dinnerSlotEnabled = dinnerSlot,
                                    dietarySpecialization = dietaryMode,
                                    operationalCurrency = "₹ INR (Rupee)",
                                    debitTrustLimit = 330.00,
                                    isOnboardingComplete = true
                                )
                            )
                            onLaunchDashboard()
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00)),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Launch Kitchen Dashboard", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Icon(Icons.Default.RocketLaunch, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }

                    Row(
                        modifier = Modifier
                            .clickable { showCsvImportDialog = true }
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.UploadFile, null, tint = Color(0xFF545F73), modifier = Modifier.size(16.dp))
                        Text(
                            text = importFeedbackMessage ?: "Import existing customer list (.csv)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (importFeedbackMessage != null) Color(0xFF006947) else Color(0xFF545F73)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        if (showCsvImportDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showCsvImportDialog = false },
                title = { Text("Import Customer List (.csv)", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            "Enter or paste CSV rows (Name, Phone, Address, City):",
                            fontSize = 12.sp,
                            color = Color(0xFF545F73)
                        )
                        OutlinedTextField(
                            value = csvInputText,
                            onValueChange = { csvInputText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp),
                            shape = RoundedCornerShape(8.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 11.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFAB2F00),
                                unfocusedBorderColor = Color(0xFFCBD5E1)
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val lines = csvInputText.lines().filter { it.isNotBlank() }
                            var imported = 0
                            lines.forEach { line ->
                                val parts = line.split(",").map { it.trim() }
                                if (parts.isNotEmpty() && parts[0].isNotBlank()) {
                                    val name = parts[0]
                                    val phone = parts.getOrNull(1) ?: "+91 98000 00000"
                                    val address = parts.getOrNull(2) ?: "Hinjewadi Phase 1"
                                    val city = parts.getOrNull(3) ?: "Pune"
                                    TiffinRepository.addCustomer(
                                        com.example.myapplicationkmp.data.models.CustomerModel(
                                            id = "cust-csv-${System.currentTimeMillis()}-${(100..999).random()}",
                                            name = name,
                                            phone = phone,
                                            email = "${name.lowercase().replace(" ", "")}@gmail.com",
                                            address = "$address, $city",
                                            branchName = "Main Central Kitchen",
                                            deliveryRoute = "Route A",
                                            latitude = "18.5204",
                                            longitude = "73.8567",
                                            isActive = true,
                                            isVeg = true,
                                            walletBalance = 0.0,
                                            dueLimit = 500.0
                                        )
                                    )
                                    imported++
                                }
                            }
                            importFeedbackMessage = "Successfully imported $imported customer(s)!"
                            showCsvImportDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Import Customers", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { showCsvImportDialog = false }) {
                        Text("Cancel", color = Color(0xFF545F73))
                    }
                }
            )
        }
    }
}
