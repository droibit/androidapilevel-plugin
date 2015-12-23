package com.github.droibit.plugin.androidapimap.ui

import com.github.droibit.plugin.androidapimap.model.ANDROID_API_JSON_PATH
import com.github.droibit.plugin.androidapimap.model.AndroidApi
import com.github.droibit.plugin.androidapimap.model.AndroidApiReader
import com.github.droibit.plugin.androidapimap.model.AndroidApiReader.jsonFile
import com.github.droibit.plugin.androidapimap.model.AndroidApiReader.readFromJson
import com.intellij.openapi.diagnostic.Logger
import java.awt.Component
import java.awt.Cursor
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
        Pair("Name", 150),
        Pair("Level", 50),
        Pair("Platform Version", 175),
        Pair("Version Code", 200)
)
private val LOGGER = Logger.getInstance(AndroidApiMapDialog::class.java.simpleName)

/**
 * @author kumagai
 */
class AndroidApiMapDialogDelegate(private val dialog: AndroidApiMapDialog) {

    init {
        dialog.apply {
            initButtonOK()
            initFooter()
            initTable()
        }
    }

    private fun AndroidApiMapDialog.initButtonOK() {
        buttonOK.addActionListener { dispose() }
    }

    private fun AndroidApiMapDialog.initFooter() {
        val url = URL(labelFooter.text)
        labelFooter.apply {
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            text = "${underlineTextHtml(url)}"
        }
        labelFooter.onMouseClicked {
            open(url).withError {
                // TODO: show notification
            }
        }
    }

    private fun AndroidApiMapDialog.initTable() {
        AndroidApiReader.logger = LOGGER

        val jsonFile = jsonFile(ANDROID_API_JSON_PATH)
        val androidApis = checkNotNull(readFromJson(jsonFile))

        val headers = TABLE_HEADERS.map { it.first }.toTypedArray()
        val tableModel = ApiTableModel(columnNames = headers,
                                       columnCount = androidApis.size).apply {
            for (api in androidApis.items) {
                addRow(api.toArray())
            }
        }

        apiTable.apply {
            setDefaultRenderer(String::class.java, ApiLabelCellRenderer())
            model = tableModel

            for (header in TABLE_HEADERS) {
                val (name, width) = header
                getColumn(name).preferredWidth = width
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
            val api = androidApis.items[table.selectedRow]
            if (api.link != null) {
                open(URL(api.link)).withError {
                    // TODO: 通知の表示
                }
            }
        }

    }
}

/**
 * @author kumagai
 */
private class ApiTableModel(columnNames: Array<String>, columnCount: Int)
        : DefaultTableModel(columnNames, columnCount) {

    override fun isCellEditable(row: Int, column: Int) = false
}

/**
 * @author kumagai
 */
private class ApiLabelCellRenderer : DefaultTableCellRenderer() {

    override fun getTableCellRendererComponent(table: JTable, value: Any, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component {
        val label = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column) as JLabel
        return if (column != COLUMN_PLATFORM_VERSION) label else label.apply {
            if (label.text !in "<html>") {
                label.text = underlineTextHtml(value)
            }
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

private fun open(url: URL): Boolean {
    // TODO: show notification.
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

private inline fun Boolean.withError(handler: ()->Unit) {
    if (this) {
        handler()
    }
}

private fun underlineTextHtml(text: Any) = "<html><u>$text</u></html>"