package com.syntax.manage;

import java.awt.Color;

public interface SyntaxHighlightingTool {
    /**
     * Highlight text in range of [start, end] with specified color
     * 
     * @param start starting position of highlighting
     * @param end ending position of highlighting
     * @param color highlighting color
     * @return reprsentation of this action which is used to remove highlight
     * 
     * @throws SyntaxException if start &gt; end or at least one of them is out of range in
     * {@link com.syntax.ui.SyntaxTextArea SyntaxTextArea}
     * @see #removeHighlight(com.syntax.manage.SyntaxHighlightingTool.SyntaxHighlight)
     */
    public SyntaxHighlight syntaxHighlight(int start, int end, Color color) throws SyntaxException;
    /**
     * Get all highlights not being removed
     * @return all highlights
     */
    public SyntaxHighlight[] getAllHighlights();
    /**
     * Remove specified highlight from {@link com.syntax.ui.SyntaxTextArea}
     * @param h specified highlight
     */
    public void removeHighlight(SyntaxHighlight h);
    /**
     * Remove all highlights from {@link com.syntax.ui.SyntaxTextArea}
     * 
     * @param removeSelection true to remove selection highlighting, false not to remove selection highlighting
     */
    public void removeAllHighlights(boolean removeSelection);
    /**
     * Store highlights that have been applied on {@link com.syntax.ui.SyntaxTextArea}
     */
    public static interface SyntaxHighlight {
        /**
         * Get start position of this highlight
         * 
         * @return start position
         */
        public int getStart();
        /**
         * Get end position of this highlight
         * 
         * @return end position
         */
        public int getEnd();
        /**
         * Get color of this highlight
         * 
         * @return color of this highlight
         */
        public Color getColor();
        /**
         * Get key which is used to identify distinct highlight
         * 
         * @return key
         */
        public Object getKey();
    }
}
