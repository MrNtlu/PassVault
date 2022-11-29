package com.mrntlu.PassVault.viewmodels.shared

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.revenuecat.purchases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(application: Application): AndroidViewModel(application) {

    var isErrorOccured = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var message = mutableStateOf<String?>(null)

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
                setError(it.message)
            },
            onSuccess = { customerInfo, _ ->
                resetError()
                checkUserPurchase(customerInfo)
            }
        )
    }

    fun logoutUser() {
        Purchases.sharedInstance.logOut()
    }

    private fun getProductList() {
        Purchases.sharedInstance.getOfferingsWith(
            onError = { error ->
                setError(error.message)
            },
            onSuccess = { offerings ->
                if (offerings.current != null) {
                    resetError()
                    productList = offerings.current!!.availablePackages
                } else {
                    setError("No offer found.")
                }
            }
        )
    }

    fun restorePurchase() {
        Purchases.sharedInstance.restorePurchasesWith(
            onError = {
                setError(it.message)
            },
            onSuccess = {
                checkUserPurchase(it)
                if (isPurchased.value) {
                    message.value = "Successfully restored."
                    resetError()
                } else {
                    setError("Couldn't find any purchase.")
                }
            },
        )
    }

    fun onPurchaseError(error: PurchasesError) {
        setError(error.message)
    }

    fun onPurchaseSuccess(customerInfo: CustomerInfo) {
        message.value = "Successfully purchased."
        resetError()
        checkUserPurchase(customerInfo)
    }

    private fun checkUserPurchase(customerInfo: CustomerInfo){
        isPurchased.value = customerInfo.entitlements["ads"]?.isActive ?: false
    }

    private fun setError(error: String) {
        isErrorOccured.value = true
        errorMessage.value = error
    }

    fun resetError() {
        isErrorOccured.value = false
        errorMessage.value = null
    }

    fun resetMessage() {
        message.value = null
    }
}