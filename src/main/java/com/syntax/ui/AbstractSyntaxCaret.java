package com.syntax.ui;

import java.awt.Color;

import javax.swing.text.DefaultCaret;
import javax.swing.text.Highlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

import com.syntax.manage.SyntaxManager;
import com.syntax.manage.SyntaxHighlighter.SyntaxHighlightePainter;
/**
 * If {@link com.syntax.ui.SyntaxTextArea#setSyntaxFont(java.awt.Font) setSyntaxFont(java.awt.Font)} is called, it
 * automatically call {@link #setCaretHeight(int) setCaretHeight(int)}, the only abstract method in this class.
 * Implement {@link #setCaretHeight(int) setCaretHeight(int)} to support showing sutiable Caret on the
 * {@link SyntaxTextArea SyntaxTextArea}
 */
public abstract class AbstractSyntaxCaret extends DefaultCaret {
    private static final long serialVersionUID = 0;
	private static final NoHighlightPainter mNoHighlightPainter = new NoHighlightPainter();
	private boolean selectionEnable;
	private SyntaxHighlightePainter mSyntaxHighlightPainter;
	/**
	 * Construct AbstractSyntaxCaret with specified {@link com.syntax.manage.SyntaxManager SyntaxManager}
	 * 
	 * @param mSyntaxManager specified SyntaxManager which provide color, font and attribute set to
	 * build AbstractSyntaxCaret
	 */
	public AbstractSyntaxCaret(SyntaxManager mSyntaxManager) {
		mSyntaxHighlightPainter = new SyntaxHighlightePainter(mSyntaxManager.getHighlightColor());
		selectionEnable = true;
	}
	/**
	 * Get selection painter which is used to paint selection highlights
	 * 
	 * @return selection painter
	 */
	@Override
	protected Highlighter.HighlightPainter getSelectionPainter() {
		if(selectionEnable)
        	return mSyntaxHighlightPainter;
		else
			return mNoHighlightPainter;
    }
	/**
	 * Set selection highlighting visible
	 * 
	 * @param selectionEnable true if highlighting visible
	 */
	public void setSelectionEnable(boolean selectionEnable) {
		this.selectionEnable = selectionEnable;
	}
	/**
	 * Get visibility of selection highlighting
	 * 
	 * @return true if highlighting visible
	 */
	public boolean isSelectionEnable() {
		return selectionEnable;
	}
    /**
	 * Set the height of caret
	 * 
	 * @param mHeight height of caret
	 */
	public abstract void setCaretHeight(int mHeight);
	/**
	 * Representation of disable highlighting
	 */
	public static class NoHighlightPainter extends DefaultHighlightPainter {
		/**
		 * Construct NoHighlightPainter
		 */
		public NoHighlightPainter() {
			super(Color.BLACK);
		}
	}
}