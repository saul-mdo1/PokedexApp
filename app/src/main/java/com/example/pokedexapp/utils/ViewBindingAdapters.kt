package com.example.pokedexapp.utils

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.pokedexapp.R
import com.example.pokedexapp.model.Stat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.LinearProgressIndicator
import timber.log.Timber

@BindingAdapter("android:visibility")
fun visibility(view: View, visible: Boolean?) = when (visible) {
    true -> view.visibility = View.VISIBLE
    else -> view.visibility = View.GONE
}

@BindingAdapter("android:text")
fun text(textView: TextView, value: Int) {
    textView.text = value.toString()
}

@BindingAdapter("android:text")
fun text(textView: TextView, value: List<String>?) {
    textView.text = value?.joinToString(" | ")
}

@BindingAdapter("data")
fun setPokemonStringsList(chipGroup: ChipGroup, list: List<String>?) {
    chipGroup.removeAllViews()

    list?.forEach { string ->
        val chip = Chip(chipGroup.context).apply {
            text = string
            isClickable = false
            isCheckable = false

            setTextColor(Color.BLACK)
        }

        chipGroup.addView(chip)
    }
}

@BindingAdapter(
    value = ["imageUrl"]
)
fun imageUrl(
    view: ImageView,
    imageUrl: String?
) {
    try {
        Glide.with(view).load(imageUrl).into(view)
    } catch (e: Exception) {
        Timber.d("ViewBindingAdapters_TAG: imageUrl: exception: $e")
    }
}

@BindingAdapter("statsList")
fun bindStats(linearLayout: LinearLayout, stats: List<Stat>?) {
    linearLayout.removeAllViews()

    stats?.forEach { stat ->
        val itemView = LayoutInflater.from(linearLayout.context)
            .inflate(R.layout.stat_item_layout, linearLayout, false)

        itemView.findViewById<TextView>(R.id.tvStatName).apply {
            text = context.getString(R.string.txt_stat_name_placeholder, stat.name, stat.base_stat.toString())
        }

        val progressBar = itemView.findViewById<LinearProgressIndicator>(R.id.progressStatValue)
        progressBar.progress = stat.base_stat
        progressBar.max = 255

        linearLayout.addView(itemView)
    }
}