package com.pixelcraftin.calcplus.data.model

data class CurrencyModel(
    val name: String,
    val code: String,
    val symbol: String
)

object CurrencyData {
    val currencies = listOf(
        CurrencyModel("Indian Rupee", "INR", "₹"),
        CurrencyModel("US Dollar", "USD", "$"),
        CurrencyModel("UAE Dirham", "AED", "د.إ"),
        CurrencyModel("Euro", "EUR", "€"),
        CurrencyModel("British Pound", "GBP", "£"),
        CurrencyModel("Saudi Riyal", "SAR", "﷼")
    )
    val defaultCurrency: CurrencyModel get() = currencies[0]
}
