package com.syntax.ui;

import java.awt.Font;

import javax.swing.JPanel;
import com.syntax.code.TextBody;
/**
 * Implementing AbstractSyntaxHeader to customizer head view of {@link SyntaxEditPane SyntaxEditPane}
 */
public abstract class AbstractSyntaxHeader extends JPanel {
    private static final long serialVersionUID = 0;
    /**
     * onSyntaxInsert is called When text is inserted to {@link SyntaxEditPane SyntaxEditPane}
     * 
     * @param textBody {@link com.syntax.code.TextBody TextBody} on attached {@link SyntaxEditPane SyntaxEditPane}
     * @param line The number of lines on {@link SyntaxEditPane SyntaxEditPane} SyntaxEditPane
     */
    public abstract void onSyntaxInsert(TextBody textBody,int line);
    /**
     * onSyntaxRemove is called When text is removed to {@link SyntaxEditPane SyntaxEditPane}
     * 
     * @param textBody {@link com.syntax.code.TextBody TextBody} on attached {@link SyntaxEditPane SyntaxEditPane}
     * @param line the number of lines on {@link SyntaxEditPane SyntaxEditPane}
     */
    public abstract void onSyntaxRemove(TextBody textBody,int line);
    /**
     * Gives notification that an attribute or set of attributes changed on {@link SyntaxEditPane SyntaxEditPane}
     * 
     * @param textBody {@link com.syntax.code.TextBody TextBody} on attached {@link SyntaxEditPane SyntaxEditPane}
     * @param line the number of lines on {@link SyntaxEditPane SyntaxEditPane}
     */
    public abstract void onSyntaxChange(TextBody textBody,int line);
    /**
     * This method is called When font of {@link SyntaxEditPane SyntaxEditPane} is changed 
     * 
     * @param font the specified font
     */
    public abstract void onSyntaxFontChange(Font font);
}