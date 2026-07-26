package com.reelcounter.watcher

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CounterStore {
    private const val PREFS = "reel_counter_prefs"
    const val KEY_COUNT = "count"
    private const val KEY_DATE = "date"

    private fun prefs(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    private fun today(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    /** Call before every read/write so the count auto-resets on a new day. */
    private fun resetIfNewDay(context: Context) {
        val p = prefs(context)
        val savedDate = p.getString(KEY_DATE, "")
        val currentDate = today()
        if (savedDate != currentDate) {
            p.edit()
                .putInt(KEY_COUNT, 0)
                .putString(KEY_DATE, currentDate)
                .apply()
        }
    }

    fun getCount(context: Context): Int {
        resetIfNewDay(context)
        return prefs(context).getInt(KEY_COUNT, 0)
    }

    fun increment(context: Context): Int {
        resetIfNewDay(context)
        val p = prefs(context)
        val newCount = p.getInt(KEY_COUNT, 0) + 1
        p.edit().putInt(KEY_COUNT, newCount).apply()
        return newCount
    }

    fun resetNow(context: Context) {
        prefs(context).edit()
            .putInt(KEY_COUNT, 0)
            .putString(KEY_DATE, today())
            .apply()
    }

    fun registerListener(context: Context, listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs(context).registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterListener(context: Context, listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs(context).unregisterOnSharedPreferenceChangeListener(listener)
    }
}
