package tutorials.contextmenu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.JPopupTextMenu
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.platform.LocalLocalization
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.CTRL_DOWN_MASK
import java.awt.event.KeyEvent.META_DOWN_MASK
import javax.swing.Icon
import javax.swing.JFrame
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.KeyStroke.getKeyStroke
import javax.swing.SwingUtilities
import org.jetbrains.skiko.hostOs

fun main() = SwingUtilities.invokeLater {
    val panel = ComposePanel()
    panel.setContent {
        JPopupTextMenuProvider(panel) {
            Column {
                SelectionContainer {
                    Text("Hello, Compose!")
                }

                var text by remember { mutableStateOf("") }

                TextField(text, { text = it })
            }
        }
    }

    val window = JFrame()
    window.contentPane.add(panel)
    window.size = Dimension(800, 600)
    window.isVisible = true
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JPopupTextMenuProvider(owner: Component, content: @Composable () -> Unit) {
    val localization = LocalLocalization.current
    CompositionLocalProvider(
        LocalTextContextMenu provides JPopupTextMenu(owner) { textManager, items ->
            JPopupMenu().apply {
                textManager.cut?.also {
                    add(
                        swingItem(localization.cut, Color.RED, KeyEvent.VK_X, it)
                    )
                }
                textManager.copy?.also {
                    add(
                        swingItem(localization.copy, Color.GREEN, KeyEvent.VK_C, it)
                    )
                }
                textManager.paste?.also {
                    add(
                        swingItem(localization.paste, Color.BLUE, KeyEvent.VK_V, it)
                    )
                }
                textManager.selectAll?.also {
                    add(JPopupMenu.Separator())
                    add(
                        swingItem(localization.selectAll, Color.BLACK, KeyEvent.VK_A, it)
                    )
                }

                // Here we add other items that can be defined additionaly in the other places of the application via ContextMenuDataProvider
                for (item in items) {
                    add(
                        JMenuItem(item.label).apply {
                            addActionListener { item.onClick() }
                        }
                    )
                }
            }
        },
        content = content
    )
}

private fun swingItem(
    label: String,
    color: Color,
    key: Int,
    onClick: () -> Unit
) = JMenuItem(label).apply {
    icon = circleIcon(color)
    accelerator = getKeyStroke(key, if (hostOs.isMacOS) META_DOWN_MASK else CTRL_DOWN_MASK)
    addActionListener { onClick() }
}

private fun circleIcon(color: Color) = object : Icon {
    override fun paintIcon(c: Component?, g: Graphics, x: Int, y: Int) {
        g.create().apply {
            this.color = color
            translate(16, 2)
            fillOval(0, 0, 16, 16)
        }
    }

    override fun getIconWidth() = 16

    override fun getIconHeight() = 16
}