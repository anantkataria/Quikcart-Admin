package com.savage9ishere.tiwarimartadmin.categories

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(val name: String = "", val uri: String = "", val key : String = "") : Parcelable{
}