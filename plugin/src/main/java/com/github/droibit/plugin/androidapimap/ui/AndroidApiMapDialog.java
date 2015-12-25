package com.github.droibit.plugin.androidapimap.ui;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * [DialogWrapper](http://www.jetbrains.org/intellij/sdk/docs/user_interface_components/dialog_wrapper.html)
 */
public class AndroidApiMapDialog extends DialogWrapper {

    private JPanel contentPane;
    private JTable apiTable;
    private JLabel labelFooter;

    public AndroidApiMapDialog() {
        super(true);

        AndroidApiMapDialogDelegate.init(this);

        init();
    }

    JTable getApiTable() {
        return apiTable;
    }

    JLabel getLabelFooter() {
        return labelFooter;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }
}
