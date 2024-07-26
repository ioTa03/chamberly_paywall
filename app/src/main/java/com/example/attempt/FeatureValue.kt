package com.example.attempt

sealed class FeatureValue(val type: PaywallActivity.FeatureValueType) {
    class Text(val value: String) : FeatureValue(PaywallActivity.FeatureValueType.TEXT)
    object Lock : FeatureValue(PaywallActivity.FeatureValueType.ICON_LOCK)
    object Check : FeatureValue(PaywallActivity.FeatureValueType.ICON_CHECK)
    object Infinity : FeatureValue(PaywallActivity.FeatureValueType.ICON_INFINITY)
    object Free : FeatureValue(PaywallActivity.FeatureValueType.FREE)
    object Paid : FeatureValue(PaywallActivity.FeatureValueType.PAID)
    class Integer(val value: Int) : FeatureValue(PaywallActivity.FeatureValueType.INTEGER)
}