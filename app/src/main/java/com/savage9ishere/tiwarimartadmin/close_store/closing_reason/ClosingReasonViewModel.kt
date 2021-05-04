package com.savage9ishere.tiwarimartadmin.close_store.closing_reason

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ClosingReasonViewModel : ViewModel() {

    val databaseRef = Firebase.database.reference

    private val _updationComplete = MutableLiveData<Boolean?>()
    val updationComplete : LiveData<Boolean?>
        get() = _updationComplete

    fun uploadReason(statusEditTextStr: String) {
        val map : Map<String, Any> = mapOf(
            "isOpen" to false,
            "openingAgainTime" to statusEditTextStr
        )
        databaseRef.child("openOrClose").updateChildren(map).addOnCompleteListener {
            _updationComplete.value = it.isSuccessful
        }
    }

    fun doneUpdationComplete() {
        _updationComplete.value = null
    }
}