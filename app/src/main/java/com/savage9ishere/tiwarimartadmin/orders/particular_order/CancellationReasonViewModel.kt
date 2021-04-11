package com.savage9ishere.tiwarimartadmin.orders.particular_order

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.savage9ishere.tiwarimartadmin.orders.OrderItem

class CancellationReasonViewModel : ViewModel() {

    val databaseRef = Firebase.database.reference

    private val _orderCancelled = MutableLiveData<Boolean?>()
    val orderCancelled : LiveData<Boolean?>
        get() = _orderCancelled

    fun cancelOrder(text: Editable, order: OrderItem?) {
        order!!.status = "ORDER CANCELLED"
        order.orderDeliveredOrCancelledTime = System.currentTimeMillis()

        val childUpdates = hashMapOf<String, Any?>(
            "/orders/${order.authPhone}/${order.orderKey}" to null,
            "/deliveredOrCancelled/${order.authPhone}/${order.orderKey}" to order
        )

        databaseRef.updateChildren(childUpdates).addOnCompleteListener {
            _orderCancelled.value = it.isSuccessful
        }
    }

    fun doneOrderCancelling() {
        _orderCancelled.value = null
    }
}