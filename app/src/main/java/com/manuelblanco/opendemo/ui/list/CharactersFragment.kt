package com.manuelblanco.opendemo.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.manuelblanco.core.LoadingState
import com.manuelblanco.core.model.Character
import com.manuelblanco.opendemo.R
import com.manuelblanco.opendemo.databinding.FragmentGridBinding
import com.manuelblanco.opendemo.ui.base.BaseFragment
import com.manuelblanco.opendemo.ui.detail.DetailNavigation
import com.manuelblanco.opendemo.ui.list.adapter.CharacterItemListeners
import com.manuelblanco.opendemo.ui.list.adapter.CharactersAdapter
import com.manuelblanco.opendemo.viewmodel.CharactersViewModel
import com.manuelblanco.opendemo.views.CustomGridRecyclerView
import com.manuelblanco.opendemo.common.viewLifecycle
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersFragment : BaseFragment(), CharacterItemListeners {

    lateinit var gridLayoutManager: GridLayoutManager
    val charactersViewModel by viewModel<CharactersViewModel>()
    var binding: FragmentGridBinding by viewLifecycle()
    lateinit var charactersAdapter: CharactersAdapter
    private var listOfCharacters: MutableList<Character> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGridBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridLayoutManager = GridLayoutManager(context, 2)

        initSwipeRefresh()
        setUpAdapter()
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun setUpAdapter() {
        binding.emptyList.text =
            getString(R.string.empty_list)
        charactersAdapter = CharactersAdapter()
        charactersAdapter.listener = this

        binding.gridView.apply {
            layoutManager = gridLayoutManager
            adapter = charactersAdapter
        }

       // fetchData()
    }

    private fun initSwipeRefresh() {
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

    override fun fetchData() {
        charactersViewModel.fetchCharactersData(10)
        charactersViewModel.characterList.observe(viewLifecycleOwner, Observer { characters ->
            if (!characters.isNullOrEmpty()) {
                listOfCharacters = characters as MutableList<Character>
                charactersViewModel.updateLoadingState(LoadingState.LOADED)
            } else {
                listOfCharacters = mutableListOf()
                charactersViewModel.updateLoadingState(LoadingState.EMPTY_OR_NULL)
            }

            charactersAdapter.addCharacters(listOfCharacters)
            //animatedGrid()
        })

        dismissSwipeRefresh()
    }

    override fun onCharacterClickListener(character: Character) {
        DetailNavigation.openDetail(character.id, findNavController())
    }

    override fun loadingState() {
        charactersViewModel.loadingState.observe(viewLifecycleOwner, Observer { state ->
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

    private fun animatedGrid() {
        val controller: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, R.anim.animation_grid_bottom)

        binding.gridView.apply {
            layoutAnimation = controller
            scheduleLayoutAnimation()
        }

        charactersAdapter.notifyDataSetChanged();
    }

}