package com.calculatorplus.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.calculatorplus.app.data.util.PreferencesManager
import com.calculatorplus.app.ui.components.TopHeaderNav
import com.calculatorplus.app.ui.navigation.Screen
import com.calculatorplus.app.ui.theme.GrayText
import com.calculatorplus.app.ui.theme.RedAccent
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    preferencesManager: PreferencesManager,
    modifier: Modifier = Modifier
) {
    val calculatorViewModel: CalculatorViewModel = viewModel()
    val pagerState = rememberPagerState(pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()
    
    var showHistoryDialog by remember { mutableStateOf(false) }
    var historyList by remember { mutableStateOf(emptyList<String>()) }
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(showHistoryDialog) {
        if (showHistoryDialog) {
            historyList = preferencesManager.getHistory()
        }
    }

    Scaffold(
        topBar = {
            TopHeaderNav(
                currentPage = pagerState.currentPage,
                onTabSelected = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                onHistoryClick = { showHistoryDialog = true },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Smooth Horizontal Screen Paging Swipe Layout
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> SimpleScreen(
                        viewModel = calculatorViewModel,
                        preferencesManager = preferencesManager
                    )
                    1 -> ScientificScreen(
                        viewModel = calculatorViewModel,
                        preferencesManager = preferencesManager
                    )
                    2 -> ConverterScreen(
                        preferencesManager = preferencesManager
                    )
                    3 -> UtilitiesScreen()
                }
            }
        }
    }

    // Calculation History Dialog Popup
    if (showHistoryDialog) {
        Dialog(onDismissRequest = { showHistoryDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .border(0.5.dp, GrayText.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "HISTORY",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (historyList.isNotEmpty()) {
                            IconButton(onClick = {
                                preferencesManager.clearHistory()
                                historyList = emptyList()
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Clear History Logs",
                                    tint = RedAccent
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    if (historyList.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No calculations recorded",
                                color = GrayText,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            historyList.forEach { entry ->
                                val split = entry.split("||")
                                val mathText = if (split.size > 1) split[1] else entry
                                
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val parts = mathText.split("=")
                                            if (parts.isNotEmpty()) {
                                                calculatorViewModel.onKeyClick("AC")
                                                parts[0].trim().forEach { char ->
                                                    calculatorViewModel.onKeyClick(char.toString())
                                                }
                                            }
                                            showHistoryDialog = false
                                        }
                                        .padding(vertical = 12.dp)
                                ) {
                                    Text(
                                        text = mathText,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        textAlign = TextAlign.Start
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    HorizontalDivider(color = GrayText.copy(alpha = 0.15f), thickness = 0.5.dp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = { showHistoryDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "CLOSE",
                            color = MaterialTheme.colorScheme.background,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
