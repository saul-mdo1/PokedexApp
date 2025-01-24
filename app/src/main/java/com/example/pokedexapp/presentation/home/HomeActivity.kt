package com.example.pokedexapp.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexapp.R
import com.example.pokedexapp.databinding.HomeActivityLayoutBinding
import com.example.pokedexapp.presentation.details.DetailsActivity
import com.example.pokedexapp.presentation.home.recyclerview.PokedexRVAdapter
import com.example.pokedexapp.presentation.home.recyclerview.PokemonItemViewModel
import com.example.pokedexapp.utils.POKEMON_ID
import com.example.pokedexapp.utils.Result
import com.example.pokedexapp.utils.showAlertError
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeActivityLayoutBinding
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var rvAdapter: PokedexRVAdapter

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
        viewModel.pokedex.observe(this) {
            rvAdapter.itemsList = it.map {
                PokemonItemViewModel().apply {
                    pokemon = it
                }
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
                viewModel.getPokemons()
            }

            viewModel.errorType.postValue(null)
        }
    }

    private fun initRecycler() {
        Timber.d("HomeActivity_TAG: initRecycler: ")
        rvAdapter = PokedexRVAdapter {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(POKEMON_ID, it.id)
            startActivity(intent)
        }

        binding.rvPokedex.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, RecyclerView.VERTICAL, false)
            adapter = rvAdapter
        }
    }
}