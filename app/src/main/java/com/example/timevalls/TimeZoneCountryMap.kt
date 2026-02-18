package com.example.timevalls

import com.example.timevalls.R

data class CountryTimeZone(
    val zoneId: String,
    val countryName: String,
    val flagResId: Int
)

object TimeZoneCountryMap {

    // ------------------ ASIA ------------------
    val Asia = listOf(
        CountryTimeZone("Asia/Tokyo", "Japón", R.drawable.jp),
        CountryTimeZone("Asia/Seoul", "Corea del Sur", R.drawable.kr),
        CountryTimeZone("Asia/Bangkok", "Tailandia", R.drawable.th),
        CountryTimeZone("Asia/Ho_Chi_Minh", "Vietnam", R.drawable.vn),
        CountryTimeZone("Asia/Shanghai", "China", R.drawable.cn),
        CountryTimeZone("Asia/Kuala_Lumpur", "Malasia", R.drawable.my),
        CountryTimeZone("Asia/Singapore", "Singapur", R.drawable.sg),
        CountryTimeZone("Asia/Colombo", "Sri Lanka", R.drawable.lk),
        CountryTimeZone("Asia/Manila", "Filipinas", R.drawable.ph),
        CountryTimeZone("Asia/Jakarta", "Indonesia", R.drawable.id)
    )

    // ------------------ EUROPA ------------------
    val Europe = listOf(
        CountryTimeZone("Europe/London", "Reino Unido", R.drawable.gb),
        CountryTimeZone("Europe/Paris", "Francia", R.drawable.fr),
        CountryTimeZone("Europe/Berlin", "Alemania", R.drawable.de),
        CountryTimeZone("Europe/Madrid", "España", R.drawable.es),
        CountryTimeZone("Europe/Rome", "Italia", R.drawable.it),
        CountryTimeZone("Europe/Moscow", "Rusia", R.drawable.ru),
        CountryTimeZone("Europe/Warsaw", "Polonia", R.drawable.pl),
        CountryTimeZone("Europe/Amsterdam", "Países Bajos", R.drawable.nl),
        CountryTimeZone("Europe/Oslo", "Noruega", R.drawable.no),
        CountryTimeZone("Europe/Helsinki", "Finlandia", R.drawable.fi)
    )

    // ------------------ AMÉRICA ------------------
    val America = listOf(
        CountryTimeZone("America/New_York", "Estados Unidos (Costa Este)", R.drawable.us),
        CountryTimeZone("America/Los_Angeles", "Estados Unidos (Costa Oeste)", R.drawable.us),
        CountryTimeZone("America/Chicago", "Estados Unidos (Central)", R.drawable.us),
        CountryTimeZone("America/Sao_Paulo", "Brasil", R.drawable.br),
        CountryTimeZone("America/Argentina/Buenos_Aires", "Argentina", R.drawable.ar),
        CountryTimeZone("America/Mexico_City", "México", R.drawable.mx),
        CountryTimeZone("America/Toronto", "Canadá", R.drawable.ca),
        CountryTimeZone("America/Vancouver", "Canadá (Oeste)", R.drawable.ca)
    )

    // ------------------ OCEANÍA ------------------
    val Oceania = listOf(
        CountryTimeZone("Australia/Sydney", "Australia", R.drawable.au),
        CountryTimeZone("Pacific/Auckland", "Nueva Zelanda", R.drawable.nz)
    )

    // Selección rápida por región
    val regions = mapOf(
        "Asia" to Asia,
        "Europa" to Europe,
        "América" to America,
        "Oceanía" to Oceania,
    )
}
