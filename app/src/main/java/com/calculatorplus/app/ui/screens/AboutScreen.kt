package com.calculatorplus.app.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.calculatorplus.app.R
import com.calculatorplus.app.ui.theme.GrayText
import com.calculatorplus.app.ui.theme.RedAccent

@Composable
fun AboutScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Inline Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                text = "ABOUT",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = com.calculatorplus.app.ui.theme.PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, GrayText.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Safe drawable logo loading (prevents adaptive mipmap resource crash!)
                    Image(
                        painter = painterResource(id = R.drawable.icon2),
                        contentDescription = "App Icon",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(20.dp))
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Text(
                        text = "Calc +",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Version 1.0.2",
                        style = MaterialTheme.typography.bodyLarge,
                        color = GrayText
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayText.copy(alpha = 0.8f)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 0.5.dp,
                        color = GrayText.copy(alpha = 0.2f)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Created by",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayText
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "pixelcraftin",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = RedAccent,
                        modifier = Modifier.clickable {
                            // Wrapped in try-catch to guarantee intent launch safety!
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/pixelcraftin"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "No app available to open links", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}
