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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun CustomersScreen(
    onAddCustomerClick: () -> Unit = {},
    onCustomerClick: (String) -> Unit = {},
    onEditCustomer: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterIndex by remember { mutableStateOf(0) }
    var dialogIsActive by remember { mutableStateOf(true) }

    var adjustingCustomer by remember { mutableStateOf<com.example.myapplicationkmp.data.models.CustomerModel?>(null) }
    var walletIsCredit by remember { mutableStateOf(true) }
    var walletAmountInput by remember { mutableStateOf("") }
    var walletRemarkInput by remember { mutableStateOf("") }
    var walletDialogError by remember { mutableStateOf<String?>(null) }

    var selectedPageSize by remember { mutableStateOf("15") }
    var pageSizeDropdownOpen by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }

    val customerList by TiffinRepository.customers.collectAsState()
    val filteredCustomers = remember(customerList, searchQuery, selectedFilterIndex) {
        customerList.filter { customer ->
            val matchesQuery = searchQuery.isBlank() ||
                customer.name.contains(searchQuery, ignoreCase = true) ||
                customer.phone.contains(searchQuery)
            val matchesFilter = when (selectedFilterIndex) {
                1 -> customer.isActive
                2 -> customer.walletBalance < 0 || customer.dueLimit > 0
                else -> true
            }
            matchesQuery && matchesFilter
        }
    }

    val filterChips = remember(customerList.size) {
        listOf(
            "All Accounts (${customerList.size})",
            "Active Only",
            "With Dues (>₹0)",
            "Main Branch"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 3. Search & Filter Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search customers by name, phone...", fontSize = 12.sp, color = ColorTokens.TextSecondary) },
                leadingIcon = {
                    Icon(Icons.Default.Search, "Search", tint = ColorTokens.TextSecondary, modifier = Modifier.size(18.dp))
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ColorTokens.SurfaceContainerLowest,
                    unfocusedContainerColor = ColorTokens.SurfaceContainerLowest,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true
            )

            // Page Size Selector
            Box {
                Row(
                    modifier = Modifier
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ColorTokens.SurfaceContainerLowest)
                        .clickable { pageSizeDropdownOpen = true }
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(selectedPageSize, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    Icon(Icons.Default.UnfoldMore, "More", tint = ColorTokens.TextSecondary, modifier = Modifier.size(16.dp))
                }
                androidx.compose.material3.DropdownMenu(
                    expanded = pageSizeDropdownOpen,
                    onDismissRequest = { pageSizeDropdownOpen = false }
                ) {
                    listOf("15", "25", "50").forEach { sz ->
                        androidx.compose.material3.DropdownMenuItem(
                            text = { Text(sz, fontSize = 13.sp) },
                            onClick = {
                                selectedPageSize = sz
                                pageSizeDropdownOpen = false
                            }
                        )
                    }
                }
            }

            // Filter Icon Button
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selectedFilterIndex != 0) ColorTokens.PrimaryContainer else ColorTokens.SurfaceContainerLowest)
                    .clickable { showFilterSheet = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.FilterList,
                    "Filter",
                    tint = if (selectedFilterIndex != 0) ColorTokens.PrimaryCTA else ColorTokens.OnSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
                  Button(
                onClick = onAddCustomerClick,
                colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryCTA),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Add, "Add", modifier = Modifier.size(18.dp))
                    Text("Add", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // 4. Quick Filter Badges
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            filterChips.forEachIndexed { idx, title ->
                val isSelected = selectedFilterIndex == idx
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(
                            if (isSelected) ColorTokens.PrimaryFixed else ColorTokens.SurfaceContainerLow
                        )
                        .clickable { selectedFilterIndex = idx }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) ColorTokens.OnPrimaryFixed else ColorTokens.OnSurfaceVariant
                    )
                }
            }
        }

        // 5. Customer Cards List Container
        val limit = selectedPageSize.toIntOrNull() ?: 15
        val displayedCustomers = filteredCustomers.take(limit)

        displayedCustomers.forEach { customer ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Header: Avatar, Name, Status, Veg marker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.SecondaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AccountCircle, "Avatar", tint = ColorTokens.OnSecondaryFixed, modifier = Modifier.size(28.dp))
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = customer.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = ColorTokens.OnSurface
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Store, contentDescription = null, tint = ColorTokens.TextSecondary, modifier = Modifier.size(13.dp))
                                    Text(customer.branchName, fontSize = 11.sp, color = ColorTokens.TextSecondary)
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Veg green dot square indicator
                            if (customer.isVeg) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .border(1.dp, ColorTokens.Tertiary, RoundedCornerShape(2.dp))
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(Modifier.size(6.dp).clip(CircleShape).background(ColorTokens.Tertiary))
                                }
                            }

                            // Interactive Status Switch for Customer
                            Switch(
                                checked = customer.isActive,
                                onCheckedChange = { isChecked ->
                                    TiffinRepository.toggleCustomerStatus(customer.id, isChecked)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = ColorTokens.Primary,
                                    uncheckedThumbColor = ColorTokens.TextSecondary,
                                    uncheckedTrackColor = ColorTokens.SurfaceContainerHigh
                                )
                            )
                        }
                    }

                    // Contact Details Cluster
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Call, "Phone", tint = ColorTokens.OnSurface, modifier = Modifier.size(14.dp))
                            Text(customer.phone, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Mail, "Email", tint = ColorTokens.TextSecondary, modifier = Modifier.size(14.dp))
                            Text(customer.email, fontSize = 12.sp, color = ColorTokens.TextSecondary)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.LocationOn, "Address", tint = ColorTokens.TextSecondary, modifier = Modifier.size(14.dp))
                            Text(customer.address, fontSize = 12.sp, color = ColorTokens.TextSecondary)
                        }
                    }

                    // Financial Balance & Credit Limit Strip
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ColorTokens.SurfaceContainer.copy(alpha = 0.7f))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Account Balance", fontSize = 10.sp, color = ColorTokens.TextSecondary)
                            Text("Wallet: ₹${customer.walletBalance}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Tertiary)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Credit Ceiling", fontSize = 10.sp, color = ColorTokens.TextSecondary)
                            Text("Due Limit: ₹${customer.dueLimit}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.Secondary)
                        }
                    }

                    // Card Actions: Details, Edit, Delete
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Details
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainer)
                                .clickable { onCustomerClick(customer.id) }
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Visibility, "Details", tint = ColorTokens.Secondary, modifier = Modifier.size(15.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Details", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        }

                        // Edit
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainer)
                                .clickable { onEditCustomer(customer.id) }
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Edit, "Edit", tint = ColorTokens.Primary, modifier = Modifier.size(15.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Edit", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        }

                        // Wallet ±
                        Row(
                            modifier = Modifier
                                .weight(1.1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.TertiaryFixed)
                                .clickable {
                                    adjustingCustomer = customer
                                    walletAmountInput = ""
                                    walletRemarkInput = ""
                                    walletIsCredit = true
                                    walletDialogError = null
                                }
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Wallet ±", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnTertiaryFixed)
                        }

                        // Delete
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.ErrorContainer.copy(alpha = 0.6f))
                                .clickable { TiffinRepository.removeCustomer(customer.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Delete, "Delete", tint = ColorTokens.Error, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // 6. Dashed "Expand your kitchen's circle" Add Slot
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .border(
                    1.5.dp,
                    ColorTokens.OutlineVariant,
                    RoundedCornerShape(14.dp)
                )
                .background(ColorTokens.SurfaceContainerLowest.copy(alpha = 0.5f))
                .clickable { onAddCustomerClick() }
                .padding(vertical = 20.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ColorTokens.PrimaryFixed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PersonAdd, "Add", tint = ColorTokens.Primary, modifier = Modifier.size(22.dp))
                }

                Text(
                    text = "Expand your kitchen's circle",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = ColorTokens.OnSurface
                )

                Text(
                    text = "Add new daily subscribers, set recurring rotis & billing cycles",
                    fontSize = 11.sp,
                    color = ColorTokens.TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Text(
                    text = "+ Register Customer Profile",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTokens.Primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Manual Wallet Adjustment Dialog for Customers
        adjustingCustomer?.let { targetCust ->
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { adjustingCustomer = null },
                title = {
                    Column {
                        Text(
                            text = "Manual Wallet Adjustment",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = "${targetCust.name} (${targetCust.phone})",
                            fontSize = 12.sp,
                            color = ColorTokens.TextSecondary
                        )
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { walletIsCredit = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (walletIsCredit) ColorTokens.Primary else ColorTokens.SurfaceContainerHigh
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "+ Credit",
                                    color = if (walletIsCredit) Color.White else ColorTokens.OnSurface,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(
                                onClick = { walletIsCredit = false },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!walletIsCredit) ColorTokens.Error else ColorTokens.SurfaceContainerHigh
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "- Debit",
                                    color = if (!walletIsCredit) Color.White else ColorTokens.OnSurface,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        OutlinedTextField(
                            value = walletAmountInput,
                            onValueChange = { walletAmountInput = it },
                            label = { Text("Amount (₹)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = walletRemarkInput,
                            onValueChange = { walletRemarkInput = it },
                            label = { Text("Reason / Remarks") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )

                        walletDialogError?.let {
                            Text(it, color = ColorTokens.Error, fontSize = 11.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amt = walletAmountInput.toDoubleOrNull()
                            if (amt == null || amt <= 0.0) {
                                walletDialogError = "Please enter a valid positive amount"
                                return@Button
                            }
                            TiffinRepository.applyWalletAdjustment(
                                customerId = targetCust.id,
                                amount = amt,
                                isCredit = walletIsCredit,
                                description = walletRemarkInput.ifBlank {
                                    if (walletIsCredit) "Manual Credit Adjustment" else "Manual Debit Adjustment"
                                }
                            )
                            adjustingCustomer = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryCTA),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Apply", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { adjustingCustomer = null }) {
                        Text("Cancel", color = ColorTokens.TextSecondary)
                    }
                }
            )
        }

        if (showFilterSheet) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showFilterSheet = false },
                title = { Text("Filter Customer Accounts", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Quick Filter", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.TextSecondary)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            filterChips.forEachIndexed { idx, title ->
                                val selected = selectedFilterIndex == idx
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (selected) ColorTokens.PrimaryContainer else ColorTokens.SurfaceContainerLow)
                                        .clickable {
                                            selectedFilterIndex = idx
                                            showFilterSheet = false
                                        }
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        title,
                                        fontSize = 13.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (selected) ColorTokens.PrimaryCTA else ColorTokens.OnSurface
                                    )
                                    if (selected) {
                                        Icon(Icons.Default.Check, null, tint = ColorTokens.PrimaryCTA, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showFilterSheet = false },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryCTA),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Apply", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = {
                        selectedFilterIndex = 0
                        showFilterSheet = false
                    }) {
                        Text("Reset", color = ColorTokens.TextSecondary)
                    }
                }
            )
        }
    }
}
