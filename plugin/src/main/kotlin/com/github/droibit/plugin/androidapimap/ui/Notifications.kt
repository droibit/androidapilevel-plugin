package com.github.droibit.plugin.androidapimap.ui

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType.*
import com.intellij.notification.Notifications


private const val ERROR_GROUP = "Plugin Error"
private const val ERROR_TITLE = "Error"

fun notifyError(text: String) {
    Notification(ERROR_GROUP, ERROR_TITLE, text, ERROR, null).run {
        Notifications.Bus.notify(this)
    }
}