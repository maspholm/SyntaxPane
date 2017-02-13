package com.syntax.code;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 * The content of StyledTextBody is synchronized with SyntaxTextArea.
 * Do not use {@link TextBody#insertText(int,String) insertText(int,string)}
 * or {@link TextBody#removeText(int,int) removeText(int,int)} to update text in SyntaxTextArea.
 * In order to avoid recursive function call, use
 * {@link #insertStyledText(int, String, AttributeSet) insertStyledText(int, String, AttributeSet)} and
 * {@link #removeStyledText(int, int) removeStyledText(int, int)} to modify text showing in the SyntaxTextArea
 * 
 * @see com.syntax.ui.SyntaxTextArea
 */
public class StyledTextBody extends TextBody {
    private StyledChangeListener callback;
    /**
     * Construct empty StyledTextBody
     */
    public StyledTextBody() {
        this(null);
    }
    /**
     * Construct empty StyledTextBody and set {@link StyledChangeListener StyledChangeListener}
     * 
     * @param callback style change call back
     */
    public StyledTextBody(StyledChangeListener callback) {
        super();
        this.callback = callback;
    }
    /**
     * Insert text to StyledTextBody. The content of StyledTextBody is synchronized with SyntaxTextArea
     * @param start the index where the text was inserted. If the location
     * is the head of paragraph, offset will should be 0. If the location is in the end
     * of paragraph, offset should be the length of paragraph
     * @param text the specified string which is instered to paragraph
     * @param attributeSet the attribute of text
     * 
     * @throws BadLocationException if the start out of range
     * @see com.syntax.ui.SyntaxTextArea
     */
    public synchronized void insertStyledText(int start, String text, AttributeSet attributeSet) throws BadLocationException {
        super.insertText(start, text);
        if(callback != null)
            callback.insertStyledText(start, text, attributeSet);
    }
    /**
     * Remove text on SyntaxTextArea. The content of StyledTextBody is synchronized with SyntaxTextArea
     * @param start the index where the text was removed. If the first character
     * of text is removed from the head of paragraph, offset should be 0
     * @param length the length of string which is removed from paragraph
     * 
     * @throws BadLocationException if the start or length out of range
     * @see com.syntax.ui.SyntaxTextArea
     */
    public synchronized void removeStyledText(int start, int length) throws BadLocationException {
        super.removeText(start, length);
        if(callback != null)
            callback.removeStyledText(start, length);
    }
    /**
     * Regist StyledChangeListener to StyledTextBody
     * 
     * @param callback specified callback
     * 
     * @see StyledChangeListener
     */
    public void setStyledChangeListener(StyledChangeListener callback) {
        this.callback = callback;
    }
    /**
     * Listen for text changing in StyledTextBody
     * 
     * @see StyledTextBody
     */
    public static interface StyledChangeListener {
        /**
         * Listeneing for text {@link StyledTextBody#insertStyledText(int,String,AttributeSet) inserted} to StyledTextBody
         * 
         * @param start the index where the text was inserted. If the location
         * is the head of paragraph, offset will should be 0. If the location is in the end
         * of paragraph, offset should be the length of paragraph
         * @param text the specified string which is instered to paragraph
         * @param attributeSet the attribute of text
         * 
         * @throws BadLocationException if the start out of range
         */
        public void insertStyledText(int start, String text, AttributeSet attributeSet) throws BadLocationException;
        /**
         * Listeneing for text {@link StyledTextBody#removeStyledText(int,int) removed} from StyledTextBody
         * 
         * @param start the index where the text was removed. If the first character
         * of text is removed from the head of paragraph, offset should be 0
         * @param length the length of string which is removed from paragraph
         * 
         * @throws BadLocationException if the start or length out of range
         */
        public void removeStyledText(int start, int length) throws BadLocationException;
    }
}