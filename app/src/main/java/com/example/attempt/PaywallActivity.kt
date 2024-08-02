package com.example.attempt

import android.graphics.Paint
import androidx.core.content.ContextCompat
import FeatureComparisonAdapter
import ReviewPagerAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Color
import android.os.Build
import android.widget.TextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class PaywallActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var planAdapter: PlanAdapter
    private lateinit var plans: List<Plan>
    private var selectedPlan: Plan? = null
    private lateinit var mainPlanAdapter: PlanAdapter
    private lateinit var additionalPlanAdapter: PlanAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPager2: ViewPager2
    private lateinit var viewPagerIndicator: ViewPagerIndicator
    private lateinit var reviewPagerAdapter: ReviewPagerAdapter
    private lateinit var featureComparisonRecyclerView: RecyclerView
    private lateinit var featureComparisonAdapter: FeatureComparisonAdapter
    private lateinit var features: List<FeatureRow>
    private lateinit var reviewViewPagerIndicator: ViewPagerIndicator
    private val shouldDelayCloseButton = false
    private lateinit var trialMessageTextView: TextView
    private lateinit var closeButton: ImageView
    private lateinit var restorePurchaseTextView: TextView
    private var lastPurchasedPlan: Plan? = null
    private lateinit var viewPager22Indicator: ViewPagerIndicator
    private lateinit var trialButton: Button

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.paywall)

        val unlockHappinessTextView: TextView = findViewById(R.id.Unlock_happiness)
        applyGradientToTextView(unlockHappinessTextView)
        val happy: TextView = findViewById(R.id.AdvancedFeatures)
        applyGradientToTextView(happy)

        initializeViews()
        setupCloseButton()
        setupPlans()
        setupViewPagers()
        setupFeatureComparison()
        setupTrialButton()
        setupRestorePurchaseTextView()
    }

    private fun initializeViews() {
        closeButton = findViewById(R.id.close_button)
        viewPager = findViewById(R.id.viewPager)
        viewPagerIndicator = findViewById(R.id.viewPagerIndicator)
        viewPager2 = findViewById(R.id.viewPager22)
        reviewViewPagerIndicator = findViewById(R.id.reviewViewPagerIndicator)
        featureComparisonRecyclerView = findViewById(R.id.featureComparisonRecyclerView)
        viewPager22Indicator = findViewById(R.id.viewPager22Indicator)
        trialButton = findViewById(R.id.trialButton)
        restorePurchaseTextView = findViewById(R.id.restorePurchase)
    }

    private fun setupCloseButton() {
        if (shouldDelayCloseButton) {
            closeButton.visibility = View.GONE
            delayCloseButtonAppearance()
        } else {
            closeButton.visibility = View.VISIBLE
        }

        closeButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isTaskRoot) {
                    showExitConfirmationDialog()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun delayCloseButtonAppearance() {
        Handler(Looper.getMainLooper()).postDelayed({
            closeButton.visibility = View.VISIBLE
        }, 5000)
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            showExitConfirmationDialog()
        } else {
            super.onBackPressed()
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit App")
            .setMessage("Are you sure you want to exit the app?")
            .setPositiveButton("Yes") { _, _ ->
                finishAffinity()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun setupPlans() {
        plans = listOf(
            Plan("YEARLY PLAN", "360KR", "30 kr/month\nFree 7-days trial", true),
//            Plan("MONTHLY PLAN", "39KR", "39 kr/month\nFree 3-days trial", false),
//            Plan("MONTHLY PLAN", "39KR", "39 kr/month\nFree 3-days trial", false),
//            Plan("SURPRISE 1", "16KR", "16 kr/month\nFree 3-days trial", false)


        )
        lastPurchasedPlan = plans.first()

        val mainPlans = plans.take(2)
        val additionalPlans = plans.drop(2)

        setupMainRecyclerView(mainPlans)
        if (additionalPlans.isNotEmpty()) {
            setupAdditionalRecyclerView(additionalPlans)
        }
    }

    private fun setupMainRecyclerView(plans: List<Plan>) {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewPlans)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        mainPlanAdapter = PlanAdapter(plans, ::onPlanSelected, selectedPlan)
        recyclerView.adapter = mainPlanAdapter
    }

    private fun setupAdditionalRecyclerView(plans: List<Plan>) {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewNew)
        if (plans.isNotEmpty()) {
            recyclerView.visibility = View.VISIBLE
            val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.layoutManager = linearLayoutManager
            additionalPlanAdapter = PlanAdapter(plans, ::onPlanSelected, selectedPlan)
            recyclerView.adapter = additionalPlanAdapter
        } else {
            recyclerView.visibility = View.GONE
        }
    }

    private fun onPlanSelected(plan: Plan?) {
        selectedPlan = plan
        mainPlanAdapter.updateSelectedPlan(plan)
        if (::additionalPlanAdapter.isInitialized) {
            additionalPlanAdapter.updateSelectedPlan(plan)
        }
    }

    private fun setupTrialButton() {
        trialButton.setOnClickListener {
            if (selectedPlan != null) {
                lastPurchasedPlan = selectedPlan
                val message = "Started trial for: ${selectedPlan!!.title}, Price: ${selectedPlan!!.price}"
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                trialButton.isEnabled = false
                trialButton.text = "Completing Purchase..."

                Handler(Looper.getMainLooper()).postDelayed({
                    trialButton.text = "Purchase Complete"

                    // Disable the restore purchase option
                    restorePurchaseTextView.isEnabled = false
                    restorePurchaseTextView.setTextColor(ContextCompat.getColor(this, R.color.text_disabled))
                }, 3000)
            } else {
                Toast.makeText(this, "Please select a plan first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupViewPagers() {
        val sliderItems = listOf(
            SliderItem(R.drawable.image111, "Endless topics to follow", "Follow vast range of topics. Stay connected to the discussions that matter to you most, without any limits."),
            SliderItem(R.drawable.image112, "Direct messaging to listeners", "Connect directly with listeners who are ready to support you."),
            SliderItem(R.drawable.image113, "Custom avatars & badges", "10 Super badges. Unlimited avatars. Showcase your achievements and stand out in the community."),
            SliderItem(R.drawable.image114, "Unlimited access to listener GAI", "Whether it's day or night, your virtual companion is always here to help."),
            SliderItem(R.drawable.image115, "Age group match", "Engage in conversations with those who understand your life stage and experiences."),
            SliderItem(R.drawable.image116, "Unlimited chat time", "Enjoy uninterrupted conversations without time limits. Talk freely and extensively with listeners and venters alike."),
            SliderItem(R.drawable.image111, "Medo", "Helo.Stay connected to the discussions that matter to you most, without any limits."),

        )
        val adapter = SliderPagerAdapter(this, sliderItems)
        viewPager.adapter = adapter
        viewPagerIndicator.setIndicatorCount(sliderItems.size)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewPagerIndicator.setSelectedPosition(position % sliderItems.size)
            }
        })

        val startPosition = (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % sliderItems.size)
        viewPager.setCurrentItem(startPosition, false)

        val reviews = listOf(
            Review("Useful app for anonymous chatting", "Its a really nice app to chat anonymously with people around the world. It helps you vent out with people without the feeling of being judged. I highly recommend this app!!! If you join, you'll probably match with me. I'm here all the time :))", "Anonymous User, 05/06/2024"),
            Review("Ahh, this helps so much", "This app has been helpful to me. Knowing there's always someone to talk to 24/7 has brought me comfort many times when I needed someone. I really appreciate the support it's been giving me. Chamberly is my best friend ❤️", "Anonymous User, 03/04/2024"),
            Review("This saved me 💞", "This app has the perfect safe space I was looking for. I remember joining this app when my world was so blurry. I met my best friend in here and we always look out for each other", "Anonymous User, 18/01/2024")
        )
        reviewPagerAdapter = ReviewPagerAdapter(reviews)
        viewPager2.adapter = reviewPagerAdapter
        reviewViewPagerIndicator.setIndicatorCount(reviews.size)

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                reviewViewPagerIndicator.setSelectedPosition(position % reviews.size)
            }
        })

        viewPager2.setCurrentItem(0, false)
        reviewViewPagerIndicator.setSelectedPosition(0)

        viewPager2.post {
            val wrapperView = viewPager2.getChildAt(0)
            wrapperView.measure(
                View.MeasureSpec.makeMeasureSpec(viewPager2.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            val params = viewPager2.layoutParams
            params.height = wrapperView.measuredHeight
            viewPager2.layoutParams = params
        }
    }

    private fun setupFeatureComparison() {
        features = listOf(
            FeatureRow("                             ", FeatureValue.Free, FeatureValue.Paid),

            FeatureRow("Sending messages", FeatureValue.Text("Limited"), FeatureValue.Infinity),
            FeatureRow("Chats", FeatureValue.Text("Limited"), FeatureValue.Infinity),
            FeatureRow("Premium Matching Quality and Speed", FeatureValue.Lock, FeatureValue.Check),
            FeatureRow("Message all Missed Matches Directly For 0 Coins", FeatureValue.Integer(5), FeatureValue.Integer(0)),
            FeatureRow("Premium Avatars", FeatureValue.Lock, FeatureValue.Infinity),
            FeatureRow("Exclusive Supported Badge + 200 coins", FeatureValue.Lock, FeatureValue.Check),
            FeatureRow("Coins Received Daily", FeatureValue.Integer(15), FeatureValue.Integer(30)),
            FeatureRow("Image Sharing Capabilities", FeatureValue.Lock, FeatureValue.Check),
            FeatureRow("Advanced Journal", FeatureValue.Text("Limited"), FeatureValue.Check),
            FeatureRow("Advanced Plupi", FeatureValue.Text("Limited"), FeatureValue.Check),
            FeatureRow("Direct messaging to contributors", FeatureValue.Lock, FeatureValue.Check)
        )

        featureComparisonAdapter = FeatureComparisonAdapter(features)
        featureComparisonRecyclerView.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean = false
        }
        featureComparisonRecyclerView.adapter = featureComparisonAdapter
        featureComparisonRecyclerView.isNestedScrollingEnabled = false

        val layoutParams = featureComparisonRecyclerView.layoutParams
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        featureComparisonRecyclerView.layoutParams = layoutParams
    }

    private fun setupRestorePurchaseTextView() {
        restorePurchaseTextView.setOnClickListener {
            restorePurchaseTextView.paintFlags = restorePurchaseTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            restorePurchaseTextView.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
            restorePurchase()
        }
    }

    private fun restorePurchase() {
        val existingPurchase = checkForExistingPurchase()

        if (existingPurchase != null) {
            restorePurchaseTextView.text = "Restoring Purchase..."

            Handler(Looper.getMainLooper()).postDelayed({
                updateUIForRestoredPurchase(existingPurchase)
                Toast.makeText(this, "Purchase restored for ${existingPurchase.title}", Toast.LENGTH_SHORT).show()
            }, 2000)
        } else {
            Toast.makeText(this, "No previous purchase found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkForExistingPurchase(): Plan? {
        return lastPurchasedPlan ?: plans.firstOrNull()
    }

    private fun updateUIForRestoredPurchase(restoredPlan: Plan) {
        selectedPlan = restoredPlan
        onPlanSelected(restoredPlan)

        trialButton.text = "Continue Subscription"
        trialButton.isEnabled = false

        restorePurchaseTextView.text = "Purchase Restored"
        restorePurchaseTextView.isClickable = false
    }

    private fun applyGradientToTextView(textView: TextView) {
        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())

        val shader = LinearGradient(
            0f, 0f, width, textView.textSize,
            intArrayOf(
                Color.parseColor("#7A7AFF"),
                Color.parseColor("#F84950")
            ),
            floatArrayOf(0f, 1f),
            Shader.TileMode.CLAMP
        )

        textView.paint.shader = shader
    }

    enum class FeatureValueType {
        TEXT, ICON_LOCK, ICON_CHECK, ICON_INFINITY, INTEGER, FREE, PAID
    }
}