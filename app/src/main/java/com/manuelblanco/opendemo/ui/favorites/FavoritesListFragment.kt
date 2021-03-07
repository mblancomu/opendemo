package com.manuelblanco.opendemo.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.manuelblanco.core.LoadingState
import com.manuelblanco.core.model.Favorite
import com.manuelblanco.opendemo.R
import com.manuelblanco.opendemo.dialogs.InfoDialog
import com.manuelblanco.opendemo.ui.base.BaseListFragment
import com.manuelblanco.opendemo.ui.detail.DetailNavigation
import com.manuelblanco.opendemo.ui.favorites.adapter.FavoritesAdapter
import com.manuelblanco.opendemo.viewmodel.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesListFragment : BaseListFragment(), FavoriteItemListeners {

    lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var favAdapter: FavoritesAdapter
    val favoritesViewModel by viewModel<FavoritesViewModel>()
    var listOfFavorites: MutableList<Favorite> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bottomBar =
            requireActivity().findViewById<View>(R.id.bottom_nav) as BottomNavigationView
        bottomBar.visibility = View.VISIBLE
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(context)

        loadingState()
        fetchData()
        setUpAdapter()
        onRefresh()
    }

    override fun onResume() {
        super.onResume()
        favoritesViewModel.getFavorites()
    }

    override fun fetchData() {
        favoritesViewModel.favoritesData.observe(viewLifecycleOwner, Observer { favorites ->
            if (!favorites.isNullOrEmpty()) {
                listOfFavorites = favorites as MutableList<Favorite>
                favoritesViewModel.updateLoadingState(LoadingState.LOADED)
            } else {
                listOfFavorites = mutableListOf()
                favoritesViewModel.updateLoadingState(LoadingState.EMPTY_OR_NULL)
            }

            favAdapter.addFavorites(listOfFavorites)
        })

        dismissSwipeRefresh()
    }

    override fun setUpAdapter() {
        binding.emptyList.text = getString(R.string.empty_favorites)
        favAdapter = FavoritesAdapter()
        favAdapter.listener = this

        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = favAdapter
        }
    }

    override fun onRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            fetchData()
        }
    }

    override fun loadingState() {
        favoritesViewModel.loadingState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                LoadingState.SUCCESS -> {
                    hideProgress()
                }
                LoadingState.EMPTY_OR_NULL -> {
                    emptyView()
                }
                LoadingState.LOADING -> {
                    showProgress(false)
                }
            }
        })
    }

    private fun showDialogRemoveItem(favorite: Favorite) {
        val dialogRemove = context?.let {
            InfoDialog(it,
                context?.getString(R.string.dialog_message_remove_fav, favorite.name)!!,
                context?.getString(R.string.dialog_title_remove_fav)!!,
                object : InfoDialog.InfoDialogListener {
                    override fun onOKSelected() {
                        favoritesViewModel.removeFavorite(favorite.id)
                        favAdapter.removeItem(favorite)
                    }
                })
        }
        dialogRemove?.setCanceledOnTouchOutside(false)
        if (dialogRemove != null) {
            if (!dialogRemove.isShowing) {
                dialogRemove.show()
            }
        }
    }

    override fun onFavoriteClickListener(favorite: Favorite) {
        DetailNavigation.openDetail(favorite.id.toString(), findNavController())
    }

    override fun onFavoriteRemoveListener(favorite: Favorite) {
        showDialogRemoveItem(favorite)
    }
}