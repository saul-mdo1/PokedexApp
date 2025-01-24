package com.example.pokedexapp.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedexapp.R
import com.example.pokedexapp.databinding.HomeActivityLayoutBinding
import com.example.pokedexapp.presentation.home.recyclerview.PokedexRVAdapter
import com.example.pokedexapp.presentation.home.recyclerview.PokemonItemViewModel
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
    }

    private fun initRecycler() {
        Timber.d("HomeActivity_TAG: initRecycler: ")
        rvAdapter = PokedexRVAdapter {
            Toast.makeText(this, "POKEMON CLICKED: ${it.name}", Toast.LENGTH_SHORT).show()
        }

        binding.rvPokedex.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, RecyclerView.VERTICAL, false)
            adapter = rvAdapter
        }
    }
}