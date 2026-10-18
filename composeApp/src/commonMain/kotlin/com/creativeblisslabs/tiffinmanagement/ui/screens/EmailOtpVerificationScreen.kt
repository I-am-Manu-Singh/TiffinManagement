package com.creativeblisslabs.tiffinmanagement.ui.screens

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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.generated.resources.Res
import com.creativeblisslabs.tiffinmanagement.generated.resources.app_logo
import kotlinx.coroutines.delay

@Composable
fun EmailOtpVerificationScreen(
    email: String = "",
    flowType: String = "forgot_password", // "registration" or "forgot_password"
    onNavigateBack: () -> Unit = {},
    onResetSuccess: () -> Unit = {},
    onVerifySuccess: () -> Unit = onResetSuccess
) {
    var otpDigits by remember { mutableStateOf(listOf("", "", "", "", "", "")) }
    var activeDigitIndex by remember { mutableIntStateOf(0) }

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isNewPasswordVisible by remember { mutableStateOf(false) }

    var resendCountdown by remember { mutableIntStateOf(48) }
    var isResendEnabled by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successToast by remember { mutableStateOf<String?>(null) }

    // Countdown effect
    LaunchedEffect(isResendEnabled, resendCountdown) {
        if (!isResendEnabled && resendCountdown > 0) {
            delay(1000)
            resendCountdown--
            if (resendCountdown <= 0) {
                isResendEnabled = true
            }
        }
    }

    val has8Chars = newPassword.length >= 8
    val has1Number = newPassword.any { it.isDigit() }
    val has1Special = newPassword.any { !it.isLetterOrDigit() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FD))
    ) {
        

        // Scrollable Body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Visual Hero Banner with Pulse Mail Badge
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F8)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier.size(68.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFDBD1)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MarkEmailUnread,
                                contentDescription = null,
                                tint = Color(0xFFAB2F00),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF006947)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.VerifiedUser, null, tint = Color.White, modifier = Modifier.size(14.dp))
                        }
                    }

                    Text(
                        text = if (flowType == "registration") "Confirm Your Account" else "Verify Email & OTP",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191C1F)
                    )

                    Text(
                        text = if (flowType == "registration") 
                            "We sent a 6-digit confirmation code to activate your account" 
                        else 
                            "We sent a 6-digit verification code to reset your password",
                        fontSize = 13.sp,
                        color = Color(0xFF5B4139),
                        textAlign = TextAlign.Center
                    )

                    // Email Chip with Edit
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(Color(0xFFECEEF2))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Mail, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(16.dp))
                        Text(
                            text = email,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF191C1F)
                        )
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { onNavigateBack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Edit, null, tint = Color(0xFF5B4139), modifier = Modifier.size(12.dp))
                        }
                    }
                }
            }

            // Main Form Container Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Dispatched notification banner if any
                    successToast?.let { toast ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFE8F5E9))
                                .border(1.dp, Color(0xFFA5D6A7), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
                                Text(
                                    text = toast,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                        }
                    }

                  // OTP Section
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ONE-TIME PASSCODE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF191C1F),
                                letterSpacing = 0.5.sp
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Timer, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(13.dp))
                                Text("Expires in 10m", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFFAB2F00))
                            }
                        }

                        // 6-digit boxes with interactive text input
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            otpDigits.forEachIndexed { index, digit ->
                                val isActive = index == activeDigitIndex
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isActive) Color(0xFFFFDBD1) else Color(0xFFF2F4F8))
                                        .border(
                                            width = if (isActive) 1.5.dp else 1.dp,
                                            color = if (isActive) Color(0xFFAB2F00) else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { activeDigitIndex = index },
                                    contentAlignment = Alignment.Center
                                ) {
                                    androidx.compose.foundation.text.BasicTextField(
                                        value = digit,
                                        onValueChange = { input ->
                                            val filtered = input.filter { it.isDigit() }
                                            if (filtered.isNotEmpty()) {
                                                val char = filtered.last().toString()
                                                val newDigits = otpDigits.toMutableList()
                                                newDigits[index] = char
                                                otpDigits = newDigits
                                                if (index < 5) activeDigitIndex = index + 1
                                            } else {
                                                val newDigits = otpDigits.toMutableList()
                                                newDigits[index] = ""
                                                otpDigits = newDigits
                                                if (index > 0) activeDigitIndex = index - 1
                                            }
                                        },
                                        modifier = Modifier.fillMaxSize().padding(top = 14.dp),
                                        textStyle = androidx.compose.ui.text.TextStyle(
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF191C1F),
                                            textAlign = TextAlign.Center
                                        ),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.NumberPassword,
                                            imeAction = if (index == 5) ImeAction.Done else ImeAction.Next
                                        ),
                                        singleLine = true,
                                        decorationBox = { innerTextField ->
                                            Box(contentAlignment = Alignment.Center) {
                                                if (digit.isEmpty() && !isActive) {
                                                    Text("•", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD8DADE))
                                                }
                                                innerTextField()
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        // Countdown & Resend Button
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Update, null, tint = Color(0xFF545F73), modifier = Modifier.size(16.dp))
                                Text(
                                    text = if (resendCountdown > 0) "Resend code in 00:${if (resendCountdown < 10) "0$resendCountdown" else resendCountdown}" else "Ready to resend",
                                    fontSize = 12.sp,
                                    color = Color(0xFF545F73)
                                )
                            }

                            Text(
                                text = "Resend OTP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isResendEnabled) Color(0xFFAB2F00) else Color(0xFFD8DADE),
                                modifier = Modifier.clickable(enabled = isResendEnabled) {
                                    val newCode = (100000..999999).random().toString()
                                    if (flowType == "registration") {
                                        com.example.myapplicationkmp.data.repository.TiffinRepository.updatePendingOtp(newCode)
                                    } else {
                                        com.example.myapplicationkmp.data.repository.TiffinRepository.sendPasswordResetOtp(email)
                                    }
                                    resendCountdown = 60
                                    isResendEnabled = false
                                    successToast = "A new verification code has been dispatched to $email"
                                }
                            )
                        }
                    }

                    // Divider Pill: Set New Password (ONLY for forgot_password flow)
                    if (flowType == "forgot_password") {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFE6E8EC)))
                            Text(
                                text = "SET NEW PASSWORD",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF545F73),
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color(0xFFE6E8EC)))
                        }

                        // Password Inputs
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            // New Password
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("New Password", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                OutlinedTextField(
                                    value = newPassword,
                                    onValueChange = { newPassword = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    leadingIcon = {
                                        Icon(Icons.Default.Lock, null, tint = Color(0xFF545F73), modifier = Modifier.size(20.dp))
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { isNewPasswordVisible = !isNewPasswordVisible }) {
                                            Icon(
                                                imageVector = if (isNewPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = null,
                                                tint = Color(0xFF5B4139)
                                            )
                                        }
                                    },
                                    visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    placeholder = { Text("Create password", fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFAB2F00),
                                        unfocusedBorderColor = Color(0xFFE2E8F0)
                                    )
                                )
                            }

                            // Confirm New Password
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Confirm New Password", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                OutlinedTextField(
                                    value = confirmPassword,
                                    onValueChange = { confirmPassword = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    leadingIcon = {
                                        Icon(Icons.Default.LockReset, null, tint = Color(0xFF545F73), modifier = Modifier.size(20.dp))
                                    },
                                    trailingIcon = {
                                        if (confirmPassword.isNotEmpty() && confirmPassword == newPassword) {
                                            Box(
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF006947)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    },
                                    visualTransformation = PasswordVisualTransformation(),
                                    placeholder = { Text("Re-enter password", fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFAB2F00),
                                        unfocusedBorderColor = Color(0xFFE2E8F0)
                                    )
                                )
                            }

                            // Password Strength Checklist Chips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Chip 1: 8+ chars
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(100.dp))
                                        .background(if (has8Chars) Color(0xFF006947).copy(alpha = 0.1f) else Color(0xFFF2F4F8))
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (has8Chars) Icons.Default.CheckCircle else Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (has8Chars) Color(0xFF006947) else Color(0xFFD8DADE),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text("8+ chars", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = if (has8Chars) Color(0xFF006947) else Color(0xFF545F73))
                                }

                                // Chip 2: 1 number
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(100.dp))
                                        .background(if (has1Number) Color(0xFF006947).copy(alpha = 0.1f) else Color(0xFFF2F4F8))
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (has1Number) Color(0xFF006947) else Color(0xFFD8DADE),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text("1 number", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = if (has1Number) Color(0xFF006947) else Color(0xFF545F73))
                                }

                                // Chip 3: 1 special char
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(100.dp))
                                        .background(if (has1Special) Color(0xFF006947).copy(alpha = 0.1f) else Color(0xFFF2F4F8))
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (has1Special) Color(0xFF006947) else Color(0xFFD8DADE),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text("1 special", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = if (has1Special) Color(0xFF006947) else Color(0xFF545F73))
                                }
                            }
                        }
                    }

                    // Error Message
                    errorMessage?.let { err ->
                        Text(text = err, fontSize = 12.sp, color = Color(0xFFBA1A1A))
                    }

                    // Primary CTA
                    Button(
                        onClick = {
                            val enteredOtp = otpDigits.joinToString("")
                            if (enteredOtp.length < 6) {
                                errorMessage = "Please enter complete 6-digit verification code"
                                return@Button
                            }
                            
                            if (flowType == "registration") {
                                isLoading = true
                                errorMessage = null
                                val isVerified = com.example.myapplicationkmp.data.repository.TiffinRepository.verifyRegistrationOtp(enteredOtp)
                                isLoading = false
                                if (isVerified) {
                                    onVerifySuccess()
                                } else {
                                    errorMessage = "Invalid verification code. Please check the code sent or click Resend OTP."
                                }
                            } else {
                                if (!has8Chars || !has1Number) {
                                    errorMessage = "Password must be at least 8 characters and include a number"
                                    return@Button
                                }
                                if (newPassword != confirmPassword) {
                                    errorMessage = "Passwords do not match"
                                    return@Button
                                }
                                isLoading = true
                                errorMessage = null
                                val isVerified = com.example.myapplicationkmp.data.repository.TiffinRepository.verifyResetOtp(enteredOtp)
                                if (isVerified) {
                                    com.example.myapplicationkmp.data.repository.TiffinRepository.resetPassword(newPassword)
                                    isLoading = false
                                    onVerifySuccess()
                                } else {
                                    isLoading = false
                                    errorMessage = "Invalid or expired OTP code. Please check the code sent to your email or click Resend OTP."
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00)),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = if (flowType == "registration") "VERIFY & COMPLETE REGISTRATION" else "VERIFY & RESET PASSWORD",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 0.5.sp
                                )
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            // Troubleshooting & Alternative Channel Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F8)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFECEEF2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.HelpOutline, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Didn't receive the email? Check your spam or junk folder.", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                        }
                    }

                }
            }

            // Secondary Navigation: Return to Sign In
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateBack() }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color(0xFF545F73), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Cancel and return to Sign In", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF545F73))
            }
        }
    }
}
