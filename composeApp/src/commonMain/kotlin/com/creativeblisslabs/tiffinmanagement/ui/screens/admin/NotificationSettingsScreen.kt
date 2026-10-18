package com.creativeblisslabs.tiffinmanagement.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.models.NotificationSettingModel
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

@Composable
fun NotificationSettingsScreen(
    onNavigateBack: () -> Unit = {}
) {
    val settingsList by TiffinRepository.notificationSettings.collectAsState()

    var activeSubTab by remember { mutableStateOf("Event Alerts") } // Event Alerts, Push Devices
    val expandedItems = remember { mutableStateMapOf<String, Boolean>() }

    val customerSettings = settingsList.filter { it.category == "customer" }
    val vendorSettings = settingsList.filter { it.category == "vendor" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorTokens.Surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
       

        // 2. Segmented Sub-Tabs (Event Alerts vs Push Devices)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ColorTokens.SurfaceContainer)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (activeSubTab == "Event Alerts") ColorTokens.SurfaceContainerLowest else Color.Transparent)
                    .clickable { activeSubTab = "Event Alerts" }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = if (activeSubTab == "Event Alerts") ColorTokens.Primary else ColorTokens.Secondary,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Event Alerts",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (activeSubTab == "Event Alerts") ColorTokens.Primary else ColorTokens.Secondary
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (activeSubTab == "Push Devices") ColorTokens.SurfaceContainerLowest else Color.Transparent)
                    .clickable { activeSubTab = "Push Devices" }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Devices,
                    contentDescription = null,
                    tint = if (activeSubTab == "Push Devices") ColorTokens.Primary else ColorTokens.Secondary,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Push Devices",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (activeSubTab == "Push Devices") ColorTokens.Primary else ColorTokens.Secondary
                )
            }
        }

        // 3. Contextual Banner / Visual Illustration
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ColorTokens.PrimaryFixed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MarkEmailRead,
                        contentDescription = null,
                        tint = ColorTokens.Primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Instant WhatsApp & In-App Alerts",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ColorTokens.OnSurface
                    )
                    Text(
                        text = "Deliveries and wallet warnings send immediately to subscribers.",
                        fontSize = 11.sp,
                        color = ColorTokens.OnSurfaceVariant
                    )
                }
            }
        }

        // 4. Section 1: Customer Facing Notifications
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Section Header
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ColorTokens.SecondaryFixed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = ColorTokens.OnSecondaryFixed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Customer Facing Notifications",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ColorTokens.SurfaceContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${customerSettings.size} Rules",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }
                        Text(
                            text = "Customise notifications dispatched to the customer via WhatsApp, email and in-app bell.",
                            fontSize = 11.sp,
                            color = ColorTokens.OnSurfaceVariant
                        )
                    }
                }

                // Accordion items
                customerSettings.forEach { setting ->
                    NotificationAccordionItem(
                        setting = setting,
                        isExpanded = expandedItems[setting.id] == true,
                        onToggleExpand = {
                            expandedItems[setting.id] = !(expandedItems[setting.id] ?: false)
                        },
                        onToggleEnabled = { enabled ->
                            TiffinRepository.toggleNotificationSetting(setting.id, enabled)
                        }
                    )
                }
            }
        }

        // 5. Section 2: Vendor Operational Notifications
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = ColorTokens.SurfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Section Header
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ColorTokens.TertiaryFixed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = null,
                            tint = ColorTokens.OnTertiaryFixed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Vendor Operational Notifications",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTokens.OnSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ColorTokens.SurfaceContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${vendorSettings.size} Rules",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ColorTokens.Secondary
                                )
                            }
                        }
                        Text(
                            text = "Customise logs and digests dispatched to the vendor owner and kitchen staff.",
                            fontSize = 11.sp,
                            color = ColorTokens.OnSurfaceVariant
                        )
                    }
                }

                // Accordion items
                vendorSettings.forEach { setting ->
                    NotificationAccordionItem(
                        setting = setting,
                        isExpanded = expandedItems[setting.id] == true,
                        onToggleExpand = {
                            expandedItems[setting.id] = !(expandedItems[setting.id] ?: false)
                        },
                        onToggleEnabled = { enabled ->
                            TiffinRepository.toggleNotificationSetting(setting.id, enabled)
                        }
                    )
                }
            }
        }

        // 6. Operational Quality Stamp
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(ColorTokens.SurfaceContainer)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = ColorTokens.Tertiary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "All message templates follow TRAI & DLT compliance",
                    fontSize = 11.sp,
                    color = ColorTokens.OnSurfaceVariant
                )
            }
        }

        // 7. Sticky Save Settings Button
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = ColorTokens.PrimaryCTA),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = null,
                tint = ColorTokens.OnPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Save Settings",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTokens.OnPrimary
            )
        }
    }
}

@Composable
private fun NotificationAccordionItem(
    setting: NotificationSettingModel,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onToggleEnabled: (Boolean) -> Unit
) {
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(ColorTokens.SurfaceContainerLow)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f).clickable { onToggleExpand() }
            ) {
                Switch(
                    checked = setting.isEnabled,
                    onCheckedChange = { onToggleEnabled(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = ColorTokens.Primary
                    ),
                    modifier = Modifier.height(24.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = setting.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ColorTokens.OnSurface,
                        maxLines = 1
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Recipient:", fontSize = 11.sp, color = ColorTokens.OnSurfaceVariant)
                        Text(setting.recipientLabel, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ColorTokens.Secondary)
                    }
                }

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    tint = ColorTokens.Secondary,
                    modifier = Modifier.size(20.dp).rotate(rotationAngle)
                )
            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorTokens.SurfaceContainerLowest)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(setting.detailText, fontSize = 11.sp, color = ColorTokens.OnSurface)
                    Text(setting.channels, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ColorTokens.Primary)
                }
                if (setting.sampleMessage != null) {
                    Text(
                        text = setting.sampleMessage,
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        color = ColorTokens.OnSurfaceVariant
                    )
                }
            }
        }
    }
}
