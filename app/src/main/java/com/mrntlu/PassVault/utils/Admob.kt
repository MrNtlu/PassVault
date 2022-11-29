package com.mrntlu.PassVault.utils

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mrntlu.PassVault.R

var mInterstitialAd: InterstitialAd? = null
var adCount = 0

fun loadInterstitial(context: Context) {
    InterstitialAd.load(
        context,
        context.getString(R.string.interstitial_ad_unit_id),
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
                adCount--
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        }
    )
}

fun addInterstitialCallbacks(context: Context) {
    mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdShowedFullScreenContent() {
            mInterstitialAd = null

            loadInterstitial(context)
        }
    }
}

fun showInterstitial(context: Context) {
    val activity = context.findActivity()

    if (mInterstitialAd != null && activity != null) {
        mInterstitialAd?.show(activity)
    }
}

fun removeInterstitial() {
    mInterstitialAd = null
    mInterstitialAd?.fullScreenContentCallback = null
}

