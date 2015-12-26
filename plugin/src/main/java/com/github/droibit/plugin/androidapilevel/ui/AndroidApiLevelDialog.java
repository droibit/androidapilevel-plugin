package com.github.droibit.plugin.androidapilevel.ui;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * [DialogWrapper](http://www.jetbrains.org/intellij/sdk/docs/user_interface_components/dialog_wrapper.html)
 */
public class AndroidApiLevelDialog extends DialogWrapper {

    private JPanel contentPane;
    private JTable apiTable;
    private JLabel labelFooter;

    public AndroidApiLevelDialog() {
        super(true);

        AndroidApiLevelDialogDelegate.init(this);

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
