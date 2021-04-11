package com.savage9ishere.tiwarimartadmin.orders.particular_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.savage9ishere.tiwarimartadmin.orders.AddressItem
import com.savage9ishere.tiwarimartadmin.orders.CartItems
import java.lang.IllegalArgumentException

class ParticularOrderViewModelFactory(
    private val listItems: ArrayList<CartItems>,
    private val address: AddressItem,
    private val paymentMethod: String,
    private val authPhone: String,
    private val orderPlacedTime: Long,
    private val status: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParticularOrderViewModel::class.java)){
            return ParticularOrderViewModel(
                listItems,
                address,
                paymentMethod,
                authPhone,
                orderPlacedTime,
                status
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}