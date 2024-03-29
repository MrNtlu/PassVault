package com.mrntlu.PassVault;

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.parse.Parse
import com.parse.ParseACL
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ParseApp: Application() {

    override fun onCreate() {
        super.onCreate()

        Parse.enableLocalDatastore(this)
        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId(getString(R.string.back4app_app_id))
            .clientKey(getString(R.string.back4app_client_key))
            .server(getString(R.string.back4app_server_url))
            .build()
        )

        val defaultACL = ParseACL()
        defaultACL.apply {
            publicReadAccess = true
            publicWriteAccess = true
        }
        ParseACL.setDefaultACL(defaultACL, true)

        Purchases.debugLogsEnabled = true
        Purchases.configure(
            PurchasesConfiguration.Builder(
                this,
                getString(R.string.revenuecat_key)
            ).build()
        )

        MobileAds.initialize(this)
        val configuration = RequestConfiguration.Builder()
//            .setTestDeviceIds(Collections.singletonList("802C7F4DE708805637B2A1EF202B75C9"))
//            .setTestDeviceIds(Collections.singletonList("802C7F4DE708805637B2A1EF202B75C9"))
            .build()
        MobileAds.setRequestConfiguration(configuration)
    }
}
