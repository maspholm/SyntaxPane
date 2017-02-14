package com.syntax.manage.shortcut;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.syntax.manage.AbstractKeyBoardShortCut;
import com.syntax.ui.SyntaxTextArea;



public class ShiftTab extends AbstractKeyBoardShortCut {
    private static final long serialVersionUID = 0;
    public ShiftTab() {
        super(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK, false));
    }
    public void ShortCutPerformed(SyntaxTextArea textArea) {
        //TODO
    }
}