package com.manuelblanco.opendemo.ui.base

import android.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.manuelblanco.opendemo.R

abstract class BaseFragment : Fragment() {
    companion object {
        val ARGS_CHARACTER_ID = "characterId"
    }

    abstract fun fetchData()

    abstract fun loadingState()

    abstract fun setUpToolbar(toolbar: Toolbar)

    fun showErrorDialog(message: String) {
        val alertDialog: AlertDialog = AlertDialog.Builder(context).create().apply {
            setTitle(getString(R.string.title_error))
            setMessage(message)
            setButton(
                AlertDialog.BUTTON_NEUTRAL, "OK"
            ) { dialog, _ -> dialog.dismiss() }
            show()
        }
    }

}