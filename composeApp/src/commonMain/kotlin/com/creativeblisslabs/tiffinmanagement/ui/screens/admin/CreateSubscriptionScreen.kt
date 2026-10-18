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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.models.SubscriptionModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun CreateSubscriptionScreen(
    subscriptionId: String? = null,
    onNavigateBack: () -> Unit = {}
) {
    val customers by TiffinRepository.customers.collectAsState()
    val subscriptions by TiffinRepository.subscriptions.collectAsState()
    val existingSubscription = remember(subscriptionId, subscriptions) {
        if (subscriptionId != null) subscriptions.find { it.id == subscriptionId } else null
    }

    var isNewCustomerToggle by remember { mutableStateOf(false) }
    var selectedCustomerId by remember(existingSubscription, customers) {
        val matchedCust = customers.find { it.name.equals(existingSubscription?.customerName, ignoreCase = true) }
        mutableStateOf(matchedCust?.id ?: customers.firstOrNull()?.id ?: "")
    }
    var customerDropdownOpen by remember { mutableStateOf(false) }

    var selectedMealPlan by remember(existingSubscription) {
        mutableStateOf(existingSubscription?.packageName ?: "Deluxe Healthy Thali (Veg)")
    }
    var mealPlanDropdownOpen by remember { mutableStateOf(false) }

    var dailyRate by remember(existingSubscription) {
        mutableStateOf(existingSubscription?.dailyRate?.toString() ?: "100.00")
    }
    var weeklyRate by remember(existingSubscription) {
        mutableStateOf(existingSubscription?.weeklyRate?.toString() ?: "700.00")
    }
    var monthlyRate by remember(existingSubscription) {
        mutableStateOf(existingSubscription?.monthlyRate?.toString() ?: "3000.00")
    }

    val daysCount = remember(existingSubscription) {
        mutableStateMapOf(
            "SUN" to (if (existingSubscription?.activeDays?.contains("SUN") != false) 1 else 0),
            "MON" to (if (existingSubscription?.activeDays?.contains("MON") != false) 1 else 0),
            "TUE" to (if (existingSubscription?.activeDays?.contains("TUE") != false) 1 else 0),
            "WED" to (if (existingSubscription?.activeDays?.contains("WED") != false) 1 else 0),
            "THU" to (if (existingSubscription?.activeDays?.contains("THU") != false) 1 else 0),
            "FRI" to (if (existingSubscription?.activeDays?.contains("FRI") != false) 1 else 0),
            "SAT" to (if (existingSubscription?.activeDays?.contains("SAT") != false) 1 else 0)
        )
    }

    var billingPeriod by remember(existingSubscription) {
        val period = when (existingSubscription?.ratePeriod?.lowercase()) {
            "daily" -> "Daily billing"
            "weekly" -> "Weekly billing"
            else -> "Monthly billing"
        }
        mutableStateOf(period)
    }
    var billingDropdownOpen by remember { mutableStateOf(false) }
    var maxSkipsLimit by remember(existingSubscription) {
        mutableStateOf(existingSubscription?.allowedSkips?.toString() ?: "5")
    }
    var startDate by remember(existingSubscription) {
        mutableStateOf(existingSubscription?.startDate ?: "04/09/2026")
    }
    var endDate by remember(existingSubscription) {
        mutableStateOf(existingSubscription?.endDate ?: "31/10/2026")
    }
    var isTrialMeal by remember { mutableStateOf(false) }
    var notesInstructions by remember(existingSubscription) {
        mutableStateOf(existingSubscription?.note ?: "e.g. deliver near door, no lunch onion...")
    }

    var shippingType by remember(existingSubscription) {
        mutableStateOf(if (existingSubscription?.shippingType?.contains("pickup", ignoreCase = true) == true) "pickup" else "home")
    } // home, pickup
    var deliveryInstructions by remember(existingSubscription) {
        mutableStateOf(existingSubscription?.instructions ?: "")
    }
    var paymentStatusSelection by remember(existingSubscription) {
        mutableStateOf(existingSubscription?.paymentStatus ?: "Paid")
    }
    var paymentStatusDropdownOpen by remember { mutableStateOf(false) }

    val liveMealPlans by TiffinRepository.mealPlans.collectAsState()
    val mealPlanOptions = remember(liveMealPlans) {
        liveMealPlans.filter { it.isActive }.map { it.name }.ifEmpty {
            listOf("Deluxe Healthy Thali (Veg)", "Keto High Protein Lunch", "Classic Homestyle Combo")
        }
    }
    val billingPeriodOptions = listOf("Daily billing", "Weekly billing", "Monthly billing")
    val paymentStatusOptions = listOf("Paid", "Unpaid (Zero Wallet Credit)")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
       

        // 2. Customer Selection Section
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(ColorTokens.PrimaryFixed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, tint = ColorTokens.PrimarySpice, modifier = Modifier.size(16.dp))
                        }
                        Text(
                            text = "CUSTOMER SELECTION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.PrimarySpice,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("New Customer?", fontSize = 12.sp, color = ColorTokens.TextSecondary)
                        Switch(
                            checked = isNewCustomerToggle,
                            onCheckedChange = { isNewCustomerToggle = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ColorTokens.PrimarySpice
                            ),
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row {
                        Text("Select Customer", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text(" *", fontSize = 12.sp, color = Color(0xFFF43F5E))
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp))
                                .clickable { customerDropdownOpen = true }
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val activeCust = customers.find { it.id == selectedCustomerId }
                            Text(
                                text = activeCust?.let { "${it.name} (${it.phone})" } ?: "Select Customer",
                                fontSize = 13.sp,
                                color = ColorTokens.OnSurface
                            )
                            Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.TextSecondary, modifier = Modifier.size(18.dp))
                        }
                        DropdownMenu(
                            expanded = customerDropdownOpen,
                            onDismissRequest = { customerDropdownOpen = false }
                        ) {
                            customers.forEach { cust ->
                                DropdownMenuItem(
                                    text = { Text("${cust.name} (${cust.phone})", fontSize = 13.sp) },
                                    onClick = {
                                        selectedCustomerId = cust.id
                                        customerDropdownOpen = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Meal Plans & Pricing Section
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MEAL PLANS & PRICING",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.PrimarySpice,
                        letterSpacing = 0.5.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ColorTokens.PrimaryFixed)
                            .clickable { }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("+ Add Plan", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.PrimarySpice)
                    }
                }

                // Inner Meal Plan Container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ColorTokens.SurfaceContainerLow)
                        .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Meal Plan Select
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row {
                            Text("Meal Plan", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Text(" *", fontSize = 12.sp, color = Color(0xFFF43F5E))
                        }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(8.dp))
                                    .clickable { mealPlanDropdownOpen = true }
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(selectedMealPlan, fontSize = 12.sp, color = ColorTokens.OnSurface)
                                Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.TextSecondary, modifier = Modifier.size(16.dp))
                            }
                            DropdownMenu(
                                expanded = mealPlanDropdownOpen,
                                onDismissRequest = { mealPlanDropdownOpen = false }
                            ) {
                                mealPlanOptions.forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt, fontSize = 12.sp) },
                                        onClick = {
                                            selectedMealPlan = opt
                                            mealPlanDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Rates 3-column
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text("Daily Rate (₹)", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                            OutlinedTextField(
                                value = dailyRate,
                                onValueChange = { dailyRate = it },
                                modifier = Modifier.fillMaxWidth().height(44.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = ColorTokens.Primary,
                                    unfocusedBorderColor = ColorTokens.BorderSubtle
                                ),
                                shape = RoundedCornerShape(8.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                        }

                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text("Weekly Rate", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                            OutlinedTextField(
                                value = weeklyRate,
                                onValueChange = { weeklyRate = it },
                                modifier = Modifier.fillMaxWidth().height(44.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = ColorTokens.Primary,
                                    unfocusedBorderColor = ColorTokens.BorderSubtle
                                ),
                                shape = RoundedCornerShape(8.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                        }

                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text("Monthly Rate", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                            OutlinedTextField(
                                value = monthlyRate,
                                onValueChange = { monthlyRate = it },
                                modifier = Modifier.fillMaxWidth().height(44.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = ColorTokens.Primary,
                                    unfocusedBorderColor = ColorTokens.BorderSubtle
                                ),
                                shape = RoundedCornerShape(8.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                        }
                    }

                    // Meals/Day Stepper for Sun-Sat
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row {
                            Text("Meals/Day", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Text(" *", fontSize = 12.sp, color = Color(0xFFF43F5E))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT").forEach { day ->
                                val count = daysCount[day] ?: 1
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White)
                                        .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(8.dp))
                                        .padding(vertical = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(day, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ColorTokens.TextSecondary)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "-",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.TextSecondary,
                                            modifier = Modifier.clickable {
                                                if (count > 0) daysCount[day] = count - 1
                                            }.padding(horizontal = 2.dp)
                                        )
                                        Text(
                                            text = count.toString(),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnSurface,
                                            modifier = Modifier.padding(horizontal = 2.dp)
                                        )
                                        Text(
                                            text = "+",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.PrimarySpice,
                                            modifier = Modifier.clickable {
                                                daysCount[day] = count + 1
                                            }.padding(horizontal = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Subscription Schedule & Settings
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "SUBSCRIPTION SCHEDULE & SETTINGS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTokens.PrimarySpice,
                    letterSpacing = 0.5.sp
                )

                // Billing Period & Max Skips Limit
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row {
                            Text("Billing Period", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Text(" *", fontSize = 12.sp, color = Color(0xFFF43F5E))
                        }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(8.dp))
                                    .clickable { billingDropdownOpen = true }
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(billingPeriod, fontSize = 12.sp, color = ColorTokens.OnSurface)
                                Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.TextSecondary, modifier = Modifier.size(16.dp))
                            }
                            DropdownMenu(
                                expanded = billingDropdownOpen,
                                onDismissRequest = { billingDropdownOpen = false }
                            ) {
                                billingPeriodOptions.forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt, fontSize = 12.sp) },
                                        onClick = {
                                            billingPeriod = opt
                                            billingDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row {
                            Text("Max Skips Limit", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Text(" *", fontSize = 12.sp, color = Color(0xFFF43F5E))
                        }
                        OutlinedTextField(
                            value = maxSkipsLimit,
                            onValueChange = { maxSkipsLimit = it },
                            modifier = Modifier.fillMaxWidth().height(44.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ColorTokens.Primary,
                                unfocusedBorderColor = ColorTokens.BorderSubtle
                            ),
                            shape = RoundedCornerShape(8.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                }

                // Start Date & End Date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row {
                            Text("Start Date", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Text(" *", fontSize = 12.sp, color = Color(0xFFF43F5E))
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(startDate, fontSize = 12.sp, color = ColorTokens.OnSurface)
                            Icon(Icons.Default.CalendarMonth, null, tint = ColorTokens.TextSecondary, modifier = Modifier.size(16.dp))
                        }
                    }

                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("End Date", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(endDate, fontSize = 12.sp, color = ColorTokens.OnSurface)
                            Icon(Icons.Default.CalendarMonth, null, tint = ColorTokens.TextSecondary, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                // Mark as Trial Meal Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Checkbox(
                        checked = isTrialMeal,
                        onCheckedChange = { isTrialMeal = it },
                        colors = CheckboxDefaults.colors(checkedColor = ColorTokens.PrimarySpice)
                    )
                    Text("Mark as Trial Meal", fontSize = 12.sp, color = ColorTokens.PrimarySpice, fontWeight = FontWeight.Medium)
                }

                // Notes / Special Instructions
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Notes / Special Instructions", fontSize = 12.sp, color = ColorTokens.TextSecondary)
                    OutlinedTextField(
                        value = notesInstructions,
                        onValueChange = { notesInstructions = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = ColorTokens.BorderSubtle
                        ),
                        shape = RoundedCornerShape(8.dp),
                        minLines = 2
                    )
                }
            }
        }

        // 5. Shipping Details Section
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "SHIPPING DETAILS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTokens.PrimarySpice,
                    letterSpacing = 0.5.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { shippingType = "home" }
                    ) {
                        RadioButton(
                            selected = shippingType == "home",
                            onClick = { shippingType = "home" },
                            colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.PrimarySpice)
                        )
                        Text("Home Delivery", fontSize = 13.sp, color = ColorTokens.OnSurface)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { shippingType = "pickup" }
                    ) {
                        RadioButton(
                            selected = shippingType == "pickup",
                            onClick = { shippingType = "pickup" },
                            colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Secondary)
                        )
                        Text("Self Pick Up", fontSize = 13.sp, color = ColorTokens.OnSurfaceVariant)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Delivery Instructions", fontSize = 12.sp, color = ColorTokens.TextSecondary)
                    OutlinedTextField(
                        value = deliveryInstructions,
                        onValueChange = { deliveryInstructions = it },
                        placeholder = { Text("Instructions for the delivery driver...", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = ColorTokens.BorderSubtle
                        ),
                        shape = RoundedCornerShape(8.dp),
                        minLines = 2
                    )
                }
            }
        }

        // 6. Invoice Billing & Initial Payment Section
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "INVOICE BILLING & INITIAL PAYMENT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTokens.PrimarySpice,
                    letterSpacing = 0.5.sp
                )

                // Calculated Sumup Rate Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ColorTokens.SurfaceContainerLow)
                        .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("CALCULATED SUMUP RATE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.TextSecondary)
                        Text("₹${monthlyRate}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .border(1.dp, ColorTokens.PrimaryFixed, RoundedCornerShape(100.dp))
                            .background(ColorTokens.PrimaryFixed.copy(alpha = 0.3f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("Monthly Cycle", fontSize = 11.sp, color = ColorTokens.PrimarySpice, fontWeight = FontWeight.Bold)
                    }
                }

                // Initial Payment Status Dropdown
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row {
                        Text("Initial Payment Status", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text(" *", fontSize = 12.sp, color = Color(0xFFF43F5E))
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(8.dp))
                                .clickable { paymentStatusDropdownOpen = true }
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(paymentStatusSelection, fontSize = 12.sp, color = ColorTokens.OnSurface)
                            Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.TextSecondary, modifier = Modifier.size(16.dp))
                        }
                        DropdownMenu(
                            expanded = paymentStatusDropdownOpen,
                            onDismissRequest = { paymentStatusDropdownOpen = false }
                        ) {
                            paymentStatusOptions.forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt, fontSize = 12.sp) },
                                    onClick = {
                                        paymentStatusSelection = opt
                                        paymentStatusDropdownOpen = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // 7. Create/Update Subscription CTA Button
        Button(
            onClick = {
                val cust = customers.find { it.id == selectedCustomerId } ?: customers.firstOrNull()
                val rateVal = monthlyRate.toDoubleOrNull() ?: 3000.0
                if (cust != null) {
                    // Determine isVeg from selected meal plan if available
                    val planIsVeg = liveMealPlans.find { it.name == selectedMealPlan }?.isVeg ?: true
                    val activeDaysList = daysCount.filter { it.value > 0 }.map { it.key }.toList()
                        .ifEmpty { listOf("MON", "TUE", "WED", "THU", "FRI") } // sane default, no duplicates
                    val targetModel = SubscriptionModel(
                        id = existingSubscription?.id ?: "sub-${System.currentTimeMillis()}",
                        subscriptionCode = existingSubscription?.subscriptionCode ?: "SUB-${System.currentTimeMillis() % 100000}",
                        customerId = cust.id, // reliable FK
                        customerName = cust.name,
                        packageName = selectedMealPlan,
                        rate = rateVal,
                        ratePeriod = if (billingPeriod.contains("daily", ignoreCase = true)) "Daily"
                            else if (billingPeriod.contains("weekly", ignoreCase = true)) "Weekly"
                            else "Monthly",
                        isVeg = planIsVeg,
                        startDate = startDate,
                        endDate = endDate,
                        walletBalance = existingSubscription?.walletBalance ?: 0.0, // new subs start at zero wallet
                        allowedSkips = maxSkipsLimit.toIntOrNull() ?: 5,
                        skipsUsed = existingSubscription?.skipsUsed ?: 0,
                        paymentStatus = paymentStatusSelection,
                        isActive = existingSubscription?.isActive ?: true,
                        activeDays = activeDaysList,
                        mealComponents = liveMealPlans.find { it.name == selectedMealPlan }
                            ?.dayWiseItems?.map { it.itemName } ?: emptyList(),
                        dailyRate = dailyRate.toDoubleOrNull() ?: 100.0,
                        weeklyRate = weeklyRate.toDoubleOrNull() ?: 700.0,
                        monthlyRate = rateVal,
                        shippingType = if (shippingType == "home") "Home Delivery" else "Self Pick Up",
                        instructions = deliveryInstructions,
                        note = notesInstructions.let { if (it == "e.g. deliver near door, no lunch onion...") "" else it },
                        vacationHolds = existingSubscription?.vacationHolds ?: emptyList()
                    )
                    if (existingSubscription != null) {
                        TiffinRepository.updateSubscription(targetModel)
                    } else {
                        TiffinRepository.addSubscription(targetModel)
                    }
                    onNavigateBack()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimarySpice),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (existingSubscription != null) "Update Subscription" else "Create Subscription",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}
