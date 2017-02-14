package com.syntax.manage;

import com.syntax.code.StyledTextBody;
import com.syntax.ui.SyntaxTextArea;

/**
 * Support basic operations in {@link SyntaxTextArea SyntaxTextArea}
 */
public class SyntaxDocumentTool {
    private SyntaxTextArea textArea;
    private SyntaxManager mSyntaxManager;
    /**
     * Construct tool and install tool in specified TextArea
     * 
     * @param specified textArea
     * @param mSyntaxManager provide default AttributeSet
     */
    public SyntaxDocumentTool(SyntaxTextArea textArea, SyntaxManager mSyntaxManager) {
        this.textArea = textArea;
        this.mSyntaxManager = mSyntaxManager;
    }
    /**
     * Find the begin index of line where position p at, suppose that p is an caret which
     * is located in the gap between character
     * TODO
     * @param p the imaginary caret position, 0 &le; p &le; {@link StyledTextBody#length() length()}
     * @return begin index of line
     * @throws SyntaxException if p out of rang
     */
    public int lineBegin(int p) throws SyntaxException {
        synchronized(textArea) {
            char txt[] = textArea.getStyledTextBody().getText().toCharArray();
            if(p > textArea.getStyledTextBody().getText().length() || p < 0)
                throw new SyntaxException("Index out of text area " + p);
            if(p == txt.length)
                p--;
            for(int index = txt[p]=='\n' ? p - 1: p ; index >= 0; index--)
                if(txt[index] == '\n')
                    return index + 1;
            return 0;
        }
    }
    /**
     * Find the end index of line where position p at, suppose that p is an caret which
     * is located in the gap between character
     * 
     * @param p the imaginary caret position, 0 &le; p &le; {@link StyledTextBody#length() length()}
     * @return end index of line
     * @throws SyntaxException if p out of rang
     */
    public int lineEnd(int p) throws SyntaxException {
        synchronized(textArea) {
            System.out.println("lineEnd");
            char txt[] = textArea.getStyledTextBody().getText().toCharArray();
            if(p > textArea.getStyledTextBody().getText().length() || p < 0)
                throw new SyntaxException("Index out of text area " + p);
            if(p == txt.length)
                p--;
            int textLength = txt.length;
            for(int index = txt[p]=='\n' ? p - 1: p; index < textLength; index++)
                if(txt[index] == '\n')
                    return index - 1;
            return textLength - 1;
        }
    }
    /**
     * Check whether two imaginary caret positions are in same line
     * 
     * @param p0 imaginary caret positions
     * @param p1 imaginary caret positions
     * @return true if two position in the same line
     * @throws SyntaxException if p0 or p1 out of rang
     */
    public boolean inSameLine(int p0, int p1) throws SyntaxException {
        return lineBegin(p0) == lineBegin(p1);
    }
    /**
     * Add a tab in front of line where position p at
     * 
     * @param p specified line
     * @throws SyntaxException if p out of rang
     */
    public void addFrontTab(int p) throws SyntaxException {
        synchronized(textArea) {
            int begin = lineBegin(p);
            StyledTextBody textBody = textArea.getStyledTextBody();
            textBody.insertStyledText(begin, "\t", mSyntaxManager.getAttributeSet());
        }
    }
    /**
     * Remove a tab in front of line where position p at
     * 
     * @param p sepcified line
     * @return false if no more tab
     * @throws SyntaxException if p out of rang
     */
    public boolean removeFrontTab(int p) throws SyntaxException {
        synchronized(textArea) {
            int begin = lineBegin(p);
            int tabs[] = countFrontTab(p);
            if(tabs[0] == 0)
                return false;
            else {
                char txt[] = textArea.getStyledTextBody().getText().toCharArray();
                StyledTextBody textBody = textArea.getStyledTextBody();
                if(tabs[1] != 0) {      // remove 1 tab
                    while(txt[begin] != '\t')
                        begin++;
                    textBody.removeStyledText(begin, 1);
                } else {                // remove 4 spaces
                    textBody.removeStyledText(begin, Math.min(4, tabs[2]));
                }
                return true;
            }
        }
    }
    /**
     * Count the number of prefix tabs at line where position p at
     * 
     * @param p specified line
     * @return 3 information in array, [number of tabs, number of preifx tab '\t', number of prefix space ' ']
     * @throws SyntaxException if p out of rang
     */
    public int[] countFrontTab(int p) throws SyntaxException {
        synchronized(textArea) {
            if(p >= textArea.getStyledTextBody().getText().length() || p < 0)
                throw new SyntaxException("Index out of text area " + p);
            char txt[] = textArea.getStyledTextBody().getText().toCharArray();
            int index = p;
            int tabs = 0;
            int spaces = 0;
            while(index >= 0 && txt[index] != '\n') {
                if(txt[index] == '\t' || txt[index] == ' ') {
                    if(txt[index] == '\t')
                        tabs++;
                    else
                        spaces++;
                } else {
                    tabs = 0;
                    spaces = 0;
                }
                index--;
            }
            int ret[] = {tabs + spaces/4 + ((spaces%4 != 0) ? 1 : 0), tabs, spaces};
            return ret;
        }
    }
}