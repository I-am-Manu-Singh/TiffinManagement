package com.creativeblisslabs.tiffinmanagement.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SupportAgent
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

@Composable
fun ContactSupportScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToFaqs: () -> Unit = {},
    onNavigateToPrivacy: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {}
) {
    val authSession = com.example.myapplicationkmp.data.repository.TiffinRepository.authState.value
    var fullName by remember { mutableStateOf(authSession.currentUser?.fullName ?: "") }
    var email by remember { mutableStateOf(authSession.currentUser?.email ?: "") }
    var phone by remember { mutableStateOf(authSession.currentUser?.phone?.replace("+91", "")?.trim() ?: "") }
    var businessName by remember { mutableStateOf(authSession.currentUser?.businessName ?: "") }
    var selectedTopic by remember { mutableStateOf("onboarding") }
    var message by remember { mutableStateOf("") }
    var isHumanVerified by remember { mutableStateOf(true) }

    var isSubmitting by remember { mutableStateOf(false) }
    var successToast by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val topicOptions = listOf(
        "onboarding" to "Onboarding & Account Setup",
        "billing" to "Customer Wallet & Recurring Billing",
        "kitchen" to "Kitchen Aggregator & Roti Counter",
        "routes" to "Driver Route Sequencing & Dispatch",
        "other" to "Other Inquiries / Custom Request"
    )

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
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Success Toast Banner
            successToast?.let { toastText ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF00855B))
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF6FFBBE), modifier = Modifier.size(20.dp))
                            Text(toastText, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp).clickable { successToast = null }
                        )
                    }
                }
            }

            // Header Badge & Intro Block
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color(0xFFFFDBD1))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.SupportAgent, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(15.dp))
                        Text(
                            text = "GET IN TOUCH",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3A0A00),
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Text(
                    text = "Talk to Our Team",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191C1F)
                )

                Text(
                    text = "For plan enquiries, onboarding setup, or product questions — reach us using any channel below. We reply on working days.",
                    fontSize = 13.sp,
                    color = Color(0xFF545F73),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            // Direct Quick Touch Cards
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "CONTACT CHANNELS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF545F73),
                    letterSpacing = 0.5.sp
                )

                // 1. Email Channel
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFFFDBD1)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Mail, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text("Email", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                Text("support@tiffinmanager.com", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFFAB2F00))
                                Text("We reply within 1 working day", fontSize = 11.sp, color = Color(0xFF545F73))
                            }
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF545F73), modifier = Modifier.size(20.dp))
                    }
                }

                // 2. WhatsApp Channel
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFD5E0F8)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Chat, null, tint = Color(0xFF586377), modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text("WhatsApp", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(100.dp))
                                            .background(Color(0xFF6FFBBE))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("INSTANT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF002113))
                                    }
                                }
                                Text("+91 76548 43346", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                                Text("Available Mon–Sat, 10am–6pm IST", fontSize = 11.sp, color = Color(0xFF545F73))
                            }
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF545F73), modifier = Modifier.size(20.dp))
                    }
                }

                // 3. Kitchen Headquarters Location
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFE6E8EC)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.LocationOn, null, tint = Color(0xFF545F73), modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text("Headquarters", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                                Text("Patna, Bihar, India", fontSize = 12.sp, color = Color(0xFF545F73))
                                Text("ClungTech Operations Hub", fontSize = 11.sp, color = Color(0xFF545F73))
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFECEEF2))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("HQ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF545F73))
                        }
                    }
                }
            }

            // Inquiry Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column {
                        Text("Send an Inquiry", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                        Text(
                            "Describe your current tiffin setup and what you need help with. We will respond with a practical answer, not a sales pitch.",
                            fontSize = 12.sp,
                            color = Color(0xFF545F73),
                            lineHeight = 17.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Full Name
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Full Name *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            placeholder = { Text("e.g. Ramesh Kulkarni", fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFAB2F00),
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )
                    }

                    // Email Address
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Email *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            placeholder = { Text("ramesh@annapurnatiffins.com", fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFAB2F00),
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )
                    }

                    // Mobile Number
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Mobile Number", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .height(52.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF2F4F8))
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+91", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                            }

                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                placeholder = { Text("98765 43210", fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFAB2F00),
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                )
                            )
                        }
                    }

                    // Business / Cloud Kitchen Name
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Business / Cloud Kitchen Name", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                        OutlinedTextField(
                            value = businessName,
                            onValueChange = { businessName = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            placeholder = { Text("e.g. Annapurna Daily Tiffin", fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFAB2F00),
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )
                    }

                    // Topic Selector
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("What do you need help with?", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                        topicOptions.forEach { (tKey, tLabel) ->
                            val isChosen = selectedTopic == tKey
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isChosen) Color(0xFFFFDBD1) else Color(0xFFF2F4F8))
                                    .clickable { selectedTopic = tKey }
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tLabel,
                                    fontSize = 12.sp,
                                    fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isChosen) Color(0xFF862300) else Color(0xFF191C1F)
                                )
                                if (isChosen) {
                                    Icon(Icons.Default.Check, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    // Message Field
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Your Message *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            modifier = Modifier.fillMaxWidth().height(110.dp),
                            shape = RoundedCornerShape(10.dp),
                            placeholder = {
                                Text(
                                    "Tell us about your current tiffin operation — how many customers, how you manage billing now, and what problem you want to solve.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8),
                                    lineHeight = 17.sp
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFAB2F00),
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )
                    }


                    // Error text
                    errorMessage?.let { err ->
                        Text(text = err, fontSize = 12.sp, color = Color(0xFFBA1A1A))
                    }

                    // Submit CTA Button
                    Button(
                        onClick = {
                            if (fullName.isBlank()) {
                                errorMessage = "Please enter your full name"
                                return@Button
                            }
                            if (email.isBlank() || !email.contains("@")) {
                                errorMessage = "Please enter a valid email address"
                                return@Button
                            }
                            if (message.isBlank()) {
                                errorMessage = "Please enter your inquiry details"
                                return@Button
                            }
                            if (!isHumanVerified) {
                                errorMessage = "Please complete the security check"
                                return@Button
                            }

                            isSubmitting = true
                            errorMessage = null

                            val inquiry = com.example.myapplicationkmp.data.models.SupportInquiry(
                                id = "inq-${email.hashCode()}-${kotlin.random.Random.nextInt(1000, 9999)}",
                                fullName = fullName.trim(),
                                email = email.trim(),
                                phone = if (phone.isNotBlank()) "+91 ${phone.trim()}" else "",
                                businessName = businessName.trim(),
                                topic = selectedTopic,
                                message = message.trim()
                            )

                            com.example.myapplicationkmp.data.repository.TiffinRepository.submitSupportInquiry(inquiry)
                            isSubmitting = false
                            successToast = "Inquiry submitted! Our support chef will reach out within 1 working day."
                            message = ""
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00)),
                        enabled = !isSubmitting
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Send, null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Text("Submit Inquiry", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    Text(
                        text = "We reply within 1 working day. You can also chat directly on WhatsApp (+91 76548 43346) or email support@tiffinmanager.com.",
                        fontSize = 11.sp,
                        color = Color(0xFF545F73),
                        lineHeight = 16.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
                    )
                }
            }

            // Quick Links Footer Navigation
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "QUICK LINKS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF545F73),
                        letterSpacing = 0.5.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToFaqs() }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.HelpOutline, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(18.dp))
                            Text("Frequently Asked Questions", fontSize = 13.sp, color = Color(0xFF191C1F))
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF545F73), modifier = Modifier.size(18.dp))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToPrivacy() }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Policy, null, tint = Color(0xFF545F73), modifier = Modifier.size(18.dp))
                            Text("Privacy Policy", fontSize = 13.sp, color = Color(0xFF191C1F))
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF545F73), modifier = Modifier.size(18.dp))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToTerms() }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Gavel, null, tint = Color(0xFF545F73), modifier = Modifier.size(18.dp))
                            Text("Terms of Service", fontSize = 13.sp, color = Color(0xFF191C1F))
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF545F73), modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}
