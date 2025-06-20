package com.luizeduardobrandao.conversordemoeda.repository

object CurrencyRepository {
    fun getCurrencies(): List<String> = listOf(
        "Dólar", "Euro", "Iene", "Peso Arg."
    )
}