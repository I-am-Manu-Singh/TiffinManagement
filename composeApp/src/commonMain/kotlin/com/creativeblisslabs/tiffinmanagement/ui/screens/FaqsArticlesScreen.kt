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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.generated.resources.Res
import com.creativeblisslabs.tiffinmanagement.generated.resources.app_logo

@Composable
fun FaqsArticlesScreen(
    initialCategory: String = "all",
    onNavigateBack: () -> Unit = {},
    onNavigateToContact: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var searchQuery by remember { mutableStateOf("") }
    var expandedArticleId by remember { mutableStateOf<String?>("faq-1") }

    val allArticles = com.example.myapplicationkmp.data.repository.TiffinRepository.faqArticles

    val categories = listOf(
        "all" to "All (${allArticles.size})",
        "getting-started" to "Getting Started",
        "kitchen-cutoffs" to "Kitchen & Cutoffs",
        "wallet-billing" to "Wallet & Billing",
        "holds-pauses" to "Holds & Pauses",
        "delivery-routes" to "Delivery Routes"
    )

    val filteredArticles = remember(selectedCategory, searchQuery) {
        allArticles.filter { article ->
            val matchesCategory = (selectedCategory == "all" || article.category == selectedCategory)
            val matchesQuery = if (searchQuery.isBlank()) true else {
                article.title.contains(searchQuery, ignoreCase = true) ||
                article.previewText.contains(searchQuery, ignoreCase = true) ||
                article.category.contains(searchQuery, ignoreCase = true)
            }
            matchesCategory && matchesQuery
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
            // Hero Intro Banner with Warm Culinary Gradient Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFAB2F00).copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Help, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(14.dp))
                        }
                        Text(
                            text = "HELP & KNOWLEDGE BASE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFAB2F00),
                            letterSpacing = 0.5.sp
                        )
                    }

                    Text(
                        text = "How can we help today?",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF191C1F)
                    )

                    Text(
                        text = "Search operational guides, kitchen setup protocols, and recurring billing tips.",
                        fontSize = 13.sp,
                        color = Color(0xFF545F73),
                        lineHeight = 18.sp
                    )

                    // Search Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF2F4F8))
                            .padding(start = 10.dp, end = 4.dp, top = 3.dp, bottom = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, null, tint = Color(0xFF545F73), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        androidx.compose.foundation.text.BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp, color = Color(0xFF191C1F)),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text("Search articles, cutoffs, billing rules...", fontSize = 12.sp, color = Color(0xFF94A3B8))
                                }
                                innerTextField()
                            }
                        )
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }, modifier = Modifier.size(26.dp)) {
                                Icon(Icons.Default.Clear, null, tint = Color(0xFF545F73), modifier = Modifier.size(14.dp))
                            }
                        }
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAB2F00)),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("Search", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            // Category Filter Pills (Horizontal Scroll)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { (catId, catLabel) ->
                    val isSelected = selectedCategory == catId
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(if (isSelected) Color(0xFFAB2F00) else Color(0xFFECEEF2))
                            .clickable { selectedCategory = catId }
                            .padding(horizontal = 14.dp, vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = catLabel,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF191C1F)
                        )
                    }
                }
            }

            // Popular Questions Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Popular Questions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF191C1F)
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color(0xFFE6E8EC))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("${filteredArticles.size} Guides", fontSize = 11.sp, color = Color(0xFF545F73), fontWeight = FontWeight.Medium)
                }
            }

            // Articles List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                filteredArticles.forEach { article ->
                    val isExpanded = expandedArticleId == article.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedArticleId = if (isExpanded) null else article.id },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isExpanded) 2.dp else 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFFFFDBD1))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = article.category.replace("-", " ").replaceFirstChar { it.uppercase() },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF862300)
                                    )
                                }

                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = Color(0xFF545F73),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Text(
                                text = article.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF191C1F),
                                modifier = Modifier.padding(top = 6.dp)
                            )

                            if (!isExpanded) {
                                Text(
                                    text = article.previewText,
                                    fontSize = 12.sp,
                                    color = Color(0xFF545F73),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            AnimatedVisibility(visible = isExpanded) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFF2F4F8))
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = article.detailedContent,
                                        fontSize = 12.sp,
                                        color = Color(0xFF191C1F),
                                        lineHeight = 18.sp
                                    )

                                    if (article.steps.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        article.steps.forEachIndexed { sIdx, step ->
                                            Row(
                                                modifier = Modifier.padding(vertical = 3.dp),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.Top
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(18.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFFAB2F00)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text("${sIdx + 1}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                }
                                                Text(
                                                    text = step,
                                                    fontSize = 12.sp,
                                                    color = Color(0xFF545F73),
                                                    lineHeight = 17.sp,
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Updated ${article.updatedAgo}", fontSize = 11.sp, color = Color(0xFF545F73))
                                        Row(
                                            modifier = Modifier.clickable { onNavigateToContact() },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text("Ask Support", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFAB2F00))
                                            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color(0xFFAB2F00), modifier = Modifier.size(12.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
