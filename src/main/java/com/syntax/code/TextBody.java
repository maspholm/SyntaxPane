package com.syntax.code;

public class TextBody {
    private StringBuilder text;
    /**
     * Construct empty {@link TextBody}.
     */
    public TextBody() {
        this("");
    }
    /**
     * Construct TextBody with initial content
     * 
     * @param initialStr initial content
     */
    public TextBody(String initialStr) {
        text = new StringBuilder(initialStr);
    }
    /**
     * Get entire paragraph
     * 
     * @return entire paragraph
     */
    public synchronized String getText() {
        return text.toString();
    }
    /**
     * Get the length of paragraph
     * 
     * @return length of paragraph
     */
    public synchronized int length() {
        return text.length();
    }
    /**
     * Insert text to specified position in TextBody
     * 
     * @param start the index where the text was inserted. If the location
     * is at the head of paragraph, offset will should be 0. If the location
     * is at the tail of paragraph, offset should be the length of paragraph
     * @param changeStr the specified string which is instered to paragraph
     */
    public synchronized void insertText(int start, String changeStr) {
        text.insert(start, changeStr);
    }
    /**
     * Remove text from TextBody
     * 
     * @param start the index where the text was removed. If the first character
     * of text is removed at the head of paragraph, offset should be 0
     * @param length the length of string which is removed from TextBody
     */
    public synchronized void removeText(int start, int length) {
        text.delete(start, start + length);
    }
    /**
     * Same as getText()
     * 
     * @return entire paragraph
     */
    @Override
    public String toString() {
        return text.toString();
    }
}