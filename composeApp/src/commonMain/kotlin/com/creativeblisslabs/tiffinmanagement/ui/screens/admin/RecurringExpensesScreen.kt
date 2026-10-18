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
import androidx.compose.material.icons.filled.AddChart
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.creativeblisslabs.tiffinmanagement.data.models.RecurringExpenseRuleModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun RecurringExpensesScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToAllExpenses: () -> Unit = {}
) {
    val recurringRules by TiffinRepository.recurringExpenseRules.collectAsState()

    var showNewModal by remember { mutableStateOf(false) }

    // Form states
    var formCategory by remember { mutableStateOf("Staff Salary & Wages") }
    var categoryDropdownOpen by remember { mutableStateOf(false) }
    var formTitle by remember { mutableStateOf("") }
    var formAmount by remember { mutableStateOf("") }
    var formTax by remember { mutableStateOf("0") }
    var selectedFrequency by remember { mutableStateOf("Monthly") }
    var formDayOfMonth by remember { mutableStateOf("4") }
    var formStartDate by remember { mutableStateOf("Today") }
    var formEndDate by remember { mutableStateOf("") }
    var generateTodayImmediate by remember { mutableStateOf(true) }

    val categoryOptions = listOf(
        "Staff Salary & Wages",
        "Kitchen Premises Rent",
        "Daily Fresh Groceries",
        "LPG & Kitchen Utilities",
        "Meal Packaging Supplies"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 2. Segmented Navigation Pills (All Expenses vs Recurring)

        // 2. Segmented Navigation Pills (All Expenses vs Recurring)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ColorTokens.SurfaceContainerHigh)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // All Expenses Tab
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onNavigateToAllExpenses() }
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = null,
                    tint = ColorTokens.OnSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "All Expenses",
                    color = ColorTokens.OnSurfaceVariant,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Recurring Tab (Selected)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorTokens.Primary)
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Autorenew,
                    contentDescription = null,
                    tint = ColorTokens.OnPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Recurring",
                    color = ColorTokens.OnPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color(0x33FFFFFF))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = recurringRules.size.toString(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnPrimary
                    )
                }
            }
        }

        // 3. Info Banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ColorTokens.SecondaryContainer.copy(alpha = 0.4f))
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = ColorTokens.Secondary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Automate fixed recurring expenses like rent, staff salary, utilities & daily groceries.",
                fontSize = 12.sp,
                color = ColorTokens.OnSecondaryContainer,
                lineHeight = 16.sp
            )
        }

        // 4. Recurring Expense List Section
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ACTIVE RULES (${recurringRules.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTokens.OnSurfaceVariant,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Next batch at 06:00 AM",
                    fontSize = 11.sp,
                    color = ColorTokens.Secondary
                )
            }

            recurringRules.forEach { rule ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Top Row: Icon + Title & Badges + Amount
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(ColorTokens.SurfaceContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Badge,
                                        contentDescription = null,
                                        tint = ColorTokens.Primary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = rule.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(100.dp))
                                                .background(ColorTokens.SurfaceContainer)
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = rule.category,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = ColorTokens.OnSurfaceVariant
                                            )
                                        }

                                        Row(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(100.dp))
                                                .background(ColorTokens.SecondaryFixed)
                                                .padding(horizontal = 8.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Sync,
                                                contentDescription = null,
                                                tint = ColorTokens.OnSecondaryFixed,
                                                modifier = Modifier.size(11.dp)
                                            )
                                            Text(
                                                text = rule.frequency,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = ColorTokens.OnSecondaryFixed
                                            )
                                        }
                                    }
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${"%,.2f".format(rule.amount)}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                                Text(
                                    text = "+₹${"%,.2f".format(rule.tax)} tax",
                                    fontSize = 11.sp,
                                    color = ColorTokens.OnSurfaceVariant
                                )
                            }
                        }

                        // Meta Details Pill Grid
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
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
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Event,
                                        contentDescription = null,
                                        tint = ColorTokens.Primary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text("Next Due Date:", fontSize = 12.sp, color = ColorTokens.OnSurfaceVariant)
                                }
                                Text(rule.nextDueDate, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            }

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
                                        imageVector = Icons.Default.History,
                                        contentDescription = null,
                                        tint = ColorTokens.Secondary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Text("Last Generated:", fontSize = 12.sp, color = ColorTokens.OnSurfaceVariant)
                                }
                                Text(rule.lastGeneratedDate ?: "None", fontSize = 12.sp, color = ColorTokens.OnSurface)
                            }

                            if (rule.endDate != null) {
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
                                            imageVector = Icons.Default.EventBusy,
                                            contentDescription = null,
                                            tint = ColorTokens.Secondary,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Text("End Date:", fontSize = 12.sp, color = ColorTokens.OnSurfaceVariant)
                                    }
                                    Text(rule.endDate, fontSize = 12.sp, color = ColorTokens.OnSurface)
                                }
                            }
                        }

                        // Footer Row: Switch + Edit & Delete Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Switch(
                                    checked = rule.isActive,
                                    onCheckedChange = { TiffinRepository.toggleRecurringRule(rule.id, it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = ColorTokens.Primary,
                                        uncheckedThumbColor = ColorTokens.OnSurfaceVariant,
                                        uncheckedTrackColor = ColorTokens.SurfaceContainerHighest
                                    ),
                                    modifier = Modifier.height(24.dp)
                                )
                                Text(
                                    text = if (rule.isActive) "Active" else "Inactive",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = ColorTokens.OnSurfaceVariant
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.SurfaceContainer)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = ColorTokens.OnSurface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { TiffinRepository.deleteRecurringRule(rule.id) },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.SurfaceContainer)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = ColorTokens.Error,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Quick Trigger CTA Button
        Button(
            onClick = { showNewModal = !showNewModal },
            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = null,
                tint = ColorTokens.OnPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "New Recurring Expense",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTokens.OnPrimary
            )
        }

        // 6. Form Card (New Recurring Expense)
        if (showNewModal) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddChart,
                                contentDescription = null,
                                tint = ColorTokens.Primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "New Recurring Expense",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                        }
                        IconButton(
                            onClick = { showNewModal = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = ColorTokens.OnSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Category
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row {
                            Text("Category", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Text(" *", fontSize = 12.sp, color = ColorTokens.Primary)
                        }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .clickable { categoryDropdownOpen = true }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(formCategory, fontSize = 13.sp, color = ColorTokens.OnSurface)
                                Icon(
                                    imageVector = Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = ColorTokens.OnSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = categoryDropdownOpen,
                                onDismissRequest = { categoryDropdownOpen = false }
                            ) {
                                categoryOptions.forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt, fontSize = 13.sp) },
                                        onClick = {
                                            formCategory = opt
                                            categoryDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Title / Note
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row {
                            Text("Title / Note", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Text(" *", fontSize = 12.sp, color = ColorTokens.Primary)
                        }
                        OutlinedTextField(
                            value = formTitle,
                            onValueChange = { formTitle = it },
                            placeholder = { Text("e.g., Kitchen Rent, Ramesh Salary...", fontSize = 13.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            singleLine = true
                        )
                    }

                    // Amount & Tax Two-Column
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row {
                                Text("Payment Amount", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                                Text(" *", fontSize = 12.sp, color = ColorTokens.Primary)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("₹ ", fontSize = 14.sp, color = ColorTokens.OnSurfaceVariant)
                                OutlinedTextField(
                                    value = formAmount,
                                    onValueChange = { formAmount = it },
                                    placeholder = { Text("0.00", fontSize = 13.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent
                                    ),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Tax (₹)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("₹ ", fontSize = 14.sp, color = ColorTokens.OnSurfaceVariant)
                                OutlinedTextField(
                                    value = formTax,
                                    onValueChange = { formTax = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent
                                    ),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                            }
                        }
                    }

                    // Frequency Tabs (Daily / Weekly / Monthly)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row {
                            Text("Frequency", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Text(" *", fontSize = 12.sp, color = ColorTokens.Primary)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf(
                                Triple("Daily", Icons.Default.Today, "Daily"),
                                Triple("Weekly", Icons.Default.CalendarViewWeek, "Weekly"),
                                Triple("Monthly", Icons.Default.CalendarMonth, "Monthly")
                            ).forEach { (label, icon, value) ->
                                val isSelected = selectedFrequency == value
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) ColorTokens.Primary else Color.Transparent)
                                        .clickable { selectedFrequency = value }
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = if (isSelected) ColorTokens.OnPrimary else ColorTokens.OnSurfaceVariant,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) ColorTokens.OnPrimary else ColorTokens.OnSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    // Day of Month Input (if Monthly)
                    if (selectedFrequency == "Monthly") {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Day of Month (1 - 31)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = formDayOfMonth,
                                    onValueChange = { formDayOfMonth = it },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent
                                    ),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                                Text("th of month", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                            }
                        }
                    }

                    // Start & Optional End Date
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row {
                                Text("Start Date", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                                Text(" *", fontSize = 12.sp, color = ColorTokens.Primary)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(horizontal = 10.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(formStartDate, fontSize = 12.sp, color = ColorTokens.OnSurface)
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = ColorTokens.OnSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Optional End Date", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(horizontal = 10.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (formEndDate.isNotEmpty()) formEndDate else "dd/mm/yyyy",
                                    fontSize = 12.sp,
                                    color = if (formEndDate.isNotEmpty()) ColorTokens.OnSurface else ColorTokens.OnSurfaceVariant.copy(alpha = 0.6f)
                                )
                                Icon(
                                    imageVector = Icons.Default.Event,
                                    contentDescription = null,
                                    tint = ColorTokens.OnSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    // Generate Today Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Switch(
                            checked = generateTodayImmediate,
                            onCheckedChange = { generateTodayImmediate = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ColorTokens.Primary
                            ),
                            modifier = Modifier.height(24.dp)
                        )
                        Text(
                            text = "Generate first expense entry immediately for today",
                            fontSize = 12.sp,
                            color = ColorTokens.OnSurface
                        )
                    }

                    // Bottom Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showNewModal = false },
                            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainer),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Text("Cancel", color = ColorTokens.OnSurface, fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                val amt = formAmount.toDoubleOrNull() ?: 0.0
                                val tx = formTax.toDoubleOrNull() ?: 0.0
                                if (amt > 0 && formTitle.isNotBlank()) {
                                    TiffinRepository.addRecurringRule(
                                        RecurringExpenseRuleModel(
                                            id = "rec-${System.currentTimeMillis()}",
                                            title = formTitle,
                                            category = formCategory,
                                            amount = amt,
                                            tax = tx,
                                            frequency = "$selectedFrequency (${formDayOfMonth}th)",
                                            dayOfMonth = formDayOfMonth.toIntOrNull() ?: 1,
                                            nextDueDate = formStartDate,
                                            lastGeneratedDate = if (generateTodayImmediate) formStartDate else null,
                                            endDate = if (formEndDate.isNotBlank()) formEndDate else null,
                                            isActive = true
                                        )
                                    )
                                    showNewModal = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = ColorTokens.OnPrimary,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Expense", color = ColorTokens.OnPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 7. Projected Outflow Snippet
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLow),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
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
                            .clip(CircleShape)
                            .background(ColorTokens.TertiaryContainer.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Savings,
                            contentDescription = null,
                            tint = ColorTokens.Tertiary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Projected Monthly Fixed Outflow",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = "Includes rent, daily staples & weekly salary",
                            fontSize = 11.sp,
                            color = ColorTokens.OnSurfaceVariant
                        )
                    }
                }

                Text(
                    text = "₹14,820",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTokens.Tertiary
                )
            }
        }
    }
}
