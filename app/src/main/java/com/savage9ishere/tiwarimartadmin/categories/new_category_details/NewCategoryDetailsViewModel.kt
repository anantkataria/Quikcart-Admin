package com.savage9ishere.tiwarimartadmin.categories.new_category_details

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.savage9ishere.tiwarimartadmin.categories.Category

class NewCategoryDetailsViewModel : ViewModel() {

    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private val imagesRef = storageRef.child("images")

    private val database = Firebase.database.reference

    private val _uploadToStorageFailed = MutableLiveData<Boolean?> ()
    val uploadToStorageFailed: LiveData<Boolean?>
        get() = _uploadToStorageFailed

    private val _uploadToStorageCompleted = MutableLiveData<Boolean?> ()
    val uploadToStorageCompleted: LiveData<Boolean?>
        get() = _uploadToStorageCompleted

    private val _uploadToDatabaseCompleted = MutableLiveData<Boolean?>()
    val uploadToDatabaseCompleted : LiveData<Boolean?>
        get() = _uploadToDatabaseCompleted

    private val _uploadToDatabaseFailed = MutableLiveData<Boolean?>()
    val uploadToDatabaseFailed : LiveData<Boolean?>
        get() = _uploadToDatabaseFailed

    fun addToDatabase(name: String, uri : Uri) {
        //add the image to firebase storage and name and image url to firebase realtime database
        //and then go back in the stack to the categories fragment
        val currentImageRef = imagesRef.child(name)
        val uploadTask = currentImageRef.putFile(uri)

        uploadTask.addOnProgressListener {
            val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
            Log.d("NCDViewModel", "Upload is $progress% done.")
        }

        uploadTask.continueWithTask {
            task -> if(!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            currentImageRef.downloadUrl
        }.addOnCompleteListener {
            if(it.isSuccessful){
                //upload is complete now we need to add the url and name to the firebase realtime database
                val downloadUrl = it.result
                _uploadToStorageCompleted.value = true
                uploadToFirebaseDatabase(name, downloadUrl)
            }
            else {
                _uploadToStorageFailed.value = true
            }
        }
    }

    private fun uploadToFirebaseDatabase(name: String, downloadUrl: Uri?) {
        //val map : Map<String, Any?> = mapOf("name" to name, "url" to downloadUrl.toString())

//        database.child("categories").child(name).updateChildren(newCategory).addOnCompleteListener {
//            if(it.isSuccessful){
//                _uploadToDatabaseCompleted.value = true
//            }
//            else {
//                _uploadToDatabaseFailed.value = true
//            }
//        }

        val key = database.child("categories").push().key ?: return

        val newCategory = Category(name, downloadUrl.toString(), key)

        database.child("categories").child(key).setValue(newCategory).addOnCompleteListener {
            if(it.isSuccessful){
                _uploadToDatabaseCompleted.value = true
            }
            else {
                _uploadToDatabaseFailed.value = true
            }
        }

    }

    fun uploadToDatabaseCompletedFinished() {
        _uploadToDatabaseCompleted.value = null
    }
    fun uploadToDatabaseFailedFinished() {
        _uploadToDatabaseFailed.value = null
    }
    fun uploadToStorageCompletedFinished() {
        _uploadToStorageCompleted.value = null
    }
    fun uploadToStorageFailedFinished() {
        _uploadToStorageFailed.value = null
    }

}