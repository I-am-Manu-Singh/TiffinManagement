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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.creativeblisslabs.tiffinmanagement.data.models.MealPlanModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinTopHeader

@Composable
fun MealPlansScreen(
    onBack: () -> Unit = {},
    onCreatePlan: () -> Unit = {},
    onEditPlan: (MealPlanModel) -> Unit = {}
) {
    val mealPlans by TiffinRepository.mealPlans.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedPageSize by remember { mutableStateOf("15") }
    var pageSizeDropdownOpen by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf("all") }

    val categories = remember(mealPlans) {
        listOf("all") + mealPlans.map { it.categoryName }.distinct()
    }

    val filteredPlans = remember(mealPlans, searchQuery, selectedCategoryFilter) {
        mealPlans.filter { plan ->
            val matchesQuery = searchQuery.isBlank() ||
                plan.name.contains(searchQuery, ignoreCase = true) ||
                plan.categoryName.contains(searchQuery, ignoreCase = true)
            val matchesCat = selectedCategoryFilter == "all" || plan.categoryName.equals(selectedCategoryFilter, ignoreCase = true)
            matchesQuery && matchesCat
        }
    }

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

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selectedCategoryFilter != "all") ColorTokens.PrimaryContainer else ColorTokens.SurfaceContainerLow)
                            .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(10.dp))
                            .clickable { showFilterSheet = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = if (selectedCategoryFilter != "all") ColorTokens.Primary else ColorTokens.Secondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search meal plans...", fontSize = 12.sp, color = ColorTokens.TextSecondary) },
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
                        onClick = onCreatePlan,
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.Primary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(15.dp))
                            Text("Create", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Cards
            val limit = selectedPageSize.toIntOrNull() ?: 15
            val displayedPlans = filteredPlans.take(limit)

            if (displayedPlans.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest)
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No meal plans found.", fontSize = 13.sp, color = ColorTokens.TextSecondary)
                    }
                }
            } else {
                displayedPlans.forEach { plan ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Header: Drag + Dish icon + Name & badging
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(end = if (plan.isFeatured) 70.dp else 0.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // 6-dot reorder grip
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

                                    // Cutlery Icon
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(ColorTokens.SurfaceContainerLow)
                                            .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.RestaurantMenu,
                                            contentDescription = null,
                                            tint = ColorTokens.Primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = plan.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.Primary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(ColorTokens.SurfaceContainerLow)
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(plan.categoryName, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.Secondary)
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(ColorTokens.VegBadgeBg)
                                                    .border(1.dp, ColorTokens.TertiaryFixed, RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(if (plan.isVeg) "Veg" else "Non-Veg", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.VegBadgeText)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Daily Rate & Description
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("₹${"%.2f".format(plan.dailyRate)}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = ColorTokens.OnSurface)
                                    Text("/ day", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = ColorTokens.TextSecondary)
                                }
                                if (plan.description.isNotBlank()) {
                                    Text(
                                        text = plan.description,
                                        fontSize = 12.sp,
                                        color = ColorTokens.TextSecondary,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Weekly & Monthly rates 2-col box
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(ColorTokens.SurfaceContainerLow)
                                        .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(12.dp))
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Weekly", fontSize = 11.sp, color = ColorTokens.TextSecondary, fontWeight = FontWeight.Medium)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("₹${"%.2f".format(plan.weeklyRate)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                    }
                                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(ColorTokens.BorderSubtle))
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Monthly", fontSize = 11.sp, color = ColorTokens.TextSecondary, fontWeight = FontWeight.Medium)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("₹${"%,.2f".format(plan.monthlyRate)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Delivery Days
                                Text("Delivery Days:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.Secondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    val days = listOf("S", "M", "T", "W", "T", "F", "S")
                                    days.forEach { day ->
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(ColorTokens.Tertiary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(day, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorTokens.BorderSubtle))
                                Spacer(modifier = Modifier.height(10.dp))

                                // Composition items & Status Pill
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Composition: ", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                                        Text("${plan.compositionItemsCount} items", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                    }

                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(22.dp))
                                            .background(Color(0xFFECFDF5))
                                            .border(1.dp, Color(0xFFA7F3D0), RoundedCornerShape(22.dp))
                                            .padding(horizontal = 8.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(ColorTokens.TertiaryMint))
                                        Text("Active", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.Tertiary)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Actions
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(1.dp, ColorTokens.Primary, RoundedCornerShape(8.dp))
                                            .clickable { onEditPlan(plan) }
                                            .padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Default.Edit, null, tint = ColorTokens.Primary, modifier = Modifier.size(13.dp))
                                        Text("Edit", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(1.dp, Color(0xFFF87171), RoundedCornerShape(8.dp))
                                            .clickable { TiffinRepository.deleteMealPlan(plan.id) }
                                            .padding(horizontal = 12.dp, vertical = 6.dp),
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

            // Secondary helper dashed box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, ColorTokens.BorderSubtle, RoundedCornerShape(16.dp))
                    .clickable { onCreatePlan() }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ColorTokens.PrimaryFixed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, null, tint = ColorTokens.Primary, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Need another meal plan?", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Create specialized packages for dinner, breakfast or customized corporate meals.", fontSize = 11.sp, color = ColorTokens.TextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        }

        if (showFilterSheet) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showFilterSheet = false },
                title = { Text("Filter Meal Plans", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Category", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorTokens.TextSecondary)
                        androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(categories.size) { idx ->
                                val cat = categories[idx]
                                val selected = selectedCategoryFilter.equals(cat, ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (selected) ColorTokens.PrimaryCTA else ColorTokens.SurfaceContainerLow)
                                        .clickable { selectedCategoryFilter = cat }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = if (cat == "all") "All Categories" else cat,
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
                        selectedCategoryFilter = "all"
                        showFilterSheet = false
                    }) {
                        Text("Reset", color = ColorTokens.TextSecondary)
                    }
                }
            )
        }
    }
}
