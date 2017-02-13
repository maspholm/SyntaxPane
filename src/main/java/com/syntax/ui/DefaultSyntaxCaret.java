package com.syntax.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.text.BadLocationException;

import com.syntax.manage.SyntaxManager;
/**
 * DefaultSyntaxCaret supports automatically changing the height of caret when font is changed
 */
public class DefaultSyntaxCaret extends AbstractSyntaxCaret {
	private static final long serialVersionUID = 0;

	private int mWidth;
	private int mHeight;
	private Color color;
	/**
	 * Construct with specified {@link SyntaxManager SyntaxManager}
	 * 
	 * @param mSyntaxManager specified SyntaxManager which provide color, font and attribute set to
	 * build AbstractSyntaxCaret
	 */
	public DefaultSyntaxCaret(SyntaxManager mSyntaxManager) {
		super(mSyntaxManager);
		setBlinkRate(500);
		setCaretColor(mSyntaxManager.getCaretColor());
		setCaretWidth(3);
		setCaretHeight(16);
	}
	/**
	 * Modify height of caret
	 * 
	 * @param mHeight hight of caret
	 */
	@Override
	public synchronized void setCaretHeight(int mHeight) {
		this.mHeight = mHeight;
	}
	/**
	 * Set the width of caret
	 * 
	 * @param mWidth witdh of caret
	 */
	public synchronized void setCaretWidth(int mWidth) {
		this.mWidth = mWidth;
	}
	/**
	 * Set the color of caret
	 * 
	 * @param color specified color
	 */
	public synchronized void setCaretColor(Color color) {
		this.color = color;
	}
	/**
	 * Get height of caret
	 * 
	 * @return height of caret
	 */
	public synchronized int getCaretHeight() {
		return mHeight;
	}
	/**
	 * Get width of caret
	 * 
	 * @return width of caret
	 */
	public synchronized int getCaretWidth() {
		return mWidth;
	}
	/**
	 * Get color of caret
	 * 
	 * @return color of caret
	 */
	public synchronized Color getCaretColor() {
		return color;
	}
	/**
	 * Re implement damage
	 * 
	 * @param r now caret shape
	 * @see javax.swing.text.DefaultCaret
	 */
	@Override
	protected synchronized void damage(Rectangle r) {
		x = r.x;
		y = r.y;
		width = getCaretWidth();
        height = getCaretHeight();
		repaint();
	}
	/**
	 * Re implement paint
	 * 
	 * Renders the caret as a vertical rectangle. If this is reimplemented the
	 * damage method should also be reimplemented as it assumes the shape of
	 * the caret is a vertical line.
	 * 
	 * @param g the graphics context
	 */
	@Override
	public void paint(Graphics g) {
		Rectangle r = null;
		try {
			r = getComponent().modelToView(getDot());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		if (isVisible()) {
			g.setColor(getCaretColor());
            g.fillRect(r.x, r.y, getCaretWidth(), getCaretHeight());
		}
	}
}