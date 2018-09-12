package com.xl.xzw;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

public class SortCode extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        com.intellij.openapi.ui.Messages.showMessageDialog("conteng","title",Messages.getInformationIcon());
    }
}
