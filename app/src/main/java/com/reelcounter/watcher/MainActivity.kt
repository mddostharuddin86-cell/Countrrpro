package com.reelcounter.watcher

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var countText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countText = findViewById(R.id.countText)

        findViewById<Button>(R.id.btnOverlayPermission).setOnClickListener {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnAccessibility).setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        findViewById<Button>(R.id.btnShowBubble).setOnClickListener {
            val intent = Intent(this, OverlayService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        findViewById<Button>(R.id.btnReset).setOnClickListener {
            CounterStore.resetNow(applicationContext)
            refreshCount()
        }
    }

    override fun onResume() {
        super.onResume()
        refreshCount()
    }

    private fun refreshCount() {
        val count = CounterStore.getCount(applicationContext)
        countText.text = "Aaj ka count: $count ${EmojiUtil.emojiFor(count)}"
    }
}
