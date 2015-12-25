package com.github.droibit.plugin.androidapimap

import com.github.droibit.plugin.androidapimap.ui.AndroidApiMapDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * @author kumagai
 */
class ShowAndroidApiMapAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        AndroidApiMapDialog().show()
    }
}
