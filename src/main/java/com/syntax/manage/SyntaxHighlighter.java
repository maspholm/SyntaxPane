package com.syntax.manage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;

import com.syntax.ui.SyntaxTextArea;
import com.syntax.ui.AbstractSyntaxCaret.NoHighlightPainter;
/**
 * Implement SyntaxHighlightingTool to support highlighting on SyntaxTextArea
 * 
 * @see SyntaxHighlightingTool
 * @see SyntaxTextArea
 */

public class SyntaxHighlighter extends DefaultHighlighter implements SyntaxHighlightingTool {
    private JTextComponent mTextComponent;
    private SyntaxHighlightePainter mPainter;
    private ArrayList<SyntaxHighlight> highlights;
    /**
     * Construct with default SyntaxManager
     * 
     * @see SyntaxManager
     */
    public SyntaxHighlighter() {
        this(new SyntaxManager());
    }
    /**
     * Construct with specified SyntaxManager
     * 
     * @param mSyntaxManager specified SyntaxManager which provide color to highlight
     * @see SyntaxManager
     */
    public SyntaxHighlighter(SyntaxManager mSyntaxManager) {
        mPainter = new SyntaxHighlightePainter(mSyntaxManager.getHighlightColor());
        highlights = new ArrayList<>();
    }
    /**
     * Highligt specified rang in SyntaxTextArea with specified painter
     * 
     * @param p0 starting position of highlighting
     * @param p1 ending position of highlighting
     * @param p specified painter
     * @return {@link Highlight} of this action
     * 
     * @throws BadLocationException if p0 &gt; p1 or at least one of them is out of range in SyntaxTextArea
     */
    @Override
    public Object addHighlight(int p0, int p1, javax.swing.text.Highlighter.HighlightPainter p) throws BadLocationException {
        System.out.println("addHighlight");
        if(p instanceof NoHighlightPainter)
            return p;
        else
            return super.addHighlight(p0, p1, p);
    }
    /**
     * Change Highlight on SyntaxTextArea
     * 
     * @param tag return from {@link #addHighlight(int,int,javax.swing.text.Highlighter.HighlightPainter) addHighlight()}
     * @param p0 start position after changed
     * @param p1 end position after changed
     * 
     * @throws BadLocationException if p0 &gt; p1 or at least one of them is out of range in SyntaxTextArea
     */
    @Override
    public void changeHighlight(Object tag, int p0, int p1) throws BadLocationException {
        System.out.println("changeHighlight");
        if(tag instanceof NoHighlightPainter)
            return;
        else
            super.changeHighlight(tag, p0, p1);
    }
    /**
     * Remove highlight from SyntaxTextArea
     * 
     * @param tag return from {@link #addHighlight(int,int,javax.swing.text.Highlighter.HighlightPainter) addHighlight()}
     */
    @Override
    public void removeHighlight(Object tag) {
        System.out.println("removeHighlight");
        if(tag instanceof NoHighlightPainter)
            return;
        else
            super.removeHighlight(tag);
    }
    /**
     * This method call automatically by JTextComponent
     * 
     * @param mTextComponent install to that JTextComponent
     * @see javax.swing.text.JTextComponent
     */
    @Override
    public void install(JTextComponent mTextComponent) {
        super.install(mTextComponent);
        this.mTextComponent = mTextComponent;
    }
    /**
     * This method call automatically by JTextComponent
     * 
     * @param mTextComponent deinstall from that JTextComponent
     * @see javax.swing.text.JTextComponent
     */
    @Override
    public void deinstall(JTextComponent mTextComponent) {
        super.deinstall(mTextComponent);
        this.mTextComponent = null;
    }
    /**
     * Highlight text in range of [start, end] with specified color
     * 
     * @param start starting position of highlighting
     * @param end ending position of highlighting
     * @param color highlighting color
     * @return reprsentation of this action which is used to remove highlight
     * 
     * @see #removeHighlight(com.syntax.manage.SyntaxHighlightingTool.SyntaxHighlight)
     */
    @Override
    public SyntaxHighlight syntaxHighlight(int start, int end, Color color) throws SyntaxException {
        if(mTextComponent == null)
            throw new SyntaxException("SyntaxHighlighter is not installed");
        mPainter.setColor(color);
        Object key = null;
        try {
            key = super.addHighlight(start, end, new DefaultHighlightPainter(color));
        } catch (BadLocationException e) {
            throw new SyntaxException(e.getMessage());
        }
        SyntaxHighlight h = new HighlightInfo(start, end, color, key);
        highlights.add(h);
        return h;
    }
    /**
     * Get all highlights showed on SyntaxTextArea.
     * The return array doesn't contain highlight made by caret selection.
     * If you want to disable caret selection highlighting, you should use
     * {@link com.syntax.ui.AbstractSyntaxCaret#setSelectionEnable(boolean) setSelectionEnable(boolean)}
     * 
     * @return all highlights added by {@link #syntaxHighlight(int,int,Color) syntaxHighlight(int,int,Color)}
     */
    @Override
    public SyntaxHighlight[] getAllHighlights() {
        return highlights.toArray(new SyntaxHighlight[0]);
    }
    /**
     * Remove specified highlight from SyntaxTextArea
     * 
     * @param h specified highlight returned from {@link #syntaxHighlight(int,int,Color) syntaxHighlight(int,int,Color)}
     */
    @Override
    public void removeHighlight(SyntaxHighlight h) {
        highlights.remove(h);
        super.removeHighlight(h.getKey());
    }
    /**
     * Remove all highlights added by {@link #syntaxHighlight(int,int,Color) syntaxHighlight(int,int,Color)}
     * 
     * @param removeSelection true to remove selection highlighting, false not to remove selection highlighting
     */
    @Override
    public void removeAllHighlights(boolean removeSelection) {
        if(removeSelection)
            super.removeAllHighlights();
        else
            for(SyntaxHighlight h: highlights)
                removeHighlight(h.getKey());
        highlights.clear();
    }
    /**
     * Support modification of highlighting color
     */
    public static class SyntaxHighlightePainter implements HighlightPainter {
        private Color color;
        /**
         * Construct painter with specified highlighting color
         * 
         * @param color specified color
         */
        public SyntaxHighlightePainter(Color color) {
            this.color = color;
        }
        /**
         * Set the color of highlighting
         * 
         * @param color highlighting color
         */
        public void setColor(Color color) {
            this.color = color;
        }
        /**
         * Paints a highlight.
         *
         * @param g the graphics context
         * @param offs0 the starting model offset &gt;= 0
         * @param offs1 the ending model offset &gt;= offs1
         * @param bounds the bounding box for the highlight
         * @param c the editor
         */
        @Override
        public void paint(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c) {
            Rectangle alloc = bounds.getBounds();
            try {
                // --- determine locations ---
                TextUI mapper = c.getUI();
                Rectangle p0 = mapper.modelToView(c, offs0);
                Rectangle p1 = mapper.modelToView(c, offs1);

                g.setColor(color);
                if (p0.y == p1.y) {
                    // same line, render a rectangle
                    Rectangle r = p0.union(p1);
                    g.fillRect(r.x, r.y, r.width, r.height);
                } else {
                    // different lines
                    int p0ToMarginWidth = alloc.x + alloc.width - p0.x;
                    g.fillRect(p0.x, p0.y, p0ToMarginWidth, p0.height);
                    if ((p0.y + p0.height) != p1.y) {
                        g.fillRect(alloc.x, p0.y + p0.height, alloc.width,
                                   p1.y - (p0.y + p0.height));
                    }
                    g.fillRect(alloc.x, p1.y, (p1.x - alloc.x), p1.height);
                }
            } catch (BadLocationException e) {
                // can't render
            }
        }
    }
    /**
     * Implementation of SyntaxHighlight
     */
    public class HighlightInfo implements SyntaxHighlight {
        private int start;
        private int end;
        private Color color;
        private Object key;
        /**
         * Construct complete HighlightInfo
         * 
         * @param start start position of highlight
         * @param end end position of highlight
         * @param color color of this highlight
         * @param key represent distinct highlight
         */
        public HighlightInfo(int start, int end, Color color, Object key) {
            this.start = start;
            this.end = end;
            this.color = color;
            this.key = key;
        }
        /**
         * Get start position of this highlight
         * 
         * @return start position
         */
        @Override
        public int getStart() {
            return start;
        }
        /**
         * Get end position of this highlight
         * 
         * @return end position
         */
        @Override
        public int getEnd() {
            return end;
        }
        /**
         * Get color of this highlight
         * 
         * @return color of this highlight
         */
        @Override
        public Color getColor() {
            return color;
        }
        /**
         * Get key which is used to identify distinct highlight
         * 
         * @return key
         */
        @Override
        public Object getKey() {
            return key;
        }
    }
}