import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.attempt.R
import com.example.attempt.Review
//import com.google.android.libraries.places.api.model.Review

class ReviewPagerAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewPagerAdapter.ReviewViewHolder>() {
    private val sortedReviews: List<Review> = reviews.sortedByDescending { it.content.length }
    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewTitle: TextView = itemView.findViewById(R.id.reviewTitle)
        val reviewContent: TextView = itemView.findViewById(R.id.reviewContent)
        val reviewedBy: TextView = itemView.findViewById(R.id.reviewedBy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reviews, parent, false)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return ReviewViewHolder(view)
    }

  override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val actualPosition = position % sortedReviews.size
        val review = sortedReviews[actualPosition]
        holder.reviewTitle.text = review.title
        holder.reviewContent.text = review.content
        holder.reviewedBy.text = review.reviewedBy
    }
    override fun getItemCount(): Int = Int.MAX_VALUE
}