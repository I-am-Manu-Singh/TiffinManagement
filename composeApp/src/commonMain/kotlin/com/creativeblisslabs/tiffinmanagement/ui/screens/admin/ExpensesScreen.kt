package com.creativeblisslabs.tiffinmanagement.ui.screens.admin

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
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
import com.creativeblisslabs.tiffinmanagement.data.models.ExpenseModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun ExpensesScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToRecurring: () -> Unit = {}
) {
    val expenses by TiffinRepository.expenses.collectAsState()
    val recurringRules by TiffinRepository.recurringExpenseRules.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterChip by remember { mutableStateOf("This Month") }
    var showInlineForm by remember { mutableStateOf(true) }

    // Form fields
    var formDate by remember { mutableStateOf("Today") }
    var formCategory by remember { mutableStateOf("Fresh Produce & Groceries") }
    var categoryDropdownOpen by remember { mutableStateOf(false) }
    var formAmount by remember { mutableStateOf("") }
    var formTax by remember { mutableStateOf("0") }
    var formDescription by remember { mutableStateOf("") }
    var selectedReceiptFile by remember { mutableStateOf("") }
    var formPaymentMode by remember { mutableStateOf("Paid Cash") }
    var paymentModeDropdownOpen by remember { mutableStateOf(false) }

    val filterChips = listOf("Today", "Last 7 Days", "Last 30 Days", "This Month", "This Year")
    val categoryOptions = listOf(
        "Fresh Produce & Groceries",
        "Spices & Seasonings",
        "Containers & Meal Foils",
        "Fuel & Van Dispatch",
        "LPG & Utility Bills",
        "Kitchen Cloud Rent",
        "Staff Salary",
        "Equipment Repair",
        "Miscellaneous"
    )
    val paymentModeOptions = listOf(
        "Paid Cash", "UPI / PhonePe", "UPI Auto-pay",
        "Bank Transfer", "Credit Card", "Debit Card", "Cheque"
    )

    val totalExpensesSum = expenses.sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
       

        // 2. Segmented Sub-Tab Switcher (All Expenses / Recurring)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ColorTokens.SurfaceContainer)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // All Expenses (Selected)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorTokens.Primary)
                    .padding(vertical = 9.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = null,
                    tint = ColorTokens.OnPrimary,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "All Expenses",
                    color = ColorTokens.OnPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Recurring Tab
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onNavigateToRecurring() }
                    .padding(vertical = 9.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = null,
                    tint = ColorTokens.OnSurfaceVariant,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Recurring",
                    color = ColorTokens.OnSurfaceVariant,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(ColorTokens.SurfaceContainerHigh)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = recurringRules.size.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurfaceVariant
                    )
                }
            }
        }

        // 3. Summary Metric Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "TOTAL EXPENSES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurfaceVariant,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "2026-08-05 to 2026-09-04",
                            fontSize = 12.sp,
                            color = ColorTokens.Secondary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ColorTokens.PrimaryFixed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CurrencyRupee,
                            contentDescription = null,
                            tint = ColorTokens.Primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "₹${"%,.2f".format(totalExpensesSum)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.Primary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Lifetime:",
                            fontSize = 11.sp,
                            color = ColorTokens.OnSurfaceVariant
                        )
                        Text(
                            text = "₹${"%,.2f".format(totalExpensesSum)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ColorTokens.OnSurface
                        )
                    }
                }
            }
        }

        // 4. Horizontal Filter Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterChips.forEach { chip ->
                val isSelected = selectedFilterChip == chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(if (isSelected) ColorTokens.PrimaryCTA else ColorTokens.SurfaceContainer)
                        .clickable { selectedFilterChip = chip }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = chip,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) ColorTokens.OnPrimary else ColorTokens.OnSurfaceVariant
                    )
                }
            }
        }

        // 5. Date Range Filter & Search Bar Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorTokens.SurfaceContainerLowest)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("05/08/2026", fontSize = 12.sp, color = ColorTokens.OnSurface)
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = ColorTokens.OnSurfaceVariant,
                        modifier = Modifier.size(15.dp)
                    )
                }
                Text("to", fontSize = 12.sp, color = ColorTokens.Secondary)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("04/09/2026", fontSize = 12.sp, color = ColorTokens.OnSurface)
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = ColorTokens.OnSurfaceVariant,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorTokens.Primary)
                    .clickable { }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Go", color = ColorTokens.OnPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorTokens.SurfaceContainerLowest)
                    .clickable { }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Tune",
                    tint = ColorTokens.OnSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Search Input & "+ Expense" Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorTokens.SurfaceContainerLowest)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (searchQuery.isEmpty()) {
                        Text("Search description...", fontSize = 13.sp, color = ColorTokens.Secondary)
                    }
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                }
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = ColorTokens.OnSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorTokens.PrimaryCTA)
                    .clickable { showInlineForm = !showInlineForm }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = ColorTokens.OnPrimary,
                    modifier = Modifier.size(17.dp)
                )
                Text(
                    text = "Expense",
                    color = ColorTokens.OnPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 6. Inline New Expense Form Card
        if (showInlineForm) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Receipt,
                                    contentDescription = null,
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "New Expense",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                        }
                        IconButton(
                            onClick = { showInlineForm = false },
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

                    // Date & Category Grid (2 columns)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Date Picker Field
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row {
                                Text("Date", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                                Text(" *", fontSize = 11.sp, color = ColorTokens.Error)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(horizontal = 10.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(formDate, fontSize = 13.sp, color = ColorTokens.OnSurface)
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = ColorTokens.OnSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Category Dropdown
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row {
                                Text("Category", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                                Text(" *", fontSize = 11.sp, color = ColorTokens.Error)
                            }
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ColorTokens.SurfaceContainerLow)
                                        .clickable { categoryDropdownOpen = true }
                                        .padding(horizontal = 10.dp, vertical = 9.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = formCategory,
                                        fontSize = 12.sp,
                                        color = ColorTokens.OnSurface,
                                        maxLines = 1
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ExpandMore,
                                        contentDescription = null,
                                        tint = ColorTokens.OnSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = categoryDropdownOpen,
                                    onDismissRequest = { categoryDropdownOpen = false }
                                ) {
                                    categoryOptions.forEach { opt ->
                                        DropdownMenuItem(
                                            text = { Text(opt, fontSize = 12.sp) },
                                            onClick = {
                                                formCategory = opt
                                                categoryDropdownOpen = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Payment Amount & Tax Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row {
                                Text("Payment Amount", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                                Text(" *", fontSize = 11.sp, color = ColorTokens.Error)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("₹ ", fontSize = 13.sp, color = ColorTokens.OnSurfaceVariant)
                                OutlinedTextField(
                                    value = formAmount,
                                    onValueChange = { formAmount = it },
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
                            Text("Tax (₹)", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("₹ ", fontSize = 13.sp, color = ColorTokens.OnSurfaceVariant)
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

                    // Payment Mode Dropdown
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row {
                            Text("Payment Mode", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                            Text(" *", fontSize = 11.sp, color = ColorTokens.Error)
                        }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .clickable { paymentModeDropdownOpen = true }
                                    .padding(horizontal = 10.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(formPaymentMode, fontSize = 12.sp, color = ColorTokens.OnSurface, maxLines = 1)
                                Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.OnSurfaceVariant, modifier = Modifier.size(16.dp))
                            }
                            DropdownMenu(expanded = paymentModeDropdownOpen, onDismissRequest = { paymentModeDropdownOpen = false }) {
                                paymentModeOptions.forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt, fontSize = 12.sp) },
                                        onClick = { formPaymentMode = opt; paymentModeDropdownOpen = false }
                                    )
                                }
                            }
                        }
                    }

                    // Description Field
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Description", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                        OutlinedTextField(
                            value = formDescription,
                            onValueChange = { formDescription = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLow),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            placeholder = { Text("Add notes, vendor or items...", fontSize = 13.sp) },
                            minLines = 2
                        )
                    }

                    // Receipt Upload Section
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row {
                            Text("Receipt", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                            Text(" (Optional)", fontSize = 11.sp, color = ColorTokens.Secondary)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ColorTokens.SurfaceContainerHigh)
                                    .clickable {
                                        selectedReceiptFile = "bill_receipt_${System.currentTimeMillis() % 10000}.jpg"
                                    }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(if (selectedReceiptFile.isNotBlank()) "Change File" else "Browse", fontSize = 11.sp, color = ColorTokens.OnSurface)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (selectedReceiptFile.isNotBlank()) Icons.Default.CheckCircle else Icons.Default.Receipt,
                                    contentDescription = null,
                                    tint = if (selectedReceiptFile.isNotBlank()) ColorTokens.Tertiary else ColorTokens.Secondary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = if (selectedReceiptFile.isNotBlank()) "1 File Attached" else "No file selected",
                                    fontSize = 11.sp,
                                    color = if (selectedReceiptFile.isNotBlank()) ColorTokens.Tertiary else ColorTokens.Secondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Receipt Image Preview Card
                        if (selectedReceiptFile.isNotBlank()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(ColorTokens.PrimaryFixed),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Receipt,
                                            contentDescription = null,
                                            tint = ColorTokens.Primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Column {
                                        Text(selectedReceiptFile, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                                        Text("Attached bill invoice", fontSize = 11.sp, color = ColorTokens.Secondary)
                                    }
                                }
                                IconButton(onClick = { selectedReceiptFile = "" }, modifier = Modifier.size(28.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = ColorTokens.OnSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Form Action Buttons (Cancel / Save)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showInlineForm = false },
                            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainer),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Text("Cancel", color = ColorTokens.OnSurface, fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                val amt = formAmount.toDoubleOrNull() ?: 0.0
                                val tx = formTax.toDoubleOrNull() ?: 0.0
                                if (amt > 0) {
                                    TiffinRepository.addExpense(
                                        ExpenseModel(
                                            id = "exp-${System.currentTimeMillis()}",
                                            dateFormatted = formDate,
                                            category = formCategory,
                                            amount = amt,
                                            tax = tx,
                                            paymentMode = formPaymentMode,
                                            description = formDescription,
                                            receiptName = if (selectedReceiptFile.isNotBlank()) selectedReceiptFile else null,
                                            receiptSizeFormatted = null // real size unknown without file API
                                        )
                                    )
                                    // Reset form
                                    formAmount = ""
                                    formTax = "0"
                                    formDescription = ""
                                    selectedReceiptFile = ""
                                    showInlineForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Text("Save Expense", color = ColorTokens.OnPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 7. Recent Dispatches & Logs Section
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                        imageVector = Icons.Default.EnergySavingsLeaf,
                        contentDescription = null,
                        tint = ColorTokens.Tertiary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "RECENT DISPATCHES & LOGS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface,
                        letterSpacing = 0.5.sp
                    )
                }
                Text(
                    text = "Showing ${expenses.size} items",
                    fontSize = 11.sp,
                    color = ColorTokens.OnSurfaceVariant
                )
            }

            expenses.forEach { expense ->
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
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Receipt,
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
                                        text = expense.category,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(ColorTokens.Tertiary)
                                    )
                                }
                                Text(
                                    text = "${expense.description} • ${expense.dateFormatted}",
                                    fontSize = 11.sp,
                                    color = ColorTokens.OnSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${"%,.2f".format(expense.amount)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Text(
                                text = expense.paymentMode,
                                fontSize = 11.sp,
                                color = ColorTokens.Tertiary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
