package com.savage9ishere.tiwarimartadmin.categories.particular_category.new_item_details

import android.os.Parcelable
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.parcelize.Parcelize

private var inStockk : Boolean? = null

class NewItemDetailsViewModel : ViewModel() {

    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private val categoryImagesRef = storageRef.child("category_images")
    private lateinit var itemInfo : Array<String>

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

    private val _itemImages = MutableLiveData<ArrayList<String>>()
    val itemImages : LiveData<ArrayList<String>>
        get() = _itemImages

    private val itemPhotosArrayList = arrayListOf<String>()

    private val _otherSizes = MutableLiveData<MutableMap<String, String>>()
    val otherSizes : LiveData<MutableMap<String, String>>
        get() = _otherSizes

    private val otherSizesMap : MutableMap<String, String> = mutableMapOf()

    fun getItemPhotosArrayList() : ArrayList<String> {
        return itemPhotosArrayList
    }

    fun addPhotoToArrayList(uri : String){
        itemPhotosArrayList.add(uri)
        _itemImages.value = itemPhotosArrayList
    }



    fun sendToDatabase(array: Array<String>, categoryName: String?, inStock: Boolean) {
        inStockk = inStock
        //first we will send photos to firebase storage
        //if that is successful then we will send Item to realtime database

        itemInfo = array
        uploadImages(ArrayList<String>(), itemPhotosArrayList, categoryName)

    }

    private fun uploadImages(arrayList: ArrayList<String>, itemPhotosArrayList: ArrayList<String>, categoryName: String?, ) {

        val uri = itemPhotosArrayList[arrayList.size].toUri()
        val uriPaths = uri.pathSegments
        val lastSegment = uriPaths[uriPaths.size-1]
        val trulyLastSegment = lastSegment.substringAfterLast("/")
        //Log.e("91", "trulyLastSegment = $trulyLastSegment")
        val currentImageRef = categoryImagesRef.child(categoryName!!).child(trulyLastSegment)

        currentImageRef.putFile(uri).continueWithTask {
            task -> if(!task.isSuccessful){
                task.exception?.let {
                    throw it
                }
            }
            currentImageRef.downloadUrl
        }.addOnCompleteListener {
            if(it.isSuccessful){
                val downloadUrl = it.result.toString()
                arrayList.add(downloadUrl)

                if(arrayList.size == itemPhotosArrayList.size){
                    uploadAllImagesToRealtimeDatabase(arrayList, categoryName)
                    //upload to storage completed
                    _uploadToStorageCompleted.value = true
                }
                else{
                    uploadImages(arrayList, itemPhotosArrayList, categoryName)
                }
            }
            else {
                //show toast message showing error for uploading particular image file
                _uploadToStorageFailed.value = true
            }
        }


    }

    private fun uploadAllImagesToRealtimeDatabase(arrayList: ArrayList<String>, categoryName: String?) {
        val itemReference = database.child("categoryWiseItems").child(categoryName!!)
        val key = itemReference.push().key ?: return

        val item = Item(itemInfo[0], itemInfo[1], itemInfo[2], itemInfo[3], itemInfo[4], itemInfo[5], arrayList, key, 0, 0, inStockk!!, otherSizesMap)

        itemReference.child(key).setValue(item).addOnCompleteListener {
            if (it.isSuccessful){
                //show item uploaded successfully and pop back stack
                _uploadToDatabaseCompleted.value = true
            }
            else {
                //toast saying try again
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

    fun removeItem(position: Int) {
        itemPhotosArrayList.removeAt(position)
        _itemImages.value = itemPhotosArrayList
    }

    fun addToOtherSizes(size: String, price: String) {
        Log.e("656565", "item size = $size, item price = $price")
        otherSizesMap[size] = price
        _otherSizes.value = otherSizesMap
    }

    fun removeOtherSize(size: String) {
        otherSizesMap.remove(size)
        _otherSizes.value = otherSizesMap
    }

    @Parcelize
    data class Item(val name: String = "",val size: String = "", val price: String = "", val discount: String = "", val description: String = "", val deliveryDuration: String = "", val photosUrl: ArrayList<String> = ArrayList(), val key: String? = "", val ratingTotal: Long = 0L, val peopleRatingCount: Long = 0L, val inStock: Boolean = true, val otherSizes: MutableMap<String, String> = mutableMapOf()) : Parcelable
}