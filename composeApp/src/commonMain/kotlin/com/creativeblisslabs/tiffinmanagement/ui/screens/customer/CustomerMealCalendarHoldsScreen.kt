package com.creativeblisslabs.tiffinmanagement.ui.screens.customer

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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinCustomerHeader
import kotlinx.coroutines.launch

/**
 * Customer / Subscriber Meal Calendar & Vacation Holds Screen
 * Matches stitch_cross_platform_universal_ui_design 11/meal_calendar_holds
 * Features:
 * - Active Monthly Plan Summary: Standard 2-Meal Pack ₹3,900/mo, Auto-renews Oct 01, Mon-Sat active
 * - Interactive September 2026 Calendar Grid:
 *   - Legend: Active (Green), Vacation Hold (Red/Orange), Off-Day (Sunday cross), Today (Sept 04 circle)
 *   - Dates 08 to 12 marked as active vacation pause
 * - Schedule a Vacation Hold / Pause booking card:
 *   - Cutoff alert pill: 08:00 AM daily cutoff
 *   - Start Date (08 Sep 2026) & End Date (12 Sep 2026)
 *   - Meals to Pause selector: Both, Lunch Only, Dinner Only
 *   - Pause Reason selector: College Exams / Semester Break
 *   - Wallet Refund Credit preview (+₹650.00 for 5 active days / 10 meals)
 *   - "Confirm Vacation Hold" button
 * - Scheduled Holds List: Diwali Break Hold (14 Oct - 18 Oct, 10 meals), Modify & Cancel Hold actions
 * - Dietary & Kitchen Preference Settings: Pure Veg, Jain, Regular Veg, Spice preference (Mild, Medium, Fiery)
 */
@Composable
fun CustomerMealCalendarHoldsScreen(
    onNavigateToWallet: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val subscriberProfile by TiffinRepository.subscriberProfile.collectAsState()
    val vacationHolds by TiffinRepository.vacationHolds.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var currentMonthIndex by remember { mutableStateOf(0) } // 0: September 2026, 1: October 2026
    val monthNames = listOf("September 2026", "October 2026")
    var startDateText by remember { mutableStateOf("08 Sep 2026") }
    var endDateText by remember { mutableStateOf("12 Sep 2026") }
    var daysCount by remember { mutableStateOf(5) }
    var selectedPauseType by remember { mutableStateOf("both") } // "both", "lunch_only", "dinner_only"
    var selectedReason by remember { mutableStateOf("College Exams / Semester Break") }
    var showReasonDialog by remember { mutableStateOf(false) }
    var showStartDateDialog by remember { mutableStateOf(false) }
    var selectedDietStandard by remember { mutableStateOf("Pure Veg") }
    var selectedSpiceLevel by remember { mutableStateOf("Medium Spicy") }

    var showCancelHoldDialog by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Screen Header
            TiffinCustomerHeader(
                title = "Schedule",
                isActive = true,
                subtitle = "TIFFIN MANAGER",
                onNotificationClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Your vacation holds are synchronized with cloud kitchen dispatcher.")
                    }
                },
                onProfileClick = onNavigateToProfile
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Active Plan Summary Card
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ColorTokens.SecondaryContainer)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RestaurantMenu,
                                        contentDescription = null,
                                        tint = ColorTokens.Primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Active Monthly Plan",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSecondaryContainer
                                    )
                                }
                            }

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
                                    text = "Auto-Renews Oct 01",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.Tertiary
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "Standard 2–Meal Pack",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "₹3,900",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.Primary
                                )
                                Text(
                                    text = "/mo",
                                    fontSize = 12.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WbSunny,
                                    contentDescription = null,
                                    tint = ColorTokens.Tertiary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(text = "Lunch", fontSize = 12.sp, color = ColorTokens.OnSurface)
                            }
                            Text(text = "•", color = ColorTokens.Secondary)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DarkMode,
                                    contentDescription = null,
                                    tint = ColorTokens.PrimaryContainer,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(text = "Dinner", fontSize = 12.sp, color = ColorTokens.OnSurface)
                            }
                            Text(text = "•", color = ColorTokens.Secondary)
                            Text(
                                text = "Mon to Sat (Sunday Off)",
                                fontSize = 12.sp,
                                color = ColorTokens.Secondary
                            )
                        }
                    }
                }

                // 2. September 2026 Calendar Grid Module
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
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Month Header
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
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = monthNames[currentMonthIndex],
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ColorTokens.SurfaceContainer)
                                        .clickable {
                                            currentMonthIndex = (currentMonthIndex - 1).coerceAtLeast(0)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ChevronLeft,
                                        contentDescription = "Previous Month",
                                        tint = if (currentMonthIndex > 0) ColorTokens.Primary else ColorTokens.Secondary.copy(alpha = 0.4f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ColorTokens.SurfaceContainer)
                                        .clickable {
                                            currentMonthIndex = (currentMonthIndex + 1).coerceAtMost(monthNames.lastIndex)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = "Next Month",
                                        tint = if (currentMonthIndex < monthNames.lastIndex) ColorTokens.Primary else ColorTokens.Secondary.copy(alpha = 0.4f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        // Calendar Legend Matrix
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                LegendItem(color = ColorTokens.Tertiary, label = "Active (Lunch & Dinner)")
                                LegendItem(color = ColorTokens.Primary, label = "Vacation Hold (Paused)")
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                LegendItem(color = ColorTokens.OutlineVariant, label = "Off-Day (Sunday)")
                                LegendItem(color = ColorTokens.SecondaryContainer, label = "Today (Sept 04)")
                            }
                        }

                        // Weekdays header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            listOf("M", "T", "W", "T", "F", "S", "S").forEachIndexed { index, day ->
                                Text(
                                    text = day,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (index == 6) ColorTokens.Outline else ColorTokens.Secondary,
                                    modifier = Modifier.width(32.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // September 2026 Days Matrix (Starts on Tuesday, Sept 1)
                        // Row 1: 31 (Aug), 1, 2, 3, 4 (Today), 5, 6 (Sun)
                        CalendarRow(
                            days = listOf(
                                DayData("31", isInactive = true),
                                DayData("1", status = "active"),
                                DayData("2", status = "active"),
                                DayData("3", status = "active"),
                                DayData("4", status = "today"),
                                DayData("5", status = "active"),
                                DayData("6", status = "off")
                            )
                        )

                        // Row 2: 7, 8 (Hold), 9 (Hold), 10 (Hold), 11 (Hold), 12 (Hold), 13 (Sun)
                        CalendarRow(
                            days = listOf(
                                DayData("7", status = "active"),
                                DayData("8", status = "hold"),
                                DayData("9", status = "hold"),
                                DayData("10", status = "hold"),
                                DayData("11", status = "hold"),
                                DayData("12", status = "hold"),
                                DayData("13", status = "off")
                            )
                        )

                        // Row 3: 14 to 20
                        CalendarRow(
                            days = listOf(
                                DayData("14", status = "active"),
                                DayData("15", status = "active"),
                                DayData("16", status = "active"),
                                DayData("17", status = "active"),
                                DayData("18", status = "active"),
                                DayData("19", status = "active"),
                                DayData("20", status = "off")
                            )
                        )

                        // Row 4: 21 to 27
                        CalendarRow(
                            days = listOf(
                                DayData("21", status = "active"),
                                DayData("22", status = "active"),
                                DayData("23", status = "active"),
                                DayData("24", status = "active"),
                                DayData("25", status = "active"),
                                DayData("26", status = "active"),
                                DayData("27", status = "off")
                            )
                        )

                        // Row 5: 28 to 30 + Next Month
                        CalendarRow(
                            days = listOf(
                                DayData("28", status = "active"),
                                DayData("29", status = "active"),
                                DayData("30", status = "active"),
                                DayData("01", isInactive = true),
                                DayData("02", isInactive = true),
                                DayData("03", isInactive = true),
                                DayData("04", isInactive = true)
                            )
                        )
                    }
                }

                // 3. Schedule a Vacation Hold / Pause Form Card
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
                        // Cutoff alert pill
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainer)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = null,
                                tint = ColorTokens.Primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Daily cutoff for morning meal holds is 08:00 AM.",
                                fontSize = 11.sp,
                                color = ColorTokens.Secondary
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Schedule a Vacation Hold / Pause",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Text(
                                text = "Going home or out of station? Pause your tiffin in 1-tap. Balance is credited instantly to your wallet.",
                                fontSize = 11.sp,
                                color = ColorTokens.Secondary,
                                lineHeight = 15.sp
                            )
                        }

                        // Date Pickers Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .clickable { showStartDateDialog = true }
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "Start Date",
                                        fontSize = 10.sp,
                                        color = ColorTokens.Secondary
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = startDateText,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnSurface
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Event,
                                            contentDescription = null,
                                            tint = ColorTokens.Primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .clickable { showStartDateDialog = true }
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "End Date (Inclusive)",
                                        fontSize = 10.sp,
                                        color = ColorTokens.Secondary
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = endDateText,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnSurface
                                        )
                                        Icon(
                                            imageVector = Icons.Default.EventAvailable,
                                            contentDescription = null,
                                            tint = ColorTokens.Primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Meals to Pause Selector
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Meals to Pause",
                                fontSize = 11.sp,
                                color = ColorTokens.Secondary
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("both" to "Both", "lunch" to "Lunch Only", "dinner" to "Dinner Only").forEach { (type, label) ->
                                    val isSelected = selectedPauseType == type
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (isSelected) ColorTokens.Primary else ColorTokens.SurfaceContainer
                                            )
                                            .clickable { selectedPauseType = type }
                                            .padding(vertical = 9.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = ColorTokens.OnPrimary,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                            }
                                            Text(
                                                text = label,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) ColorTokens.OnPrimary else ColorTokens.OnSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Reason Selector Box
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Pause Reason",
                                fontSize = 11.sp,
                                color = ColorTokens.Secondary
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorTokens.SurfaceContainerLow)
                                    .clickable { showReasonDialog = true }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        tint = ColorTokens.Secondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = selectedReason,
                                        fontSize = 12.sp,
                                        color = ColorTokens.OnSurface
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = ColorTokens.Secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Wallet Refund Credit Preview Banner
                        val refundPerMeal = 65.0
                        val mealsPerDay = if (selectedPauseType == "both") 2 else 1
                        val totalMeals = daysCount * mealsPerDay
                        val totalRefund = totalMeals * refundPerMeal

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.TertiaryFixed)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Savings,
                                    contentDescription = null,
                                    tint = ColorTokens.Tertiary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                    Text(
                                        text = "Wallet Refund Credit",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnTertiaryFixed
                                    )
                                    Text(
                                        text = "$daysCount active days • $totalMeals total meals",
                                        fontSize = 10.sp,
                                        color = ColorTokens.OnTertiaryFixed.copy(alpha = 0.8f)
                                    )
                                }
                            }

                            Text(
                                text = "+₹${totalRefund.toInt()}.00",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.Tertiary
                            )
                        }

                        // CTA Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.Primary)
                                .clickable {
                                    TiffinRepository.addVacationHold(
                                        startDate = startDateText,
                                        endDate = endDateText,
                                        pauseType = selectedPauseType,
                                        daysCount = daysCount
                                    )
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Vacation Hold Confirmed ($startDateText - $endDateText)! ₹${totalRefund.toInt()}.00 scheduled for wallet auto-credit.")
                                    }
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PauseCircle,
                                    contentDescription = null,
                                    tint = ColorTokens.OnPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Confirm Vacation Hold",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnPrimary
                                )
                            }
                        }
                    }
                }

                // 4. Scheduled Holds List
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Scheduled Holds",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = "${vacationHolds.size} Active",
                            fontSize = 11.sp,
                            color = ColorTokens.Secondary
                        )
                    }

                    vacationHolds.forEach { hold ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
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
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(ColorTokens.PrimaryFixed),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.FlightTakeoff,
                                                contentDescription = null,
                                                tint = ColorTokens.Primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                            Text(
                                                text = "Diwali Break Hold",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ColorTokens.OnSurface
                                            )
                                            Text(
                                                text = "${hold.startDateFormatted} - ${hold.endDateFormatted} (${hold.daysCount * 2} meals)",
                                                fontSize = 11.sp,
                                                color = ColorTokens.Secondary
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(ColorTokens.PrimaryFixed)
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = hold.status,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnPrimaryFixed
                                        )
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
                                    Text(
                                        text = "Saved to Wallet: ₹${hold.refundCreditAmount}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "Ready for Auto-credit",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ColorTokens.Tertiary
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(ColorTokens.SurfaceContainer)
                                            .clickable {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Opening modify window for ${hold.startDateFormatted} hold...")
                                                }
                                            }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.EditCalendar,
                                                contentDescription = null,
                                                tint = ColorTokens.OnSurface,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Text(
                                                text = "Modify",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ColorTokens.OnSurface
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(ColorTokens.ErrorContainer)
                                            .clickable { showCancelHoldDialog = hold.id }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Cancel,
                                                contentDescription = null,
                                                tint = ColorTokens.Error,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Text(
                                                text = "Cancel Hold",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ColorTokens.OnErrorContainer
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 5. Dietary & Meal Taste Settings
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SoupKitchen,
                                contentDescription = null,
                                tint = ColorTokens.Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                Text(
                                    text = "Dietary & Meal Taste",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                                Text(
                                    text = "Sent directly to kitchen chef for prep",
                                    fontSize = 11.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }

                        // Diet Standard switcher
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Diet Standard",
                                fontSize = 11.sp,
                                color = ColorTokens.Secondary
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("Pure Veg", "Jain (No Onion)", "Regular Veg").forEach { diet ->
                                    val isSelected = selectedDietStandard == diet
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (isSelected) ColorTokens.PrimaryFixed else ColorTokens.SurfaceContainer
                                            )
                                            .clickable { selectedDietStandard = diet }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(7.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (diet.contains("Pure")) ColorTokens.Tertiary
                                                        else if (diet.contains("Jain")) ColorTokens.TertiaryFixedDim
                                                        else ColorTokens.Secondary
                                                    )
                                            )
                                            Text(
                                                text = diet,
                                                fontSize = 10.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) ColorTokens.OnPrimaryFixed else ColorTokens.Secondary
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Spice Preference Switcher
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Spice Preference",
                                    fontSize = 11.sp,
                                    color = ColorTokens.Secondary
                                )
                                Text(
                                    text = selectedSpiceLevel,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.Primary
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("Mild", "Medium", "Fiery").forEach { spice ->
                                    val isSelected = selectedSpiceLevel == spice
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (isSelected) ColorTokens.Secondary else ColorTokens.SurfaceContainer
                                            )
                                            .clickable { selectedSpiceLevel = spice }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = spice,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) ColorTokens.OnSecondary else ColorTokens.Secondary
                                        )
                                    }
                                }
                            }
                        }

                        // Chef update strip
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = ColorTokens.Tertiary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "Chef updated for dinner dispatch",
                                    fontSize = 11.sp,
                                    color = ColorTokens.Secondary
                                )
                            }
                            Text(
                                text = "Edit Notes",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.Primary,
                                modifier = Modifier.clickable {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Chef preferences updated for upcoming shifts.")
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp)
        )

        // Cancel Hold Confirmation Dialog
        if (showCancelHoldDialog != null) {
            AlertDialog(
                onDismissRequest = { showCancelHoldDialog = null },
                title = {
                    Text(
                        text = "Cancel Vacation Hold?",
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                },
                text = {
                    Text(
                        text = "Canceling this hold will restore regular daily tiffin dispatch. Any refund calculations will be adjusted.",
                        fontSize = 13.sp,
                        color = ColorTokens.Secondary
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val id = showCancelHoldDialog ?: ""
                            TiffinRepository.cancelVacationHold(id)
                            showCancelHoldDialog = null
                            scope.launch {
                                snackbarHostState.showSnackbar("Vacation hold canceled. Regular meals resumed.")
                            }
                        }
                    ) {
                        Text("Confirm Cancel", color = ColorTokens.Error, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCancelHoldDialog = null }) {
                        Text("Keep Hold", color = ColorTokens.Secondary)
                    }
                }
            )
        }

        // Reason Selection Dialog
        if (showReasonDialog) {
            val reasons = listOf(
                "College Exams / Semester Break",
                "Home Visit / Out of Town",
                "Fasting / Festive Ritual",
                "Personal Dietary Break",
                "Work Travel / Offsite"
            )
            AlertDialog(
                onDismissRequest = { showReasonDialog = false },
                title = {
                    Text(
                        text = "Select Pause Reason",
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        reasons.forEach { reason ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedReason == reason) ColorTokens.PrimaryFixed else ColorTokens.SurfaceContainerLow)
                                    .clickable {
                                        selectedReason = reason
                                        showReasonDialog = false
                                    }
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = reason,
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedReason == reason) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedReason == reason) ColorTokens.OnPrimaryFixed else ColorTokens.OnSurface
                                )
                                if (selectedReason == reason) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showReasonDialog = false }) {
                        Text("Done", color = ColorTokens.Primary)
                    }
                }
            )
        }

        // Quick Date Range Preset Dialog
        if (showStartDateDialog) {
            val presets = listOf(
                Triple("08 Sep 2026", "12 Sep 2026", 5),
                Triple("15 Sep 2026", "18 Sep 2026", 4),
                Triple("22 Sep 2026", "24 Sep 2026", 3),
                Triple("01 Oct 2026", "07 Oct 2026", 7)
            )
            AlertDialog(
                onDismissRequest = { showStartDateDialog = false },
                title = {
                    Text(
                        text = "Select Pause Duration",
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        presets.forEach { (start, end, days) ->
                            val isSelected = startDateText == start && endDateText == end
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) ColorTokens.PrimaryFixed else ColorTokens.SurfaceContainerLow)
                                    .clickable {
                                        startDateText = start
                                        endDateText = end
                                        daysCount = days
                                        showStartDateDialog = false
                                    }
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "$start  →  $end",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) ColorTokens.OnPrimaryFixed else ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "$days active meal days paused",
                                        fontSize = 11.sp,
                                        color = ColorTokens.Secondary
                                    )
                                }
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = ColorTokens.Primary, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showStartDateDialog = false }) {
                        Text("Cancel", color = ColorTokens.Secondary)
                    }
                }
            )
        }
    }
}

private data class DayData(
    val dayNumber: String,
    val status: String = "active", // "active", "hold", "off", "today"
    val isInactive: Boolean = false
)

@Composable
private fun CalendarRow(days: List<DayData>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEach { day ->
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(
                        if (day.status == "hold") RoundedCornerShape(6.dp)
                        else CircleShape
                    )
                    .background(
                        if (day.status == "today") ColorTokens.SecondaryContainer
                        else if (day.status == "hold") ColorTokens.PrimaryFixed
                        else Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Text(
                        text = day.dayNumber,
                        fontSize = 11.sp,
                        fontWeight = if (day.status == "today" || day.status == "hold") FontWeight.Bold else FontWeight.Normal,
                        color = when {
                            day.isInactive -> ColorTokens.OutlineVariant
                            day.status == "today" -> ColorTokens.OnSecondaryFixed
                            day.status == "hold" -> ColorTokens.OnPrimaryFixed
                            day.status == "off" -> ColorTokens.Secondary.copy(alpha = 0.6f)
                            else -> ColorTokens.OnSurface
                        }
                    )

                    when (day.status) {
                        "active" -> Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.Tertiary)
                        )
                        "today" -> Box(
                            modifier = Modifier
                                .size(3.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.Tertiary)
                        )
                        "hold" -> Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.Primary)
                        )
                        "off" -> Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(9.dp),
                            tint = ColorTokens.Outline
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = ColorTokens.Secondary
        )
    }
}
