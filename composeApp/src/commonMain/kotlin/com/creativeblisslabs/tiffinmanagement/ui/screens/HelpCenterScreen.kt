package com.creativeblisslabs.tiffinmanagement.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.generated.resources.Res
import com.creativeblisslabs.tiffinmanagement.generated.resources.app_logo

@Composable
fun HelpCenterScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToFaqs: () -> Unit = {},
    onNavigateToContact: () -> Unit = {},
    onNavigateToAppInfo: () -> Unit = {},
    onTopicClick: (categorySlug: String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("English (IN)") }
    var showLanguageMenu by remember { mutableStateOf(false) }
    var expandedFaqId by remember { mutableStateOf<String?>("faq-1") }

    val topics = com.example.myapplicationkmp.data.repository.TiffinRepository.knowledgeTopics
    val faqs = com.example.myapplicationkmp.data.repository.TiffinRepository.faqArticles

    val filteredFaqs = remember(searchQuery) {
        if (searchQuery.isBlank()) faqs
        else faqs.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.previewText.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true)
        }
    }

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
                .padding(bottom = 32.dp)
        ) {
            // Interactive Search & Hero Prompt Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Guides, setup tutorials & live support",
                        fontSize = 13.sp,
                        color = Color(0xFF545F73)
                    )

                    // Language Selector Capsule
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(Color(0xFFE6E8EC))
                            .clickable {
                                selectedLanguage = if (selectedLanguage == "English (IN)") "Hindi (हिंदी)" else "English (IN)"
                            }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Translate, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(14.dp))
                        Text(selectedLanguage, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F))
                        Icon(Icons.Default.ExpandMore, null, tint = Color(0xFF545F73), modifier = Modifier.size(14.dp))
                    }
                }

                // Search Bar with clear and action button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                        .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, null, tint = Color(0xFF545F73), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    androidx.compose.foundation.text.BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 14.sp,
                            color = Color(0xFF191C1F)
                        ),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text("Search articles, features, questions...", fontSize = 13.sp, color = Color(0xFF94A3B8))
                            }
                            innerTextField()
                        }
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Clear, null, tint = Color(0xFF545F73), modifier = Modifier.size(16.dp))
                        }
                    }
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00)),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text("Search", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            // Real-time Quick Assistance Pills / Horizontal Scroll Cards
            Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Card 1: Live Operator Chat
                    Card(
                        modifier = Modifier
                            .width(260.dp)
                            .clickable { onNavigateToContact() },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFAB2F00).copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Forum, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(20.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Live Operator Chat", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F), maxLines = 1)
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF00855B)))
                                }
                                Text("Avg reply < 5 mins", fontSize = 12.sp, color = Color(0xFF545F73))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(top = 6.dp)
                                ) {
                                    Text("Start chat", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00))
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(13.dp))
                                }
                            }
                        }
                    }

                    // Card 2: WhatsApp Direct
                    Card(
                        modifier = Modifier.width(260.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF006947).copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Chat, null, tint = Color(0xFF006947), modifier = Modifier.size(20.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("WhatsApp Direct", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F), maxLines = 1)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(100.dp))
                                            .background(Color(0xFF6FFBBE))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("FAST", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF002113))
                                    }
                                }
                                Text("+91 76548 43346", fontSize = 12.sp, color = Color(0xFF545F73))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(top = 6.dp)
                                ) {
                                    Text("Open WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006947))
                                    Icon(Icons.Default.OpenInNew, null, tint = Color(0xFF006947), modifier = Modifier.size(13.dp))
                                }
                            }
                        }
                    }

                    // Card 3: 5-Min Video Setup
                    Card(
                        modifier = Modifier.width(260.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFD5E0F8)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PlayCircle, null, tint = Color(0xFF586377), modifier = Modifier.size(22.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("5-Min Video Setup", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F), maxLines = 1)
                                Text("Kitchen onboarding walkthru", fontSize = 12.sp, color = Color(0xFF545F73))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(top = 6.dp)
                                ) {
                                    Text("Watch tutorial", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                                    Icon(Icons.Default.SmartDisplay, null, tint = Color(0xFF191C1F), modifier = Modifier.size(13.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Explore Help Topics Section (2-Column Grid)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Explore Help Topics", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                        Text("Browse guides by kitchen functional area", fontSize = 12.sp, color = Color(0xFF545F73))
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(Color(0xFFE6E8EC))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("10 Modules", fontSize = 11.sp, color = Color(0xFF545F73), fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2-Column Grid of 10 Modules
                val chunkedTopics = topics.chunked(2)
                chunkedTopics.forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        pair.forEach { topic ->
                            TopicCard(
                                topic = topic,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onTopicClick(topic.categorySlug)
                                    onNavigateToFaqs()
                                }
                            )
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Frequently Asked Questions Section (Accordion)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Frequently Asked Questions", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF191C1F))
                        Text("Step-by-step solutions to common operational tasks", fontSize = 12.sp, color = Color(0xFF545F73))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                filteredFaqs.forEach { faq ->
                    val isExpanded = expandedFaqId == faq.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clickable {
                                expandedFaqId = if (isExpanded) null else faq.id
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isExpanded) 2.dp else 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = faq.category.uppercase().replace("-", " & "),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFAB2F00),
                                    letterSpacing = 0.5.sp
                                )
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = Color(0xFF545F73),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Text(
                                text = faq.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF191C1F),
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            if (!isExpanded) {
                                Text(
                                    text = faq.previewText,
                                    fontSize = 12.sp,
                                    color = Color(0xFF545F73),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            AnimatedVisibility(visible = isExpanded) {
                                Column(modifier = Modifier.padding(top = 10.dp)) {
                                    Text(
                                        text = faq.detailedContent,
                                        fontSize = 12.sp,
                                        color = Color(0xFF545F73),
                                        lineHeight = 18.sp
                                    )
                                    if (faq.steps.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        faq.steps.forEachIndexed { sIdx, step ->
                                            Text(
                                                text = "${sIdx + 1}. $step",
                                                fontSize = 12.sp,
                                                color = Color(0xFF191C1F),
                                                lineHeight = 17.sp,
                                                modifier = Modifier.padding(vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.clickable { onNavigateToFaqs() },
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text("Read full walkthrough", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00))
                                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Operator Assistance Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFECEEF2)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFAB2F00).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.SupportAgent, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(24.dp))
                    }
                    Text(
                        text = "Still stuck with an order or route?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191C1F),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "Our kitchen ops engineering team is available 6:00 AM – 10:00 PM IST daily to assist dispatchers and chefs.",
                        fontSize = 12.sp,
                        color = Color(0xFF545F73),
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 14.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onNavigateToContact() },
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Call, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Text("Call Support", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        IconButton(
                            onClick = { onNavigateToContact() },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                        ) {
                            Icon(Icons.Default.Mail, null, tint = Color(0xFF191C1F), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopicCard(
    topic: com.example.myapplicationkmp.data.models.KnowledgeTopic,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val (icon, bgColor, tintColor) = when (topic.iconName) {
        "rocket_launch" -> Triple(Icons.Default.RocketLaunch, Color(0xFFAB2F00).copy(alpha = 0.1f), Color(0xFFAB2F00))
        "restaurant_menu" -> Triple(Icons.Default.RestaurantMenu, Color(0xFFFFDBD1), Color(0xFF862300))
        "group" -> Triple(Icons.Default.Group, Color(0xFFD8E3FB), Color(0xFF3C475A))
        "account_balance_wallet" -> Triple(Icons.Default.AccountBalanceWallet, Color(0xFF6FFBBE), Color(0xFF005236))
        "pause_circle" -> Triple(Icons.Default.PauseCircle, Color(0xFFE6E8EC), Color(0xFF191C1F))
        "soup_kitchen" -> Triple(Icons.Default.SoupKitchen, Color(0xFFAB2F00).copy(alpha = 0.1f), Color(0xFFAB2F00))
        "local_shipping" -> Triple(Icons.Default.LocalShipping, Color(0xFFD5E0F8), Color(0xFF586377))
        "query_stats" -> Triple(Icons.Default.QueryStats, Color(0xFFE6E8EC), Color(0xFF191C1F))
        "palette" -> Triple(Icons.Default.Palette, Color(0xFFFFDBD1), Color(0xFFAB2F00))
        else -> Triple(Icons.Default.Person, Color(0xFF6FFBBE), Color(0xFF002113))
    }

    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(bgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = tintColor, modifier = Modifier.size(17.dp))
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFF2F4F8))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text("${topic.articleCount} Articles", fontSize = 9.sp, color = Color(0xFF545F73), fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(topic.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF191C1F), maxLines = 1)
            Text(topic.subtitle, fontSize = 11.sp, color = Color(0xFF545F73), maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 2.dp))
        }
    }
}
