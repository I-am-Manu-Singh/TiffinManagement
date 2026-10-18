package com.creativeblisslabs.tiffinmanagement.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import com.creativeblisslabs.tiffinmanagement.generated.resources.Res
import com.creativeblisslabs.tiffinmanagement.generated.resources.app_logo
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinishLoading: () -> Unit = {}
) {
    val branch by TiffinRepository.currentBranch.collectAsState()
    val customers by TiffinRepository.customers.collectAsState()
    val routes by TiffinRepository.deliveryRoutes.collectAsState()

    var progress by remember { mutableFloatStateOf(0.68f) }
    var statusText by remember { mutableStateOf("Initializing Main Branch database v2.4...") }
    var statusPct by remember { mutableStateOf("68%") }

    LaunchedEffect(Unit) {
        delay(700)
        progress = 0.74f
        statusPct = "74%"
        statusText = "Syncing recurring subscription ledgers..."
        delay(800)
        progress = 0.89f
        statusPct = "89%"
        statusText = "Optimizing morning tiffin dispatch paths..."
        delay(800)
        progress = 1.0f
        statusPct = "100%"
        statusText = "Kitchen Console Ready."
        delay(500)
        onFinishLoading()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "steamPulse")
    val steamAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "steamAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FD))
    ) {
        // Ambient Culinary Glows
        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.Center)
                .blur(80.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0x28AB2F00), Color(0x18FFDBD1), Color.Transparent)
                    )
                )
        )
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.BottomEnd)
                .blur(70.dp)
                .background(Color(0x30D5E0F8))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Top Operational Badge
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF006947))
                    )
                    Text(
                        text = "CLOUD HUB ACTIVE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF545F73),
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "•",
                        fontSize = 11.sp,
                        color = Color(0xFF545F73).copy(alpha = 0.4f)
                    )
                    Text(
                        text = "${branch.name} #04",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF5B4139)
                    )
                }
            }

            // 2. Center Hero Branding Unit
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                // Tactile Logo Mark Container
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(124.dp)
                ) {
                    // Outer glow blur
                    Box(
                        modifier = Modifier
                            .size(118.dp)
                            .blur(14.dp)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFFAB2F00), Color(0xFFD53E02), Color(0xFFAF3100))
                                ),
                                shape = RoundedCornerShape(32.dp)
                            )
                    )

                    // Main Box
                    Box(
                        modifier = Modifier
                            .size(108.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFFD53E02), Color(0xFFAB2F00))
                                )
                            )
                            .border(1.dp, Color(0xFFFFDBD1).copy(alpha = 0.4f), RoundedCornerShape(28.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        org.jetbrains.compose.resources.painterResource(Res.drawable.app_logo).let { painter ->
                            androidx.compose.foundation.Image(
                                painter = painter,
                                contentDescription = "Tiffin Manager App Logo",
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(28.dp)),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        }

                        // Culinary Steam Micro Indicator (Animated)
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 8.dp, end = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Color.White.copy(alpha = steamAlpha * 0.7f))
                            )
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Color.White.copy(alpha = steamAlpha))
                            )
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Color.White.copy(alpha = steamAlpha * 0.5f))
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // App Title & Tagline
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Tiffin Manager",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191C1F),
                        letterSpacing = (-0.5).sp
                    )
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFAB2F00))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Smart Cloud Kitchen & Meal Subscription Operating System",
                    fontSize = 14.sp,
                    color = Color(0xFF5B4139),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Core Pillars Pill Matrix
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F8)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Meal Plans", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF545F73))
                        Text("•", fontSize = 11.sp, color = Color(0xFF545F73).copy(alpha = 0.4f))
                        Text("Route Sequence", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF545F73))
                        Text("•", fontSize = 11.sp, color = Color(0xFF545F73).copy(alpha = 0.4f))
                        Text("Kitchen KDS", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF545F73))
                        Text("•", fontSize = 11.sp, color = Color(0xFF545F73).copy(alpha = 0.4f))
                        Text("Ledger", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF545F73))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Dietary Verification Visual Anchors
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color.White)
                                .border(1.dp, Color(0xFF006947), RoundedCornerShape(3.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF006947)))
                        }
                        Text("100% Veg Prep", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF545F73))
                    }

                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color(0xFF545F73).copy(alpha = 0.3f)))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color.White)
                                .border(1.dp, Color(0xFFBA1A1A), RoundedCornerShape(3.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFBA1A1A)))
                        }
                        Text("Non-Veg Route", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF545F73))
                    }
                }
            }

            // 3. Bottom Interactive Status, Progress & Compliance Suite
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Dynamic Boot Progress Indicator
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(22.dp)),
                    color = Color(0xFFAB2F00),
                    trackColor = Color(0xFFE6E8EC)
                )

                // System Telemetry Status
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.Sync,
                            contentDescription = null,
                            tint = Color(0xFFAB2F00),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = statusText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF545F73),
                            maxLines = 1
                        )
                    }
                    Text(
                        text = statusPct,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191C1F)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Trust & Enterprise Compliance Footer
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.VerifiedUser, null, tint = Color(0xFF006947), modifier = Modifier.size(16.dp))
                            Text("Enterprise Cloud", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF191C1F))
                        }

                        Box(modifier = Modifier.width(1.dp).height(14.dp).background(Color(0xFFE6E8EC)))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("₹", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF545F73))
                            Text("Auto Billing", fontSize = 11.sp, color = Color(0xFF545F73))
                        }

                        Box(modifier = Modifier.width(1.dp).height(14.dp).background(Color(0xFFE6E8EC)))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.HealthAndSafety, null, tint = Color(0xFF545F73), modifier = Modifier.size(15.dp))
                            Text("FSSAI READY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF545F73), letterSpacing = 0.5.sp)
                        }
                    }
                }
            }
        }
    }
}
