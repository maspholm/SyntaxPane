package com.syntax.manage;

import javax.swing.text.AttributeSet;

import com.syntax.code.StyledTextBody;

/**
 * This abstact class support APIs to design personal syntax attribute of 
 * SyntaxTextArea. There are three abstract methods needed to be implemented.
 * For more details, see methods' descriptions.
 * 
 * @see com.syntax.ui.SyntaxTextArea
 */
public interface AbstractCodeAdapter {
    /**
     * SyntaxTextArea get {@link AttributeSet attribute} from this method to colour text
     * before it call {@link #insertString(int, String, StyledTextBody, SyntaxPainter) insertString}
     * 
     * @return the default attribute of paragraph
     */
    public AttributeSet getDefaultAttributeSet();
    /**
     * When text is inserted into SyntaxTextArea, it will send notification
     * through this method
     * 
     * @param offset the index where the text was inserted. If the location
     * is the head of paragraph, offset will be 0. If the location is the end
     * of paragraph, offset will be the length of paragraph before text inserted
     * @param text the specified string which is instered to paragraph
     * @param textBody entire paragraph after text has been inserted
     * @param painter color syntax through painter
     * 
     * @see com.syntax.ui.SyntaxTextArea
     */
    public void insertString(int offset, String text, StyledTextBody textBody, SyntaxPainter painter);
    /**
     * When text is removed from {@link com.syntax.ui.SyntaxTextArea SyntaxTextArea}, it will send notification
     * through this method
     * 
     * @param offset the index where the text was removed. If the first
     * character at the head of paragraph, offset will be 0
     * @param text the text which is removed from paragraph
     * @param textBody entire paragraph after text has been removed
     * @param painter color syntax through painter
     */
    public void remove(int offset, String text, StyledTextBody textBody, SyntaxPainter painter);
}