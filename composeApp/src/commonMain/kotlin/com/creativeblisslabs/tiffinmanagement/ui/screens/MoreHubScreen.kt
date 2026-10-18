package com.creativeblisslabs.tiffinmanagement.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import com.creativeblisslabs.tiffinmanagement.ui.components.NavItem

@Composable
fun MoreHubScreen(
    onNavigateTo: (NavItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      

        // Section 1: Financial & Expenses
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "FINANCIAL & EXPENSE CONTROL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTokens.OnSurfaceVariant,
                letterSpacing = 0.5.sp
            )

            MoreActionItem(
                title = "Daily & Recurring Expenses Tracking",
                subtitle = "Log daily groceries, fuel, packaging Automated salary, rent, LPG utilities & scheduled cycles",
                icon = Icons.Default.ReceiptLong,
                iconBg = ColorTokens.PrimaryFixed,
                iconTint = ColorTokens.Primary,
                onClick = { onNavigateTo(NavItem.Expenses) }
            )
        }

        // Section 2: Operational Controls & Staff
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "KITCHEN CONTROLS & TEAM",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTokens.OnSurfaceVariant,
                letterSpacing = 0.5.sp
            )

            MoreActionItem(
                title = "Category & Branch Holds",
                subtitle = "Kitchen holidays, rain pauses, auto customer wallet credits",
                icon = Icons.Default.PauseCircle,
                iconBg = ColorTokens.PrimaryFixed,
                iconTint = ColorTokens.Primary,
                onClick = { onNavigateTo(NavItem.CategoryBranchHolds) }
            )

            MoreActionItem(
                title = "Staff Directory & Roles",
                subtitle = "Kitchen cooks, packing operators, delivery drivers roster",
                icon = Icons.Default.Badge,
                iconBg = ColorTokens.TertiaryFixed,
                iconTint = ColorTokens.Tertiary,
                onClick = { onNavigateTo(NavItem.StaffDirectory) }
            )
        }

        // Section 3: Menu & Meal Operations (Folder 4)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "MENU & DELIVERY LOGISTICS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTokens.OnSurfaceVariant,
                letterSpacing = 0.5.sp
            )

            MoreActionItem(
                title = "Meal Plans & Packages",
                subtitle = "Configure daily, weekly & monthly rates, veg/non-veg",
                icon = Icons.Default.DateRange,
                iconBg = ColorTokens.TertiaryFixed,
                iconTint = ColorTokens.Tertiary,
                onClick = { onNavigateTo(NavItem.MealPlans) }
            )


            MoreActionItem(
                title = "Delivery Routes & Drivers",
                subtitle = "Configure dispatch routes, sequence stops & assigned driver",
                icon = Icons.Default.AltRoute,
                iconBg = ColorTokens.PrimaryFixed,
                iconTint = ColorTokens.Primary,
                onClick = { onNavigateTo(NavItem.UpdateDeliveryRoute) }
            )
        }

        // Section 4: Preferences & Alerts
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "COMMUNICATION & ALERTS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTokens.OnSurfaceVariant,
                letterSpacing = 0.5.sp
            )


            MoreActionItem(
                title = "Kitchen Setup Tour",
                subtitle = "Revisit 3-step onboarding walkthrough & brand initialization",
                icon = Icons.Default.Restaurant,
                iconBg = ColorTokens.PrimaryFixed,
                iconTint = ColorTokens.Primary,
                onClick = { onNavigateTo(NavItem.OnboardingKitchenPlans) }
            )

            MoreActionItem(
                title = "Help & Support Center",
                subtitle = "Guides, operational tutorials, live operator chat & WhatsApp",
                icon = Icons.Default.HelpOutline,
                iconBg = ColorTokens.TertiaryFixed,
                iconTint = ColorTokens.Tertiary,
                onClick = { onNavigateTo(NavItem.HelpCenter) }
            )

            MoreActionItem(
                title = "Legal Center & Policies",
                subtitle = "Terms, Privacy, DPA, Refund, Cancellation, Security charters",
                icon = Icons.Default.Info,
                iconBg = ColorTokens.PrimaryFixed,
                iconTint = ColorTokens.Primary,
                onClick = { onNavigateTo(NavItem.LegalCenterHub) }
            )

            MoreActionItem(
                title = "Kitchen Guidelines & App Info",
                subtitle = "Operational principles, 6 kitchen models & software release notes",
                icon = Icons.Default.Info,
                iconBg = ColorTokens.SecondaryFixed,
                iconTint = ColorTokens.Secondary,
                onClick = { onNavigateTo(NavItem.AppInfo) }
            )
        }
    }
}

@Composable
private fun MoreActionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: androidx.compose.ui.graphics.Color,
    iconTint: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTokens.OnSurface
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = ColorTokens.OnSurfaceVariant,
                        lineHeight = 15.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = ColorTokens.OnSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
