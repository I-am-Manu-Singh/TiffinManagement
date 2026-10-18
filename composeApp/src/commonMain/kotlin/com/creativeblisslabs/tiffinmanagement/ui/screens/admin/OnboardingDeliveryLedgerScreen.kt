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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Route
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository

@Composable
fun OnboardingDeliveryLedgerScreen(
    onNavigateBack: () -> Unit = {},
    onContinue: () -> Unit = {},
    onSkip: () -> Unit = {}
) {
    val routes by TiffinRepository.deliveryRoutes.collectAsState()
    val activeRoute = routes.firstOrNull() ?: com.example.myapplicationkmp.data.models.DeliveryRouteModel(
        id = "sample",
        name = "Sector 4 Morning Run",
        originBranchId = "branch-1",
        originBranchName = "Main Branch",
        assignedDriverName = "Amit Kumar",
        isActive = true,
        stopsCount = 8
    )

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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  
                       Icon(
                        imageVector = Icons.Default.AltRoute,
                        contentDescription = null,
                        tint = Color(0xFFF95721),
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "Meal Slots Setup",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF191C1F),
                        letterSpacing = (-0.3).sp
                    )
                }
                // Progress Header Indicator (Step 2 of 3)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFBCC7DE)))
                        Box(modifier = Modifier.width(26.dp).height(8.dp).clip(RoundedCornerShape(22.dp)).background(Color(0xFFD53E02)))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFBCC7DE)))
                    }
                    Text(
                        text = "STEP 2 OF 3",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF545F73),
                        letterSpacing = 0.8.sp
                    )
                }

                // Title & Supporting Context
                Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                    Text(
                        text = "Seamless Deliveries & Trust Wallet Ledgers",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191C1F),
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Streamline morning tiffin drop-offs with sequenced driver manifests and zero-loss customer accounting.",
                        fontSize = 13.sp,
                        color = Color(0xFF545F73),
                        lineHeight = 18.sp
                    )
                }

                // Key Highlights Feature List
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
                                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFFFDBD1)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AltRoute, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text("Smart Route Sequencer", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                Text(
                                    "Drag & drop delivery stops to minimize transit time and keep curries steaming hot.",
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
                                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFD5E0F8)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Payments, null, tint = Color(0xFF545F73), modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text("Prepaid Wallet & Credit Ceiling", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                Text(
                                    "Automate balance alerts and pause meals instantly if negative credit crosses safety thresholds.",
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
                                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFECEEF2)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ReceiptLong, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text("Instant Thermal Sticker Labels", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                Text(
                                    "Print 58mm customer dabba slips via Bluetooth with 1 tap right off the packing table.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF545F73),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Bottom Actions Container
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onNavigateBack,
                        modifier = Modifier.height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE6E8EC)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color(0xFF191C1F), modifier = Modifier.size(16.dp))
                            Text("Back", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                        }
                    }

                    Button(
                        onClick = onContinue,
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00)),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Continue", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
