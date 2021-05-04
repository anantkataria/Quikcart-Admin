package com.savage9ishere.tiwarimartadmin.close_store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CloseStoreViewModel : ViewModel() {

    private val databaseRef = Firebase.database.reference

    private val _isOpen = MutableLiveData<Boolean?>()
    val isOpen : LiveData<Boolean?>
        get() = _isOpen

    private val _openingStatus = MutableLiveData<String?>()
    val openingAgainStatus : LiveData<String?>
        get() = _openingStatus

    private val _openingSuccessful = MutableLiveData<Boolean?>()
    val openingSuccessful : LiveData<Boolean?>
        get() = _openingSuccessful

    init {
        databaseRef.child("openOrClose").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _isOpen.value = snapshot.child("isOpen").getValue(Boolean::class.java)
                _openingStatus.value = snapshot.child("openingAgainTime").getValue(String::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun openStore() {
        databaseRef.child("openOrClose").child("isOpen").setValue(true).addOnCompleteListener {
            _openingSuccessful.value = it.isSuccessful
        }
    }

    fun doneOpeningSuccessful() {
        _openingSuccessful.value = null
    }

    fun doneOpeningAgainStatus() {
        _openingStatus.value = null
    }

    fun doneIsOpen() {
        _isOpen.value = null
    }
}