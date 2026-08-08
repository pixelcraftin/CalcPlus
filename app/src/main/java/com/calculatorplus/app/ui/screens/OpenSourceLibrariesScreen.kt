package com.calculatorplus.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.calculatorplus.app.ui.theme.GrayText

data class LibraryInfo(
    val name: String,
    val author: String,
    val license: String,
    val description: String
)

@Composable
fun OpenSourceLibrariesScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val libraries = listOf(
        LibraryInfo(
            name = "Jetpack Compose",
            author = "Google LLC",
            license = "Apache License 2.0",
            description = "Android's modern toolkit for building native UI."
        ),
        LibraryInfo(
            name = "Jetpack Navigation",
            author = "Google LLC",
            license = "Apache License 2.0",
            description = "Support for navigating between composables in Jetpack Compose."
        ),
        LibraryInfo(
            name = "Kotlin Standard Library",
            author = "JetBrains s.r.o.",
            license = "Apache License 2.0",
            description = "The Kotlin core language utility standard library."
        ),
        LibraryInfo(
            name = "Google Doto Font",
            author = "Óliver Lalan",
            license = "SIL Open Font License 1.1 (OFL)",
            description = "Monospace geometric dot-matrix typeface family designed for digital screens."
        ),
        LibraryInfo(
            name = "Roboto Slab Font",
            author = "Christian Robertson",
            license = "Apache License 2.0",
            description = "A slab serif typeface designed by Christian Robertson."
        ),
        LibraryInfo(
            name = "Open Sans Condensed Font",
            author = "Steve Matteson",
            license = "Apache License 2.0",
            description = "A condensed sans-serif typeface designed by Steve Matteson."
        ),
        LibraryInfo(
            name = "AndroidX Lifecycle",
            author = "Google LLC",
            license = "Apache License 2.0",
            description = "Lifecycles, ViewModels, and state persistence integrations for Android components."
        )
    )

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
                text = "LICENSES",
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            libraries.forEach { lib ->
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
                            text = lib.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "By ${lib.author}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = GrayText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = lib.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = lib.license,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 11.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }
            }

            // Extra padding at the bottom so text scrolls well above the screen edge
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
