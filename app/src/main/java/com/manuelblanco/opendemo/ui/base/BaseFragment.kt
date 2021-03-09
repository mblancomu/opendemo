package com.manuelblanco.opendemo.ui.base

import android.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.manuelblanco.opendemo.R
import com.manuelblanco.opendemo.dialogs.ErrorDialog

abstract class BaseFragment : Fragment() {
    companion object {
        val ARGS_CHARACTER_ID = "characterId"
    }

    abstract fun fetchData()

    abstract fun loadingState()

    abstract fun setUpToolbar(toolbar: Toolbar)

    fun showErrorDialog(message: String) {
        val dialogError = context?.let {
            ErrorDialog(it, message, object : ErrorDialog.ErrorDialogListener {
                override fun onTrySelected() {
                }
            })
        }
        dialogError?.setCanceledOnTouchOutside(false)
        if (dialogError != null && !dialogError.isShowing) {
            dialogError.show()
        }
    }
}