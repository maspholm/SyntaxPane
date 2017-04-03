package com.syntax.manage;

import javax.swing.text.JTextComponent;

import com.syntax.code.TextBody;

/**
 * Listening for selection updating of {@link com.syntax.ui.SyntaxTextArea SyntaxTextArea}
 */
public interface SyntaxSelectionListener {
    /**
     * When selection area of SyntaxTextArea updats, this method is used as callback method
     * 
     * @param component text component of SyntaxTextArea. Use this component to locate caret position in coordinate
     * @param start start position of selection
     * @param end end position of selection
     * @param textBody entire paragraph of SyntaxTextArea
     * @param highlightTool support highlight text on SyntaxTextArea
     */
    public void selectionChange(JTextComponent component, int start, int end, TextBody textBody, SyntaxHighlightingTool highlightTool);
    public void selectionRemove(JTextComponent component, TextBody textBody, SyntaxHighlightingTool highlightTool);
}