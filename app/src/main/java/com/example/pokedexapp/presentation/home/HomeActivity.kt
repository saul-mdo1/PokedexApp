package com.example.pokedexapp.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.pokedexapp.R
import com.example.pokedexapp.databinding.HomeActivityLayoutBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeActivityLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity_layout)
    }
}