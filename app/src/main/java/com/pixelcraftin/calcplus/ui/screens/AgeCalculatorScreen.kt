package com.pixelcraftin.calcplus.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pixelcraftin.calcplus.ui.theme.GrayText
import com.pixelcraftin.calcplus.ui.theme.RedAccent
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun AgeCalculatorScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val today = remember { LocalDate.now() }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    var dobDate by remember { mutableStateOf<LocalDate?>(null) }
    var targetDate by remember { mutableStateOf(today) }

    var ageYears by remember { mutableStateOf<Int?>(null) }
    var ageMonths by remember { mutableStateOf<Int?>(null) }
    var ageDays by remember { mutableStateOf<Int?>(null) }
    var totalDays by remember { mutableStateOf<Long?>(null) }
    var totalWeeks by remember { mutableStateOf<Long?>(null) }
    var totalMonths by remember { mutableStateOf<Long?>(null) }
    var nextBirthdayDays by remember { mutableStateOf<Long?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isDark = MaterialTheme.colorScheme.background.red < 0.5f
    val borderColor = if (isDark) Color(0xFF3A3A3D) else Color(0xFFD1D1D6)
    val boxBg = if (isDark) Color(0xFF141416) else Color.White

    fun showDatePicker(initialDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
        val dpd = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            },
            initialDate.year,
            initialDate.monthValue - 1,
            initialDate.dayOfMonth
        )
        dpd.show()
    }

    fun calculateAge() {
        val birth = dobDate
        if (birth == null) {
            errorMessage = "Please select Date of Birth"
            return
        }
        if (birth.isAfter(targetDate)) {
            errorMessage = "DOB cannot be after Target Date"
            return
        }
        errorMessage = null
        val period = Period.between(birth, targetDate)
        ageYears = period.years
        ageMonths = period.months
        ageDays = period.days

        val daysBetween = ChronoUnit.DAYS.between(birth, targetDate)
        totalDays = daysBetween
        totalWeeks = daysBetween / 7
        totalMonths = ChronoUnit.MONTHS.between(birth, targetDate)

        var nextBday = birth.withYear(targetDate.year)
        if (nextBday.isBefore(targetDate) || nextBday.isEqual(targetDate)) {
            nextBday = nextBday.plusYears(1)
        }
        nextBirthdayDays = ChronoUnit.DAYS.between(targetDate, nextBday)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Nothing OS Header
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
                text = "AGE CALCULATOR",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        HorizontalDivider(color = GrayText.copy(alpha = 0.2f), thickness = 0.5.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Date of Birth Field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(boxBg)
                    .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                    .clickable {
                        showDatePicker(dobDate ?: LocalDate.of(2000, 1, 1)) { dobDate = it }
                    }
                    .padding(horizontal = 14.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dobDate?.let { "DOB: ${it.format(dateFormatter)}" } ?: "Select Date of Birth (DOB)",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        color = if (dobDate != null) MaterialTheme.colorScheme.onSurface else GrayText
                    )
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GrayText, modifier = Modifier.size(18.dp))
                }
            }

            // Target Date Field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(boxBg)
                    .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                    .clickable {
                        showDatePicker(targetDate) { targetDate = it }
                    }
                    .padding(horizontal = 14.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Age at: ${targetDate.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GrayText, modifier = Modifier.size(18.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = ::calculateAge,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) Color.White else Color.Black,
                        contentColor = if (isDark) Color.Black else Color.White
                    ),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("CALCULATE", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
                }

                Button(
                    onClick = {
                        dobDate = null
                        targetDate = today
                        ageYears = null
                        errorMessage = null
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RedAccent,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("RESET", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
                }
            }

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = RedAccent, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 13.sp))
            }

            NothingOSSummaryCard(title = "Age Breakdown Summary", isDark = isDark) {
                NothingOSMetricRow(
                    label = "Age",
                    value = if (ageYears != null) "$ageYears Y, $ageMonths M, $ageDays D" else "—",
                    isProminent = true
                )
                NothingOSMetricRow(
                    label = "Total Months",
                    value = totalMonths?.toString() ?: "—"
                )
                NothingOSMetricRow(
                    label = "Total Weeks",
                    value = totalWeeks?.toString() ?: "—"
                )
                NothingOSMetricRow(
                    label = "Total Days",
                    value = totalDays?.toString() ?: "—"
                )
                NothingOSMetricRow(
                    label = "Next Birthday in",
                    value = nextBirthdayDays?.let { "$it days" } ?: "—",
                    textColor = RedAccent
                )
            }
        }
    }
}
