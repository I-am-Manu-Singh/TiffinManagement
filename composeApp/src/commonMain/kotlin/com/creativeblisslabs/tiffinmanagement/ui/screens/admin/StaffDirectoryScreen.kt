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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.TwoWheeler
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.creativeblisslabs.tiffinmanagement.data.models.StaffMemberModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun StaffDirectoryScreen(
    onNavigateBack: () -> Unit = {}
) {
    val staffList by TiffinRepository.staffMembers.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedPageSize by remember { mutableStateOf("15") }
    var pageSizeDropdownOpen by remember { mutableStateOf(false) }
    var showAddStaffModal by remember { mutableStateOf(false) }

    // Form fields for Add Staff Member
    var formFullName by remember { mutableStateOf("") }
    var formPhone by remember { mutableStateOf("") }
    var formEmail by remember { mutableStateOf("") }
    var formRole by remember { mutableStateOf("Cook") } // Cook, Packing, Driver, Manager
    val branches by TiffinRepository.branches.collectAsState()
    val branchOptions = remember(branches) {
        if (branches.isNotEmpty()) branches.map { it.name }
        else listOf("Main Branch")
    }
    var formBranch by remember { mutableStateOf(branchOptions.firstOrNull() ?: "Main Branch") }
    var formAccountActive by remember { mutableStateOf(true) }
    var branchDropdownOpen by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var selectedRoleFilter by remember { mutableStateOf("all") }

    val filteredStaff = remember(staffList, searchQuery, selectedRoleFilter) {
        staffList.filter { staff ->
            val matchesQuery = searchQuery.isBlank() ||
                staff.fullName.contains(searchQuery, ignoreCase = true) ||
                staff.phone.contains(searchQuery) ||
                (staff.email?.contains(searchQuery, ignoreCase = true) == true)
            val matchesRole = selectedRoleFilter == "all" || staff.role.equals(selectedRoleFilter, ignoreCase = true)
            matchesQuery && matchesRole
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
       

        // 2. Search, Page Size & Filter Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search Box
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ColorTokens.SurfaceContainerLowest)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = ColorTokens.OnSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (searchQuery.isEmpty()) {
                        Text("Search with name, phone...", fontSize = 12.sp, color = ColorTokens.OnSurfaceVariant.copy(alpha = 0.6f))
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
            }

            // Page Size Selector
            Box {
                Row(
                    modifier = Modifier
                        .height(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ColorTokens.SurfaceContainerLowest)
                        .clickable { pageSizeDropdownOpen = true }
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(selectedPageSize, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.OnSurfaceVariant, modifier = Modifier.size(16.dp))
                }
                DropdownMenu(
                    expanded = pageSizeDropdownOpen,
                    onDismissRequest = { pageSizeDropdownOpen = false }
                ) {
                    listOf("15", "25", "50").forEach { sz ->
                        DropdownMenuItem(
                            text = { Text(sz, fontSize = 12.sp) },
                            onClick = {
                                selectedPageSize = sz
                                pageSizeDropdownOpen = false
                            }
                        )
                    }
                }
            }

            // Filter Tune Icon
            IconButton(
                onClick = { showFilterSheet = true },
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selectedRoleFilter != "all") ColorTokens.PrimaryContainer else ColorTokens.SurfaceContainerLowest)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Tune",
                    tint = if (selectedRoleFilter != "all") ColorTokens.PrimaryCTA else ColorTokens.OnSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
               Button(
                onClick = { showAddStaffModal = true },
                colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryCTA),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = ColorTokens.OnPrimary,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "New",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ColorTokens.OnPrimary
                )
            }
        }

        // Subtitle & Count Row
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
                    text = "Staff Members",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTokens.OnSurface
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(ColorTokens.SurfaceContainer)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = filteredStaff.size.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurfaceVariant
                    )
                }
            }
            val limit = selectedPageSize.toIntOrNull() ?: 15
            val countShown = minOf(limit, filteredStaff.size)
            Text(
                text = "Showing $countShown of ${filteredStaff.size}",
                fontSize = 11.sp,
                color = ColorTokens.OnSurfaceVariant
            )
        }

        // 3. Records Not Available / Empty State Card
        if (filteredStaff.isEmpty()) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(ColorTokens.SurfaceContainerLow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SentimentDissatisfied,
                            contentDescription = null,
                            tint = ColorTokens.OnSurfaceVariant,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Text(
                        text = "Records Not Available!",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                    Text(
                        text = "No staff profiles created yet. Add kitchen cooks, packing operators, and delivery drivers to assign granular permissions.",
                        fontSize = 12.sp,
                        color = ColorTokens.OnSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 18.sp
                    )
                    Button(
                        onClick = { showAddStaffModal = true },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryCTA),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Icon(Icons.Default.PersonAdd, null, tint = ColorTokens.OnPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add First Staff Member", color = ColorTokens.OnPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 4. Sample / Active Staff Profiles Card
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
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.Primary)
                        )
                        Text(
                            text = "Sample Staff Profiles",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                    }
                    Text(
                        text = "Quick Preview",
                        fontSize = 11.sp,
                        color = ColorTokens.Secondary
                    )
                }

                val limit = selectedPageSize.toIntOrNull() ?: 15
                val displayedStaff = filteredStaff.take(limit)

                displayedStaff.forEach { staff ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLow)
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
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (staff.role == "Head Cook") ColorTokens.SecondaryContainer else ColorTokens.PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = staff.initials,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (staff.role == "Head Cook") ColorTokens.OnSecondaryContainer else ColorTokens.OnPrimaryFixed
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = staff.fullName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(ColorTokens.TertiaryFixed)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "Active",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnTertiaryFixed
                                        )
                                    }
                                }
                                Text(
                                    text = staff.phone,
                                    fontSize = 12.sp,
                                    color = ColorTokens.OnSurfaceVariant
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (staff.role == "Head Cook") ColorTokens.SurfaceContainerHighest else ColorTokens.SecondaryFixed)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = staff.role,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (staff.role == "Head Cook") ColorTokens.OnSurface else ColorTokens.OnSecondaryFixed
                                )
                            }
                            Text(
                                text = staff.branchName,
                                fontSize = 11.sp,
                                color = ColorTokens.OnSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // 5. Granular Operational Roles Presets Card
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = ColorTokens.Primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Column {
                        Text(
                            text = "Granular Operational Roles",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = "Available presets for your team members",
                            fontSize = 11.sp,
                            color = ColorTokens.OnSurfaceVariant
                        )
                    }
                }

                // 2x2 Grid of Roles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Kitchen Cook
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Restaurant, null, tint = ColorTokens.Primary, modifier = Modifier.size(16.dp))
                            Text("Kitchen Cook", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                        }
                        Text("View daily counts, mark dispatches", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                    }

                    // Packing Staff
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Inventory2, null, tint = ColorTokens.Tertiary, modifier = Modifier.size(16.dp))
                            Text("Packing Staff", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Tertiary)
                        }
                        Text("Print labels & dietary packing", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Delivery Driver
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.TwoWheeler, null, tint = ColorTokens.Secondary, modifier = Modifier.size(16.dp))
                            Text("Delivery Driver", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Secondary)
                        }
                        Text("Route navigation & drop proof", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                    }

                    // Branch Manager
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, null, tint = ColorTokens.OnSurface, modifier = Modifier.size(16.dp))
                            Text("Branch Manager", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        }
                        Text("Billing, plans, customer edits", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                    }
                }
            }
        }

        // 6. Form Card (Add Staff Member modal)
        if (showAddStaffModal) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Modal Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Add Staff Member", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            Text("Create login credentials and operational roles", fontSize = 12.sp, color = ColorTokens.OnSurfaceVariant)
                        }
                        IconButton(onClick = { showAddStaffModal = false }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, "Close", tint = ColorTokens.OnSurfaceVariant, modifier = Modifier.size(18.dp))
                        }
                    }

                    // Full Name
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row {
                            Text("Full Name", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Text(" *", fontSize = 12.sp, color = ColorTokens.Primary)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Badge, null, tint = ColorTokens.OnSurfaceVariant, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            OutlinedTextField(
                                value = formFullName,
                                onValueChange = { formFullName = it },
                                placeholder = { Text("e.g. Jaswinder Singh", fontSize = 13.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                singleLine = true
                            )
                        }
                    }

                    // Phone & Email Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row {
                                Text("Phone Number", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                                Text(" *", fontSize = 12.sp, color = ColorTokens.Primary)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Call, null, tint = ColorTokens.OnSurfaceVariant, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                OutlinedTextField(
                                    value = formPhone,
                                    onValueChange = { formPhone = it },
                                    placeholder = { Text("10-digit mobile", fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent
                                    ),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    singleLine = true
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Email Address", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Mail, null, tint = ColorTokens.OnSurfaceVariant, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                OutlinedTextField(
                                    value = formEmail,
                                    onValueChange = { formEmail = it },
                                    placeholder = { Text("staff@kitchen.com", fontSize = 12.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent
                                    ),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                    singleLine = true
                                )
                            }
                        }
                    }

                    // Assigned Operational Role (2x2 Grid)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Assigned Operational Role", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Cook" to "Kitchen Cook", "Packing" to "Packing Staff").forEach { (code, label) ->
                                val isSelected = formRole == code
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) ColorTokens.PrimaryFixed else ColorTokens.SurfaceContainerLow)
                                        .clickable { formRole = code }
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { formRole = code },
                                        colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Primary)
                                    )
                                    Text(label, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) ColorTokens.OnPrimaryFixed else ColorTokens.OnSurface)
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Driver" to "Delivery Driver", "Manager" to "Branch Manager").forEach { (code, label) ->
                                val isSelected = formRole == code
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) ColorTokens.PrimaryFixed else ColorTokens.SurfaceContainerLow)
                                        .clickable { formRole = code }
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { formRole = code },
                                        colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Primary)
                                    )
                                    Text(label, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = if (isSelected) ColorTokens.OnPrimaryFixed else ColorTokens.OnSurface)
                                }
                            }
                        }
                    }

                    // Branch Assignment
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Branch Assignment", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .clickable { branchDropdownOpen = true }
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.Store, null, tint = ColorTokens.OnSurfaceVariant, modifier = Modifier.size(18.dp))
                                    Text(formBranch, fontSize = 12.sp, color = ColorTokens.OnSurface, maxLines = 1)
                                }
                                Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.OnSurfaceVariant, modifier = Modifier.size(18.dp))
                            }
                            DropdownMenu(
                                expanded = branchDropdownOpen,
                                onDismissRequest = { branchDropdownOpen = false }
                            ) {
                                branchOptions.forEach { b ->
                                    DropdownMenuItem(
                                        text = { Text(b, fontSize = 12.sp) },
                                        onClick = {
                                            formBranch = b
                                            branchDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Account Status Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Account Status", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Text("Enable staff app sign-in immediately", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                        }
                        Switch(
                            checked = formAccountActive,
                            onCheckedChange = { formAccountActive = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ColorTokens.Tertiary
                            )
                        )
                    }

                    // Modal Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showAddStaffModal = false },
                            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.SurfaceContainer),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Text("Cancel", color = ColorTokens.OnSurface, fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                if (formFullName.isNotBlank() && formPhone.isNotBlank()) {
                                    // Validate 10-digit phone (strip country code for check)
                                    val rawPhone = formPhone.trim().removePrefix("+91").replace(" ", "")
                                    if (rawPhone.length < 10) return@Button
                                    TiffinRepository.addStaffMember(
                                        StaffMemberModel(
                                            id = "staff-${System.currentTimeMillis()}",
                                            fullName = formFullName.trim(),
                                            phone = formPhone.trim(),
                                            email = if (formEmail.isNotBlank()) formEmail.trim().lowercase() else null,
                                            role = when (formRole) {
                                                "Cook"    -> "Head Cook"
                                                "Packing" -> "Packing Staff"
                                                "Driver"  -> "Delivery Lead"
                                                else      -> "Branch Manager"
                                            },
                                            branchName = formBranch, // use exact selected branch name
                                            isActive = formAccountActive
                                        )
                                    )
                                    formFullName = ""
                                    formPhone = ""
                                    formEmail = ""
                                    showAddStaffModal = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryCTA),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).height(44.dp)
                        ) {
                            Text("Save Staff", color = ColorTokens.OnPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (showFilterSheet) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showFilterSheet = false },
                title = { Text("Filter Staff Directory", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Role", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurfaceVariant)
                        androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val roles = listOf("all" to "All Roles", "Head Cook" to "Head Cook", "Packing Staff" to "Packing Staff", "Delivery Lead" to "Delivery Lead", "Branch Manager" to "Branch Manager")
                            items(roles.size) { idx ->
                                val (key, lbl) = roles[idx]
                                val selected = selectedRoleFilter.equals(key, ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (selected) ColorTokens.PrimaryCTA else ColorTokens.SurfaceContainerLow)
                                        .clickable { selectedRoleFilter = key }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = lbl,
                                        fontSize = 11.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selected) Color.White else ColorTokens.OnSurface
                                    )
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
                        selectedRoleFilter = "all"
                        showFilterSheet = false
                    }) {
                        Text("Reset", color = ColorTokens.OnSurfaceVariant)
                    }
                }
            )
        }
    }
}
