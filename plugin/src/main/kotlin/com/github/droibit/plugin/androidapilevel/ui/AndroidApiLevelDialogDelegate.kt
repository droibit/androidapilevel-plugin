@file:JvmName("AndroidApiLevelDialogDelegate")

package com.github.droibit.plugin.androidapilevel.ui

import com.github.droibit.plugin.androidapilevel.model.AndroidApi
import com.github.droibit.plugin.androidapilevel.model.AndroidApiReader
import com.github.droibit.plugin.androidapilevel.util.stringBundle
import com.intellij.openapi.diagnostic.Logger
import java.awt.Component
import java.awt.Cursor
import java.awt.Cursor.HAND_CURSOR
import java.awt.Desktop
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URL
import javax.swing.JLabel
import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel

private const val COLUMN_PLATFORM_VERSION = 2

private val TABLE_HEADERS_WITH_WIDTH = arrayOf(
        stringBundle.headerName to 160,
        stringBundle.headerLevel to 50,
        stringBundle.headerPlatformVersion to 220,
        stringBundle.headerVersionCode to 225
)
private val logger = Logger.getInstance(AndroidApiLevelDialog::class.java.simpleName)

private class ApiTableModel(columnNames: Array<String>, columnCount: Int = 0)
    : DefaultTableModel(columnNames, columnCount) {

    override fun isCellEditable(row: Int, column: Int) = false
}

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
    title = stringBundle.titleAndroidApiLevelDialog
    isModal = true
}

private fun AndroidApiLevelDialog.initFooter() {
    val url = labelFooter.text
    labelFooter.apply {
        cursor = Cursor.getPredefinedCursor(HAND_CURSOR)
        text = linkTextHtml(before = "From: ", text = url)
    }
    labelFooter.onMouseClicked {
        open(url).withError { notifyError(stringBundle.errorLaunchBrowser) }
    }
}

private fun AndroidApiLevelDialog.initTable() {
    AndroidApiReader.logger = logger

    val androidApis = checkNotNull(AndroidApiReader.readFromJson(stringBundle.jsonPathAndroidApi)) {
        notifyError(stringBundle.errorJsonParse)
        return
    }

    val headers = TABLE_HEADERS_WITH_WIDTH.map { it.first }.toTypedArray()
    val tableModel = ApiTableModel(columnNames = headers).apply {
        for (api in androidApis) {
            addRow(api.toArray())
        }
    }

    apiTable.apply {
        model = tableModel
        setShowGrid(false)

        for (i in TABLE_HEADERS_WITH_WIDTH.indices) {
            val (name, width) = TABLE_HEADERS_WITH_WIDTH[i]
            getColumn(name).apply {
                if (COLUMN_PLATFORM_VERSION == i) {
                    cellRenderer = LinkableLabelCellRenderer(androidApis.raw)
                }
                preferredWidth = width
            }
        }
    }
    apiTable.onMouseClicked { e ->
        if (!e.isDoubleClicked()) {
            return@onMouseClicked
        }
        val table = e.source as JTable
        if (!table.selectedColumn.isPlatformVersion()) {
            return@onMouseClicked
        }
        val api = androidApis[table.selectedRow]
        api.link?.let {
            open(it).withError { notifyError(stringBundle.errorLaunchBrowser) }
        }
    }
}

private inline fun Component.onMouseClicked(crossinline action: (e: MouseEvent) -> Unit) {
    addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) = action(e)
    })
}

private fun AndroidApi.toArray() = arrayOf(
        name,
        apiLevel.toString(),
        platformVersion,
        versionCode
)

private inline fun Boolean.withError(handler: () -> Unit) {
    if (!this) {
        handler()
    }
}

private fun open(urlString: String): Boolean {
    if (!Desktop.isDesktopSupported()) {
        return false
    }
    return try {
        Desktop.getDesktop().browse(URL(urlString).toURI())
        true
    } catch (e: Exception) {
        logger.error(e)
        false
    }
}

private fun MouseEvent.isDoubleClicked() = clickCount >= 2

private fun Int.isPlatformVersion() = this == COLUMN_PLATFORM_VERSION

private fun linkTextHtml(text: Any, before: String = "", after: String = "") = "<html>$before<font><u>$text</u></font>$after</html>"