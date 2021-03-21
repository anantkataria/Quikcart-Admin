package com.savage9ishere.tiwarimartadmin.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

class CategoriesViewModel : ViewModel() {

    private val databaseRef = Firebase.database.reference
    private val storageRef = Firebase.storage.reference

    private val _categories = MutableLiveData<ArrayList<Category?>>()
    val categories: LiveData<ArrayList<Category?>>
        get() = _categories

    private val categoriesList: ArrayList<Category?> = arrayListOf()


    init {
        //as soon as the viewmodel is created, we need to fetch whatever data there
        //is in the realtime database and show that data to recyclerView
        databaseRef.child("categories").addChildEventListener(
            object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    categoriesList.add(snapshot.getValue(Category::class.java))
                    _categories.value = categoriesList

                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val updatedCategory = snapshot.getValue(Category::class.java)
                    for(index in categoriesList.indices){
                        if(categoriesList[index]!!.key == updatedCategory!!.key){
                            categoriesList.removeAt(index)
                            categoriesList.add(index, updatedCategory)
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val category = snapshot.getValue(Category::class.java)
                    categoriesList.remove(category)
                    _categories.value = categoriesList
                    Log.e("acacac", "onChildRemove called")
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

    fun removeSelectedItems(selectedItemsList: ArrayList<Category>) {
        val map = hashMapOf<String, Any?>()
        for (category in selectedItemsList) {
            map["categories/${category.key}"] = null
            map["categoryWiseItems/${category.name}"] = null
        }

        databaseRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                //TODO show toast
            } else {
                //TODO show toast
            }
        }
        removeImagesFromStorage(selectedItemsList)
    }

    private fun removeImagesFromStorage(list: ArrayList<Category>) {
        // TODO remove images for this category as well as all item images of this category
        for (category in list) {
            storageRef.child("images").child(category.name).delete()
                .addOnSuccessListener {
                    //todo success message
                }.addOnFailureListener {
                    //todo pass failed message
                }
            storageRef.child("category_images").child(category.name).listAll()
                .addOnSuccessListener { (items, _) ->
                    if (items.isNotEmpty()) {
                        items.forEach { item ->
                            storageRef.child("category_images").child(category.name)
                                .child(item.name).delete()
                        }
                    }
                }.addOnFailureListener {
                    //todo pass failed message
                }
        }
    }

}