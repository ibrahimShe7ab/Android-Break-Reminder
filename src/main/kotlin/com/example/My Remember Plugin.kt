package org.example

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.util.*
import javax.sound.sampled.AudioSystem
import kotlin.concurrent.schedule

class MyRememberPlugin : AnAction() {
    private var timer: Timer? = null
    private val interval =  2 * 60 * 60 * 1000L
    private var isRunning = false

    override fun actionPerformed(e: AnActionEvent) {
        if (isRunning) {
            stopReminder()
            showNotification("❌ تم إيقاف التذكيرات. صلِّ على الرسول ﷺ", NotificationType.WARNING)
        } else {
            startReminder()
            showNotification("✅ تم تفعيل تذكير الاستراحة كل ساعتين استعن بالله و لا تعجز .  صلِّ على الرسول ﷺ", NotificationType.INFORMATION)
            playSound("sound.wav") // تشغيل الصوت عند التفعيل
        }
    }

    fun startReminder() {
        timer = Timer()
        timer?.schedule(interval) {
            playSound("sound_weak_up.wav") // تشغيل الصوت بعد ساعتين
            showNotification("🚀 وقت الاستراحة! خذ استراحة قصيرة. صلِّ على الرسول ﷺ واشربلك حاجة و لا مش رجلك بس اوعك تكلم خطيبتك و لا امك و لا خالتك و لا حد من العيلة انقطع عن العالم  ورانا شغل الله يخليك", NotificationType.INFORMATION)
        }
        isRunning = true
    }

    private fun stopReminder() {
        timer?.cancel()
        timer = null
        isRunning = false
    }

    fun playSound(fileName: String) {
        try {
            val url = javaClass.classLoader.getResource(fileName)
            if (url == null) return

            val audioInputStream = AudioSystem.getAudioInputStream(url)
            val clip = AudioSystem.getClip()
            clip.open(audioInputStream)
            clip.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showNotification(message: String, type: NotificationType) {
        val notificationGroup = NotificationGroupManager.getInstance()
            .getNotificationGroup("Break Reminder Notifications")

        val notification = notificationGroup.createNotification(message, type)
        Notifications.Bus.notify(notification)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = true
    }
}
