package com.syntax.manage;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.syntax.ui.SyntaxTextArea;

public abstract class AbstractKeyBoardShortCut extends AbstractAction {
    private static final long serialVersionUID = 0;
    private KeyStroke keyStroke;
    private SyntaxTextArea textArea;
    /**
     * Construct short cut with specified key stroke. The
     * {@link #ShortCutPerformed(SyntaxTextArea) ShortCutPerformed()} is called only
     * when input key matchs keyStroke
     * 
     * @param keyStroke short cut key stroke associate with this action
     */
    public AbstractKeyBoardShortCut(KeyStroke keyStroke) {
        this.keyStroke = keyStroke;
    }
    /**
     * Get key stroke associate with this action
     * 
     * @return associate key stroke
     */
    public final KeyStroke getKeyStroke() {
        return keyStroke;
    }
    /**
     * Install this action into {@link com.syntax.ui.SyntaxTextArea SyntaxTextArea}.
     * This method is automatically called when add this action to text area
     * 
     * @param textArea installed destination
     */
    public final void install(SyntaxTextArea textArea) {
        this.textArea = textArea;
    }
    /**
     * Implement abstract method to convert key action into appropriate
     * short cut action
     * 
     * @param ActionEvent given by system
     */
    @Override
    public final void actionPerformed(ActionEvent e) {
        ShortCutPerformed(textArea);
    }
    /**
     * When associate key stroke is satisfied, this method is called to
     * ask for some appropriate react
     * 
     * @param textArea the textArea this short cut listen to
     */
    public abstract void ShortCutPerformed(SyntaxTextArea textArea);
}