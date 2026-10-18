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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Save
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
import com.creativeblisslabs.tiffinmanagement.data.models.KitchenHoldModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun CategoryBranchHoldsScreen(
    onNavigateBack: () -> Unit = {}
) {
    val holds by TiffinRepository.kitchenHolds.collectAsState()

    val liveBranches by TiffinRepository.branches.collectAsState()
    var selectedBranch by remember { mutableStateOf("All Branches (Global)") }
    var branchDropdownOpen by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All Meal Categories") }
    var categoryDropdownOpen by remember { mutableStateOf(false) }

    var startDate by remember { mutableStateOf("Today") }
    var endDate by remember { mutableStateOf("Today") }
    var holdMessage by remember { mutableStateOf("") }
    var holdStatus by remember { mutableStateOf("active") } // active, inactive

    val branchOptions = remember(liveBranches) {
        listOf("All Branches (Global)") + liveBranches.map { it.name }
    }
    val categoryOptions = listOf("All Meal Categories", "Breakfast", "Lunch", "Hi-Tea", "Dinner")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
       

        // 2. Info Card (Automatic Customer Notifications)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ColorTokens.PrimaryFixed.copy(alpha = 0.4f))
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = ColorTokens.Primary,
                modifier = Modifier.size(20.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Automatic Customer Notifications",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTokens.OnSurface
                )
                Text(
                    text = "Scheduled holds will automatically credit customer wallets and skip morning dispatch manifests.",
                    fontSize = 12.sp,
                    color = ColorTokens.OnSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }

        // 3. Create Hold Form Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Select Branch Dropdown
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Select Branch", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text("Optional", fontSize = 11.sp, color = ColorTokens.Secondary)
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .clickable { branchDropdownOpen = true }
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(selectedBranch, fontSize = 13.sp, color = ColorTokens.OnSurface)
                            Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.Secondary, modifier = Modifier.size(20.dp))
                        }
                        DropdownMenu(
                            expanded = branchDropdownOpen,
                            onDismissRequest = { branchDropdownOpen = false }
                        ) {
                            branchOptions.forEach { b ->
                                DropdownMenuItem(
                                    text = { Text(b, fontSize = 13.sp) },
                                    onClick = {
                                        selectedBranch = b
                                        branchDropdownOpen = false
                                    }
                                )
                            }
                        }
                    }
                    Text(
                        text = "All Branches applies to customers of all branches if left unselected.",
                        fontSize = 11.sp,
                        color = ColorTokens.OnSurfaceVariant
                    )
                }

                // Select Category Dropdown
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Select Category", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text("Optional", fontSize = 11.sp, color = ColorTokens.Secondary)
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .clickable { categoryDropdownOpen = true }
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(selectedCategory, fontSize = 13.sp, color = ColorTokens.OnSurface)
                            Icon(Icons.Default.ExpandMore, null, tint = ColorTokens.Secondary, modifier = Modifier.size(20.dp))
                        }
                        DropdownMenu(
                            expanded = categoryDropdownOpen,
                            onDismissRequest = { categoryDropdownOpen = false }
                        ) {
                            categoryOptions.forEach { c ->
                                DropdownMenuItem(
                                    text = { Text(c, fontSize = 13.sp) },
                                    onClick = {
                                        selectedCategory = c
                                        categoryDropdownOpen = false
                                    }
                                )
                            }
                        }
                    }
                    Text(
                        text = "All Categories applies to all meal categories if left unselected.",
                        fontSize = 11.sp,
                        color = ColorTokens.OnSurfaceVariant
                    )
                }

                // Start Date & End Date Grid
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
                                .height(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(startDate, fontSize = 13.sp, color = ColorTokens.OnSurface)
                            Icon(Icons.Default.CalendarToday, null, tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                        }
                    }

                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row {
                            Text("End Date", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                            Text(" *", fontSize = 12.sp, color = ColorTokens.Primary)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(endDate, fontSize = 13.sp, color = ColorTokens.OnSurface)
                            Icon(Icons.Default.Event, null, tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                // Hold Message
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row {
                        Text("Hold Message", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Text(" *", fontSize = 12.sp, color = ColorTokens.Primary)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                    ) {
                        OutlinedTextField(
                            value = holdMessage,
                            onValueChange = { if (it.length <= 140) holdMessage = it },
                            placeholder = { Text("e.g. Kitchen is closed on 20th July due to severe rain.", fontSize = 13.sp, color = ColorTokens.Secondary.copy(alpha = 0.7f)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            minLines = 3
                        )
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = null,
                            tint = ColorTokens.Secondary.copy(alpha = 0.4f),
                            modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp).size(18.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Visible to customers in order tracker", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                        Text("${holdMessage.length}/140", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                    }
                }

                // Status Radio Buttons
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Status", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { holdStatus = "active" }
                        ) {
                            RadioButton(
                                selected = holdStatus == "active",
                                onClick = { holdStatus = "active" },
                                colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Tertiary)
                            )
                            Text("Active", fontSize = 13.sp, color = if (holdStatus == "active") ColorTokens.Tertiary else ColorTokens.OnSurface)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { holdStatus = "inactive" }
                        ) {
                            RadioButton(
                                selected = holdStatus == "inactive",
                                onClick = { holdStatus = "inactive" },
                                colors = RadioButtonDefaults.colors(selectedColor = ColorTokens.Secondary)
                            )
                            Text("Inactive", fontSize = 13.sp, color = ColorTokens.OnSurfaceVariant)
                        }
                    }
                }

                // Submit Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            if (holdMessage.isNotBlank() && startDate.isNotBlank() && endDate.isNotBlank()) {
                                val branchId = liveBranches.find { it.name == selectedBranch }?.id
                                val categoryValue = if (selectedCategory == "All Meal Categories") null
                                    else selectedCategory.lowercase()
                                TiffinRepository.addKitchenHold(
                                    KitchenHoldModel(
                                        id = "hold-${System.currentTimeMillis()}",
                                        branchId = branchId, // null = global across all branches
                                        branchName = if (branchId != null) selectedBranch else "All Branches",
                                        category = categoryValue,
                                        startDate = startDate,
                                        endDate = endDate,
                                        message = holdMessage,
                                        isActive = (holdStatus == "active")
                                    )
                                )
                                holdMessage = ""
                                startDate = "Today"
                                endDate = "Today"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryCTA),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(44.dp)
                    ) {
                        Icon(Icons.Default.Save, null, tint = ColorTokens.OnPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Hold", color = ColorTokens.OnPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 4. Current Active Holds Zero-State Card
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
                    Text(
                        text = "Current Active Holds",
                        fontSize = 16.sp,
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
                            text = holds.size.toString(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.Secondary
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.clickable { }
                ) {
                    Text("View History", fontSize = 11.sp, color = ColorTokens.Primary, fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Default.ChevronRight, null, tint = ColorTokens.Primary, modifier = Modifier.size(15.dp))
                }
            }

            if (holds.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.SecondaryContainer.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EventBusy,
                                contentDescription = null,
                                tint = ColorTokens.Secondary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Text(
                            text = "Kitchen Running Smoothly",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = "No operational holds active right now. All meals and dispatch routes will operate on normal schedules.",
                            fontSize = 12.sp,
                            color = ColorTokens.OnSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.Tertiary)
                            )
                            Text(
                                text = "Live sync connected to driver manifests",
                                fontSize = 11.sp,
                                color = ColorTokens.OnSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                holds.forEach { hold ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(hold.branchName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(ColorTokens.TertiaryFixed)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("Active", fontSize = 10.sp, color = ColorTokens.OnTertiaryFixed, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(hold.message, fontSize = 12.sp, color = ColorTokens.OnSurfaceVariant)
                            Text("${hold.startDate} to ${hold.endDate} • Category: ${hold.category?.replaceFirstChar { it.uppercase() } ?: "All Categories"}", fontSize = 11.sp, color = ColorTokens.Secondary)
                        }
                    }
                }
            }
        }
    }
}
