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

data class DayMenuData(
    val dayNumber: String,
    val dayName: String,
    val tag: String,
    val isToday: Boolean = false,
    val lunchTitle: String,
    val lunchDesc: String,
    val lunchCalories: String,
    val lunchSlotTime: String,
    val dinnerTitle: String,
    val dinnerDesc: String,
    val dinnerSlotTime: String
)

@Composable
fun CustomerMonthlyMenuScheduleScreen(
    repository: TiffinRepository,
    onNavigateBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToChangePlan: () -> Unit,
    onNavigateToGuestMeal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by repository.subscriberProfile.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedWeek by remember { mutableStateOf(1) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedFilterDiet by remember { mutableStateOf("All") }

    val weekDays = remember(selectedWeek) {
        when (selectedWeek) {
            1 -> listOf(
                DayMenuData(
                    dayNumber = "04",
                    dayName = "Wednesday",
                    tag = "Today",
                    isToday = true,
                    lunchTitle = "Shahi Paneer Deluxe Meal",
                    lunchDesc = "Paneer Butter Masala, Garlic Tadka Dal, 4 Phulka Rotis, Jeera Rice, Cucumber Raita, Gulab Jamun",
                    lunchCalories = "680 kcal",
                    lunchSlotTime = "12:45 PM",
                    dinnerTitle = "Sev Tamatar & Kadhi Khichdi",
                    dinnerDesc = "Kathiyawadi Sev Tamatar, Moong Dal Khichdi, Gujarati Kadhi, 3 Rotis, Roasted Papad",
                    dinnerSlotTime = "08:15 PM"
                ),
                DayMenuData(
                    dayNumber = "05",
                    dayName = "Thursday",
                    tag = "Tomorrow",
                    isToday = false,
                    lunchTitle = "Dal Makhani & Mix Vegetable",
                    lunchDesc = "Slow-cooked Dal Makhani, Seasonal Gobi Gajjar Sabzi, 4 Desi Ghee Phulkas, Steamed Basmati Rice, Mint Chutney",
                    lunchCalories = "640 kcal",
                    lunchSlotTime = "12:45 PM",
                    dinnerTitle = "Aloo Palak & Masoor Dal",
                    dinnerDesc = "Homestyle Aloo Palak, Tadka Masoor Dal, 3 Phulka Rotis, Jeera Rice, Salad",
                    dinnerSlotTime = "08:15 PM"
                ),
                DayMenuData(
                    dayNumber = "06",
                    dayName = "Friday",
                    tag = "Action Open",
                    isToday = false,
                    lunchTitle = "Mutter Mushroom & Pulao",
                    lunchDesc = "Matar Mushroom Curry, Yellow Moong Dal, Laccha Paratha, Fragrant Veg Pulao, Boondi Raita",
                    lunchCalories = "710 kcal",
                    lunchSlotTime = "12:45 PM",
                    dinnerTitle = "Paneer Bhurji & Multigrain Rotis",
                    dinnerDesc = "Fresh Paneer Bhurji, Chana Dal, 3 Multigrain Rotis, Pickled Onion",
                    dinnerSlotTime = "08:15 PM"
                ),
                DayMenuData(
                    dayNumber = "07",
                    dayName = "Saturday",
                    tag = "Weekend Prep",
                    isToday = false,
                    lunchTitle = "Chole Bhature & Sweet Lassi",
                    lunchDesc = "Amritsari Pindi Chole, 2 Puffed Bhature, Jeera Pulao, Iced Sweet Lassi, Green Chili Pickle",
                    lunchCalories = "820 kcal",
                    lunchSlotTime = "01:00 PM",
                    dinnerTitle = "Khichdi & Gujarati Baingan Bharta",
                    dinnerDesc = "Comforting Toor Dal Khichdi, Smoked Baingan Bharta, Roasted Papad, Buttermilk",
                    dinnerSlotTime = "08:00 PM"
                )
            )
            2 -> listOf(
                DayMenuData(
                    dayNumber = "11",
                    dayName = "Friday",
                    tag = "Upcoming",
                    isToday = false,
                    lunchTitle = "Kadai Paneer & Dal Fry",
                    lunchDesc = "Bell pepper tossed Kadai Paneer, Dhaba style Dal Fry, 4 Tawa Phulkas, Steamed Rice, Mint Laccha Onion",
                    lunchCalories = "690 kcal",
                    lunchSlotTime = "12:45 PM",
                    dinnerTitle = "Lauki Kofta & Jeera Paratha",
                    dinnerDesc = "Creamy Lauki Kofta Curry, Tadka Yellow Dal, 3 Jeera Parathas, Papad",
                    dinnerSlotTime = "08:15 PM"
                ),
                DayMenuData(
                    dayNumber = "12",
                    dayName = "Saturday",
                    tag = "Upcoming",
                    isToday = false,
                    lunchTitle = "Rajma Masala & Kashmiri Pulao",
                    lunchDesc = "Slow simmered Jammu Rajma, Jeera Basmati Pulao, Aloo Jeera dry, 3 Phulkas, Boondi Raita",
                    lunchCalories = "720 kcal",
                    lunchSlotTime = "12:45 PM",
                    dinnerTitle = "Veg Handi Biryani Feast",
                    dinnerDesc = "Layered Dum Biryani, Mixed Veg Mirchi Ka Salan, Burani Raita",
                    dinnerSlotTime = "08:15 PM"
                )
            )
            3 -> listOf(
                DayMenuData(
                    dayNumber = "18",
                    dayName = "Friday",
                    tag = "Upcoming",
                    isToday = false,
                    lunchTitle = "Malai Kofta & Panchmel Dal",
                    lunchDesc = "Cashew gravy Malai Kofta, Rajasthani Panchmel Dal, 4 Phulka Rotis, Peas Pulao, Gulab Jamun",
                    lunchCalories = "760 kcal",
                    lunchSlotTime = "12:45 PM",
                    dinnerTitle = "Methi Thepla & Sukha Aloo",
                    dinnerDesc = "Gujarati Methi Theplas (4 pcs), Hing Aloo Bhaji, Chhundo & Sweet Buttermilk",
                    dinnerSlotTime = "08:15 PM"
                )
            )
            else -> listOf(
                DayMenuData(
                    dayNumber = "25",
                    dayName = "Friday",
                    tag = "Upcoming",
                    isToday = false,
                    lunchTitle = "Paneer Tikka Masala & Dal Makhani",
                    lunchDesc = "Char-grilled Paneer Tikka in rich makhani gravy, 4 Butter Phulkas, Jeera Rice, Spiced Onion",
                    lunchCalories = "740 kcal",
                    lunchSlotTime = "12:45 PM",
                    dinnerTitle = "Dal Khichdi & Masala Papad",
                    dinnerDesc = "Desi Ghee Moong Dal Khichdi, Gujarati Kadhi, Roasted Masala Papad, Achaar",
                    dinnerSlotTime = "08:15 PM"
                )
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = ColorTokens.Surface,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorTokens.Surface)
        ) {
            // Screen Header
            TiffinCustomerHeader(
                title = "Schedule",
                subtitle = "TIFFIN MANAGER",
                isActive = true,
                onNotificationClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("September Cycle A menus are certified by our nutritionist.")
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
                // Screen sub-header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.SurfaceContainer)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Go Back",
                                tint = ColorTokens.OnSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Monthly Menu Schedule",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Text(
                                text = "September 2026 • Cycle A",
                                style = MaterialTheme.typography.labelSmall,
                                color = ColorTokens.Secondary
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        IconButton(
                            onClick = { showFilterDialog = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.SurfaceContainerLow)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Filter",
                                tint = ColorTokens.OnSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Active Plan Banner Card
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
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .border(1.dp, ColorTokens.Tertiary, RoundedCornerShape(2.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(ColorTokens.Tertiary)
                                    )
                                }
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text("Active Plan", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(ColorTokens.TertiaryFixed)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text("Pure Veg", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = ColorTokens.OnTertiaryFixed)
                                        }
                                    }
                                    Text(profile.planTitle, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                                    Text("Daily Lunch & Dinner • Slot #TK-402", style = MaterialTheme.typography.bodySmall, color = ColorTokens.Secondary)
                                }
                            }

                            Button(
                                onClick = onNavigateToChangePlan,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ColorTokens.PrimaryFixed,
                                    contentColor = ColorTokens.OnPrimaryFixed
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("Change", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PinDrop,
                                    contentDescription = null,
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Delivery to: ${profile.flatDetails}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ColorTokens.Secondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            TextButton(onClick = onNavigateToProfile) {
                                Text("Edit", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                            }
                        }
                    }
                }

                // Week Selector Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    (1..4).forEach { week ->
                        val isSelected = selectedWeek == week
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) ColorTokens.Primary else ColorTokens.SurfaceContainerLowest)
                                .clickable { selectedWeek = week }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (week == 1) "Week 1 (Now)" else "Week $week",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) ColorTokens.OnPrimary else ColorTokens.Secondary
                            )
                        }
                    }
                }

                // Days Cards Stack
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    weekDays.forEach { day ->
                        DayMenuCard(
                            day = day,
                            onSkipLunch = {
                                repository.skipDinnerWithRefund()
                                scope.launch {
                                    snackbarHostState.showSnackbar("Skipped lunch for ${day.dayName}. ₹65 refunded to Wallet.")
                                }
                            },
                            onAddGuestMeal = onNavigateToGuestMeal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (showFilterDialog) {
            val filters = listOf("All", "Pure Veg", "Jain (No Onion/Garlic)", "High Protein")
            AlertDialog(
                onDismissRequest = { showFilterDialog = false },
                title = {
                    Text("Dietary Filter", fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        filters.forEach { filter ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedFilterDiet == filter) ColorTokens.PrimaryFixed else ColorTokens.SurfaceContainerLow)
                                    .clickable {
                                        selectedFilterDiet = filter
                                        showFilterDialog = false
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Filter applied: $filter")
                                        }
                                    }
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = filter,
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedFilterDiet == filter) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedFilterDiet == filter) ColorTokens.OnPrimaryFixed else ColorTokens.OnSurface
                                )
                                if (selectedFilterDiet == filter) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showFilterDialog = false }) {
                        Text("Close", color = ColorTokens.Primary)
                    }
                }
            )
        }
    }
}

@Composable
private fun DayMenuCard(
    day: DayMenuData,
    onSkipLunch: () -> Unit,
    onAddGuestMeal: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = if (day.isToday) 3.dp else 1.dp),
        border = if (day.isToday) androidx.compose.foundation.BorderStroke(1.5.dp, ColorTokens.Primary) else null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Date Header
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
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (day.isToday) ColorTokens.Primary else ColorTokens.SurfaceContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.dayNumber,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (day.isToday) ColorTokens.OnPrimary else ColorTokens.OnSurface
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = day.dayName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (day.isToday) ColorTokens.PrimaryFixed else ColorTokens.SurfaceContainerHigh)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = day.tag,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (day.isToday) ColorTokens.OnPrimaryFixed else ColorTokens.Secondary
                                )
                            }
                        }
                        Text(
                            text = "Sep 2026 • Normal Kitchen Shift",
                            style = MaterialTheme.typography.labelSmall,
                            color = ColorTokens.Secondary
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = ColorTokens.Tertiary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Lunch slot
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ColorTokens.SurfaceContainerLow)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
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
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = ColorTokens.Primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text("LUNCH DISPATCH", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        Text("• ${day.lunchSlotTime}", style = MaterialTheme.typography.labelSmall, color = ColorTokens.Secondary)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(ColorTokens.SurfaceContainer)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(day.lunchCalories, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = ColorTokens.OnSurface)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(ColorTokens.TertiaryFixed)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Veg", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnTertiaryFixed)
                        }
                    }
                }

                Text(
                    text = day.lunchTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = ColorTokens.OnSurface
                )
                Text(
                    text = day.lunchDesc,
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorTokens.Secondary
                )
            }

            // Dinner slot
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ColorTokens.SurfaceContainerLow)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
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
                        Icon(
                            imageVector = Icons.Default.Bedtime,
                            contentDescription = null,
                            tint = ColorTokens.Secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text("DINNER DISPATCH", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        Text("• ${day.dinnerSlotTime}", style = MaterialTheme.typography.labelSmall, color = ColorTokens.Secondary)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(ColorTokens.TertiaryFixed)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("Veg", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnTertiaryFixed)
                    }
                }

                Text(
                    text = day.dinnerTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = ColorTokens.OnSurface
                )
                Text(
                    text = day.dinnerDesc,
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorTokens.Secondary
                )
            }

            // Quick Actions if tomorrow or later
            if (!day.isToday) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onSkipLunch,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = ColorTokens.SurfaceContainerHigh,
                            contentColor = ColorTokens.OnSurface
                        ),
                        border = null
                    ) {
                        Icon(imageVector = Icons.Default.EventBusy, contentDescription = null, tint = ColorTokens.Error, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Skip Lunch", style = MaterialTheme.typography.labelSmall)
                    }

                    Button(
                        onClick = onAddGuestMeal,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorTokens.PrimaryFixed,
                            contentColor = ColorTokens.OnPrimaryFixed
                        )
                    ) {
                        Icon(imageVector = Icons.Default.GroupAdd, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Guest Meal", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
