package com.syntax.manage;

import java.awt.Rectangle;

import javax.swing.text.JTextComponent;

import com.syntax.code.TextBody;

/**
 * Listening for caret updating in SyntaxTextArea
 * 
 * @see com.syntax.ui.SyntaxTextArea
 */
public interface SyntaxCaretListener {
    /**
     * When caret of SyntaxTextArea updats, this method is used as callback method
     * 
     * @param component text component of SyntaxTextArea. Use this component to locate caret position in coordinate
     * @param r representing shape of caret on SyntaxTextArea
     * @param position caret position in SyntaxTextArea
     * @param textBody entire paragraph of SyntaxTextArea
     * @param highlightTool support highlight text on SyntaxTextArea
     */
    public void caretUpdate(JTextComponent component, Rectangle r, int position, TextBody textBody, SyntaxHighlightingTool highlightTool);
}