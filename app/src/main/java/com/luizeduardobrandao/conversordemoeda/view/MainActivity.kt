package com.luizeduardobrandao.conversordemoeda.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luizeduardobrandao.conversordemoeda.R
import com.luizeduardobrandao.conversordemoeda.databinding.ActivityMainBinding
import com.luizeduardobrandao.conversordemoeda.repository.BannerRepository
import com.luizeduardobrandao.conversordemoeda.repository.CurrencyRepository
import com.luizeduardobrandao.conversordemoeda.viewmodel.ConverterViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ConverterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ativa a renderização edge-to-edge (layout atrás das barras de status e navegação)
        enableEdgeToEdge()

        // 1) Infla o layout via ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2) Configura a Toolbar como ActionBar
        setSupportActionBar(binding.toolbarMain)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // inicializa SDK
        BannerRepository.initialize(this)

        setListeners()
        setObservers()

        // carrega o banner no container da view binding
        BannerRepository.loadBanner(this, binding.bannerContainer)
    }

    private fun setListeners(){
        // Dropdown de moedas
        val currencies = CurrencyRepository.getCurrencies()
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            currencies
        )
        binding.actvCurrency.setAdapter(adapter)
        binding.actvCurrency.setOnItemClickListener { _, _, pos, _ ->
            viewModel.setSelectedCurrency(currencies[pos])
        }

        // Clique no botão Converter
        binding.btnConvert.setOnClickListener {
            val text = binding.edittextAmount.text.toString()
            val amount = text.toDoubleOrNull()

            if (amount == null){
                Toast.makeText(
                    this,
                    getString(R.string.error_invalid_value),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else{
                viewModel.convert(amount)
            }
        }
    }

    private fun setObservers(){
        // Atualiza resultado na tela
        viewModel.conversionResult.observe(this) { formatted ->
            binding.textviewResult.text = formatted
        }

        // Exibe erros
        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}