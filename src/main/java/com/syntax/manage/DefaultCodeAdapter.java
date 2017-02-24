package com.syntax.manage;

import javax.swing.text.AttributeSet;

import com.syntax.ui.SyntaxTextArea;

/**
 * A basic implementation of AbstractCodeAdapter. It give all text default attribute provided by SyntaxManager.
 * 
 * @see SyntaxManager
 * @see AbstractCodeAdapter
 */
public class DefaultCodeAdapter implements AbstractCodeAdapter {
    private SyntaxManager mSyntaxManager;
    /**
     * Construct with specified SyntaxManager
     * 
     * @param mSyntaxManager specified SyntaxManager which provide color and attribute to build adapter
     */
    public DefaultCodeAdapter(SyntaxManager mSyntaxManager) {
        this.mSyntaxManager = mSyntaxManager;
    }
    /**
     * Construct with default SyntaxManager
     * 
     * @see SyntaxManager
     */
    public DefaultCodeAdapter() {
        this(new SyntaxManager());
    }
    /**
     * Get default attribute from SyntaxManager specified by 
     * {@link #DefaultCodeAdapter(SyntaxManager) constructor}
     * 
     * @return attribute for SyntaxTextArea
     */
    @Override
    public AttributeSet getDefaultAttributeSet() {
        return mSyntaxManager.getAttributeSet();
    }
    /**
     * Insert string with default attribute from SyntaxManager specified by {@link #DefaultCodeAdapter(SyntaxManager) constructor}
     * 
     * @param offset the index where the text was inserted. If the location
     * is the head of paragraph, offset will be 0. If the location is the end
     * of paragraph, offset will be the length of paragraph before text inserted
     * @param text the specified string which is instered to paragraph
     * @param textArea registered syntax text area
     */
    @Override
    public void insertString(int offset, String text, SyntaxTextArea textArea) {
        textArea.getSyntaxPainter().paintSyntax(offset, text.length(), getDefaultAttributeSet(), true);
    }
    /**
     * Empty method, this do not change any attribute on SyntaxTextArea
     * 
     * @param offset the index where the text was removed. If the first
     * character at the head of paragraph, offset will be 0
     * @param text the text which is removed from paragraph
     * @param textArea registered syntax text area
     */
    @Override
    public void remove(int offset, String text, SyntaxTextArea textArea) {
        // Empty method
    }
    @Override
    public boolean replace(int offset, int length, String text, SyntaxTextArea textArea) {
        return true;
    }
}