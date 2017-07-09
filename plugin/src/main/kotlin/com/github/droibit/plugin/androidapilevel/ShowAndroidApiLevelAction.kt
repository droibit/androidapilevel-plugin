package com.github.droibit.plugin.androidapilevel

import com.github.droibit.plugin.androidapilevel.ui.AndroidApiLevelDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ShowAndroidApiLevelAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        AndroidApiLevelDialog().show()
    }
}
