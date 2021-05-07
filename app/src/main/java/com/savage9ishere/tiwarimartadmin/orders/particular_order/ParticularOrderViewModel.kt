package com.savage9ishere.tiwarimartadmin.orders.particular_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.savage9ishere.tiwarimartadmin.orders.AddressItem
import com.savage9ishere.tiwarimartadmin.orders.CartItems
import com.savage9ishere.tiwarimartadmin.orders.OrderItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ParticularOrderViewModel(
    listItems: ArrayList<CartItems>,
    address: AddressItem,
    paymentMethod: String,
    authPhone: String,
    orderPlacedTime: Long,
    status: String
) : ViewModel() {

    val databaseRef = Firebase.database.reference

    private val _itemAddress = MutableLiveData<String?>()
    val itemAddress: LiveData<String?>
        get() = _itemAddress

    private val _deliveryTime = MutableLiveData<String?>()
    val deliveryTime: LiveData<String?>
        get() = _deliveryTime

    private val _billAmountTotal = MutableLiveData<String?>()
    val billAmountTotal: LiveData<String?>
        get() = _billAmountTotal

    private val _billAmountTotalOriginal = MutableLiveData<String?>()
    val billAmountTotalOriginal : LiveData<String?>
        get() = _billAmountTotalOriginal

    private val _deliveryCharge = MutableLiveData<String?>()
    val deliveryCharge: LiveData<String?>
        get() = _deliveryCharge

    private val _billAmountWithDeliveryCharge = MutableLiveData<String?>()
    val billAmountWithDeliveryCharge : LiveData<String?>
        get() = _billAmountWithDeliveryCharge

    private val _totalSavings = MutableLiveData<String?>()
    val totalSavings : LiveData<String?>
        get() = _totalSavings

    private val _paymentMethodd = MutableLiveData<String?>()
    val paymentMethodd: LiveData<String?>
        get() = _paymentMethodd

    private val _cartItems = MutableLiveData<List<CartItems>?>()
    val cartItems : LiveData<List<CartItems>?>
        get() = _cartItems

    private val _authPhone = MutableLiveData<String?>()
    val authPhone : LiveData<String?>
        get() = _authPhone

    private val _orderPlacedTime = MutableLiveData<String?>()
    val orderPlacedTime : LiveData<String?>
        get() = _orderPlacedTime

    private val _status = MutableLiveData<String?>()
    val status : LiveData<String?>
        get() = _status

    private val _deliveryInstructions = MutableLiveData<String?>()
    val deliveryInstructions : LiveData<String?>
        get() = _deliveryInstructions

    init {
        val addresss = address.getAddress()
        _itemAddress.value = addresss

        val deliveryDurations = Array(listItems.size){""}
        for(i in deliveryDurations.indices){
            deliveryDurations[i] = listItems[i].deliveryDuration
        }

        val durationInt = Array(deliveryDurations.size){0}
        for(i in durationInt.indices){
            val duration = deliveryDurations[i]
            val timeUnit = duration.substring(duration.indexOf(" ")+1)
            val time = duration.substringBefore(" ").toInt()
            when (timeUnit) {
                "Minutes" -> {
                    durationInt[i] = time
                }
                "Hours" -> {
                    durationInt[i] = time * 60
                }
                else -> {
                    durationInt[i] = time * 60 * 24
                }
            }
        }

        var max = 0
        for(duration in durationInt){
            if(duration > max) max = duration
        }

        var deliveryTime = "$max Minutes"

        if(max in 101..1439){
            max /= 60
            deliveryTime = "$max Hours"
        }
        else if(max > 1439){
            max /= 1440
            deliveryTime = "$max Days"
        }

        _deliveryTime.value = deliveryTime
        _deliveryInstructions.value = address.deliveryInstructions

        var billAmountTotal = 0
        var billTotalOriginal = 0
        for (item in listItems){
            billAmountTotal += item.price.toInt()
            billTotalOriginal += item.priceOriginal.toInt()
        }

        _billAmountTotal.value = billAmountTotal.toString()
        _billAmountTotalOriginal.value = billTotalOriginal.toString()

        _totalSavings.value = (billTotalOriginal - billAmountTotal).toString()


        if(billAmountTotal > 499){
            _deliveryCharge.value = "0"
        }
        else {
            _deliveryCharge.value = "40"
        }

        _billAmountWithDeliveryCharge.value = (_billAmountTotal.value!!.toInt() + _deliveryCharge.value!!.toInt()).toString()
        _paymentMethodd.value = paymentMethod

        _cartItems.value = listItems

        _authPhone.value = authPhone


        val date = Date(orderPlacedTime)
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        _orderPlacedTime.value = simpleDateFormat.format(date)

        _status.value = status

    }

    private val _orderOnWay = MutableLiveData<Boolean?>()
    val orderOnWay : LiveData<Boolean?>
        get() = _orderOnWay

    fun makeOrderOnWay(order: OrderItem) {
        order.status = "ORDER ON WAY"
        databaseRef.child("orders").child(order.authPhone).child(order.orderKey).child("status").setValue(order.status).addOnCompleteListener {
            _orderOnWay.value = it.isSuccessful
        }
    }

    fun doneMakingOrderOnWay() {
        _orderOnWay.value = null
    }

    private val _orderDelivered = MutableLiveData<Boolean?>()
    val orderDelivered : LiveData<Boolean?>
        get() = _orderDelivered

    fun makeOrderDelivered(order: OrderItem) {
        order.status = "ORDER DELIVERED"
        order.orderDeliveredOrCancelledTime = System.currentTimeMillis()

        val childUpdates = hashMapOf<String, Any?>(
            "/orders/${order.authPhone}/${order.orderKey}" to null,
            "/deliveredOrCancelled/${order.authPhone}/${order.orderKey}" to order
        )

        databaseRef.updateChildren(childUpdates).addOnCompleteListener {
            _orderDelivered.value = it.isSuccessful
        }
    }

    fun doneMakingOrderDelivered() {
        _orderDelivered.value = null
    }

    private val _orderCancelled = MutableLiveData<Boolean?>()
    val orderCancelled : LiveData<Boolean?>
        get() = _orderCancelled

    fun makeOrderCancelled(){
        _orderCancelled.value = true
    }

    fun doneMakingOrderCancelled() {
        _orderCancelled.value = null
    }
}