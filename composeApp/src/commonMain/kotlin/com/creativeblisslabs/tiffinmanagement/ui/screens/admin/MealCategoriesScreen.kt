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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.creativeblisslabs.tiffinmanagement.data.models.MealCategoryModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinTopHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealCategoriesScreen(
    onBack: () -> Unit = {},
    onAddCategory: () -> Unit = {},
    onEditCategory: (MealCategoryModel) -> Unit = {}
) {
    val categories by TiffinRepository.mealCategories.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedPageSize by remember { mutableStateOf("15") }
    var pageSizeDropdownOpen by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var activeStatusFilter by remember { mutableStateOf("all") } // 'all', 'active', 'inactive'

    // Dialog state for Add / Edit Category
    var isCategoryDialogOpen by remember { mutableStateOf(false) }
    var categoryBeingEdited by remember { mutableStateOf<MealCategoryModel?>(null) }
    var dialogName by remember { mutableStateOf("") }
    var dialogStartTime by remember { mutableStateOf("") }
    var dialogEndTime by remember { mutableStateOf("") }
    var dialogDescription by remember { mutableStateOf("") }
    var dialogIsActive by remember { mutableStateOf(true) }
    var dialogError by remember { mutableStateOf<String?>(null) }

    fun openAddDialog() {
        categoryBeingEdited = null
        dialogName = ""
        dialogStartTime = "07:00 AM"
        dialogEndTime = "09:30 AM"
        dialogDescription = ""
        dialogIsActive = true
        dialogError = null
        isCategoryDialogOpen = true
    }

    fun openEditDialog(category: MealCategoryModel) {
        categoryBeingEdited = category
        dialogName = category.name
        dialogStartTime = category.startTime
        dialogEndTime = category.endTime
        dialogDescription = category.description ?: ""
        dialogIsActive = category.isActive
        dialogError = null
        isCategoryDialogOpen = true
    }

    val filteredCategories = remember(categories, searchQuery, activeStatusFilter) {
        categories.filter { cat ->
            val matchesQuery = searchQuery.isBlank() || cat.name.contains(searchQuery, ignoreCase = true)
            val matchesStatus = when (activeStatusFilter) {
                "active" -> cat.isActive
                "inactive" -> !cat.isActive
                else -> true
            }
            matchesQuery && matchesStatus
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        // Main Content Area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Controls Bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Page size selector
                    Box {
                        Row(
                            modifier = Modifier
                                .height(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp))
                                .clickable { pageSizeDropdownOpen = true }
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = selectedPageSize,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ColorTokens.OnSurface
                            )
                            Icon(Icons.Default.FilterList, null, tint = ColorTokens.TextSecondary, modifier = Modifier.size(14.dp))
                        }
                        androidx.compose.material3.DropdownMenu(
                            expanded = pageSizeDropdownOpen,
                            onDismissRequest = { pageSizeDropdownOpen = false }
                        ) {
                            listOf("15", "25", "50").forEach { sz ->
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text(sz, fontSize = 12.sp) },
                                    onClick = {
                                        selectedPageSize = sz
                                        pageSizeDropdownOpen = false
                                    }
                                )
                            }
                        }
                    }

                    // Filter icon button
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (activeStatusFilter != "all") ColorTokens.PrimaryContainer else ColorTokens.SurfaceContainerLow)
                            .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp))
                            .clickable { showFilterSheet = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = if (activeStatusFilter != "all") ColorTokens.Primary else ColorTokens.Secondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Search input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search categories...", fontSize = 12.sp, color = ColorTokens.TextSecondary) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, null, tint = ColorTokens.TextSecondary, modifier = Modifier.size(16.dp))
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ColorTokens.SurfaceContainerLow,
                            unfocusedContainerColor = ColorTokens.SurfaceContainerLow,
                            focusedBorderColor = ColorTokens.Primary,
                            unfocusedBorderColor = ColorTokens.BorderSubtle
                        )
                    )

                    Button(
                        onClick = { openAddDialog() },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(15.dp))
                            Text("Add", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Category Cards List
            val limit = selectedPageSize.toIntOrNull() ?: 15
            val displayedCategories = filteredCategories.take(limit)

            if (displayedCategories.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest)
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No meal categories found.", fontSize = 13.sp, color = ColorTokens.TextSecondary)
                    }
                }
            } else {
                displayedCategories.forEach { category ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Header Row: Drag indicator + Title + Active badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // 6-dot grip indicator
                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                                            Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                                            Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                                            Box(modifier = Modifier.size(3.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                                        }
                                    }

                                    Text(
                                        text = category.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.Primary
                                    )
                                }
                                
                                Switch(
                            checked = dialogIsActive,
                            onCheckedChange = { dialogIsActive = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ColorTokens.Primary
                            )
                        )
                    
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(22.dp))
                                        .background(if (category.isActive) Color(0xFFECFDF5) else Color(0xFFFEF2F2))
                                        .border(
                                            1.dp,
                                            if (category.isActive) Color(0xFFA7F3D0) else Color(0xFFFECACA),
                                            RoundedCornerShape(22.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(if (category.isActive) ColorTokens.TertiaryMint else Color(0xFFEF4444))
                                    )
                                    Text(
                                        text = if (category.isActive) "Active" else "Inactive",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (category.isActive) ColorTokens.Tertiary else Color(0xFFB91C1C)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Operating Window
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.padding(start = 14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = ColorTokens.Tertiary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${category.startTime} – ${category.endTime}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = ColorTokens.Secondary
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Description / Notes
                            Text(
                                text = category.description ?: "No description available",
                                fontSize = 12.sp,
                                color = ColorTokens.TextSecondary,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                modifier = Modifier.padding(start = 14.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorTokens.BorderSubtle))
                            Spacer(modifier = Modifier.height(10.dp))

                            // Actions: Edit / Delete
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color.White)
                                        .border(1.dp, ColorTokens.Primary, RoundedCornerShape(10.dp))
                                        .clickable { openEditDialog(category) }
                                        .padding(horizontal = 14.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Edit, null, tint = ColorTokens.Primary, modifier = Modifier.size(13.dp))
                                    Text("Edit", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color.White)
                                        .border(1.dp, Color(0xFFF87171), RoundedCornerShape(10.dp))
                                        .clickable { TiffinRepository.deleteMealCategory(category.id) }
                                        .padding(horizontal = 14.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Delete, null, tint = Color(0xFFDC2626), modifier = Modifier.size(13.dp))
                                    Text("Delete", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Category Dialog
    if (isCategoryDialogOpen) {
        BasicAlertDialog(
            onDismissRequest = { isCategoryDialogOpen = false }
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
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
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.RestaurantMenu, null, tint = ColorTokens.Primary, modifier = Modifier.size(20.dp))
                            }
                            Text(
                                text = if (categoryBeingEdited != null) "Edit Category" else "Add Category",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                        }

                        IconButton(
                            onClick = { isCategoryDialogOpen = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Close, "Close", tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorTokens.BorderSubtle))

                    // Category Name
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Category Name *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        OutlinedTextField(
                            value = dialogName,
                            onValueChange = { dialogName = it },
                            placeholder = { Text("e.g. Breakfast, Lunch, Dinner", fontSize = 12.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ColorTokens.Primary,
                                unfocusedBorderColor = ColorTokens.BorderSubtle
                            )
                        )
                    }

                    // Operating Window
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Start Time", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            OutlinedTextField(
                                value = dialogStartTime,
                                onValueChange = { dialogStartTime = it },
                                placeholder = { Text("e.g. 07:00 AM", fontSize = 12.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ColorTokens.Primary,
                                    unfocusedBorderColor = ColorTokens.BorderSubtle
                                )
                            )
                        }

                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("End Time", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            OutlinedTextField(
                                value = dialogEndTime,
                                onValueChange = { dialogEndTime = it },
                                placeholder = { Text("e.g. 09:30 AM", fontSize = 12.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ColorTokens.Primary,
                                    unfocusedBorderColor = ColorTokens.BorderSubtle
                                )
                            )
                        }
                    }

                    // Description
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Description", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        OutlinedTextField(
                            value = dialogDescription,
                            onValueChange = { dialogDescription = it },
                            placeholder = { Text("Optional slot guidelines or menu notes...", fontSize = 12.sp) },
                            minLines = 2,
                            maxLines = 3,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ColorTokens.Primary,
                                unfocusedBorderColor = ColorTokens.BorderSubtle
                            )
                        )
                    }

                    // Active Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Active Status", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Switch(
                            checked = dialogIsActive,
                            onCheckedChange = { dialogIsActive = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ColorTokens.Primary
                            )
                        )
                    }

                    if (dialogError != null) {
                        Text(dialogError ?: "", color = Color(0xFFDC2626), fontSize = 12.sp)
                    }

                    // Save Button
                    Button(
                        onClick = {
                            if (dialogName.isBlank()) {
                                dialogError = "Category Name is required"
                                return@Button
                            }
                            val targetId = categoryBeingEdited?.id ?: "cat-${System.currentTimeMillis()}"
                            val newCategory = MealCategoryModel(
                                id = targetId,
                                name = dialogName.trim(),
                                startTime = dialogStartTime.trim().ifBlank { "07:00 AM" },
                                endTime = dialogEndTime.trim().ifBlank { "09:30 AM" },
                                description = dialogDescription.trim().ifBlank { null },
                                isActive = dialogIsActive,
                                displayOrder = categoryBeingEdited?.displayOrder ?: (categories.size + 1)
                            )
                            if (categoryBeingEdited != null) {
                                TiffinRepository.updateMealCategory(newCategory)
                            } else {
                                TiffinRepository.addMealCategory(newCategory)
                            }
                            isCategoryDialogOpen = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Text(
                                text = if (categoryBeingEdited != null) "Update Category" else "Create Category",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        if (showFilterSheet) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showFilterSheet = false },
                title = { Text("Filter Meal Categories", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Category Status", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.TextSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("all" to "All Categories", "active" to "Active Only", "inactive" to "Inactive").forEach { (key, lbl) ->
                                val selected = activeStatusFilter == key
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (selected) ColorTokens.PrimaryCTA else ColorTokens.SurfaceContainerLow)
                                        .clickable { activeStatusFilter = key }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        lbl,
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
                        activeStatusFilter = "all"
                        showFilterSheet = false
                    }) {
                        Text("Reset", color = ColorTokens.TextSecondary)
                    }
                }
            )
        }
    }
}
