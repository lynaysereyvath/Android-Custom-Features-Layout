package com.lynaysereyvath.customfeatures

import android.widget.FrameLayout

sealed class RecyclerViewItemModel(val id: Int, val name: String) {
    companion object {
        object PinIndicator : RecyclerViewItemModel(0, "" )
    }
}