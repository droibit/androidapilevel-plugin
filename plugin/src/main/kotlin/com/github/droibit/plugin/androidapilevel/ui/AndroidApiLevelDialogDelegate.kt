@file:JvmName("AndroidApiLevelDialogDelegate")

package com.github.droibit.plugin.androidapilevel.ui

import com.github.droibit.plugin.androidapilevel.model.AndroidApi
import com.github.droibit.plugin.androidapilevel.model.AndroidApiReader
import com.github.droibit.plugin.androidapilevel.model.AndroidApiReader.jsonFile
import com.github.droibit.plugin.androidapilevel.model.AndroidApiReader.readFromJson
import com.github.droibit.plugin.androidapilevel.util.stringBundle
import com.intellij.openapi.diagnostic.Logger
import java.awt.Component
import java.awt.Cursor
import java.awt.Cursor.HAND_CURSOR
import java.awt.Desktop
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URL
import javax.swing.JLabel
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel

private const val COLUMN_PLATFORM_VERSION = 2

private val TABLE_HEADERS = arrayOf(
        stringBundle.headerName to 150,
        stringBundle.headerLevel to 50,
        stringBundle.headerPlatformVersion to 175,
        stringBundle.headerVersionCode to 200
)
private val logger = Logger.getInstance(AndroidApiLevelDialog::class.java.simpleName)

/**
 * @author kumagai
 */
private class ApiTableModel(columnNames: Array<String>, columnCount: Int = 0)
    : DefaultTableModel(columnNames, columnCount) {

    override fun isCellEditable(row: Int, column: Int) = false
}

/**
 * @author kumagai
 */
private class LinkableLabelCellRenderer(private val apis: Array<AndroidApi>)
    : DefaultTableCellRenderer() {

    override fun getTableCellRendererComponent(table: JTable, value: Any?, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component {
        val label = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column) as JLabel
        return label.apply {
            apis[row].link?.let {
                text = linkTextHtml(text)
            }
            //cursor = Cursor.getPredefinedCursor(HAND_CURSOR)
        }
    }
}

fun init(dialog: AndroidApiLevelDialog) {
    dialog.apply {
        initAppearance()
        initFooter()
        initTable()
    }
}

private fun AndroidApiLevelDialog.initAppearance() {
    setSize(600, 550)
    title = stringBundle.titleAndroidApiLevelDialog
    isModal = true
}

private fun AndroidApiLevelDialog.initFooter() {
    val url = URL(labelFooter.text)
    labelFooter.apply {
        cursor = Cursor.getPredefinedCursor(HAND_CURSOR)
        text = "${linkTextHtml(before = "From: ", text = url)}"
    }
    labelFooter.onMouseClicked {
        open(url).withError { notifyError(stringBundle.errorLaunchBrowser) }
    }
}

private fun AndroidApiLevelDialog.initTable() {
    AndroidApiReader.logger = logger

    val jsonFile = jsonFile(stringBundle.jsonPathAndroidApi)
    val androidApis = checkNotNull(readFromJson(jsonFile)) {
        notifyError(stringBundle.errorJsonParse)
        return
    }

    val headers = TABLE_HEADERS.map { it.first }.toTypedArray()
    val tableModel = ApiTableModel(columnNames = headers).apply {
        for (api in androidApis) {
            addRow(api.toArray())
        }
    }

    apiTable.apply {
        model = tableModel
        setShowGrid(false)

        for (i in TABLE_HEADERS.indices) {
            val (name, width) = TABLE_HEADERS[i]
            getColumn(name).apply {
                if (COLUMN_PLATFORM_VERSION == i) {
                    cellRenderer = LinkableLabelCellRenderer(androidApis.raw)
                }
                preferredWidth = width
            }
        }
    }
    apiTable.onMouseClicked { e ->
        if (e.clickCount < 2) {
            return@onMouseClicked
        }
        val table = e.source as JTable
        if (COLUMN_PLATFORM_VERSION != table.selectedColumn) {
            return@onMouseClicked
        }
        val api = androidApis[table.selectedRow]
        api.link?.let {
            open(URL(it)).withError { notifyError(stringBundle.errorLaunchBrowser) }
        }
    }
}

private inline fun Component.onMouseClicked(crossinline action: (e: MouseEvent)->Unit) {
    addMouseListener(object: MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) { action(e) }
    })
}

private fun AndroidApi.toArray() = arrayOf(
        name,
        apiLevel.toString(),
        platformVersion,
        versionCode
)

private inline fun Boolean.withError(handler: ()->Unit) {
    if (!this) {
        handler()
    }
}

private fun open(url: URL): Boolean {
    if (!Desktop.isDesktopSupported()) {
        return false
    }
    return try {
        Desktop.getDesktop().browse(url.toURI())
        true
    } catch (e: Exception) {
        logger.error(e)
        false
    }
}

private fun linkTextHtml(text: Any, before: String="", after: String="") = "<html>$before<font><u>$text</u></font>$after</html>"