package com.cashcluster.collect

import android.content.Context
import android.content.SharedPreferences

class OnboardingPrefs(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
    private val KEY_ONBOARDING_COMPLETE = "onboarding_complete"

    fun isOnboardingComplete(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETE, false)
    }

    fun setOnboardingComplete(complete: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETE, complete).apply()
    }
} 