package com.example.timevalls

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.timevalls.ui.theme.TimeVallsTheme
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {

    private fun enableImmersive() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableImmersive()

        setContent {
            KeepImmersive() // Mantiene el modo inmersivo incluso en Compose

            TimeVallsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ClockScreen()
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) enableImmersive()
    }
}

@Composable
fun KeepImmersive() {
    val activity = LocalContext.current as Activity

    DisposableEffect(Unit) {
        val window = activity.window
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        onDispose {}
    }
}

@Composable
fun ClockScreen() {

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val clockFontSize = if (isLandscape) (minOf(screenWidthDp, screenHeightDp) * 0.75f).sp
    else (minOf(screenWidthDp, screenHeightDp) * 0.30f).sp

    val flagSize = if (isLandscape) 40.dp else 32.dp
    val countryTextSize = if (isLandscape) 20.sp else 16.sp
    val rowPadding = if (isLandscape) 20.dp else 8.dp
    val rowSpacing = if (isLandscape) 8.dp else 4.dp

    var showingMadrid by remember { mutableStateOf(true) }
    var otherZoneId by remember { mutableStateOf(ZoneId.of("Asia/Bangkok")) }
    var showRegionDialog by remember { mutableStateOf(false) }
    var showCountryDialog by remember { mutableStateOf(false) }
    var selectedRegion by remember { mutableStateOf("Asia") }

    val formatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    var currentTime by remember { mutableStateOf("00:00") }

    LaunchedEffect(showingMadrid, otherZoneId) {
        while (true) {
            val zone = if (showingMadrid) ZoneId.of("Europe/Madrid") else otherZoneId
            val now = Instant.now().atZone(zone)
            currentTime = now.format(formatter)
            delay(1000)
        }
    }

    fun getCountryForZone(zoneId: ZoneId): CountryTimeZone? {
        return TimeZoneCountryMap.regions.values
            .flatten()
            .firstOrNull { it.zoneId == zoneId.id }
    }

    val selectedCountry = getCountryForZone(otherZoneId)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { showingMadrid = !showingMadrid },
        contentAlignment = if (isLandscape) Alignment.Center else Alignment.TopCenter
    ) {

        if (!showingMadrid && selectedCountry != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .align(if (isLandscape) Alignment.TopEnd else Alignment.TopCenter)
                    .padding(rowPadding)
            ) {
                Text(
                    text = selectedCountry.countryName,
                    fontSize = countryTextSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(end = rowSpacing)
                )
                Image(
                    painter = painterResource(id = selectedCountry.flagResId),
                    contentDescription = selectedCountry.countryName,
                    modifier = Modifier.size(flagSize),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentTime,
                fontSize = clockFontSize,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF4700)
            )
        }

        IconButton(
            onClick = { showRegionDialog = true },
            modifier = Modifier
                .align(if (isLandscape) Alignment.BottomEnd else Alignment.TopEnd)
                .padding(rowPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.configuration),
                contentDescription = "Configuration",
                modifier = Modifier.size(if (isLandscape) 48.dp else 36.dp)
            )
        }
    }

    if (showRegionDialog) {
        AlertDialog(
            onDismissRequest = { showRegionDialog = false },
            title = { Text("Continente") },
            text = {
                Column {
                    TimeZoneCountryMap.regions.keys.forEach { region ->
                        Text(
                            text = region,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedRegion = region
                                    showRegionDialog = false
                                    showCountryDialog = true
                                }
                                .padding(10.dp)
                        )
                    }
                }
            },
            confirmButton = {},
            containerColor = Color(0xFF222222),
            textContentColor = Color.White,
            titleContentColor = Color.White
        )
    }

    if (showCountryDialog) {
        var searchQuery by remember { mutableStateOf("") }
        val countries = TimeZoneCountryMap.regions[selectedRegion] ?: emptyList()
        val filteredCountries = countries
            .filter { it.countryName.contains(searchQuery, ignoreCase = true) }
            .sortedBy { it.countryName }

        AlertDialog(
            onDismissRequest = { showCountryDialog = false },
            title = { Text("Selecciona País") },
            text = {
                Column {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        singleLine = true,
                        placeholder = { Text("Buscar...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                    ) {
                        items(filteredCountries) { country ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        otherZoneId = ZoneId.of(country.zoneId)
                                        showCountryDialog = false
                                    }
                                    .padding(horizontal = 8.dp, vertical = 10.dp)
                            ) {
                                Image(
                                    painter = painterResource(country.flagResId),
                                    contentDescription = country.countryName,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .padding(end = 14.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = country.countryName,
                                    fontSize = 20.sp,
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            containerColor = Color(0xFF222222),
            textContentColor = Color.White,
            titleContentColor = Color.White
        )
    }
}
