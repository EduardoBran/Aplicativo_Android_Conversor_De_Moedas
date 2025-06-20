package com.luizeduardobrandao.conversordemoeda.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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

    // ViewBinding: gera uma classe binding com todas as views do layout activity_main.xml
    private lateinit var binding: ActivityMainBinding

    // Inicializa o ViewModel usando o delegate viewModels()
    private val viewModel: ConverterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ativa o modo edge-to-edge: o layout fica abaixo das barras do sistema
        enableEdgeToEdge()

        // 1) Infla o layout via ViewBinding e define como conteúdo da Activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2) Configura a Toolbar (binding.toolbarMain) como ActionBar da Activity
        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false) // esconde o título padrão

        // Ajusta padding em tempo de execução para respeitar as insets (status bar, nav bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // aplica padding igual às margens do sistema
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa o SDK de anúncios de banner
        BannerRepository.initialize(this)

        // Chama funções para configurar ouvintes de eventos e observadores de LiveData
        setListeners()
        setObservers()

        // Carrega o banner de AdMob dentro do container definido no layout
        BannerRepository.loadBanner(this, binding.bannerContainer)
    }

    private fun setListeners() {
        // --- clique no ícone para navegar para AboutActivity ---
        binding.icForward.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        // --- captura o evento "Done" do teclado (IME) no campo de valor ---
        binding.edittextAmount.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // esconde o teclado
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(v.windowToken, 0)
                // foca no campo de seleção de moeda e abre o dropdown
                binding.actvCurrency.requestFocus()
                binding.actvCurrency.showDropDown()
                true  // consumiu o evento
            } else {
                false // não trata outros actions
            }
        }

        // --- configura o dropdown de moedas ---
        val currencies = CurrencyRepository.getCurrencies()
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            currencies
        )
        binding.actvCurrency.setAdapter(adapter)

        // Quando o usuário seleciona uma moeda na lista:
        binding.actvCurrency.setOnItemClickListener { _, _, pos, _ ->
            // _ = parâmetros que não usamos (parent, view, id)
            // pos = posição selecionada na lista de currencies
            viewModel.setSelectedCurrency(currencies[pos])
        }
        // Os underscores antes e depois de pos ignoram os parâmetros parent, view e id,
        // pois só precisamos de pos (o índice selecionado).

        // --- clique no botão Converter ---
        binding.btnConvert.setOnClickListener {
            val text = binding.edittextAmount.text.toString()
            val amount = text.toDoubleOrNull()
            if (amount == null) {
                // valor inválido: mostra um Toast de erro
                Toast.makeText(
                    this,
                    getString(R.string.error_invalid_value),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // valor válido: delega ao ViewModel para converter
                viewModel.convert(amount)
            }
        }
    }

    private fun setObservers() {
        // Observa a LiveData com o resultado formatado da conversão
        viewModel.conversionResult.observe(this) { formatted ->
            binding.textviewResult.text = formatted
        }
        // Observa mensagens de erro do ViewModel e exibe Toast
        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
