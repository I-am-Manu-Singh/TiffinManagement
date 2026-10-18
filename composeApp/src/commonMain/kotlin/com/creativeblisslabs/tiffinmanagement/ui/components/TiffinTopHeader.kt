package com.creativeblisslabs.tiffinmanagement.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.creativeblisslabs.tiffinmanagement.generated.resources.Res
import com.creativeblisslabs.tiffinmanagement.generated.resources.app_logo
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.creativeblisslabs.tiffinmanagement.data.models.BranchModel
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun TiffinTopHeader(
    branchName: String = "",
    screenTitle: String = "Dashboard",
    userName: String = "",
    userRole: String = "",
    activeBranch: String? = null,
    subtitle: String? = null,
    branches: List<BranchModel> = emptyList(),
    onSelectBranch: (BranchModel) -> Unit = {},
    onBranchClick: () -> Unit = {},
    onEditBranchClick: () -> Unit = {},
    onAddBranchClick: () -> Unit = {},
    onHoldsSettingsClick: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val displayBranch = activeBranch ?: branchName
    val displayTitle = subtitle ?: screenTitle
    var showBranchDropdown by remember { mutableStateOf(false) }

    val isAdmin = userRole.equals("owner", ignoreCase = true) ||
        userRole.equals("admin", ignoreCase = true) ||
        userRole.equals("founder & master administrator", ignoreCase = true)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorTokens.SurfaceContainerLowest)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Logo + Branch (Admin) OR User Name & Role (Non-Admin)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Official Tiffin Box App Logo Icon
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ColorTokens.PrimaryCTA),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Image(
                        painter = org.jetbrains.compose.resources.painterResource(Res.drawable.app_logo),
                        contentDescription = "Tiffin Manager Logo",
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }

                if (isAdmin) {
                    // Admin View: Branch Selector Container with open/close indicator
                    Box {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (showBranchDropdown) ColorTokens.SurfaceContainerLow else Color.Transparent)
                                .clickable {
                                    if (branches.isNotEmpty()) {
                                        showBranchDropdown = !showBranchDropdown
                                    } else {
                                        onBranchClick()
                                    }
                                }
                                .padding(horizontal = 6.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(
                                        text = displayBranch,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorTokens.OnSurface
                                    )
                                    Icon(
                                        imageVector = if (showBranchDropdown) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Toggle branch dropdown",
                                        tint = if (showBranchDropdown) ColorTokens.Primary else ColorTokens.OnSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = displayTitle,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = ColorTokens.OnSurfaceVariant
                                )
                            }
                        }

                        if (branches.isNotEmpty()) {
                            DropdownMenu(
                                expanded = showBranchDropdown,
                                onDismissRequest = { showBranchDropdown = false }
                            ) {
                                Text(
                                    text = "SELECT KITCHEN BRANCH",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorTokens.Secondary,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )

                                branches.forEach { b ->
                                    val isCurrent = b.name.equals(displayBranch, ignoreCase = true)
                                    DropdownMenuItem(
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Store,
                                                contentDescription = null,
                                                tint = if (isCurrent) ColorTokens.Primary else ColorTokens.Secondary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        },
                                        text = {
                                            Column {
                                                Text(
                                                    b.name,
                                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                                    fontSize = 13.sp,
                                                    color = if (isCurrent) ColorTokens.Primary else ColorTokens.OnSurface
                                                )
                                                b.address?.let {
                                                    Text(it, fontSize = 11.sp, color = ColorTokens.Secondary)
                                                }
                                            }
                                        },
                                        onClick = {
                                            onSelectBranch(b)
                                            showBranchDropdown = false
                                        }
                                    )
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                                // Edit current branch profile & operations
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Branch",
                                            tint = ColorTokens.Primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    text = {
                                        Text(
                                            "Edit Branch Profile & Operations",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = ColorTokens.OnSurface
                                        )
                                    },
                                    onClick = {
                                        showBranchDropdown = false
                                        onEditBranchClick()
                                    }
                                )

                                // Settings: Category & Branch Holds
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.PauseCircle,
                                            contentDescription = "Branch Holds",
                                            tint = ColorTokens.Tertiary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    text = {
                                        Text(
                                            "Category & Branch Holds",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = ColorTokens.OnSurface
                                        )
                                    },
                                    onClick = {
                                        showBranchDropdown = false
                                        onHoldsSettingsClick()
                                    }
                                )

                                // Add new branch kitchen
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Add Branch",
                                            tint = ColorTokens.TertiaryMint,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    text = {
                                        Text(
                                            "+ Add New Kitchen Branch",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ColorTokens.TertiaryMint
                                        )
                                    },
                                    onClick = {
                                        showBranchDropdown = false
                                        onAddBranchClick()
                                    }
                                )
                            }
                        }
                    }
                } else {
                    // Non-Admin (Mess Subscriber or Rider): Show User Name + Subtitle (Rider / Mess Subscriber)
                    Column(
                        modifier = Modifier.clickable { onProfileClick() }
                    ) {
                        Text(
                            text = userName.ifBlank { "Tiffin User" },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorTokens.OnSurface
                        )
                        Text(
                            text = if (userRole.contains("rider", ignoreCase = true) || userRole.contains("driver", ignoreCase = true)) "Rider • Delivery Partner" else "Mess User • $displayTitle",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = ColorTokens.OnSurfaceVariant
                        )
                    }
                }
            }

            // Right: Notifications + User Avatar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Notification Bell with badge
                Box(
                    modifier = Modifier.size(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onNotificationClick, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = ColorTokens.Secondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    // Red Notification dot
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 6.dp, end = 6.dp)
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(ColorTokens.Primary)
                    )
                }
                // Settings icon (Only visible to Admin / Owner)
                if (isAdmin) {
                    Box(
                        modifier = Modifier.size(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onSettingClick, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = ColorTokens.Secondary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
                // Profile Avatar (Clean, no role or username text next to it)
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(ColorTokens.SecondaryContainer)
                        .clickable { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    val initial = userName.trim().firstOrNull()?.uppercase() ?: "U"
                    Text(
                        text = initial,
                        color = ColorTokens.OnSecondaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
