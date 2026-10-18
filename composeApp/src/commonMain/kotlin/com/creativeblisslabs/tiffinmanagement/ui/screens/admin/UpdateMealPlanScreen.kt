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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.creativeblisslabs.tiffinmanagement.data.models.DayWiseMenuItemModel
import com.creativeblisslabs.tiffinmanagement.data.models.MealPlanModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateMealPlanScreen(
    planId: String = "pln-8402",
    onNavigateBack: () -> Unit = {}
) {
    val mealPlans by TiffinRepository.mealPlans.collectAsState()
    val isNewPlan = planId == "new" || planId.isBlank()
    val plan = if (isNewPlan) null else (mealPlans.find { it.id == planId } ?: mealPlans.firstOrNull())

    var planName by remember(plan, isNewPlan) { mutableStateOf(if (isNewPlan) "" else (plan?.name ?: "Standard Thali Plan")) }
    var categoryName by remember(plan, isNewPlan) { mutableStateOf(if (isNewPlan) "Lunch" else (plan?.categoryName ?: "Lunch")) }
    var isCategoryDropdownOpen by remember { mutableStateOf(false) }
    var description by remember(plan, isNewPlan) {
        mutableStateOf(
            if (isNewPlan) "Balanced nutritious meal with fresh rotis and seasonal sabzi"
            else (plan?.description ?: "Balanced nutritious lunch with fresh rotis and seasonal sabzi")
        )
    }

    var dietaryType by remember(plan, isNewPlan) {
        mutableStateOf(
            if (isNewPlan) "veg"
            else if (plan?.isJain == true) "jain"
            else if (plan?.isVeg != false) "veg"
            else "non-veg"
        )
    }
    var dailyRate by remember(plan, isNewPlan) { mutableStateOf(if (isNewPlan) "100.00" else (plan?.dailyRate?.toString() ?: "100.00")) }
    var weeklyRate by remember(plan, isNewPlan) { mutableStateOf(if (isNewPlan) "700.00" else (plan?.weeklyRate?.toString() ?: "700.00")) }
    var monthlyRate by remember(plan, isNewPlan) { mutableStateOf(if (isNewPlan) "3000.00" else (plan?.monthlyRate?.toString() ?: "3000.00")) }
    var maxOrderIncrement by remember(plan, isNewPlan) { mutableStateOf(if (isNewPlan) "5" else (plan?.maxOrderIncrement?.toString() ?: "5")) }

    val daysMap = remember(plan, isNewPlan) {
        mutableStateMapOf(
            "Sun" to (plan?.deliveryDays?.contains("Sun") ?: true),
            "Mon" to (plan?.deliveryDays?.contains("Mon") ?: true),
            "Tue" to (plan?.deliveryDays?.contains("Tue") ?: true),
            "Wed" to (plan?.deliveryDays?.contains("Wed") ?: true),
            "Thu" to (plan?.deliveryDays?.contains("Thu") ?: true),
            "Fri" to (plan?.deliveryDays?.contains("Fri") ?: true),
            "Sat" to (plan?.deliveryDays?.contains("Sat") ?: true)
        )
    }

    var selectedDayTab by remember { mutableStateOf("Monday") }
    val weekDays = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    val dayItems = remember(plan, isNewPlan) {
        androidx.compose.runtime.mutableStateListOf<DayWiseMenuItemModel>().apply {
            if (plan?.dayWiseItems?.isNotEmpty() == true) {
                addAll(plan.dayWiseItems)
            } else {
                weekDays.forEach { d ->
                    add(DayWiseMenuItemModel("item-${d}-1", d, "Chapati / Roti", "4 units", true))
                    add(DayWiseMenuItemModel("item-${d}-2", d, "Paneer Butter Masala", "1 bowl", true))
                    add(DayWiseMenuItemModel("item-${d}-3", d, "Dal Tadka & Steamed Rice", "1 pack", true))
                }
            }
        }
    }

    // Modal Dialog State for Add/Edit Menu Item
    var isItemDialogOpen by remember { mutableStateOf(false) }
    var itemBeingEdited by remember { mutableStateOf<DayWiseMenuItemModel?>(null) }
    var dialogItemName by remember { mutableStateOf("") }
    var dialogItemPortion by remember { mutableStateOf("1 bowl") }
    var dialogItemIsVeg by remember { mutableStateOf(true) }
    var dialogItemError by remember { mutableStateOf<String?>(null) }

    fun openAddItemDialog() {
        itemBeingEdited = null
        dialogItemName = ""
        dialogItemPortion = "1 bowl"
        dialogItemIsVeg = (dietaryType != "non-veg")
        dialogItemError = null
        isItemDialogOpen = true
    }

    fun openEditItemDialog(item: DayWiseMenuItemModel) {
        itemBeingEdited = item
        dialogItemName = item.itemName
        dialogItemPortion = item.portionCount
        dialogItemIsVeg = item.isVeg
        dialogItemError = null
        isItemDialogOpen = true
    }

    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        // Scrollable Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Card 1: Meal Plan Details
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                                    .background(ColorTokens.PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.RestaurantMenu, null, tint = ColorTokens.Primary, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text(
                                    if (isNewPlan) "Create Meal Plan" else "Meal Plan Details",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                                Text("Core configuration & pricing", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(ColorTokens.PrimaryFixed)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                if (isNewPlan) "New Plan" else "Step 1 of 2",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.Primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Meal Category Selector
                    Text("Meal Category *", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp))
                            .clickable { isCategoryDropdownOpen = !isCategoryDropdownOpen }
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(categoryName, fontSize = 13.sp, color = ColorTokens.OnSurface, fontWeight = FontWeight.Medium)
                            Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                        }

                        DropdownMenu(
                            expanded = isCategoryDropdownOpen,
                            onDismissRequest = { isCategoryDropdownOpen = false }
                        ) {
                            listOf("Breakfast", "Lunch", "Dinner", "Full Day Combo (3 Meals)").forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat, fontSize = 13.sp) },
                                    onClick = {
                                        categoryName = cat
                                        isCategoryDropdownOpen = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Plan Name
                    Text("Plan Name *", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = planName,
                        onValueChange = { planName = it },
                        placeholder = { Text("e.g. Standard Thali Plan", fontSize = 13.sp, color = ColorTokens.TextSecondary) },
                        trailingIcon = {
                            if (planName.isNotBlank()) {
                                Icon(Icons.Default.CheckCircle, null, tint = ColorTokens.Tertiary, modifier = Modifier.size(18.dp))
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ColorTokens.SurfaceContainerLow,
                            unfocusedContainerColor = ColorTokens.SurfaceContainerLow,
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = ColorTokens.BorderSubtle
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Description
                    Text("Description", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        minLines = 3,
                        maxLines = 3,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ColorTokens.SurfaceContainerLow,
                            unfocusedContainerColor = ColorTokens.SurfaceContainerLow,
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = ColorTokens.BorderSubtle
                        )
                    )
                    Text(
                        text = "${description.length}/160 chars",
                        fontSize = 10.sp,
                        color = ColorTokens.TextSecondary,
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.End
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dietary Classification
                    Text("Dietary Classification", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pure Veg
                        val isVeg = dietaryType == "veg"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isVeg) Color(0xFFECFDF5) else ColorTokens.SurfaceContainerLow)
                                .border(1.dp, if (isVeg) Color(0xFF006947) else ColorTokens.BorderSubtle, RoundedCornerShape(8.dp))
                                .clickable { dietaryType = "veg" }
                                .padding(horizontal = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF006947)))
                                Text("Pure Veg", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isVeg) Color(0xFF006947) else ColorTokens.Secondary)
                            }
                        }

                        // Non-Veg
                        val isNonVeg = dietaryType == "non-veg"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isNonVeg) Color(0xFFFEF2F2) else ColorTokens.SurfaceContainerLow)
                                .border(1.dp, if (isNonVeg) Color(0xFFDC2626) else ColorTokens.BorderSubtle, RoundedCornerShape(8.dp))
                                .clickable { dietaryType = "non-veg" }
                                .padding(horizontal = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFDC2626)))
                                Text("Non-Veg", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isNonVeg) Color(0xFFDC2626) else ColorTokens.Secondary)
                            }
                        }

                        // Jain Veg
                        val isJain = dietaryType == "jain"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isJain) Color(0xFFF0FDF4) else ColorTokens.SurfaceContainerLow)
                                .border(1.dp, if (isJain) Color(0xFF16A34A) else ColorTokens.BorderSubtle, RoundedCornerShape(8.dp))
                                .clickable { dietaryType = "jain" }
                                .padding(horizontal = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Eco, null, tint = Color(0xFF16A34A), modifier = Modifier.size(13.dp))
                                Text("Jain Veg", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isJain) Color(0xFF16A34A) else ColorTokens.Secondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Subscription Pricing Tiers (3 columns)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Subscription Pricing Tiers", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text("GST included", fontSize = 10.sp, color = ColorTokens.TextSecondary)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Daily
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp))
                                .padding(8.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("DAILY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ColorTokens.TextSecondary)
                                Spacer(modifier = Modifier.height(2.dp))
                                OutlinedTextField(
                                    value = dailyRate,
                                    onValueChange = { dailyRate = it },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ColorTokens.Primary,
                                        unfocusedBorderColor = Color.Transparent
                                    ),
                                    textStyle = androidx.compose.ui.text.TextStyle(
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                )
                                Text("per meal", fontSize = 10.sp, color = ColorTokens.TextSecondary)
                            }
                        }

                        // Weekly
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp))
                                .padding(8.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("WEEKLY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ColorTokens.TextSecondary)
                                Spacer(modifier = Modifier.height(2.dp))
                                OutlinedTextField(
                                    value = weeklyRate,
                                    onValueChange = { weeklyRate = it },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ColorTokens.Primary,
                                        unfocusedBorderColor = Color.Transparent
                                    ),
                                    textStyle = androidx.compose.ui.text.TextStyle(
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.Primary,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                )
                                Text("7 meals", fontSize = 10.sp, color = ColorTokens.TextSecondary)
                            }
                        }

                        // Monthly (SAVE 15%)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp))
                                .padding(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .clip(RoundedCornerShape(bottomStart = 6.dp))
                                    .background(Color(0xFF006947))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("SAVE 15%", fontSize = 7.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("MONTHLY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ColorTokens.TextSecondary)
                                Spacer(modifier = Modifier.height(2.dp))
                                OutlinedTextField(
                                    value = monthlyRate,
                                    onValueChange = { monthlyRate = it },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ColorTokens.Primary,
                                        unfocusedBorderColor = Color.Transparent
                                    ),
                                    textStyle = androidx.compose.ui.text.TextStyle(
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                )
                                Text("30 meals", fontSize = 10.sp, color = ColorTokens.TextSecondary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Max Order Increment per slot
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Max Order Increment per slot", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text("Optional", fontSize = 10.sp, color = ColorTokens.TextSecondary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = maxOrderIncrement,
                        onValueChange = { maxOrderIncrement = it },
                        placeholder = { Text("e.g. 5", fontSize = 12.sp, color = ColorTokens.TextSecondary) },
                        trailingIcon = { Text("tiffins max", fontSize = 11.sp, color = ColorTokens.TextSecondary, modifier = Modifier.padding(end = 12.dp)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ColorTokens.SurfaceContainerLow,
                            unfocusedContainerColor = ColorTokens.SurfaceContainerLow,
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = ColorTokens.BorderSubtle
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Delivery Days Scheduled
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Delivery Days Scheduled", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text(
                            text = "Select All",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.Primary,
                            modifier = Modifier.clickable {
                                daysMap.keys.forEach { daysMap[it] = true }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        daysMap.keys.forEach { dayKey ->
                            val isSelected = daysMap[dayKey] == true
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) ColorTokens.Primary else Color(0xFFF1F5F9))
                                    .clickable { daysMap[dayKey] = !isSelected },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayKey,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else ColorTokens.Secondary
                                )
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 12.sp)
                    }

                    if (successMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(successMessage ?: "", color = Color(0xFF16A34A), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            // Card 2: Day-wise Menu Composition
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.DateRange, null, tint = ColorTokens.Primary, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text("Day-wise Items (Week Long)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text("Configure rotation schedule", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(22.dp))
                                .clickable {
                                    val currentItems = dayItems.filter { it.dayOfWeek == selectedDayTab }
                                    weekDays.filter { it != selectedDayTab }.forEach { otherDay ->
                                        dayItems.removeAll { it.dayOfWeek == otherDay }
                                        currentItems.forEach { item ->
                                            dayItems.add(item.copy(id = "item-${otherDay}-${System.currentTimeMillis()}-${item.id}", dayOfWeek = otherDay))
                                        }
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.ContentCopy, null, tint = ColorTokens.Secondary, modifier = Modifier.size(12.dp))
                                Text("Apply to All Days", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.Secondary)
                            }
                        }
                    }

                    // Horizontal Scrollable Day Tabs
                    androidx.compose.foundation.lazy.LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(weekDays.size) { idx ->
                            val day = weekDays[idx]
                            val isSelected = (day == selectedDayTab)
                            val dayCount = dayItems.count { it.dayOfWeek == day }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(if (isSelected) ColorTokens.Primary else ColorTokens.SurfaceContainerLow)
                                    .border(1.dp, if (isSelected) ColorTokens.Primary else ColorTokens.BorderSubtle, RoundedCornerShape(22.dp))
                                    .clickable { selectedDayTab = day }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "$day ($dayCount)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else ColorTokens.Secondary
                                    )
                                    if (isSelected) {
                                        Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color.White))
                                    }
                                }
                            }
                        }
                    }

                    // Active Day Indicator Banner
                    val currentDayItems = dayItems.filter { it.dayOfWeek == selectedDayTab }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Today, null, tint = ColorTokens.Primary, modifier = Modifier.size(16.dp))
                            Text(
                                "$selectedDayTab $categoryName Menu (${currentDayItems.size} Items)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ColorTokens.OnSurface
                            )
                        }
                        val allVeg = currentDayItems.all { it.isVeg }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (allVeg) Color(0xFFECFDF5) else Color(0xFFFEF2F2))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                if (allVeg) "All Veg" else "Contains Non-Veg",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (allVeg) Color(0xFF047857) else Color(0xFFDC2626)
                            )
                        }
                    }

                    // Menu Items List for Selected Day
                    if (currentDayItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No items added for $selectedDayTab yet.", fontSize = 12.sp, color = ColorTokens.TextSecondary)
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            currentDayItems.forEach { mItem ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(ColorTokens.SurfaceContainerLow)
                                        .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp))
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // 6-dot reorder grip
                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Box(modifier = Modifier.size(2.5.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                                            Box(modifier = Modifier.size(2.5.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Box(modifier = Modifier.size(2.5.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                                            Box(modifier = Modifier.size(2.5.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                                        }
                                    }

                                    // Veg/Non-veg icon badge
                                    val badgeBorderColor = if (mItem.isVeg) Color(0xFF006947) else Color(0xFFDC2626)
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(Color.White)
                                            .border(1.dp, badgeBorderColor, RoundedCornerShape(3.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(badgeBorderColor))
                                    }

                                    // Item Name
                                    Text(
                                        text = mItem.itemName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = ColorTokens.OnSurface,
                                        modifier = Modifier.weight(1f)
                                    )

                                    // Portion Pill
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color.White)
                                            .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(mItem.portionCount, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Secondary)
                                    }

                                    // Edit button
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .clickable { openEditItemDialog(mItem) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Edit, null, tint = ColorTokens.Primary, modifier = Modifier.size(16.dp))
                                    }

                                    // Delete
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .clickable { dayItems.remove(mItem) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Delete, null, tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    // Add Menu Item Button (Dashed style)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .border(1.dp, ColorTokens.Primary, RoundedCornerShape(10.dp))
                            .clickable { openAddItemDialog() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Add, null, tint = ColorTokens.Primary, modifier = Modifier.size(16.dp))
                            Text("Add Menu Item to $selectedDayTab", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                        }
                    }
                }
            }

            // Visual Polish: Kitchen Nutrition Preview Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLow)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLowest),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.RestaurantMenu, null, tint = ColorTokens.Primary, modifier = Modifier.size(24.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Thali Nutritional Preview", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        Text("Est. 680 kcal • 24g Protein • Low Sodium", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF006947), modifier = Modifier.size(12.dp))
                            Text("Compliant with standard lunch tray packaging", fontSize = 10.sp, color = Color(0xFF006947), fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Sticky Bottom Action Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Configured: ${dayItems.size} rotating items across 7 days", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                        Text("Active Schedule", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.Primary)
                    }

                    if (errorMessage != null) {
                        Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 12.sp)
                    }

                    if (successMessage != null) {
                        Text(successMessage ?: "", color = Color(0xFF16A34A), fontSize = 12.sp)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(12.dp))
                                .clickable { onNavigateBack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.Secondary, modifier = Modifier.size(20.dp))
                        }

                        Button(
                            onClick = {
                                errorMessage = null
                                successMessage = null
                                val dRate = dailyRate.toDoubleOrNull() ?: 100.0
                                val wRate = weeklyRate.toDoubleOrNull() ?: 700.0
                                val mRate = monthlyRate.toDoubleOrNull() ?: 3000.0
                                val inc = maxOrderIncrement.toIntOrNull() ?: 5

                                if (planName.isBlank()) {
                                    errorMessage = "Plan Name cannot be empty"
                                    return@Button
                                }

                                val scheduledDays = daysMap.filter { it.value }.map { it.key }

                                if (isNewPlan) {
                                    val newId = "pln-${System.currentTimeMillis() % 100000}"
                                    val createdPlan = MealPlanModel(
                                        id = newId,
                                        name = planName.trim(),
                                        categoryName = categoryName,
                                        isVeg = (dietaryType == "veg"),
                                        isJain = (dietaryType == "jain"),
                                        dailyRate = dRate,
                                        weeklyRate = wRate,
                                        monthlyRate = mRate,
                                        maxOrderIncrement = inc,
                                        description = description.trim(),
                                        isFeatured = true,
                                        isActive = true,
                                        deliveryDays = scheduledDays,
                                        compositionItemsCount = dayItems.size,
                                        dayWiseItems = dayItems.toList()
                                    )
                                    TiffinRepository.addMealPlan(createdPlan)
                                    successMessage = "Meal plan created successfully!"
                                } else {
                                    val updatedPlan = MealPlanModel(
                                        id = plan?.id ?: "pln-8402",
                                        name = planName.trim(),
                                        categoryName = categoryName,
                                        isVeg = (dietaryType == "veg"),
                                        isJain = (dietaryType == "jain"),
                                        dailyRate = dRate,
                                        weeklyRate = wRate,
                                        monthlyRate = mRate,
                                        maxOrderIncrement = inc,
                                        description = description.trim(),
                                        isFeatured = plan?.isFeatured ?: true,
                                        isActive = true,
                                        deliveryDays = scheduledDays,
                                        compositionItemsCount = dayItems.size,
                                        dayWiseItems = dayItems.toList()
                                    )
                                    TiffinRepository.updateMealPlan(updatedPlan)
                                    successMessage = "Meal plan updated successfully!"
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Text(
                                    if (isNewPlan) "Create Meal Plan" else "Save Changes",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Modal Dialog for Adding / Editing Day-wise Rotation Item
    if (isItemDialogOpen) {
        BasicAlertDialog(
            onDismissRequest = { isItemDialogOpen = false },
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Dialog Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (itemBeingEdited == null) "Add Item to $selectedDayTab" else "Edit Menu Item",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.Primary
                        )
                        Text(
                            text = "Item will be rotated on $selectedDayTab",
                            fontSize = 11.sp,
                            color = ColorTokens.TextSecondary
                        )
                    }
                    IconButton(onClick = { isItemDialogOpen = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = ColorTokens.Secondary)
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorTokens.BorderSubtle))

                // Item Name Field
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Item Name *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    OutlinedTextField(
                        value = dialogItemName,
                        onValueChange = {
                            dialogItemName = it
                            dialogItemError = null
                        },
                        placeholder = { Text("e.g. Paneer Butter Masala", fontSize = 13.sp, color = ColorTokens.TextSecondary) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ColorTokens.SurfaceContainerLow,
                            unfocusedContainerColor = ColorTokens.SurfaceContainerLow,
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = ColorTokens.BorderSubtle
                        )
                    )
                }

                // Portion Field
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Portion / Quantity *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    OutlinedTextField(
                        value = dialogItemPortion,
                        onValueChange = {
                            dialogItemPortion = it
                            dialogItemError = null
                        },
                        placeholder = { Text("e.g. 4 units, 1 bowl, 200g", fontSize = 13.sp, color = ColorTokens.TextSecondary) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ColorTokens.SurfaceContainerLow,
                            unfocusedContainerColor = ColorTokens.SurfaceContainerLow,
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = ColorTokens.BorderSubtle
                        )
                    )
                }

                // Veg / Non-Veg Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(if (dialogItemIsVeg) "Vegetarian Item" else "Non-Vegetarian Item", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text(if (dialogItemIsVeg) "Green dietary marker" else "Red dietary marker", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                    }
                    Switch(
                        checked = dialogItemIsVeg,
                        onCheckedChange = { dialogItemIsVeg = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = ColorTokens.TertiaryMint,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFDC2626)
                        )
                    )
                }

                if (dialogItemError != null) {
                    Text(
                        text = dialogItemError ?: "",
                        fontSize = 12.sp,
                        color = Color(0xFFDC2626)
                    )
                }

                // Dialog Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { isItemDialogOpen = false },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainerLow),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = ColorTokens.Secondary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = {
                            if (dialogItemName.isBlank()) {
                                dialogItemError = "Item name is required"
                                return@Button
                            }
                            if (dialogItemPortion.isBlank()) {
                                dialogItemError = "Portion is required"
                                return@Button
                            }

                            val editing = itemBeingEdited
                            if (editing != null) {
                                val idx = dayItems.indexOfFirst { it.id == editing.id }
                                if (idx != -1) {
                                    dayItems[idx] = editing.copy(
                                        itemName = dialogItemName.trim(),
                                        portionCount = dialogItemPortion.trim(),
                                        isVeg = dialogItemIsVeg
                                    )
                                }
                            } else {
                                dayItems.add(
                                    DayWiseMenuItemModel(
                                        id = "item-${selectedDayTab}-${System.currentTimeMillis()}",
                                        dayOfWeek = selectedDayTab,
                                        itemName = dialogItemName.trim(),
                                        portionCount = dialogItemPortion.trim(),
                                        isVeg = dialogItemIsVeg
                                    )
                                )
                            }
                            isItemDialogOpen = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (itemBeingEdited == null) "Add Item" else "Save Item", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
