package com.syntax.manage;

import javax.swing.text.AttributeSet;
import javax.swing.text.StyledDocument;
/**
 * A painter that can mark up registered {@link com.syntax.ui.SyntaxTextArea SyntaxTextArea}
 */
public class SyntaxPainter {
    private StyledDocument mStyledDocument;
    /**
     * Construct and register with specified {@link StyledDocument StyledDocument}
     * 
     * @param mStyledDocument specified SyntaxManager which provide color to build painter
     */
    public SyntaxPainter(StyledDocument mStyledDocument) {
        this.mStyledDocument = mStyledDocument;
    }
    /**
     * Changes the content element attributes used for the given range of
     * existing content in the document. This method can be used to completely
     * remove all content level attributes for the given range by giving an
     * Attributes argument that has no attributes defined and setting replace to true.
     * @param start the start of the change &ge; 0
     * @param length the length of the change &ge; 0
     * @param attributeSet the non-null attributes to change to. Any attributes
     * defined will be applied to the text for the given range.
     * @param replace indicates whether or not the previous attributes should be
     * cleared before the new attributes as set. If true, the operation will replace
     * the previous attributes entirely. If false, the new attributes will be merged
     * with the previous attributes.
     */
    public void paintSyntax(int start, int length, AttributeSet attributeSet, boolean replace) {
        mStyledDocument.setCharacterAttributes(start, length, attributeSet, replace);
    }
}