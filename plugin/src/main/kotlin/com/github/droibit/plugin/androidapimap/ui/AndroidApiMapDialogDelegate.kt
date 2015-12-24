package com.github.droibit.plugin.androidapimap.ui

import com.github.droibit.plugin.androidapimap.model.ANDROID_API_JSON_PATH
import com.github.droibit.plugin.androidapimap.model.AndroidApi
import com.github.droibit.plugin.androidapimap.model.AndroidApiReader
import com.github.droibit.plugin.androidapimap.model.AndroidApiReader.jsonFile
import com.github.droibit.plugin.androidapimap.model.AndroidApiReader.readFromJson
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

private const val ERROR_JSON_PARSE = "Failed to read Anroid API data."
private const val ERROR_LAUNCH_BROWSER = "Failed to launch browser."

private val TABLE_HEADERS = arrayOf(
        Pair("Name", 150),
        Pair("Level", 50),
        Pair("Platform Version", 175),
        Pair("VERSION_CODE", 200)
)
private val LOGGER = Logger.getInstance(AndroidApiMapDialog::class.java.simpleName)

/**
 * @author kumagai
 */
class AndroidApiMapDialogDelegate(private val dialog: AndroidApiMapDialog) {

    fun init() {
        dialog.apply {
            initButtonOK()
            initFooter()
            initTable()
        }
    }

    private fun AndroidApiMapDialog.initButtonOK() {
        buttonOK.addActionListener { dispose() }
        buttonOK.requestFocus()
    }

    private fun AndroidApiMapDialog.initFooter() {
        val url = URL(labelFooter.text)
        labelFooter.apply {
            cursor = Cursor.getPredefinedCursor(HAND_CURSOR)
            text = "${linkTextHtml(before="From: ",text=url)}"
        }
        labelFooter.onMouseClicked {
            // FIXME:
            open(url).withError { notifyError(ERROR_LAUNCH_BROWSER) }
        }
    }

    private fun AndroidApiMapDialog.initTable() {
        AndroidApiReader.logger = LOGGER

        val jsonFile = jsonFile(ANDROID_API_JSON_PATH)
        val androidApis = checkNotNull(readFromJson(jsonFile)) {
            notifyError(ERROR_JSON_PARSE)
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
                open(URL(it)).withError { notifyError(ERROR_LAUNCH_BROWSER) }
            }
        }
    }
}

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
    if (this) {
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
        LOGGER.error(e)
        false
    }
}

private fun linkTextHtml(text: Any, before: String="", after: String="") = "<html>$before<font><u>$text</u></font>$after</html>"