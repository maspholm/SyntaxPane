package com.syntax.manage.shortcut;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.syntax.manage.AbstractKeyBoardShortCut;
import com.syntax.ui.SyntaxTextArea;

/**
 * Make {@link com.syntax.ui.SyntaxTextArea SyntaxTextArea} reverse to previous state
 */
public class CtrlZ extends AbstractKeyBoardShortCut {
    private static final long serialVersionUID = 0;
    public CtrlZ() {
        super(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK, false));
    }
    public void ShortCutPerformed(SyntaxTextArea textArea) {
        textArea.getStyledTextBody().reverse();
    }
}