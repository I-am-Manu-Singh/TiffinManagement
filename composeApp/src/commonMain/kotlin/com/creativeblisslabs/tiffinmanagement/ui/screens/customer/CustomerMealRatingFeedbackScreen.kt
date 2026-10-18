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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.models.MealRatingFeedback
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinCustomerHeader
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import kotlinx.coroutines.launch

@Composable
fun CustomerMealRatingFeedbackScreen(
    repository: TiffinRepository,
    onNavigateBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToReportIssue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var starRating by remember { mutableStateOf(4) }
    var portionOption by remember { mutableStateOf("Just Right") }
    var temperatureOption by remember { mutableStateOf("Lukewarm") }
    var chefNotes by remember { mutableStateOf("") }

    val dishItems = remember {
        listOf(
            "Paneer Butter Masala",
            "Garlic Tadka Dal",
            "4 Phulka Rotis",
            "Jeera Basmati Rice",
            "Gulab Jamun (1 pc)"
        )
    }
    val dishSentiments = remember {
        mutableStateMapOf(
            "Paneer Butter Masala" to "good",
            "Garlic Tadka Dal" to "good",
            "4 Phulka Rotis" to "neutral",
            "Jeera Basmati Rice" to "good",
            "Gulab Jamun (1 pc)" to "good"
        )
    }

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
                            repository.submitMealRating(
                                MealRatingFeedback(
                                    overallRating = starRating,
                                    dishRatings = dishSentiments.toMap(),
                                    portionCheck = portionOption,
                                    foodTemperature = temperatureOption,
                                    chefNote = chefNotes
                                )
                            )
                            scope.launch {
                                snackbarHostState.showSnackbar("Thank you! Your feedback reached the kitchen chef.")
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
                        Text("Submit Meal Rating & Review", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
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
                title = "Meal Rating",
                subtitle = "TIFFIN MANAGER",
                isActive = true,
                onNotificationClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Daily rating rewards you with loyalty badges!")
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
              

                // Delivered Recap Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ColorTokens.PrimaryFixed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = ColorTokens.Primary,
                                modifier = Modifier.size(36.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(4.dp)
                                    .size(10.dp)
                                    .border(1.dp, ColorTokens.Tertiary, RoundedCornerShape(2.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.Tertiary)
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(ColorTokens.Tertiary)
                                )
                                Text(
                                    text = "Delivered • Today, 12:48 PM",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ColorTokens.Secondary
                                )
                            }
                            Text(
                                text = "Shahi Paneer Deluxe Meal",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Text(
                                text = "Regular Plan • Mess Subscription",
                                style = MaterialTheme.typography.bodySmall,
                                color = ColorTokens.OnSurfaceVariant
                            )
                            Row(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = ColorTokens.Tertiary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "Verified doorstep drop",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }
                    }
                }

                // Overall Experience Rating Hero
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.PrimaryFixed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestaurantMenu,
                                contentDescription = null,
                                tint = ColorTokens.Primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Text(
                            text = "How was your lunch today?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = "Tap a star to share quick feedback with our kitchen",
                            style = MaterialTheme.typography.bodySmall,
                            color = ColorTokens.Secondary
                        )

                        // 5 Stars Row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            (1..5).forEach { star ->
                                IconButton(
                                    onClick = { starRating = star },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = if (star <= starRating) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = "Star $star",
                                        tint = if (star <= starRating) ColorTokens.Primary else ColorTokens.SurfaceContainerHighest,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }

                        // Sentiment Chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(ColorTokens.PrimaryFixed)
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SentimentVerySatisfied,
                                    contentDescription = null,
                                    tint = ColorTokens.OnPrimaryFixed,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = when (starRating) {
                                        5 -> "Outstanding & Truly Delicious!"
                                        4 -> "Delicious & Satisfying!"
                                        3 -> "Average, Can be Improved"
                                        2 -> "Disappointing Experience"
                                        else -> "Needs Urgent Attention"
                                    },
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnPrimaryFixed
                                )
                            }
                        }
                    }
                }

                // Dish-by-Dish Rating Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dish-by-Dish Rating",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                    Text(
                        text = "${dishItems.size} items",
                        style = MaterialTheme.typography.labelSmall,
                        color = ColorTokens.Secondary
                    )
                }

                // Dish cards
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    dishItems.forEach { dish ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
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
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .border(1.dp, ColorTokens.Tertiary, RoundedCornerShape(2.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(ColorTokens.Tertiary)
                                        )
                                    }
                                    Text(
                                        text = dish,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ColorTokens.OnSurface
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    val sentimentOptions = listOf(
                                        Triple("bad", Icons.Default.SentimentDissatisfied, Color(0xFFEF4444)),
                                        Triple("neutral", Icons.Default.SentimentNeutral, Color(0xFFF59E0B)),
                                        Triple("good", Icons.Default.SentimentSatisfied, Color(0xFF10B981))
                                    )
                                    sentimentOptions.forEach { (key, icon, tint) ->
                                        val isSelected = dishSentiments[dish] == key
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isSelected) tint.copy(alpha = 0.2f) else ColorTokens.SurfaceContainer
                                                )
                                                .clickable { dishSentiments[dish] = key },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = key,
                                                tint = if (isSelected) tint else ColorTokens.OnSurfaceVariant,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Meal Experience Checks Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Meal Experience Checks",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )

                        // Portion Size
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Portion Size", style = MaterialTheme.typography.labelMedium, color = ColorTokens.Secondary)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Too Little", "Just Right", "Extra").forEach { option ->
                                    val isSelected = portionOption == option
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isSelected) ColorTokens.Primary else ColorTokens.SurfaceContainer)
                                            .clickable { portionOption = option }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = option,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) ColorTokens.OnPrimary else ColorTokens.Secondary
                                        )
                                    }
                                }
                            }
                        }

                        // Food Temperature
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Food Temperature", style = MaterialTheme.typography.labelMedium, color = ColorTokens.Secondary)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Hot & Fresh", "Lukewarm", "Cold").forEach { option ->
                                    val isSelected = temperatureOption == option
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(if (isSelected) ColorTokens.Primary else ColorTokens.SurfaceContainer)
                                            .clickable { temperatureOption = option }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = option,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) ColorTokens.OnPrimary else ColorTokens.Secondary
                                        )
                                    }
                                }
                            }
                        }

                        // Packaging & Hygiene
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Packaging & Hygiene", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                Text("Airtight, sealed container", style = MaterialTheme.typography.bodySmall, color = ColorTokens.Secondary)
                            }
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ColorTokens.TertiaryFixed)
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = ColorTokens.Tertiary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text("Spill-free 5/5", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = ColorTokens.OnTertiaryFixed)
                            }
                        }
                    }
                }

                // Chef Note Text Area
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Comments & Note for Head Chef",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        OutlinedTextField(
                            value = chefNotes,
                            onValueChange = { chefNotes = it },
                            placeholder = { Text("Tell the chef about spices, salt, or packing...", style = MaterialTheme.typography.bodySmall) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ColorTokens.Primary,
                                unfocusedBorderColor = ColorTokens.OutlineVariant
                            )
                        )
                    }
                }

                // Help Desk Link
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = onNavigateToReportIssue) {
                        Icon(imageVector = Icons.Default.ReportProblem, contentDescription = null, tint = ColorTokens.Error, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Had a serious issue with this meal? Report here", style = MaterialTheme.typography.labelSmall, color = ColorTokens.Error)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
