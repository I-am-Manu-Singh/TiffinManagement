package com.creativeblisslabs.tiffinmanagement.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.generated.resources.Res
import com.creativeblisslabs.tiffinmanagement.generated.resources.app_logo
import kotlinx.coroutines.delay

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit = {},
    onSendOtpSuccess: (emailOrPhone: String) -> Unit = {}
) {
    var recoveryMode by remember { mutableStateOf("email") } // "email" or "mobile"
    var identityInput by remember { mutableStateOf("") }

    var isVerifyingBot by remember { mutableStateOf(true) }
    var isBotVerified by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        delay(1000)
        isVerifyingBot = false
        isBotVerified = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FD))
    ) {
        

        // Body Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Visual Delight & Culinary Security Badge
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier.size(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer glow disc
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFDBD1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFAB2F00)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LockReset,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    // Floating micro badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = Color(0xFF006947),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = "Forgot Password?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191C1F)
                )

                Text(
                    text = "Enter your registered credentials. We will send a 6-digit culinary verification OTP to quickly restore kitchen access.",
                    fontSize = 13.sp,
                    color = Color(0xFF545F73),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }

            // Main Card Container
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Tab Toggle: Email vs Mobile
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF2F4F8))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Email Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (recoveryMode == "email") Color.White else Color.Transparent)
                                .clickable {
                                    recoveryMode = "email"
                                    identityInput = ""
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mail,
                                    contentDescription = null,
                                    tint = if (recoveryMode == "email") Color(0xFFAB2F00) else Color(0xFF545F73),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Email Address",
                                    fontSize = 12.sp,
                                    fontWeight = if (recoveryMode == "email") FontWeight.Bold else FontWeight.Medium,
                                    color = if (recoveryMode == "email") Color(0xFFAB2F00) else Color(0xFF545F73)
                                )
                            }
                        }

                        // Mobile Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (recoveryMode == "mobile") Color.White else Color.Transparent)
                                .clickable {
                                    recoveryMode = "mobile"
                                    identityInput = ""
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = if (recoveryMode == "mobile") Color(0xFFAB2F00) else Color(0xFF545F73),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Mobile Number",
                                    fontSize = 12.sp,
                                    fontWeight = if (recoveryMode == "mobile") FontWeight.Bold else FontWeight.Medium,
                                    color = if (recoveryMode == "mobile") Color(0xFFAB2F00) else Color(0xFF545F73)
                                )
                            }
                        }
                    }

                    // Input Field Section
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (recoveryMode == "email") "Email Account" else "Registered Mobile",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF5B4139)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.VerifiedUser, null, tint = Color(0xFF006947), modifier = Modifier.size(13.dp))
                                Text("Verified Tiffin ID", fontSize = 11.sp, color = Color(0xFF006947), fontWeight = FontWeight.Medium)
                            }
                        }

                        OutlinedTextField(
                            value = identityInput,
                            onValueChange = { identityInput = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = if (recoveryMode == "email") Icons.Default.AlternateEmail else Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = Color(0xFF545F73),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                if (identityInput.isNotEmpty()) {
                                    IconButton(onClick = { identityInput = "" }) {
                                        Icon(Icons.Default.Cancel, "Clear", tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                                    }
                                }
                            },
                            placeholder = {
                                Text(
                                    text = if (recoveryMode == "email") "name@example.com" else "+91 98765 43210",
                                    fontSize = 13.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = if (recoveryMode == "email") KeyboardType.Email else KeyboardType.Phone,
                                imeAction = ImeAction.Done
                            ),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFAB2F00),
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )
                    }

                    // Security Notice Pill Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFD5E0F8).copy(alpha = 0.5f))
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Timer, null, tint = Color(0xFF545F73), modifier = Modifier.size(16.dp).padding(top = 1.dp))
                            Text(
                                text = "Password reset links and OTP codes expire in 15 minutes. Keep this kitchen screen open while checking your messages.",
                                fontSize = 11.sp,
                                color = Color(0xFF586377),
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // Primary Action CTA
                    Button(
                        onClick = {
                            if (identityInput.isBlank()) {
                                errorMessage = "Please enter your identity details"
                                return@Button
                            }
                            isLoading = true
                            errorMessage = null
                            com.example.myapplicationkmp.data.repository.TiffinRepository.sendPasswordResetOtp(identityInput.trim())
                            isLoading = false
                            onSendOtpSuccess(identityInput.trim())
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00)),
                        enabled = !isLoading && isBotVerified
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "SEND OTP CODE",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 1.sp
                                )
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

           

            // Footer Navigation: Back to Login
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Remember your password?", fontSize = 12.sp, color = Color(0xFF545F73))
                    Row(
                        modifier = Modifier.clickable { onNavigateBack() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Back to Login", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00))
                        Icon(Icons.Default.ChevronRight, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(16.dp))
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.SupportAgent, null, tint = Color(0xFF545F73), modifier = Modifier.size(15.dp))
                    Text("Need operator supervisor help? Contact Support", fontSize = 11.sp, color = Color(0xFF545F73))
                }
            }
        }
    }
}
