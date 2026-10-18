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
import com.creativeblisslabs.tiffinmanagement.data.models.PlanUpgradeOption
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinCustomerHeader
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import kotlinx.coroutines.launch

@Composable
fun CustomerChangeUpgradePlanScreen(
    repository: TiffinRepository,
    onNavigateBack: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToWallet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by TiffinRepository.subscriberProfile.collectAsState()
    val availablePlans: List<PlanUpgradeOption> by TiffinRepository.availablePlans.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedPlanId by remember { mutableStateOf("executive") }
    var isImmediateSwitch by remember { mutableStateOf(true) }

    val selectedPlan = availablePlans.find { it.id == selectedPlanId } ?: availablePlans.first()

    // Dynamic Proration math (10 days remaining in current cycle)
    val remainingDays = 10
    val totalDays = 30
    val proratedCost = remember(selectedPlanId, isImmediateSwitch) {
        if (!isImmediateSwitch) 0.0
        else {
            val diff = selectedPlan.priceDifference
            if (diff > 0) (diff / totalDays) * remainingDays else 0.0
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = ColorTokens.Surface,
        bottomBar = {
            // Sticky Bottom Confirmation CTA Deck
            Surface(
                color = ColorTokens.Surface.copy(alpha = 0.95f),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isImmediateSwitch) "Payable Now (Prorated)" else "Next Renewal Rate",
                                style = MaterialTheme.typography.bodySmall,
                                color = ColorTokens.Secondary
                            )
                            Text(
                                text = if (isImmediateSwitch) "₹${proratedCost.toInt()}.00" else "₹${selectedPlan.priceMonthly.toInt()}/mo",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = ColorTokens.Primary
                            )
                        }

                        Button(
                            onClick = {
                                repository.upgradePlan(selectedPlanId, isImmediateSwitch)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Successfully switched to ${selectedPlan.title}! Plan active."
                                    )
                                }
                            },
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f, fill = false)
                                .padding(start = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ColorTokens.Primary,
                                contentColor = ColorTokens.OnPrimary
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isImmediateSwitch) "Confirm & Switch Today" else "Schedule for Next Cycle",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                // .padding(paddingValues)
                .background(ColorTokens.Surface)
        ) {
            // Screen Header
            TiffinCustomerHeader(
                title = "Subscription Plan",
                subtitle = "TIFFIN MANAGER",
                isActive = true,
                onNotificationClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Plan changes take effect within 15 minutes.")
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
                                .background(ColorTokens.SurfaceContainerHigh)
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
                                text = "Change / Upgrade Plan",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Text(
                                text = "Tiffin Subscription ${profile.subscriberNumber}",
                                style = MaterialTheme.typography.labelSmall,
                                color = ColorTokens.Secondary
                            )
                        }
                    }

                    AssistChip(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("All plans include free delivery & pause flexibility.")
                            }
                        },
                        label = { Text("Plan Help", style = MaterialTheme.typography.labelSmall) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = ColorTokens.Primary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    )
                }

                // Current Plan Summary Card
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "CURRENT PLAN",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.Secondary
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(ColorTokens.TertiaryFixed)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "Active",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.OnTertiaryFixed
                                        )
                                    }
                                }
                                Text(
                                    text = profile.planTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Lunch + Dinner • Mon – Sat (Sunday Optional)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ColorTokens.OnSurfaceVariant
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ColorTokens.PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bento,
                                    contentDescription = null,
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "₹3,900",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.OnSurface
                                )
                                Text(
                                    text = " / month",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ColorTokens.Secondary
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Autorenew,
                                    contentDescription = null,
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "Renews 01 Oct 2026",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ColorTokens.OnSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Available Meal Plans Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Available Meal Plans",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = "Select a plan that best fits your daily hunger and routine",
                            style = MaterialTheme.typography.bodySmall,
                            color = ColorTokens.Secondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ColorTokens.SurfaceContainerHigh)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${availablePlans.size} Options",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = ColorTokens.Secondary
                        )
                    }
                }

                // Available Plans Stack
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    availablePlans.forEach { plan ->
                        PlanSelectionCard(
                            plan = plan,
                            isSelected = selectedPlanId == plan.id,
                            onSelect = { selectedPlanId = plan.id }
                        )
                    }
                }

                // Transition Configuration Card
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
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = ColorTokens.Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Transition Configuration",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                        }

                        Text(
                            text = "When should this plan start?",
                            style = MaterialTheme.typography.labelMedium,
                            color = ColorTokens.OnSurfaceVariant
                        )

                        // 2 Toggle Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (!isImmediateSwitch) ColorTokens.PrimaryContainer else ColorTokens.SurfaceContainer)
                                    .clickable { isImmediateSwitch = false }
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Next Billing Cycle",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (!isImmediateSwitch) ColorTokens.OnPrimaryContainer else ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "01 Oct 2026",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (!isImmediateSwitch) ColorTokens.PrimaryFixed else ColorTokens.Secondary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isImmediateSwitch) ColorTokens.PrimaryContainer else ColorTokens.SurfaceContainer)
                                    .clickable { isImmediateSwitch = true }
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Immediate Switch",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isImmediateSwitch) ColorTokens.OnPrimaryContainer else ColorTokens.OnSurface
                                    )
                                    Text(
                                        text = "Prorated today",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isImmediateSwitch) ColorTokens.PrimaryFixed else ColorTokens.Secondary
                                    )
                                }
                            }
                        }

                        // Proration Breakdown
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Remaining cycle duration", style = MaterialTheme.typography.bodySmall, color = ColorTokens.OnSurfaceVariant)
                                Text("10 Days remaining", style = MaterialTheme.typography.labelMedium, color = ColorTokens.OnSurface)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Prorated upgrade cost", style = MaterialTheme.typography.bodySmall, color = ColorTokens.OnSurfaceVariant)
                                Text("₹${proratedCost.toInt()}.00", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface)
                            }
                            Divider(color = ColorTokens.SurfaceContainerHigh)
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
                                        contentDescription = null,
                                        tint = ColorTokens.Tertiary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Available Wallet: ₹${profile.walletBalance.toInt()}.00",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ColorTokens.Tertiary
                                    )
                                }
                                Text("No card needed", style = MaterialTheme.typography.labelSmall, color = ColorTokens.Secondary)
                            }
                        }

                        // Address Confirmation
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorTokens.SurfaceContainerLow)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PinDrop,
                                    contentDescription = null,
                                    tint = ColorTokens.Primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Column {
                                    Text("Delivery Location", style = MaterialTheme.typography.labelSmall, color = ColorTokens.Secondary)
                                    Text(profile.flatDetails, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = ColorTokens.OnSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                            }
                            TextButton(onClick = onNavigateToProfile) {
                                Text("Change", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                            }
                        }
                    }
                }

                // Assurance note
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = ColorTokens.Secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "You can pause individual meals or switch back anytime with zero fee.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ColorTokens.Secondary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun PlanSelectionCard(
    plan: PlanUpgradeOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) ColorTokens.PrimaryFixed.copy(alpha = 0.15f) else ColorTokens.SurfaceContainerLowest
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, ColorTokens.Primary) else androidx.compose.foundation.BorderStroke(1.dp, ColorTokens.OutlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (plan.isPopular) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(ColorTokens.Primary)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = ColorTokens.OnPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Most Popular Upgrade",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnPrimary
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = onSelect,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = ColorTokens.Primary,
                            unselectedColor = ColorTokens.Secondary
                        )
                    )
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = plan.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
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
                        }
                        Text(
                            text = plan.subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) ColorTokens.Primary else ColorTokens.Secondary
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${plan.priceMonthly.toInt()}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = ColorTokens.OnSurface
                    )
                    Text(
                        text = plan.differenceLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (plan.priceDifference > 0) ColorTokens.Primary else ColorTokens.Tertiary
                    )
                }
            }

            // Features list
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorTokens.SurfaceContainerLow.copy(alpha = 0.7f))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                plan.features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = ColorTokens.Tertiary,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = feature,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                            color = ColorTokens.OnSurface
                        )
                    }
                }
            }

            Text(
                text = plan.containerNote,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                color = ColorTokens.Secondary
            )
        }
    }
}
