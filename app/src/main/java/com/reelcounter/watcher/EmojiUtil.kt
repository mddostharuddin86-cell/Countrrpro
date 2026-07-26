package com.reelcounter.watcher

object EmojiUtil {
    /** Emoji gets more "gone" as the daily reel count climbs. */
    fun emojiFor(count: Int): String = when {
        count < 5 -> "🙂"
        count < 10 -> "😐"
        count < 20 -> "😑"
        count < 30 -> "😵"
        count < 50 -> "💀"
        else -> "☠️"
    }
}
