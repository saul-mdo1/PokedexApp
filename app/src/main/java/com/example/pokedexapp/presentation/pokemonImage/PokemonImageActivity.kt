package com.example.pokedexapp.presentation.pokemonImage

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pokedexapp.R
import com.example.pokedexapp.utils.EXTRA_IMAGE_URL
import com.example.pokedexapp.utils.EXTRA_TRANSITION_NAME
import com.google.android.material.button.MaterialButton

class PokemonImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pokemon_image_activity_layout)

        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        val transitionName = intent.getStringExtra(EXTRA_TRANSITION_NAME)

        val imageView: ImageView = findViewById(R.id.expandedImageView)
        imageView.transitionName = transitionName

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .into(imageView)

        findViewById<MaterialButton>(R.id.bBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}