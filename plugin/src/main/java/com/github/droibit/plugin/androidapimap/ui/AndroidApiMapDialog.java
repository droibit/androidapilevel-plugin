package com.github.droibit.plugin.androidapimap.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.event.KeyEvent.VK_ESCAPE;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

public class AndroidApiMapDialog extends JDialog implements ActionListener {

    private JPanel contentPane;
    private JButton buttonOK;
    private JTable apiTable;

    private JLabel labelFooter;

    private final AndroidApiMapDialogDelegate delegate;

    public AndroidApiMapDialog() {
        setSize(600, 550);

        setContentPane(contentPane);

        setModal(true);
        setLocationRelativeTo(null);

        setTitle("Android API Level");

        getRootPane().setDefaultButton(buttonOK);
        getRootPane().registerKeyboardAction(
                        this,
                        KeyStroke.getKeyStroke(VK_ESCAPE, 0),
                        WHEN_IN_FOCUSED_WINDOW);

        delegate = new AndroidApiMapDialogDelegate(this);
        delegate.init();
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }

    JButton getButtonOK() {
        return buttonOK;
    }

    JTable getApiTable() {
        return apiTable;
    }

    JLabel getLabelFooter() {
        return labelFooter;
    }
}
