package com.example.myrestaurant.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.util.Locale


fun switchLanguage(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = context.resources.configuration
    config.setLocale(locale)

    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    // Restart the activity
    (context as? Activity)?.recreate()
}
