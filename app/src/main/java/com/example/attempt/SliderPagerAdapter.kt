package com.example.attempt

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SliderPagerAdapter(private val context: Context, private val items: List<SliderItem>) :
    RecyclerView.Adapter<SliderPagerAdapter.SliderViewHolder>() {

    private val sortedItems: List<SliderItem> = items.sortedByDescending {
        it.title.length + it.description.length
    }


    class SliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imageView9)
        val colorfulTitle: TextView = view.findViewById(R.id.colorfulTitle)
        val description: TextView = view.findViewById(R.id.description)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.slidesfirst, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val item = sortedItems[position % sortedItems.size]
        holder.image.setImageResource(item.imageResId)
        holder.colorfulTitle.text = item.title
        holder.description.text = item.description

        applyTextGradient(holder.colorfulTitle)

        // Adjust text sizes based on position
//        val scaleFactor = 1f - (position % sortedItems.size) * 0.1f
//        holder.colorfulTitle.textSize = 24f * scaleFactor
//        holder.description.textSize = 16f * scaleFactor
    }
    override fun getItemCount() = Int.MAX_VALUE

    private fun applyTextGradient(textView: TextView) {
        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())

        val textShader: Shader = LinearGradient(
            0f, 0f, width, textView.textSize,
            intArrayOf(
                Color.parseColor("#7A7AFF"),
                Color.parseColor("#F84950")
            ),
            floatArrayOf(0f, 1f),
            Shader.TileMode.CLAMP
        )

        textView.paint.shader = textShader
        textView.invalidate()
    }
}

data class SliderItem(val imageResId: Int, val title: String, val description: String)