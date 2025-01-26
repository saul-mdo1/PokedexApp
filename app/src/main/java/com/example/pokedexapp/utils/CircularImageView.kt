package com.example.pokedexapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.pokedexapp.R

class CircularImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView

    //region Attributes
    private var placeholder: Int = 0
    private var textColor: Int = Color.WHITE
    private var backgroundColor: Int = Color.GRAY

    //endregion
    init {
        // Init layout
        val view = View.inflate(context, R.layout.circular_image_view, this)
        imageView = view.findViewById(R.id.image)

        // Set attrs from layout where the view is used
        attrs?.let {
            // Get reference to attributes file
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CircularImageView)

            placeholder = typedArray.getResourceId(
                R.styleable.CircularImageView_placeholder,
                R.drawable.placeholder
            )
            textColor =
                typedArray.getColor(R.styleable.CircularImageView_initialsTextColor, textColor)
            backgroundColor = typedArray.getColor(
                R.styleable.CircularImageView_initialsBackgroundColor,
                backgroundColor
            )
            typedArray.recycle()
        }
    }

    fun setImage(url: String?, name: String?) {
        val loadOfflineImage =
            generateInitialsBitmap(extractInitials(name), backgroundColor, textColor) ?: placeholder
        if (!url.isNullOrEmpty()) {
            Glide.with(context)
                .load(url)
                .circleCrop()
                .error(loadOfflineImage) // When the url can't be loaded due offline mode
                .into(imageView)

            imageView.setBackgroundResource(R.drawable.border)
        } else {
            imageView.background = null
            showInitialsOrPlaceholder(name)
        }
    }

    private fun showInitialsOrPlaceholder(name: String?) {
        val initials = extractInitials(name)
        if (initials.isNotBlank()) {
            //Show initials
            val bitmap = generateInitialsBitmap(initials, backgroundColor, textColor)
            if (bitmap != null)
                // Bitmap with initials was generated correctly
                Glide.with(context).load(bitmap).circleCrop().into(imageView)
            else
                // Bitmap couldn't be generated, so display the placeholder
                Glide.with(this).load(placeholder).circleCrop().into(imageView)
        } else {
            //Show Placeholder
            Glide.with(context).load(placeholder).circleCrop().into(imageView)
        }
    }

    private fun extractInitials(name: String?): String {
        if (name.isNullOrEmpty()) return ""
        val words = name.trim().split(" ")
        val filteredWords = words.take(2).filter { it.firstOrNull()?.isLetter() == true }
        return filteredWords.joinToString("") { it.first().uppercase() }
    }

    private fun generateInitialsBitmap(
        initials: String?,
        backgroundColor: Int,
        textColor: Int
    ): Bitmap? {
        if (initials.isNullOrEmpty()) return null

        val width = 100
        val height = 100

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        // Create circular background
        val paintBackground = Paint().apply {
            color = backgroundColor
            isAntiAlias = true
        }

        // Draw circular background
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2).toFloat(),
            paintBackground
        )

        // Create initials text
        val textPaint = TextPaint().apply {
            color = textColor
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            textSize = 32f
        }

        val xPos = (width / 2).toFloat()
        val yPos = (height / 2).toFloat() - (textPaint.descent() + textPaint.ascent()) / 2

        // Draw initials in canvas
        canvas.drawText(initials, xPos, yPos, textPaint)

        return bitmap
    }
}
