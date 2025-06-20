package com.luizeduardobrandao.conversordemoeda.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luizeduardobrandao.conversordemoeda.databinding.ActivityAboutBinding
import com.luizeduardobrandao.conversordemoeda.repository.BannerRepository

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate e seta content view
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajuste de insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        // Se você não estiver usando a seta nativa, não precisa chamar setSupportActionBar
        // e nem setDisplayHomeAsUpEnabled. Vamos usar o iv_back:
        binding.ivBack.setOnClickListener {
            finish()
        }

        // Carrega banner
        BannerRepository.initialize(this)
        BannerRepository.loadBanner(this, binding.bannerContainer)
    }
}
