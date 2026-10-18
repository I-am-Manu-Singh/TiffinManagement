package com.creativeblisslabs.tiffinmanagement.ui.screens.admin

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.LocalPrintshop
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import com.creativeblisslabs.tiffinmanagement.ui.components.NavItem

@Composable
fun DashboardScreen(
    onNavigateTo: (NavItem) -> Unit = {}
) {
    var showSetupProgress by remember { mutableStateOf(true) }
    val overview by TiffinRepository.financialOverview.collectAsState()
    val branch by TiffinRepository.currentBranch.collectAsState()

    val branches by TiffinRepository.branches.collectAsState()
    val mealPlans by TiffinRepository.mealPlans.collectAsState()
    val deliveryRoutes by TiffinRepository.deliveryRoutes.collectAsState()
    val staffMembers by TiffinRepository.staffMembers.collectAsState()
    val customers by TiffinRepository.customers.collectAsState()

    val setupTasksTotal = 5
    var setupTasksCompleted = 0
    if (branches.isNotEmpty()) setupTasksCompleted++
    if (mealPlans.isNotEmpty()) setupTasksCompleted++
    if (deliveryRoutes.isNotEmpty()) setupTasksCompleted++
    if (staffMembers.isNotEmpty()) setupTasksCompleted++
    if (customers.isNotEmpty()) setupTasksCompleted++
    val setupProgressFraction = setupTasksCompleted.toFloat() / setupTasksTotal.toFloat()
    val setupProgressPercent = (setupProgressFraction * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      

        if (showSetupProgress) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(ColorTokens.PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TaskAlt,
                                    contentDescription = "Setup",
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Business Setup Progress",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = ColorTokens.OnSurface
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(100.dp))
                                            .background(ColorTokens.PrimaryFixed)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "$setupProgressPercent%",
                                            color = ColorTokens.OnPrimaryFixed,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Text(
                                    text = "$setupTasksCompleted of $setupTasksTotal tasks complete",
                                    fontSize = 12.sp,
                                    color = ColorTokens.TextSecondary
                                )
                            }
                        }

                        IconButton(
                            onClick = { showSetupProgress = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = ColorTokens.TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Progress Track
                    LinearProgressIndicator(
                        progress = { setupProgressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(100.dp)),
                        color = ColorTokens.Primary,
                        trackColor = ColorTokens.SurfaceContainer
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Done",
                                tint = ColorTokens.Tertiary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (setupProgressFraction >= 1f) "All kitchen operations ready" else "Menu, Routes & Pricing in setup",
                                fontSize = 11.sp,
                                color = ColorTokens.TextSecondary
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.clickable {
                                when {
                                    mealPlans.isEmpty() -> onNavigateTo(NavItem.MealPlans)
                                    deliveryRoutes.isEmpty() -> onNavigateTo(NavItem.UpdateDeliveryRoute)
                                    staffMembers.isEmpty() -> onNavigateTo(NavItem.StaffDirectory)
                                    else -> onNavigateTo(NavItem.OnboardingKitchenSetup)
                                }
                            }
                        ) {
                            Text(
                                text = if (setupProgressFraction >= 1f) "Review Setup" else "Complete Setup",
                                color = ColorTokens.Primary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Next",
                                tint = ColorTokens.Primary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // 3. 4 Primary Stat Cards (2x2 Grid)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Card 1: Active Customers
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Active Customers",
                    value = overview.activeSubscribersCount.toString(),
                    subtitle = "Active subscribers",
                    icon = Icons.Default.Person,
                    iconBg = ColorTokens.PrimaryFixed,
                    iconColor = ColorTokens.Primary,
                    accentColor = ColorTokens.Primary,
                    isPositive = true
                )

                // Card 2: Monthly Revenue
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Monthly Revenue",
                    value = "₹${overview.monthlyRevenue}",
                    subtitle = "Billing cycle: ${overview.billingCycleMonth}",
                    icon = Icons.Default.Payments,
                    iconBg = ColorTokens.SecondaryContainer,
                    iconColor = ColorTokens.Secondary,
                    accentColor = ColorTokens.Secondary,
                    isSchedule = true
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Card 3: Monthly Expenses
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Monthly Expenses",
                    value = "₹${overview.monthlyExpenses}",
                    subtitle = overview.expenseCategory,
                    icon = Icons.Default.ReceiptLong,
                    iconBg = ColorTokens.TertiaryFixed,
                    iconColor = ColorTokens.Tertiary,
                    accentColor = ColorTokens.Tertiary,
                    isExpense = true
                )

                // Card 4: Monthly Orders
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Monthly Orders",
                    value = overview.monthlyOrdersCount.toString(),
                    subtitle = "Current month",
                    icon = Icons.Default.DateRange,
                    iconBg = ColorTokens.SecondaryFixed,
                    iconColor = ColorTokens.OnSecondaryFixedVariant,
                    accentColor = ColorTokens.SecondaryFixedDim
                )
            }
        }

        // 4. Financial History Graph Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Financial History",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = "Last 12 Months (Oct 2025 – Sep 2026)",
                            fontSize = 11.sp,
                            color = ColorTokens.TextSecondary
                        )
                    }
                }

                // Legend
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LegendItem("Revenue", ColorTokens.Tertiary)
                    LegendItem("Expenses", ColorTokens.Error)
                    LegendItem("Profit / Loss", ColorTokens.Secondary)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("₹500", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                    Text("Baseline Trend", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                }

                // Interactive SVG/Canvas Graph
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val baselineY = h * 0.58f

                        // Baseline dashed line
                        drawLine(
                            color = Color(0xFFE2E8F0),
                            start = Offset(0f, baselineY),
                            end = Offset(w, baselineY),
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                        )

                        // Oct Dip Bar (₹377 Expense)
                        val barWidth = 18f
                        val barX = w * 0.08f
                        drawRoundRect(
                            color = ColorTokens.Error,
                            topLeft = Offset(barX, baselineY),
                            size = androidx.compose.ui.geometry.Size(barWidth, h * 0.38f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
                        )

                        // Profit curve
                        val path = Path().apply {
                            moveTo(barX + barWidth / 2, baselineY + h * 0.35f)
                            cubicTo(
                                w * 0.18f, baselineY + h * 0.35f,
                                w * 0.22f, baselineY,
                                w * 0.32f, baselineY
                            )
                            lineTo(w, baselineY)
                        }
                        drawPath(
                            path = path,
                            color = ColorTokens.Secondary,
                            style = Stroke(width = 4f, cap = StrokeCap.Round)
                        )

                        // Pulsing Dot on dip
                        drawCircle(
                            color = ColorTokens.Secondary,
                            radius = 6f,
                            center = Offset(barX + barWidth / 2, baselineY + h * 0.35f)
                        )
                    }
                }

                // Month Labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Oct", "Dec", "Feb", "Apr", "Jun", "Sep").forEach { m ->
                        Text(m, fontSize = 10.sp, color = ColorTokens.TextSecondary)
                    }
                }

                // Dip notification pill
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
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.Error)
                        )
                        Text(
                            text = "Oct '25 Dip: -₹${overview.recordedDipAmount}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.Error
                        )
                    }
                    Text(
                        text = "Net Profit: ₹${overview.netProfit}",
                        fontSize = 11.sp,
                        color = ColorTokens.TextSecondary
                    )
                }
            }
        }

        // 5. Today's Delivery Progress
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Today's Delivery Progress",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = "Live dispatch status",
                            fontSize = 11.sp,
                            color = ColorTokens.TextSecondary
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(ColorTokens.SurfaceContainer)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("Today Total", fontSize = 11.sp, color = ColorTokens.OnSurface, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.width(6.dp))
                        Text(
                            (overview.homeDeliveryTotal + overview.storePickupTotal).toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.Primary
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(ColorTokens.Tertiary))
                        Text("Delivered (${overview.homeDeliveryCompleted + overview.storePickupCompleted})", fontSize = 12.sp, color = ColorTokens.OnSurface)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(ColorTokens.PrimaryFixedDim))
                        Text(
                            "Pending / Preparing (${(overview.homeDeliveryTotal + overview.storePickupTotal) - (overview.homeDeliveryCompleted + overview.storePickupCompleted)})",
                            fontSize = 12.sp,
                            color = ColorTokens.OnSurface
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Home Delivery
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.PrimaryFixed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Moped, "Delivery", tint = ColorTokens.Primary, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text("Home Delivery", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                            Text("${overview.homeDeliveryCompleted} / ${overview.homeDeliveryTotal}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        }
                    }

                    // Store Pickup
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ColorTokens.SurfaceContainerLow)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(ColorTokens.SecondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Storefront, "Pickup", tint = ColorTokens.Secondary, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text("Store Pickup", fontSize = 11.sp, color = ColorTokens.TextSecondary)
                            Text("${overview.storePickupCompleted} / ${overview.storePickupTotal}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                        }
                    }
                }
            }
        }

        // 6. Low Wallet Balance Alerts Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                            Icons.Default.AccountBalanceWallet,
                            "Wallet",
                            tint = ColorTokens.Error,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Low Wallet Balance Alerts",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = ColorTokens.OnSurface
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(ColorTokens.TertiaryFixed)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Healthy",
                            color = ColorTokens.OnTertiaryFixed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(ColorTokens.SurfaceContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AccountBalanceWallet,
                            "Savings",
                            tint = ColorTokens.TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "No customers with low wallet balance.",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = ColorTokens.OnSurface
                    )
                    Text(
                        text = "All 1 active accounts are settled or currently within the allowed credit threshold.",
                        fontSize = 11.sp,
                        color = ColorTokens.TextSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconColor: Color,
    accentColor: Color,
    isPositive: Boolean = false,
    isExpense: Boolean = false,
    isSchedule: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        color = ColorTokens.TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(iconBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, title, tint = iconColor, modifier = Modifier.size(16.dp))
                    }
                }

                Text(
                    text = value,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ColorTokens.OnSurface
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isPositive) {
                        Icon(Icons.Default.TrendingFlat, "Trending", tint = ColorTokens.Tertiary, modifier = Modifier.size(14.dp))
                        Text(subtitle, fontSize = 11.sp, color = ColorTokens.Tertiary, fontWeight = FontWeight.SemiBold)
                    } else if (isExpense) {
                        Icon(Icons.Default.TrendingFlat, contentDescription = null, tint = ColorTokens.Error, modifier = Modifier.size(14.dp))
                        Text(subtitle, fontSize = 11.sp, color = ColorTokens.Error, fontWeight = FontWeight.SemiBold)
                    } else if (isSchedule) {
                        Icon(Icons.Default.Schedule, "Schedule", tint = ColorTokens.TextSecondary, modifier = Modifier.size(12.dp))
                        Text(subtitle, fontSize = 11.sp, color = ColorTokens.TextSecondary)
                    } else {
                        Icon(Icons.Default.DateRange, "Calendar", tint = ColorTokens.TextSecondary, modifier = Modifier.size(12.dp))
                        Text(subtitle, fontSize = 11.sp, color = ColorTokens.TextSecondary)
                    }
                }
            }

            // Bottom accent color strip
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(accentColor)
            )
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(color))
        Text(label, fontSize = 11.sp, color = ColorTokens.OnSurface, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun QuickActionRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    rightIcon: androidx.compose.ui.graphics.vector.ImageVector = Icons.Default.ArrowForward,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, title, tint = iconTint, modifier = Modifier.size(20.dp))
                }
                Column {
                    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                    Text(subtitle, fontSize = 11.sp, color = ColorTokens.TextSecondary)
                }
            }

            Icon(rightIcon, "Action", tint = iconTint, modifier = Modifier.size(18.dp))
        }
    }
}
