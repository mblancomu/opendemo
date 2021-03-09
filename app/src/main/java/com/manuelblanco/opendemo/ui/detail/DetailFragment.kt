package com.manuelblanco.opendemo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import coil.load
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.manuelblanco.core.LoadingState
import com.manuelblanco.core.model.Character
import com.manuelblanco.core.model.Favorite
import com.manuelblanco.opendemo.R
import com.manuelblanco.opendemo.common.AnimationUtils
import com.manuelblanco.opendemo.common.ThumbnailSize
import com.manuelblanco.opendemo.common.viewLifecycle
import com.manuelblanco.opendemo.databinding.FragmentDetailBinding
import com.manuelblanco.opendemo.ui.base.BaseFragment
import com.manuelblanco.opendemo.viewmodel.DetailViewModel
import com.manuelblanco.opendemo.viewmodel.FavoritesViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : BaseFragment() {
    var characterId = ""
    val detailViewModel by viewModel<DetailViewModel>()
    val favoritesViewModel by viewModel<FavoritesViewModel>()
    var characterDetail: Character? = null
    private var binding: FragmentDetailBinding by viewLifecycle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val bottomBar =
            requireActivity().findViewById<View>(R.id.bottom_nav) as BottomNavigationView
        bottomBar.visibility = View.GONE
        val view = binding.root
        characterId = arguments?.getString(ARGS_CHARACTER_ID)!!
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar(binding.toolbar)
        loadingState()
        fetchData()
        initAppBarLayout()
    }

    private fun initAppBarLayout() {
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            var isShow = false
            var scrollRange = -1

            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange;
            }
            if (scrollRange + verticalOffset == 0) {
                isShow = true;
            } else if (isShow) {
                isShow = false;
            }
        })

        binding.fab.setOnClickListener {
            val isFavorite =
                characterDetail?.id?.let { id -> favoritesViewModel.isFavorite(id.toInt()) }
            if (isFavorite == true) {
                context?.let { context -> AnimationUtils.animateFab(false, fab, context) }
                characterDetail?.id?.let { it1 -> favoritesViewModel.removeFavorite(it1.toInt()) }
            } else {
                context?.let { context -> AnimationUtils.animateFab(true, fab, context) }
                saveFavorite(false)
            }
        }
    }

    /**
     *  Save the detail character in Favorites using Room
     */
    private fun saveFavorite(isFavorite: Boolean) {
        if (!isFavorite) {
            val imageUrl =
                characterDetail?.thumbnail?.path.plus(ThumbnailSize.MEDIUM.size).plus(".")
                    .plus(characterDetail?.thumbnail?.extension)
            val favorite = characterDetail?.id?.let { id ->
                Favorite(
                    id.toInt(),
                    characterDetail?.name,
                    characterDetail?.description,
                    characterDetail?.resourceURI,
                    imageUrl,
                )
            }
            favorite?.let { fav -> favoritesViewModel.addFavorite(fav) }
        }
    }

    /**
     *  Fetch data for the Character id, using LiveData and Coroutines.
     */
    override fun fetchData() {
        detailViewModel.fetchCharacter(characterId.toInt())
        detailViewModel.character.observe(viewLifecycleOwner, { detail ->
            if (detail != null) {
                characterDetail = detail
                binding.collapsingToolbar.title = characterDetail?.name
                binding.textDescription.text = if (characterDetail?.description.isNullOrEmpty()) getString(R.string.no_description)
                else characterDetail?.description
                val imageUrl = characterDetail?.thumbnail?.path.plus(".")
                    .plus(characterDetail?.thumbnail?.extension)
                binding.detailImg.apply {
                    load(imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.placeholder_marvel)
                    }
                }
                binding.appbar.setExpanded(true)

                val isFavorite =
                    characterDetail?.id?.let { id -> favoritesViewModel.isFavorite(id.toInt()) }
                isFavorite?.let { favorite ->
                    context?.let { context ->
                        AnimationUtils.animateFab(
                            favorite,
                            fab,
                            context
                        )
                    }
                }
            }
        })
    }

    /**
     * Status receiver from the coroutine.
     */
    override fun loadingState() {
        detailViewModel.loadingState.observe(viewLifecycleOwner, { state ->
            when (state) {
                LoadingState.SUCCESS -> {
                    hideProgress(false, binding.appbar, binding.mainList, binding.fab)
                }
                LoadingState.EMPTY_OR_NULL -> {
                    hideProgress(false, binding.appbar, binding.mainList, binding.fab)
                }
                LoadingState.LOADING -> {
                    showProgress(true, binding.appbar, binding.mainList, binding.fab)
                }
                LoadingState.NETWORK -> {
                    showErrorDialog(getString(R.string.no_network_message))
                }
                LoadingState.error("") -> {
                    hideProgress(false, binding.appbar, binding.mainList, binding.fab)
                    showErrorDialog(getString(R.string.msg_error))
                }
            }
        })
    }

    override fun setUpToolbar(toolbar: Toolbar) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun showProgress(
        isVisible: Boolean, appBar: AppBarLayout, mainList: NestedScrollView,
        fab: FloatingActionButton
    ) {
        val view = if (isVisible) View.INVISIBLE else View.VISIBLE
        appBar.visibility = view
        mainList.visibility = view
        fab.visibility = view
    }

    private fun hideProgress(
        isVisible: Boolean, appBar: AppBarLayout, mainList: NestedScrollView,
        fab: FloatingActionButton
    ) {
        val view = if (isVisible) View.INVISIBLE else View.VISIBLE
        appBar.visibility = view
        mainList.visibility = view
        fab.visibility = view

    }
}