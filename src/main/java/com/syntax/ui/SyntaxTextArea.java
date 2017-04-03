package com.syntax.ui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import com.syntax.code.StyledTextBody;
import com.syntax.code.StyledTextBody.StyledChangeListener;
import com.syntax.code.TextBody.TextChangeListener;
import com.syntax.manage.AbstractCodeAdapter;
import com.syntax.manage.AbstractKeyBoardShortCut;
import com.syntax.manage.SyntaxManager;
import com.syntax.manage.SyntaxCaretListener;
import com.syntax.manage.SyntaxDocumentTool;
import com.syntax.manage.SyntaxHighlighter;
import com.syntax.manage.SyntaxHighlightingTool;
import com.syntax.manage.SyntaxPainter;
import com.syntax.manage.SyntaxSelectionListener;
import com.syntax.manage.SyntaxHighlighter.BasicSelectionListener;
import com.syntax.manage.shortcut.CtrlY;
import com.syntax.manage.shortcut.CtrlZ;
import com.syntax.manage.shortcut.ShiftTab;
/**
 * This is an text area which supports syntax coloring, highlighting,
 * cursor painting and some other attribute setting
 */
public class SyntaxTextArea extends JTextPane {
    public static final long serialVersionUID = 0;
    public static final int DEFAULT_MAX_TAB_NUM = 200;

    private StyledTextBody mStyledTextBody;
    private AbstractCodeAdapter mCodeAdapter;
    private SyntaxStyledDocument mSyntaxStyledDocument;
    private AbstractSyntaxCaret mSyntaxCaret;
    private SyntaxCaretListener mSyntaxCaretListener;
    private SyntaxSelectionListener mSyntaxSelectionListener;
    private SyntaxHighlighter mSyntaxHighlighter;
    private SyntaxDocumentTool mSyntaxDocumentTool;
    private InnerSyntaxSelectionCaretListener mInnerSyntaxSelectionCaretListener;
    private int maxTabNum;

    /**
     * Construct text area with default syntax manager and code adapter
     */
    public SyntaxTextArea() {
        this(new SyntaxManager(), SyntaxManager.getDefaultCodeAdpater());
    }
    /**
     * Construct empty text with specified SyntaxManager which provide color,
     * font and attribute to build text area
     * 
     * @param mSyntaxManager the specified SyntaxManager which provide color, font and attribute to build text area
     */
    public SyntaxTextArea(SyntaxManager mSyntaxManager) {
        this(mSyntaxManager, SyntaxManager.getDefaultCodeAdpater());
    }
    /**
     * Construct empty text area with specified syntax manager and code adapter,
     * syntax manager provide color, font and attribute to build and code adapter handle syntax coloring.
     * 
     * @param mSyntaxManager the specified SyntaxManager which provide color, font and attribute to build text area
     * @param codeAdapter the specified AbstractCodeAdapter which handle syntax coloring and additional UI updating
     */
    public SyntaxTextArea(SyntaxManager mSyntaxManager,AbstractCodeAdapter codeAdapter) {
        super();
        mSyntaxStyledDocument   = new SyntaxStyledDocument();
        mStyledTextBody         = new StyledTextBody();
        mSyntaxHighlighter      = new SyntaxHighlighter(mSyntaxManager);
        mSyntaxDocumentTool     = new SyntaxDocumentTool(this, mSyntaxManager);
        mInnerSyntaxSelectionCaretListener = new InnerSyntaxSelectionCaretListener();
        maxTabNum               = DEFAULT_MAX_TAB_NUM;
        setStyledDocument(mSyntaxStyledDocument);
        setCodeAdapter(codeAdapter);
        setSyntaxCaret(new DefaultSyntaxCaret(mSyntaxManager));
        setBackground(mSyntaxManager.getAreaColor());
        setSyntaxFont(SyntaxManager.FONT);
        setSyntaxCaretListener(null);
        setSyntaxSelectionListener(null);
        mStyledTextBody.setStyledChangeListener(mSyntaxStyledDocument);
        mStyledTextBody.setTextChangeListener(mSyntaxStyledDocument);
        setHighlighter(mSyntaxHighlighter);
        addCaretListener(mInnerSyntaxSelectionCaretListener);
        mSyntaxHighlighter.setBasicSelectionListener(mInnerSyntaxSelectionCaretListener);

        addShortCut(new ShiftTab());
        addShortCut(new CtrlZ());
        addShortCut(new CtrlY());
    }
    /**
     * Get the {@link com.syntax.code.StyledTextBody StyledTextBody} of paragraph in text area
     * 
     * @return StyledTextBody of paragraph in text area
     */
    public StyledTextBody getStyledTextBody() {
        synchronized(mStyledTextBody) {
            return mStyledTextBody;
        }
    }
    /**
     * Get SyntaxPainter of this text area
     * 
     * @return SyntaxPainter of this text area
     */
    public SyntaxPainter getSyntaxPainter() {
        return mSyntaxStyledDocument.getSyntaxPainter();
    }
    /**
     * Get SyntaxHighlightingTool of this text area
     * 
     * @return SyntaxHighlightingTool of this text area
     */
    public SyntaxHighlightingTool getSyntaxHighlightingTool() {
        return mSyntaxHighlighter;
    }
    /**
     * Get SyntaxDocumentTool of this text area
     * 
     * @return SyntaxDocumentTool of this text area
     */
    public SyntaxDocumentTool getSyntaxDocumentTool() {
        return mSyntaxDocumentTool;
    }
    /**
     * Set selection highlightling visible. If set to not visible, the highlight of selection will not
     * be included in {@link com.syntax.manage.SyntaxHighlightingTool#getAllHighlights() getAllHighlights()}
     * 
     * @param enable ture to set selection highlight visible
     */
    public void setSelectionEnable(boolean enable) {
        mSyntaxCaret.setSelectionEnable(enable);
    }

    /**
     * Set the {@link AbstractSyntaxCaret AbstractSyntaxCaret}. When setFont() is called,
     * mSyntaxCaret.setCaretHeight() is automatically called
     * 
     * @param mSyntaxCaret specified {@link AbstractSyntaxCaret AbstractSyntaxCaret}
     */
    public void setSyntaxCaret(AbstractSyntaxCaret mSyntaxCaret) {
        setCaret(mSyntaxCaret);
        this.mSyntaxCaret = mSyntaxCaret;
    }
    /**
     * Set up the code adapter of text area. The code adapter
     * manages the policy of syntax coloring and UI updating of on text area
     * 
     * @param codeAdapter the specified code adapter
     */
    public void setCodeAdapter(AbstractCodeAdapter codeAdapter) {
        this.mCodeAdapter = codeAdapter;
    }
    /**
     * Set font of paragraph and notify caret that the font height is changed
     * 
     * @param font specified font
     */
    public void setSyntaxFont(Font font) {
        super.setFont(font);
        FontMetrics fontMetrics = getFontMetrics(getFont());
        mSyntaxCaret.setCaretHeight(fontMetrics.getHeight());
		int tabWidth = fontMetrics.charWidth('w') * 4;
		TabStop[] tabs = new TabStop[maxTabNum];
		for(int i = 0 ;i < tabs.length; i++)
			tabs[i] = new TabStop((1+i)*tabWidth);
		TabSet tabSet = new TabSet(tabs);
		AttributeSet att = SyntaxManager.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, tabSet);
		mSyntaxStyledDocument.setParagraphAttributes(0, mSyntaxStyledDocument.getLength(), att, false);
    }
    /**
     * Set SyntaxCaretListener for caret updating
     * 
     * @param l specified SyntaxCaretListener
     */
    public void setSyntaxCaretListener(SyntaxCaretListener l) {
        mSyntaxCaretListener = l;
    }
    /**
     * Set SyntaxSelectionListener for selection updating
     * 
     * @param l specified SyntaxSelectionListener
     */
    public void setSyntaxSelectionListener(SyntaxSelectionListener l) {
        mSyntaxSelectionListener = l;
    }
    /**
     * Add keyboard shortcut to text area. If two keyboard shortcut have same key stroke
     * the latter added shortcut will replace the former added shortcut
     * 
     * @param shortCut specified keyboard shortcut
     */
    public void addShortCut(AbstractKeyBoardShortCut shortCut) {
        shortCut.install(this);
        getInputMap().put(shortCut.getKeyStroke(), shortCut);
    }
    /**
     * Remove specified keyboard shortcut, use {@link AbstractKeyBoardShortCut#getKeyStroke() getKeyStroke()}
     * to identify distinct shortcut
     * 
     * @param shortCut specified keyboard shortcut
     */
    public void removeShortCut(AbstractKeyBoardShortCut shortCut) {
        shortCut.deinstall();
        getInputMap().remove(shortCut.getKeyStroke());
    }
    /**
     * Listen to styled document changing and ask registered code adapter to color the
     * changing part.
     */
    private class SyntaxStyledDocument extends DefaultStyledDocument implements StyledChangeListener, TextChangeListener {
        public static final long serialVersionUID = 0;
        public SyntaxPainter painter;

        public SyntaxStyledDocument() {
            painter = new SyntaxPainter(this);
        }
        @Override
        public void insertString (int offset, String text, AttributeSet attributeset) throws BadLocationException {
            synchronized(mStyledTextBody) {
                mStyledTextBody.insertStyledText(offset, text, null);
            }
        }
        private void askAdapterToDrawInsert(int offset, String text) {
            mCodeAdapter.insertString(offset, text, SyntaxTextArea.this);
        }
        @Override
        public void remove(int offset, int len) throws BadLocationException {
            synchronized(mStyledTextBody) {
                mStyledTextBody.removeStyledText(offset, len);
            }
        }
        private void askAdaptertoDrawRemove(int start, String text) {
            mCodeAdapter.remove(start, text, SyntaxTextArea.this);
        }
        @Override
        public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if(length == 0) {
                super.replace(offset, length, text, attrs);
                return;
            }
            if(mCodeAdapter.replace(offset, length, text, SyntaxTextArea.this))
                super.replace(offset, length, text, mCodeAdapter.getDefaultAttributeSet());
            
        }
        @Override
        public void changeStyledText(int start, String text, AttributeSet attributeSet) {
            if(attributeSet != null)
                super.setCharacterAttributes(start, text.length(), attributeSet, true);
            else {
                askAdapterToDrawInsert(start, text);
            }
        }
        @Override
        public void removeStyledText(int start, String text) {
            askAdaptertoDrawRemove(start, text);
        }
        @Override
        public void insertChange(int start, String changeStr) {
            try{
                super.insertString(start, changeStr, mCodeAdapter.getDefaultAttributeSet());
            } catch(BadLocationException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void removeChange(int start, int length) {
            try {
                super.remove(start, length);
            } catch(BadLocationException e) {
                e.printStackTrace();
            }
        }
        public SyntaxPainter getSyntaxPainter() {
            return painter;
        }
    }

    private class InnerSyntaxSelectionCaretListener implements CaretListener, BasicSelectionListener {
        @Override
        public void caretUpdate(CaretEvent event) {
            int dot = event.getDot();
            int mark = event.getMark();
            int start = Math.min(dot, mark);
            int end = Math.max(dot, mark);
            if(start == end && mSyntaxCaretListener != null) {
                //TODO
                mSyntaxCaretListener.caretUpdate(SyntaxTextArea.this, null, getCaretPosition(), mStyledTextBody, mSyntaxHighlighter);
                /*
                try {
                    Rectangle rec = SyntaxTextArea.this.modelToView(getCaretPosition());
                    mSyntaxCaretListener.caretUpdate(SyntaxTextArea.this, rec, getCaretPosition(), mStyledTextBody, mSyntaxHighlighter);
                } catch (BadLocationException e) {
                    // Can't model caret position to view.
                }
                */
            }
        }
        @Override
        public void selectionChange(int p0,int p1) {
            if(mSyntaxSelectionListener != null)
                mSyntaxSelectionListener.selectionChange(SyntaxTextArea.this, p0, p1, mStyledTextBody, mSyntaxHighlighter);
        }
        @Override
        public void selectionRemove() {
            if(mSyntaxSelectionListener != null)
                mSyntaxSelectionListener.selectionRemove(SyntaxTextArea.this, mStyledTextBody, mSyntaxHighlighter);
        }
    }
}