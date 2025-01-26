package com.example.pokedexapp.presentation.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexapp.R
import com.example.pokedexapp.databinding.HomeActivityLayoutBinding
import com.example.pokedexapp.presentation.details.DetailsActivity
import com.example.pokedexapp.presentation.home.recyclerview.PokedexRVAdapter
import com.example.pokedexapp.presentation.home.recyclerview.PokemonItemViewModel
import com.example.pokedexapp.presentation.pokemonImage.PokemonImageActivity
import com.example.pokedexapp.utils.EXTRA_IMAGE_URL
import com.example.pokedexapp.utils.EXTRA_POKEMON_NAME
import com.example.pokedexapp.utils.EXTRA_TRANSITION_NAME
import com.example.pokedexapp.utils.Mapper.toItemViewModel
import com.example.pokedexapp.utils.POKEMON_FAVORITE_STATUS
import com.example.pokedexapp.utils.POKEMON_ID
import com.example.pokedexapp.utils.Result
import com.example.pokedexapp.utils.showAlertError
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeActivityLayoutBinding
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var rvAdapter: PokedexRVAdapter
    private var errorDisplayed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("HomeActivity_TAG: onCreate: ")
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity_layout)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.getPokemons()

        initObservers()
        initRecycler()
    }

    private fun initObservers() {
        Timber.d("HomeActivity_TAG: initObservers: ")
        viewModel.pokedex.observe(this) { pokemons ->
            val itemsList = pokemons.map { it.toItemViewModel() }
            rvAdapter.updateList(itemsList.takeLast(25))
        }

        viewModel.errorType.observe(this) { error ->
            if (error == null)
                return@observe

            val message = when (error) {
                is Result.NetworkError -> getString(R.string.txt_internet_error)
                is Result.EmptyResponse -> getString(R.string.txt_emptyInfo_error)
                else -> getString(R.string.txt_unexpected_error)
            }

            if (!errorDisplayed) {
                errorDisplayed = true
                showAlertError(
                    this,
                    message
                ) {
                    viewModel.loading.postValue(true)
                    viewModel.getPokemons()
                    errorDisplayed = false
                }
            }

            viewModel.errorType.postValue(null)
        }
    }

    private fun initRecycler() {
        Timber.d("HomeActivity_TAG: initRecycler: ")
        rvAdapter = PokedexRVAdapter(
            itemClicked = {
                navigateToDetails(it)
            },
            imageClicked = { item, imageView ->
                openImageWithTransition(item, imageView)
            },
            favoriteClicked = { item, favorite ->
                viewModel.toggleFavoriteStatus(item.id, favorite)
            }
        )

        binding.rvPokedex.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, RecyclerView.VERTICAL, false)
            adapter = rvAdapter

            setPositionListener(this)
        }
    }

    private fun navigateToDetails(it: PokemonItemViewModel) {
        Timber.d("HomeActivity_TAG: navigateToDetails: ")
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(POKEMON_ID, it.id)
        detailsResultLauncher.launch(intent)
    }

    private val detailsResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val pokemonId =
                    result.data?.getIntExtra(POKEMON_ID, -1) ?: return@registerForActivityResult
                val isFavorite = result.data?.getBooleanExtra(POKEMON_FAVORITE_STATUS, false)
                    ?: return@registerForActivityResult

                val itemToUpdate = viewModel.pokedex.value?.first { it.id == pokemonId }
                    ?.copy(isFavorite = isFavorite)?.toItemViewModel()

                if (itemToUpdate != null)
                    rvAdapter.updateItemFavoriteStatus(itemToUpdate)
            }
        }

    private fun openImageWithTransition(
        item: PokemonItemViewModel,
        imageView: View
    ) {
        Timber.d("HomeActivity_TAG: openImageWithTransition: ")
        val intent = Intent(this, PokemonImageActivity::class.java).apply {
            putExtra(EXTRA_IMAGE_URL, item.sprite)
            putExtra(EXTRA_POKEMON_NAME, item.name)
            putExtra(EXTRA_TRANSITION_NAME, imageView.transitionName)
        }

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            imageView,
            imageView.transitionName
        )
        startActivity(intent, options.toBundle())
    }

    private fun setPositionListener(recyclerView: RecyclerView) {
        Timber.d("HomeActivity_TAG: setPositionListener: ")
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                    firstVisibleItemPosition >= 0 &&
                    !viewModel.loading.value!!
                ) {
                    viewModel.getPokemons()
                }
            }
        })
    }
}