package com.luizeduardobrandao.conversordemoeda.repository

import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

/**
 * Responsável por inicializar o SDK AdMob e carregar banners de teste.
 */
object BannerRepository {
    // IDs de teste do Google
    private const val TEST_APP_ID          = "ca-app-pub-3940256099942544~3347511713"
    private const val TEST_BANNER_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

    /** Inicializa o SDK do AdMob. */
    fun initialize(context: Context) {
        MobileAds.initialize(context) { /* callback opcional */ }
    }

    /**
     * Cria um AdView de banner de teste e adiciona ao container informado.
     * @param context contexto da Activity ou Fragment
     * @param container ViewGroup onde o banner será inserido
     */
    fun loadBanner(context: Context, container: ViewGroup) {
        // se já houver algo, limpa
        container.removeAllViews()

        // cria e configura o AdView
        val adView = AdView(context).apply {
            setAdSize(AdSize.BANNER)            // usa o setter em vez de atribuir à val
            adUnitId = TEST_BANNER_UNIT_ID
        }

        // adiciona ao layout
        container.addView(adView)

        // carrega o anúncio
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}