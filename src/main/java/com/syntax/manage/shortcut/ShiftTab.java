package com.syntax.manage.shortcut;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.syntax.code.StyledTextBody;


import com.syntax.manage.AbstractKeyBoardShortCut;
import com.syntax.manage.SyntaxDocumentTool;
import com.syntax.manage.SyntaxException;
import com.syntax.ui.SyntaxTextArea;

/**
 * Provide deleting multiline prefix tab
 */
public class ShiftTab extends AbstractKeyBoardShortCut {
    private static final long serialVersionUID = 0;
    public ShiftTab() {
        super(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK, false));
    }
    public void ShortCutPerformed(SyntaxTextArea textArea) {
        try {
            SyntaxDocumentTool docTool = textArea.getSyntaxDocumentTool();
            int sStart = textArea.getSelectionStart();
            int sEnd = textArea.getSelectionEnd();
            int begin = docTool.lineBegin(sStart);
            int end = docTool.lineEnd(sEnd);
            StyledTextBody textBody = textArea.getStyledTextBody();
            textBody.startForcedMerge();
            while( true ) {
                int numRemoved = docTool.removeFrontTab(begin);
                if(numRemoved != -1)
                    end -= numRemoved;
                begin = docTool.lineEnd(begin) + 2;
                if(begin > end)
                    break;
            }
            textBody.finishForcedMerge();
        } catch(SyntaxException e) {
            e.printStackTrace();
        }
    }
}