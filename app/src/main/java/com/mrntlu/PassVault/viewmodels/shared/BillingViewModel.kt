package com.mrntlu.PassVault.viewmodels.shared

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.mrntlu.PassVault.utils.printLog
import com.revenuecat.purchases.*
import com.revenuecat.purchases.models.StoreTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(application: Application): AndroidViewModel(application) {

    var isErrorOccured = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    var isPurchased = mutableStateOf(false)
        private set

    var productList: List<Package>? = null

    init {
        getProductList()
    }

    fun loginUser(userID: String) {
        Purchases.sharedInstance.logInWith(
            userID,
            onError = {
                printLog("OnError $it")
                isErrorOccured.value = true
                errorMessage.value = it.message
            },
            onSuccess = { customerInfo, _ ->
                resetError()
                printLog("LoginUser $customerInfo")
            }
        )
    }

    fun logoutUser() {
        Purchases.sharedInstance.logOut()
    }

    fun getProductList() {
        Purchases.sharedInstance.getOfferingsWith(
            onError = { error ->
                isErrorOccured.value = true
                errorMessage.value = error.message
            },
            onSuccess = { offerings ->
                if (offerings.current != null) {
                    resetError()
                    productList = offerings.current!!.availablePackages
                } else {
                    isErrorOccured.value = true
                    errorMessage.value = "No offer found."
                }
            }
        )
    }

    fun restorePurchase() {
        Purchases.sharedInstance.restorePurchasesWith(
            onError = {
                printLog("Restore Error $it")
                isErrorOccured.value = true
                errorMessage.value = it.message
            },
            onSuccess = {
                if (checkUserPurchase(it)) {
                    resetError()
                    isPurchased.value = checkUserPurchase(it)
                } else {
                    isErrorOccured.value = true
                    errorMessage.value = "Couldn't find any purchase."
                }
            },
        )
    }

    fun onPurchaseError(error: PurchasesError) {
        printLog("OnError $error")
        isErrorOccured.value = true
        errorMessage.value = error.message
    }

    fun onPurchaseSuccess(purchase: StoreTransaction, customerInfo: CustomerInfo) {
        printLog("OnSuccess $purchase $customerInfo")
        resetError()
    }

    private fun checkUserPurchase(customerInfo: CustomerInfo): Boolean = customerInfo.entitlements["ads"]?.isActive ?: false

    fun resetError() {
        isErrorOccured.value = false
        errorMessage.value = null
    }
}