package com.creativeblisslabs.tiffinmanagement.ui.screens.admin

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository

@Composable
fun OnboardingKitchenPlansScreen(
    onNavigateBack: () -> Unit = {},
    onContinue: () -> Unit = {},
    onSkip: () -> Unit = {}
) {
    val branch by TiffinRepository.currentBranch.collectAsState()
    val customers by TiffinRepository.customers.collectAsState()
    val mealPlans by TiffinRepository.mealPlans.collectAsState()

    val totalSubscribersCount = customers.size
    val activePlansCount = mealPlans.filter { it.isActive }.size

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FD))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
           

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                            Icon(
                                imageVector = Icons.Default.SoupKitchen,
                                contentDescription = null,
                                tint = Color(0xFFF95721),
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Kitchen Identity Intro",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF191C1F),
                                letterSpacing = (-0.3).sp
                            )
                        }

                        Button(
                            onClick = onNavigateBack,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF95721)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Branch", tint = Color.White, modifier = Modifier.size(16.dp))
                                Text("+ Add Branch Kitchen", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                // Step Indicator Progress Bar
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.width(28.dp).height(8.dp).clip(RoundedCornerShape(22.dp)).background(Color(0xFFAB2F00)))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE0E3E6)))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE0E3E6)))
                    }
                    Text(
                        text = "STEP 1 OF 3",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF545F73),
                        letterSpacing = 0.8.sp
                    )
                }

                // Title & Value Proposition
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Effortless Tiffin & Subscription Business",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191C1F),
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Automate daily order lock cutoffs, schedule custom meal rotations (Breakfast, Lunch & Dinner), and eliminate manual spreadsheet chaos.",
                        fontSize = 13.sp,
                        color = Color(0xFF545F73),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }

                // Feature Pillars
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Feature 1
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFFDBD1)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Timer, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text("Automated Daily Cutoffs", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                Text(
                                    "Set rigid lock hours for cancellations or additions. No more prep waste or uncounted late meals.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF545F73),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    // Feature 2
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFD5E0F8)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CalendarMonth, null, tint = Color(0xFF545F73), modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text("Multi-Tier Meal Plans", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                Text(
                                    "Flexible weekly and monthly subscriptions with customer self-service vacation & weekend pauses.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF545F73),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    // Feature 3
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x2200855B)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.SoupKitchen, null, tint = Color(0xFF006947), modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text("Live Kitchen Display (KDS)", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                Text(
                                    "Aggregate quantities for rotis, gravies, and special dietary tags instantly for your head chef.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF545F73),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom CTA Button
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Continue", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
