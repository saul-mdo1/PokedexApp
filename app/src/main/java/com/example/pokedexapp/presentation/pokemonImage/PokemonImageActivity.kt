package com.example.pokedexapp.presentation.pokemonImage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedexapp.R
import com.example.pokedexapp.utils.ui.CircularImageView
import com.example.pokedexapp.utils.EXTRA_IMAGE_URL
import com.example.pokedexapp.utils.EXTRA_POKEMON_NAME
import com.example.pokedexapp.utils.EXTRA_TRANSITION_NAME
import com.google.android.material.button.MaterialButton

class PokemonImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pokemon_image_activity_layout)

        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        val pokemonName = intent.getStringExtra(EXTRA_POKEMON_NAME)
        val transitionName = intent.getStringExtra(EXTRA_TRANSITION_NAME)

        val imageView: CircularImageView = findViewById(R.id.expandedImageView)
        imageView.transitionName = transitionName

        imageView.setImage(imageUrl, pokemonName)

        findViewById<MaterialButton>(R.id.bBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}