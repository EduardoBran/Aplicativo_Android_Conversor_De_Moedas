package com.luizeduardobrandao.conversordemoeda.viewmodel

import android.icu.text.NumberFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luizeduardobrandao.conversordemoeda.model.ConverterCalculator
import java.util.Locale

class ConverterViewModel: ViewModel() {

    // 1) Estado da moeda selecionada
    private val _selectedCurrency = MutableLiveData<String?>()
    val selectedCurrency: LiveData<String?> = _selectedCurrency

    // 2) Resultado da conversão
    private val _conversionResult = MutableLiveData<String>()
    val conversionResult: LiveData<String> = _conversionResult

    // 3) Mensagem de erro (por exemplo, sem moeda selecionada ou moeda inválida)
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Instância do Model
    private val converter = ConverterCalculator()


    // Define a moeda atual escolhida pelo usuário
    fun setSelectedCurrency(currency: String) {
        _selectedCurrency.value = currency
    }

    // Dispara o cálculo de conversão (@param amount valor em BRL digitado pelo usuário)
    fun convert(amount: Double) {
        val currency = _selectedCurrency.value
        if (currency.isNullOrBlank()) {
            _errorMessage.value = "Selecione uma moeda antes de converter."
            return
        }

        try {
            // chama o model
            val raw = converter.convertRealTo(currency, amount)

            // formata de acordo com a moeda escolhida
            val formatted = when (currency) {
                "Dólar"          -> NumberFormat.getCurrencyInstance(Locale.US).format(raw)
                "Euro"           -> NumberFormat.getCurrencyInstance(Locale.GERMANY).format(raw)
                "Iene"           -> NumberFormat.getCurrencyInstance(Locale.JAPAN).format(raw)
                "Peso Arg."      -> NumberFormat.getCurrencyInstance(Locale("es","AR")).format(raw)
                else             -> String.format("%.2f", raw)
            }

            _conversionResult.value = formatted
            // Limpa mensagem de erro
            _errorMessage.value = null

        } catch (e: IllegalArgumentException) {
            _errorMessage.value = e.message
        }
    }
}