package com.manuelblanco.opendemo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import coil.load
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.manuelblanco.core.LoadingState
import com.manuelblanco.core.model.Character
import com.manuelblanco.core.model.Favorite
import com.manuelblanco.opendemo.R
import com.manuelblanco.opendemo.common.AnimationUtils
import com.manuelblanco.opendemo.databinding.FragmentDetailBinding
import com.manuelblanco.opendemo.ui.base.BaseFragment
import com.manuelblanco.opendemo.ui.base.BaseFragment.Companion.ARGS_CHARACTER_ID
import com.manuelblanco.opendemo.viewmodel.DetailViewModel
import com.manuelblanco.opendemo.viewmodel.FavoritesViewModel
import com.mcogeo.parkingandbici.utils.viewLifecycle
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

fun newDetailBikeFragment(characterId: String): DetailFragment {
    val bundle = Bundle()
    bundle.putString(ARGS_CHARACTER_ID, characterId)
    val detailFragment = DetailFragment()
    detailFragment.arguments = bundle
    return detailFragment
}

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
        val view = binding.root
        characterId = arguments?.getString(ARGS_CHARACTER_ID)!!
        showProgress(true, binding.appbar, binding.mainList, binding.fab)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        loadingState()
        fetchData()
        initAppBarLayout()
    }

    fun initAppBarLayout() {
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

    private fun saveFavorite(isFavorite: Boolean) {
        if (!isFavorite) {
            val favorite = characterDetail?.id?.let { id ->
                Favorite(
                    id.toInt(),
                    characterDetail?.name,
                    characterDetail?.description,
                    characterDetail?.resourceURI,
                    characterDetail?.thumbnail?.path,
                )
            }
            favorite?.let { fav -> favoritesViewModel.addFavorite(fav) }
        }
    }

    override fun fetchData() {
        detailViewModel.fetchCharacter(characterId.toInt())
        detailViewModel.character.observe(viewLifecycleOwner, Observer { detail ->
            if (detail != null) {
                characterDetail = detail
                binding.collapsingToolbar.title = characterDetail?.name
                binding.detailImg.apply {
                    load(characterDetail?.thumbnail?.path) {
                        placeholder(R.drawable.marvel_logo)
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

    override fun loadingState() {
        detailViewModel.loadingState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                LoadingState.LOADED -> {
                }
                LoadingState.EMPTY_OR_NULL -> {
                }
                LoadingState.LOADING -> {
                }
                LoadingState.NETWORK -> {
                    showErrorDialog(getString(R.string.no_network_message))
                }
            }
        })
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
}