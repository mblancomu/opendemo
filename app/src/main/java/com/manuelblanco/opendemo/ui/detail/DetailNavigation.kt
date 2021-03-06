package com.manuelblanco.opendemo.ui.detail

import android.os.Bundle
import androidx.navigation.NavController
import com.manuelblanco.opendemo.R
import com.manuelblanco.opendemo.ui.base.BaseFragment

object DetailNavigation {

    fun openDetail(characterId: String, navController: NavController){
        val bundle = Bundle().apply {
            putString(BaseFragment.ARGS_CHARACTER_ID, characterId)
        }
        navController.navigate(R.id.action_list_to_detail, bundle)
    }
}