package com.savage9ishere.tiwarimartadmin.categories.particular_category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.savage9ishere.tiwarimartadmin.categories.particular_category.new_item_details.NewItemDetailsViewModel

class ParticularCategoryViewModel(categoryName: String) : ViewModel() {

    private val databaseRef = Firebase.database.reference
    private val storageRef = Firebase.storage.reference

    private val _items = MutableLiveData<ArrayList<NewItemDetailsViewModel.Item?>>()
    val items: LiveData<ArrayList<NewItemDetailsViewModel.Item?>>
        get() = _items

    private val itemsList : ArrayList<NewItemDetailsViewModel.Item?> = arrayListOf()

    init {
        databaseRef.child("categoryWiseItems").child(categoryName).addChildEventListener(
            object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    itemsList.add(snapshot.getValue(NewItemDetailsViewModel.Item::class.java))
                    _items.value = itemsList
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val item = snapshot.getValue(NewItemDetailsViewModel.Item::class.java)
                    itemsList.remove(item)
                    _items.value = itemsList
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }

    fun removeSelectedItems(selectedItemsList: ArrayList<NewItemDetailsViewModel.Item>, categoryName: String) {
        val map = hashMapOf<String, Any?>()
        for(item in selectedItemsList){
            map["categoryWiseItems/$categoryName/${item.key}"] = null
        }

        databaseRef.updateChildren(map).addOnCompleteListener {
            if(it.isSuccessful){
                // todo show success
            }
            else {
                // todo show failure
            }
        }

        removeImagesFromStorage(selectedItemsList, categoryName)
    }

    private fun removeImagesFromStorage(selectedItemsList: ArrayList<NewItemDetailsViewModel.Item>, categoryName: String) {
        for(item in selectedItemsList){
            for(imageUrl in item.photosUrl){
                val photoRef = Firebase.storage.getReferenceFromUrl(imageUrl)
                photoRef.delete()
            }
        }
    }


}