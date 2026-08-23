package com.calculatorplus.app.data.model

data class CurrencyModel(
    val name: String,
    val code: String,
    val symbol: String
)

object CurrencyData {
    val currencies = listOf(
        CurrencyModel("US Dollar", "USD", "$"),
        CurrencyModel("Indian Rupee", "INR", "₹"),
        CurrencyModel("UAE Dirham", "AED", "د.إ"),
        CurrencyModel("Euro", "EUR", "€"),
        CurrencyModel("British Pound", "GBP", "£"),
        CurrencyModel("Saudi Riyal", "SAR", "﷼")
    )
}
