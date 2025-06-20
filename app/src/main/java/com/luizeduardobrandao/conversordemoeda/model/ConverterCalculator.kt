package com.luizeduardobrandao.conversordemoeda.model

import com.luizeduardobrandao.conversordemoeda.helper.CurrencyConstants

// Responsável por converter valores em BRL para outras moedas,
// usando as constantes definidas em CurrencyConstants.
class ConverterCalculator {

    /**
     * Converte um valor em reais (BRL) para a moeda especificada.
     *
     * @param currency nome da moeda alvo ("Dólar", "Euro" ou "Iene")
     * @param amount valor em BRL a ser convertido
     * @return valor convertido na moeda alvo
     * @throws IllegalArgumentException se a moeda for inválida
     */
    fun convertRealTo(currency: String, amount: Double): Double {
        val rate = when (currency){
            "Dólar" -> CurrencyConstants.RATES.DOLAR
            "Euro" -> CurrencyConstants.RATES.EURO
            "Iene" -> CurrencyConstants.RATES.IENE
            "Peso Arg." -> CurrencyConstants.RATES.PESO_ARGENTINO
            else -> throw IllegalArgumentException("Moeda Inválida.")
        }
        return amount * rate
    }
}