package com.calculatorplus.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.calculatorplus.app.ui.theme.GrayText

@Composable
fun PrivacyPolicyScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Inline Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "PRIVACY POLICY",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = com.calculatorplus.app.ui.theme.PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PolicySection(
                title = "Local Execution",
                details = "Calc Plus operates completely locally on your device. We do not transmit, share, or upload any values, calculations, history records, or preferences to external servers."
            )

            PolicySection(
                title = "Offline Security",
                details = "Since the application requires no internet or network connection permissions, there is no technical way for any of your calculation data to leave your device."
            )

            PolicySection(
                title = "Ad-Free & Tracker-Free",
                details = "This application is entirely free of ads, user behavior tracking, analytics frameworks, and third-party advertising SDKs."
            )

            PolicySection(
                title = "Open Source Transparency",
                details = "Calc Plus is published as a free, open-source utility under the GNU GPL v3.0 license. The source code is fully transparent, verifiable, and available on GitHub."
            )
        }

        // Extra padding at the bottom so text scrolls well above the screen edge
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun PolicySection(
    title: String,
    details: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, GrayText.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = details,
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
