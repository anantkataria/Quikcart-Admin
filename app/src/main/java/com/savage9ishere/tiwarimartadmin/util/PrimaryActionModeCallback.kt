package com.savage9ishere.tiwarimartadmin.util

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes

class PrimaryActionModeCallback : ActionMode.Callback {

    var onActionItemClickListener: OnActionItemClickListener? = null
    var onDestroyActionModeClickListener: OnDestroyActionModeClickListener? = null

    private var mode: ActionMode? = null
    @MenuRes
    private var menuResId: Int = 0
    private var title: String? = null
    private var subtitle: String? = null

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        this.mode = mode
        mode.menuInflater.inflate(menuResId, menu)
        mode.title = title
        mode.subtitle = subtitle
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        this.mode = null
        //call notify dataset changed in the adapter through a callback
        onDestroyActionModeClickListener?.onDestroyActionModeClicked()
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        onActionItemClickListener?.onActionItemClick(item)
        mode.finish()
        return true
    }

    fun startActionMode(view: View,
                        @MenuRes menuResId: Int,
                        title: String? = null,
                        subtitle: String? = null) {
        this.menuResId = menuResId
        this.title = title
        this.subtitle = subtitle
        view.startActionMode(this)
    }

    fun finishActionMode() {
        mode?.finish()
    }
}

interface OnActionItemClickListener {
    fun onActionItemClick(item: MenuItem)
}
interface OnDestroyActionModeClickListener {
    fun onDestroyActionModeClicked()
}