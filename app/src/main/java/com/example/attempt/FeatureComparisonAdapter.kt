import android.graphics.Paint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attempt.FeatureRow
import com.example.attempt.FeatureValue
import com.example.attempt.R

class FeatureComparisonAdapter(private val features: List<FeatureRow>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var expandedList = false
    private val initialItemCount = 6
    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_SEE_MORE = 1
    }
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val featureName: TextView = view.findViewById(R.id.featureName)
        val freeValue: ViewGroup = view.findViewById(R.id.freeValue)
        val paidValue: ViewGroup = view.findViewById(R.id.paidValue)
        val dividerLine: ImageView = view.findViewById(R.id.dividerLine)
    }
    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    private fun scrollToTop() {
        recyclerView?.smoothScrollToPosition(0)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.feature_row, parent, false)
                ItemViewHolder(view)
            }
            VIEW_TYPE_SEE_MORE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.see_more_text, parent, false)
                SeeMoreViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
    class SeeMoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val seeMoreText: TextView = view.findViewById(R.id.seeMoreLessText)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                val feature = features[position]
                holder.featureName.text = feature.featureName
                setFeatureValue(holder.freeValue, feature.freeValue)
                setFeatureValue(holder.paidValue, feature.paidValue)

                if (position == features.size - 1 || position == 0) {
                    holder.dividerLine.visibility = View.GONE
                } else {
                    holder.dividerLine.visibility = View.VISIBLE
                    holder.dividerLine.alpha = 0.4f
                }
//                if we want to not display divider line after the initialitemcount, use the commented code below
//                when {
//                    // Hide divider for the last visible item
//                    (!expandedList && position == initialItemCount - 1) ||
//                            (expandedList && position == features.size - 1) -> {
//                        holder.dividerLine.visibility = View.GONE
//                    }
//                    // Hide divider for the first item
//                    position == 0 -> {
//                        holder.dividerLine.visibility = View.GONE
//                    }
//                    // Show divider for all other items
//                    else -> {
//                        holder.dividerLine.visibility = View.VISIBLE
//                        holder.dividerLine.alpha = 0.4f
//                    }
            }
            is SeeMoreViewHolder -> {
                holder.seeMoreText.text = if (expandedList) "Collapse" else "See More"
                holder.seeMoreText.setOnClickListener {
                    val wasExpanded = expandedList
                    expandedList = !expandedList
                    if (wasExpanded) {
                        notifyDataSetChanged()
                        scrollToTop()
                    } else {
                        notifyDataSetChanged()
                    }
                }

                // Add underline to the text
                holder.seeMoreText.paintFlags = holder.seeMoreText.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            }
        }
    }
    override fun getItemCount(): Int {
        return if (expandedList) features.size + 1 else minOf(initialItemCount, features.size) + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == minOf(if (expandedList) features.size else initialItemCount, features.size)) {
            VIEW_TYPE_SEE_MORE
        } else {
            VIEW_TYPE_ITEM
        }
    }

//    override fun getItemCount() = features.size

    private fun setFeatureValue(container: ViewGroup, value: FeatureValue) {
        container.removeAllViews()
        when (value) {
            is FeatureValue.Text -> addTextView(container, value.value)
            is FeatureValue.Lock -> addImageView(container, R.drawable.ic_lock)
            is FeatureValue.Check -> addImageView(container, R.drawable.tick)
            is FeatureValue.Infinity -> addImageView(container, R.drawable.ic_infinity)
            is FeatureValue.Integer -> addTextView(container, value.value.toString())
            is FeatureValue.Free->addImageView(container,R.drawable.free)
            is FeatureValue.Paid->addImageView(container,R.drawable.plus1)
        }
    }

    private fun addTextView(container: ViewGroup, text: String) {
        val textView = TextView(container.context)
        textView.text = text
        textView.gravity = Gravity.CENTER
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }
        container.addView(textView)
    }

    private fun addImageView(container: ViewGroup, drawableRes: Int) {
        val imageView = ImageView(container.context)
        imageView.setImageResource(drawableRes)
        imageView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }
        container.addView(imageView)
    }
}