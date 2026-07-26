package com.reelcounter.watcher

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Build
import android.view.accessibility.AccessibilityEvent

class ReelAccessibilityService : AccessibilityService() {

    private val trackedPackages = setOf(
        "com.instagram.android",
        "com.google.android.youtube"
    )

    private var lastCountedAt = 0L
    private val minIntervalMs = 1000L

    override fun onServiceConnected() {
        super.onServiceConnected()
        val intent = Intent(this, OverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val pkg = event.packageName?.toString() ?: return
        if (pkg !in trackedPackages) return
        if (event.eventType != AccessibilityEvent.TYPE_VIEW_SCROLLED) return

        val className = event.className?.toString() ?: ""
        val looksLikePager = className.contains("ViewPager2") || className.contains("RecyclerView")
        if (!looksLikePager) return

        val now = System.currentTimeMillis()
        if (now - lastCountedAt < minIntervalMs) return
        lastCountedAt = now

        CounterStore.increment(applicationContext)
    }

    override fun onInterrupt() { }
}
