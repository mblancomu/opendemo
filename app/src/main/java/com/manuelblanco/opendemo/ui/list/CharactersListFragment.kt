package com.manuelblanco.opendemo.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.manuelblanco.core.LoadingState
import com.manuelblanco.core.model.Character
import com.manuelblanco.opendemo.R
import com.manuelblanco.opendemo.ui.base.BaseListFragment
import com.manuelblanco.opendemo.ui.detail.DetailNavigation
import com.manuelblanco.opendemo.ui.list.adapter.CharacterItemListeners
import com.manuelblanco.opendemo.ui.list.adapter.CharactersAdapter
import com.manuelblanco.opendemo.viewmodel.CharactersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CharactersListFragment : BaseListFragment(), CharacterItemListeners {

    lateinit var gridLayoutManager: GridLayoutManager
    val charactersViewModel by viewModel<CharactersViewModel>()
    lateinit var charactersAdapter: CharactersAdapter
    private var listOfCharacters: MutableList<Character> = mutableListOf()
    private var isLoading: Boolean = false
    private var isFromUpdate: Boolean = false

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
        gridLayoutManager = GridLayoutManager(context, 2)

        setUpAdapter()
        onRefresh()
        loadingState()
        paginationScroll()
        fetchData()
    }

    override fun onResume() {
        super.onResume()
        charactersViewModel.fetchCharactersData()
    }

    override fun setUpAdapter() {
        binding.emptyList.text =
            getString(R.string.empty_list)
        charactersAdapter = CharactersAdapter()
        charactersAdapter.listener = this

        binding.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = charactersAdapter
        }
    }

    override fun onRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            charactersViewModel.fetchCharactersData()
        }
    }

    override fun fetchData() {
        charactersViewModel.characterList.observe(viewLifecycleOwner, Observer { characters ->
            if (!characters.isNullOrEmpty()) {
                if (isFromUpdate) {
                    listOfCharacters.addAll(characters)
                    isFromUpdate = false
                    isLoading = false
                } else {
                    listOfCharacters = characters as MutableList<Character>
                }
                charactersViewModel.updateLoadingState(LoadingState.LOADED)
            } else {
                listOfCharacters = mutableListOf()
                charactersViewModel.updateLoadingState(LoadingState.EMPTY_OR_NULL)
            }

            charactersAdapter.addCharacters(listOfCharacters)
        })

        dismissSwipeRefresh()
    }

    override fun onCharacterClickListener(character: Character) {
        DetailNavigation.openDetail(character.id, findNavController())
    }

    override fun loadingState() {
        charactersViewModel.loadingState.observe(viewLifecycleOwner, { state ->
            when (state) {
                LoadingState.SUCCESS -> {
                    hideProgress()
                }
                LoadingState.EMPTY_OR_NULL -> {
                    emptyView()
                }
                LoadingState.LOADING -> {
                    showProgress(isFromUpdate)
                }
            }
        })
    }

    private fun paginationScroll() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == listOfCharacters.size - 1) {
                        isFromUpdate = true
                        charactersViewModel.fetchCharactersData(listOfCharacters.size)
                        isLoading = true
                    }
                }
            }
        })
    }
}