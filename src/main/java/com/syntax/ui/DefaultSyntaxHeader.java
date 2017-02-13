package com.syntax.ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JTextPane;

import com.syntax.code.TextBody;
import com.syntax.manage.SyntaxManager;

/**
 * DefaultSyntaxHeader support showing line numbers and automatically change lines according to content
 */
public class DefaultSyntaxHeader extends AbstractSyntaxHeader {
    private static final long serialVersionUID = 0;
    private JTextPane numberPanel;
    private int preLine;
    /**
     * Construct DefaultSyntaxHeader with specified {@link com.syntax.manage.SyntaxManager SyntaxManager}
     * 
	 * @param mSyntaxManager specified SyntaxManager which provide color, font and attribute set to
	 * build AbstractSyntaxCaret
	 */
    public DefaultSyntaxHeader(SyntaxManager mSyntaxManager) {
        preLine = 1;
        numberPanel = new JTextPane();
		numberPanel.setEditable(false);
        numberPanel.setFont(SyntaxManager.FONT);
        numberPanel.setBackground(mSyntaxManager.getHeaderBackgroundColor());
        numberPanel.setForeground(mSyntaxManager.getHeaderTextColor());
        numberPanel.setText(getNumbersString(preLine));
        setLayout(new BorderLayout());
        add(numberPanel, BorderLayout.CENTER);
    }
    /**
     * onSyntaxInsert is called When text is inserted to {@link SyntaxEditPane SyntaxEditPane}
     * 
     * @param textBody {@link com.syntax.code.TextBody TextBody} on attached {@link SyntaxEditPane SyntaxEditPane}
     * @param line The number of lines on {@link SyntaxEditPane SyntaxEditPane} SyntaxEditPane
     */
    @Override
    public void onSyntaxInsert(TextBody textBody,int line) {
        updateLineNumbers(line);
    }
    /**
     * onSyntaxRemove is called When text is removed to {@link SyntaxEditPane SyntaxEditPane}
     * 
     * @param textBody {@link com.syntax.code.TextBody TextBody} on attached {@link SyntaxEditPane SyntaxEditPane}
     * @param line the number of lines on {@link SyntaxEditPane SyntaxEditPane}
     */
    @Override
    public void onSyntaxRemove(TextBody textBody,int line) {
        updateLineNumbers(line);
    }
    /**
     * Gives notification that an attribute or set of attributes changed on {@link SyntaxEditPane SyntaxEditPane}
     * 
     * @param textBody {@link com.syntax.code.TextBody TextBody} on attached {@link SyntaxEditPane SyntaxEditPane}
     * @param line the number of lines on {@link SyntaxEditPane SyntaxEditPane}
     */
    @Override
    public void onSyntaxChange(TextBody textBody,int line) {
        // Empty method
    }
    /**
     * This method is called When font of {@link SyntaxEditPane SyntaxEditPane} is changed 
     * 
     * @param font the specified font
     */
    @Override
    public void onSyntaxFontChange(Font font) {
        setFont(getFont().deriveFont(font.getSize()));
    }
    private void updateLineNumbers(int line) {
        if(line != preLine) {
            numberPanel.setText(getNumbersString(line));
            preLine = line;
        }
    }
    private String getNumbersString(int lineNumber) {
        StringBuilder lineNumbersTextBuilder = new StringBuilder();
		String format = "%" + Math.max(Integer.toString(lineNumber).length(), 2) + "d ";
		for (int i = 1; i <= lineNumber; i++)
			lineNumbersTextBuilder.append(String.format(format, i)).append(System.lineSeparator());
		return lineNumbersTextBuilder.toString();
	}
}