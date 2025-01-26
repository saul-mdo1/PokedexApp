package com.example.pokedexapp.presentation.details

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokedexapp.R
import com.example.pokedexapp.databinding.DetailsActivityLayoutBinding
import com.example.pokedexapp.utils.POKEMON_FAVORITE_STATUS
import com.example.pokedexapp.utils.POKEMON_ID
import com.example.pokedexapp.utils.Result
import com.example.pokedexapp.utils.showAlertError
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: DetailsActivityLayoutBinding
    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("DetailsActivity_TAG: onCreate: ")
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.details_activity_layout)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initObservers()
        getId()

        viewModel.getDetails()

        binding.icon.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleFavoriteStatus(isChecked)
        }

        onBackPressedDispatcher.addCallback {
            if (viewModel.favorite != viewModel.pokemon.value?.isFavorite) {
                val intent = Intent()
                val isFavorite = viewModel.favorite
                intent.putExtra(POKEMON_FAVORITE_STATUS, isFavorite)
                intent.putExtra(POKEMON_ID, viewModel.id)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    private fun getId() {
        Timber.d("DetailsActivity_TAG: getId: ")
        intent.extras?.getInt(POKEMON_ID)?.let { id ->
            viewModel.id = id
        }
    }

    private fun initObservers() {
        Timber.d("DetailsActivity_TAG: initObservers: ")
        viewModel.pokemon.observe(this) { pokemon ->
            pokemon.moves?.let { moves ->
                initRecyclerView(moves)
            }
        }

        viewModel.errorType.observe(this) { error ->
            if (error == null)
                return@observe

            val message = when (error) {
                is Result.NetworkError -> getString(R.string.txt_internet_error)
                is Result.EmptyResponse -> getString(R.string.txt_emptyInfo_error)
                else -> getString(R.string.txt_unexpected_error)
            }

            showAlertError(
                this,
                message
            ) {
                viewModel.loading.postValue(true)
                viewModel.getDetails()
            }

            viewModel.errorType.postValue(null)
        }
    }

    private fun initRecyclerView(moves: List<String>) {
        Timber.d("DetailsActivity_TAG: initRecyclerView: ")
        val rvAdapter = MovesRVAdapter(moves)
        binding.rvMoves.apply {
            layoutManager = GridLayoutManager(this@DetailsActivity, 3)
            adapter = rvAdapter
        }
    }
}