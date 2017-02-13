package com.syntax.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.syntax.manage.SyntaxManager;

/**
 * Derived class that draw horizontal scroll bar UI with specified color.
 * The thumb color and track color are not allowed to be dynamically changed.
 */
public class SyntaxVerticalScrollbarUI extends BasicScrollBarUI {
    private SyntaxManager mSyntaxManager;
    /**
     * Construct scrollBarUI with specified SyntaxManager which provide thumb color and track color
     * 
     * @param mSyntaxManager specified SyntaxManager
     */
    public SyntaxVerticalScrollbarUI(SyntaxManager mSyntaxManager) {
        this.mSyntaxManager = mSyntaxManager;
    }
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(mSyntaxManager.getAreaColor());
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        g.setColor(mSyntaxManager.getThumbColor());
        g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
    }
    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroJButton();
    }
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroJButton();
    }
    /**
     * Create zero area button to simulate that button is removed
     * 
     * @return zero size button
     */
    protected JButton createZeroJButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
}