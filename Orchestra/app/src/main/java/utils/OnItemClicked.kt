package utils

import android.view.View

interface OnItemClicked {
    fun colorClicked(v : View, color: Int, position: Int)
}