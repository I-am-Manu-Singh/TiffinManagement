package com.creativeblisslabs.tiffinmanagement.ui.screens.customer

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinCustomerHeader
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import kotlinx.coroutines.launch

data class IssueCategoryOption(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun CustomerReportMealIssueScreen(
    repository: TiffinRepository,
    onNavigateBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val riderProfile by repository.riderProfile.collectAsState()
    val driverName = riderProfile.fullName.ifBlank { "Your Delivery Partner" }

    val categories = remember {
        listOf(
            IssueCategoryOption("Food Quality / Taste Issue", Icons.Default.Restaurant),
            IssueCategoryOption("Missing Item / Wrong Dish", Icons.Default.Inventory2),
            IssueCategoryOption("Spill or Packaging Leak", Icons.Default.SoupKitchen),
            IssueCategoryOption("Tiffin Delayed / Late", Icons.Default.Schedule),
            IssueCategoryOption("Driver / Drop Location", Icons.Default.Explore),
            IssueCategoryOption("Billing & Wallet Query", Icons.Default.AccountBalanceWallet)
        )
    }
    var selectedCategory by remember { mutableStateOf(categories[1].title) }

    val availableTags = remember {
        listOf("Raita missing", "Roti was hard/cold", "Dal spilled", "Too spicy", "Portion too small")
    }
    val selectedTags = remember { mutableStateListOf("Raita missing") }

    var issueDescription by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = ColorTokens.Surface,
        bottomBar = {
            Surface(
                color = ColorTokens.Surface.copy(alpha = 0.95f),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Button(
                        onClick = {
                            repository.submitMealIssue(
                                category = selectedCategory,
                                tags = selectedTags.toList(),
                                description = issueDescription
                            )
                            isSubmitted = true
                            scope.launch {
                                snackbarHostState.showSnackbar("Ticket submitted! Kitchen manager reviewing (Guaranteed 15m resolution).")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorTokens.Primary,
                            contentColor = ColorTokens.OnPrimary
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Submit Issue Ticket", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorTokens.Surface)
        ) {
            // Header
            TiffinCustomerHeader(
                title = "Report Issue",
                subtitle = "TIFFIN MANAGER",
                isActive = true,
                onNotificationClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Instant resolution active: 15-minute response SLA.")
                    }
                },
                onProfileClick = onNavigateToProfile
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                // Recent Meal Context Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("TARGET DELIVERY", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Secondary)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.TertiaryFixed.copy(alpha = 0.4f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = ColorTokens.Tertiary, modifier = Modifier.size(12.dp))
                                    Text("Delivered 12:48 PM", style = MaterialTheme.typography.labelSmall, color = ColorTokens.Tertiary)
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Restaurant, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(28.dp))
                            }
                            Column {
                                Text("Shahi Paneer Deluxe Meal", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text("Today's Delivery • $driverName", style = MaterialTheme.typography.bodySmall, color = ColorTokens.Secondary)
                            }
                        }
                    }
                }

                // What went wrong category selector
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("What went wrong?", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        Text("Step 1 of 2", style = MaterialTheme.typography.labelSmall, color = ColorTokens.Secondary)
                    }

                    // 2-column grid of 6 categories
                    val rows = categories.chunked(2)
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { cat ->
                                val isSelected = selectedCategory == cat.title
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(96.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) ColorTokens.PrimaryFixed.copy(alpha = 0.35f) else ColorTokens.SurfaceContainerLowest)
                                        .border(
                                            width = if (isSelected) 1.5.dp else 1.dp,
                                            color = if (isSelected) ColorTokens.Primary else ColorTokens.OutlineVariant.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { selectedCategory = cat.title }
                                        .padding(10.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(if (isSelected) ColorTokens.Primary else ColorTokens.SurfaceContainer),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = cat.icon,
                                                    contentDescription = null,
                                                    tint = if (isSelected) ColorTokens.OnPrimary else ColorTokens.Secondary,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = ColorTokens.Primary,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }

                                        Text(
                                            text = cat.title,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnSurface,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Issue tags card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Specific Issue Tags", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)

                        // Flow tags
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            availableTags.take(3).forEach { tag ->
                                val isSelected = selectedTags.contains(tag)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                                    },
                                    label = { Text(tag, style = MaterialTheme.typography.labelSmall) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ColorTokens.PrimaryFixed,
                                        selectedLabelColor = ColorTokens.OnPrimaryFixed
                                    )
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            availableTags.drop(3).forEach { tag ->
                                val isSelected = selectedTags.contains(tag)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                                    },
                                    label = { Text(tag, style = MaterialTheme.typography.labelSmall) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ColorTokens.PrimaryFixed,
                                        selectedLabelColor = ColorTokens.OnPrimaryFixed
                                    )
                                )
                            }
                        }

                        // Photo Proof Attachment
                        Text("Proof Attachment (Recommended)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = null, tint = ColorTokens.Secondary, modifier = Modifier.size(28.dp))
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(72.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .clickable {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Camera / Gallery photo attached.")
                                        }
                                    }
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(20.dp))
                                    Text("Add more photos", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                    Text("JPG, PNG up to 5MB", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = ColorTokens.Secondary)
                                }
                            }
                        }

                        // Description for Head Chef
                        Text("Description for Head Chef", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = ColorTokens.OnSurface)
                        OutlinedTextField(
                            value = issueDescription,
                            onValueChange = { issueDescription = it },
                            placeholder = { Text("Describe what went wrong so kitchen head chef can fix it immediately...", style = MaterialTheme.typography.bodySmall) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ColorTokens.Primary,
                                unfocusedBorderColor = ColorTokens.OutlineVariant
                            )
                        )
                    }
                }

                // Resolution SLA Banner
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SecondaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.OnSecondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(20.dp))
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("Guaranteed Resolution in 15 mins", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = ColorTokens.OnSecondaryFixed)
                            Text(
                                text = "The kitchen manager will review your ticket and either dispatch an instant fresh meal replacement or immediately credit ₹120.00 meal cost back to your Mess Wallet.",
                                style = MaterialTheme.typography.bodySmall,
                                color = ColorTokens.OnSecondaryContainer
                            )
                        }
                    }
                }

                // Urgent Contact Shortcuts
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("URGENT SUPPORT CHANNELS", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Secondary)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Connecting to Kitchen Dispatcher...")
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ColorTokens.SurfaceContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = ColorTokens.OnSurface, modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("Call Kitchen", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                    Text("Direct Dispatcher", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = ColorTokens.Secondary)
                                }
                            }
                        }

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Opening WhatsApp Support Desk")
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ColorTokens.TertiaryFixed),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.Chat, contentDescription = null, tint = ColorTokens.OnTertiaryFixed, modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text("WhatsApp Desk", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                    Text("Online now", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = ColorTokens.Tertiary)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
