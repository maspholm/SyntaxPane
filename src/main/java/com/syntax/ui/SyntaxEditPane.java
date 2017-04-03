package com.syntax.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

import com.syntax.manage.AbstractCodeAdapter;
import com.syntax.manage.SyntaxCaretListener;
import com.syntax.manage.SyntaxManager;
import com.syntax.manage.SyntaxSelectionListener;
/**
 * Support editting text with shortcut keys and scrollbars. 
 * By default setting, edit pane will show line number of content in the left of text area.
 * Combine with {@link com.syntax.manage.JavaCodeAdapter JavaCodeAdapter} to support java code coloring.
 */
public class SyntaxEditPane extends JScrollPane {
    private static final long serialVersionUID = 0;
    private static final int DEFAULT_SCROLL_UNIT = 16;

    private SyntaxTextArea mSyntaxTextArea;
    private AbstractSyntaxHeader mAbstractSyntaxHeader;
    /**
     * Construct empty SyntaxEditPane with default setting
     */
    public SyntaxEditPane() {
        this(new SyntaxManager(), SyntaxManager.getDefaultCodeAdpater());
    }
    /**
     * Construct empty SyntaxEditPane with default code adapter and specified SyntaxManager
     * 
     * @param mSyntaxManager specified SyntaxManager which provide color, font and attribute set to
	 * build AbstractSyntaxCaret
     */
    public SyntaxEditPane(SyntaxManager mSyntaxManager) {
        this(mSyntaxManager, SyntaxManager.getDefaultCodeAdpater());
    }
    /**
     * Construct empty SyntaxEditPane with specified code adapter and specified SyntaxManager
     * 
     * @param mSyntaxManager specified SyntaxManager which provide color, font and attribute set to
	 * build AbstractSyntaxCaret
     * @param codeAdapter specified codeAdapter to handle syntax coloring
     */
    public SyntaxEditPane(SyntaxManager mSyntaxManager, AbstractCodeAdapter codeAdapter) {
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mSyntaxTextArea = new SyntaxTextArea(mSyntaxManager, codeAdapter);

        JPanel noWrappedPane = new JPanel(new BorderLayout());
        noWrappedPane.add(mSyntaxTextArea);
		setViewportView(noWrappedPane);
        
		setSyntaxHeader(new DefaultSyntaxHeader(mSyntaxManager));
        mSyntaxTextArea.getDocument().addDocumentListener(new SyntaxEditPaneHeader());
        setSyntaxFont(SyntaxManager.FONT);
        getVerticalScrollBar().setUI(new SyntaxVerticalScrollbarUI(mSyntaxManager));
        getHorizontalScrollBar().setUI(new SyntaxHorizontalScrollbarUI(mSyntaxManager));
        getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        setVerticallScrollSensivity(DEFAULT_SCROLL_UNIT);
        setHorizontalScrollSensivity(DEFAULT_SCROLL_UNIT);
        setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER, createEmptyPanel(mSyntaxManager));
        setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, createEmptyPanel(mSyntaxManager));
    }
    private JPanel createEmptyPanel(SyntaxManager mSyntaxManager) {
        JPanel z = new JPanel();
        z.setBackground(mSyntaxManager.getAreaColor());
        return z;
    }
    /**
     * Set header pane for this edit pane
     * 
     * @param mAbstractSyntaxHeader specified header
     */
    public void setSyntaxHeader(AbstractSyntaxHeader mAbstractSyntaxHeader) {
        this.mAbstractSyntaxHeader = mAbstractSyntaxHeader;
		setRowHeaderView(mAbstractSyntaxHeader);
    }
    /**
     * Set Font on {@link SyntaxEditPane SyntaxEditPane}.
     * This method automatically sen notification to {@link AbstractSyntaxHeader header}
     * by {@link AbstractSyntaxHeader#onSyntaxFontChange(Font) onSyntaxFontChange(Font)}
     * 
     * @param font font on edit pane
     */
    public void setSyntaxFont(Font font) {
        super.setFont(font);
        mSyntaxTextArea.setSyntaxFont(font);
        mAbstractSyntaxHeader.onSyntaxFontChange(font);
    }
    
    /**
     * Set the caret on edit pane
     * 
     * @param mSyntaxCaret specified {@link AbstractSyntaxCaret}.
     */
    public void setSyntaxCaret(AbstractSyntaxCaret mSyntaxCaret) {
        mSyntaxTextArea.setSyntaxCaret(mSyntaxCaret);
    }
    /**
     * Set the code adapter of edit pane. The adapter
     * manages the policy of syntax coloring and UI updating
     * 
     * @param codeAdapter the specified adapter
     */
    public void setCodeAdapter(AbstractCodeAdapter codeAdapter) {
        mSyntaxTextArea.setCodeAdapter(codeAdapter);
    }
    /**
     * Set unit of verticall scrolling. The sign of unit manage whether
     * it reverse scrolling or not. Positive sign is associate with
     * non-reverse srcolling. Negative sign is associate with reverse of srcolling
     * 
     * @param unit absolute value represents scal of scrolling and the sign
     * represents whether it reverse scrolling or not
     */
    public void setVerticallScrollSensivity(int unit) {
		getVerticalScrollBar().setUnitIncrement(unit);
    }
    /**
     * Set scale of horizontal scrolling. The sign of unit manage whether
     * it reverse scrolling or not. Positive sign is associate with
     * non-reverse srcolling. Negative sign is associate with reverse of srcolling
     * 
     * @param unit abslute value represents scal of scrolling and the sign
     * represents whether it reverse scrolling or not.
     */
    public void setHorizontalScrollSensivity(int unit) {
		getHorizontalScrollBar().setUnitIncrement(unit);
    }
    /**
     * Set SyntaxCaretListener for caret updating
     * 
     * @param l specified SyntaxCaretListener
     */
    public void setSyntaxCaretListener(SyntaxCaretListener l) {
        mSyntaxTextArea.setSyntaxCaretListener(l);
    }
    /**
     * Set SyntaxSelectionListener for selection updating
     * 
     * @param l specified SyntaxSelectionListener
     */
    public void setSyntaxSelectionListener(SyntaxSelectionListener l) {
        mSyntaxTextArea.setSyntaxSelectionListener(l);
    }
    private class SyntaxEditPaneHeader implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            if(mAbstractSyntaxHeader != null)
                mAbstractSyntaxHeader.onSyntaxInsert(mSyntaxTextArea.getStyledTextBody(), getNumberOfLines());
        }
        @Override
	    public void removeUpdate(DocumentEvent e) {
            if(mAbstractSyntaxHeader != null)
                mAbstractSyntaxHeader.onSyntaxRemove(mSyntaxTextArea.getStyledTextBody(), getNumberOfLines());
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            if(mAbstractSyntaxHeader != null)
                mAbstractSyntaxHeader.onSyntaxChange(mSyntaxTextArea.getStyledTextBody(), getNumberOfLines());
        }
        private int getNumberOfLines() {
            int textLength = mSyntaxTextArea.getDocument().getLength();
            Element root = mSyntaxTextArea.getDocument().getDefaultRootElement();
            if(textLength > 0)
                return root.getElementCount();
            else
                return 1;
        }
    }
}