package com.github.droibit.plugin.androidapilevel.ui

import com.github.droibit.plugin.androidapilevel.util.stringBundle
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType.ERROR
import com.intellij.notification.Notifications


fun notifyError(text: String) {
    Notification(stringBundle.notificationErrorGroup,
            stringBundle.notificationErrorTitle, text, ERROR, null).run {
        Notifications.Bus.notify(this)
    }
}