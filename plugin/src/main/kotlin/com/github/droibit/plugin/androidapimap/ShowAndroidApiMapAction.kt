package com.github.droibit.plugin.androidapimap

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * Created by kumagai on 2015/12/21.
 */
class ShowAndroidApiMapAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        AndroidApiMapDialog().show()
    }
}
