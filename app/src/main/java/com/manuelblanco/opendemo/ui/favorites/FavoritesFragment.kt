package com.manuelblanco.opendemo.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.manuelblanco.core.LoadingState
import com.manuelblanco.core.model.Favorite
import com.manuelblanco.opendemo.R
import com.manuelblanco.opendemo.databinding.FragmentFavoritesBinding
import com.manuelblanco.opendemo.ui.base.BaseFragment
import com.manuelblanco.opendemo.ui.detail.DetailNavigation
import com.manuelblanco.opendemo.ui.favorites.adapter.FavoritesAdapter
import com.manuelblanco.opendemo.viewmodel.FavoritesViewModel
import com.mcogeo.parkingandbici.dialogs.InfoDialog
import com.mcogeo.parkingandbici.utils.viewLifecycle
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : BaseFragment(), FavoriteItemListeners {

    lateinit var linearLayoutManager: LinearLayoutManager
    private var binding: FragmentFavoritesBinding by viewLifecycle()
    private lateinit var favAdapter: FavoritesAdapter
    val favoritesViewModel by viewModel<FavoritesViewModel>()
    var listOfFavorites: MutableList<Favorite> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        linearLayoutManager = LinearLayoutManager(context)

        loadingState()
        fetchData()
        initSwipeRefresh()
        setUpAdapter()
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

    fun setUpAdapter() {
        favAdapter = FavoritesAdapter()
        favAdapter.listener = this

        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = favAdapter
        }
    }

    private fun initSwipeRefresh() {
        binding.emptyList.text = getString(R.string.empty_favorites)
        binding.swipeRefresh.setOnRefreshListener {
            fetchData()
        }
        binding.swipeRefresh.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    fun dismissSwipeRefresh() {
        binding.swipeRefresh.isRefreshing = false
    }

    override fun loadingState() {
        favoritesViewModel.loadingState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                LoadingState.LOADED -> {
                    dismissSwipeRefresh()
                    binding.emptyList.visibility = View.GONE
                    binding.swipeRefresh.visibility = View.VISIBLE
                }
                LoadingState.EMPTY_OR_NULL -> {
                    dismissSwipeRefresh()
                    binding.emptyList.visibility = View.VISIBLE
                    binding.swipeRefresh.visibility = View.GONE
                }
                LoadingState.LOADING -> {
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