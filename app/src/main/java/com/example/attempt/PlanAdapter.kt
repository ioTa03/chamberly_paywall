package com.example.attempt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class PlanAdapter(private val plans: List<Plan>,
                  private val onPlanSelected: (Plan?) -> Unit,
                  private var selectedPlan: Plan?
) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_BEST_PLAN) R.layout.bestplan2 else R.layout.notbestplan2
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return PlanViewHolder(view)
    }


override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
    val plan = plans[position]
    holder.bind(plan)

    holder.itemView.isSelected = plan == selectedPlan
    holder.itemView.alpha = if (plan == selectedPlan) 0.9f else 1f

    holder.itemView.setOnClickListener {
        if (selectedPlan == plan) {
            selectedPlan = null
            onPlanSelected(null)
        } else {
            selectedPlan = plan
            onPlanSelected(plan)
        }
        notifyDataSetChanged()
    }
}
    fun updateSelectedPlan(plan: Plan?) {
        selectedPlan = plan
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = plans.size

    override fun getItemViewType(position: Int): Int =
        if (plans[position].bestDeal) VIEW_TYPE_BEST_PLAN else VIEW_TYPE_NORMAL_PLAN

    companion object {
        private const val VIEW_TYPE_BEST_PLAN = 1
        private const val VIEW_TYPE_NORMAL_PLAN = 2
    }

    class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTitle: TextView = itemView.findViewById(R.id.textView)
        private val textViewPrice: TextView = itemView.findViewById(R.id.textView2)
        private val textViewDescription: TextView = itemView.findViewById(R.id.textView3)

        fun bind(plan: Plan) {
            textViewTitle.text = plan.title
            textViewPrice.text = plan.price
            textViewDescription.text = plan.description
        }
    }


}