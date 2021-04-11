package com.savage9ishere.tiwarimartadmin.orders

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.parcelize.Parcelize
import kotlin.math.log

class OrdersViewModel : ViewModel() {

    private val databaseRef  = Firebase.database.reference

    private val _orders = MutableLiveData<ArrayList<OrderItem>>()
    val orders : LiveData<ArrayList<OrderItem>>
        get() = _orders

    private val ordersList = arrayListOf<OrderItem>()

    init {

        databaseRef.child("orders").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ordersList.clear()

                for(data in snapshot.children){
                    for (d in data.children){
                        ordersList.add(d.getValue(OrderItem::class.java)!!)
                    }
                }

                _orders.value = ordersList
            }

            override fun onCancelled(error: DatabaseError) {
                //later
            }

        })

    }

}

@Parcelize
data class OrderItem(val listItems: ArrayList<CartItems> = arrayListOf(), val address: AddressItem = AddressItem(), val paymentMethod : String = "", val orderKey : String = "", val orderPlacedTime : Long = 0L, var orderDeliveredOrCancelledTime : Long = 0L, var status : String = "", val authPhone : String = "") : Parcelable

@Parcelize
data class CartItems(val name: String = "", val size: String = "", val price: String = "", val priceOriginal: String = "", val qty : Int = 0, val photoUrl: String = "", val key: String? = "", val itemCategory: String = "", val deliveryDuration: String = "") :
    Parcelable

@Parcelize
data class AddressItem(val fullName: String = "", val mobileNumber: String = "", val pinCode: String = "", val flatHouseNoName: String = "", val areaColonyStreet: String = "", val landMark : String = "", val townCity : String = "", val state : String = "", val deliveryInstructions: String = "") : Parcelable{

    fun getAddress() : String {
        return "$fullName\n$mobileNumber\n$flatHouseNoName,\n$areaColonyStreet, $landMark\n$townCity, $pinCode\n$state"
    }
}