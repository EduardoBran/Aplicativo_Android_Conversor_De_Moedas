package com.luizeduardobrandao.conversordemoeda.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luizeduardobrandao.conversordemoeda.model.ConverterCalculator

class ConverterViewModel: ViewModel() {

    // 1) Estado da moeda selecionada
    private val _selectedCurrency = MutableLiveData<String?>()
    val selectedCurrency: LiveData<String?> = _selectedCurrency

    // 2) Resultado da conversão
    private val _conversionResult = MutableLiveData<Double>()
    val conversionResult: LiveData<Double> = _conversionResult

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
    fun convert(amount: Double){

        val currency = _selectedCurrency.value

        if (currency.isNullOrBlank()) {
            _errorMessage.value = "Selecione uma moeda."
            return
        }

        try {
            val result = converter.convertRealTo(currency, amount)
            _conversionResult.value = result
            _errorMessage.value = null // limpa erro anterior
        }
        catch (e: IllegalStateException) {
            _errorMessage.value = e.message
        }
    }
}