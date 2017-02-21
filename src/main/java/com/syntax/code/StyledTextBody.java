package com.syntax.code;

import java.util.ArrayList;

import javax.swing.text.AttributeSet;

/**
 * The content of StyledTextBody is synchronized with SyntaxTextArea.
 * Do not use {@link TextBody#insertText(int,String) insertText(int,string)}
 * or {@link TextBody#removeText(int,int) removeText(int,int)} to update text in SyntaxTextArea.
 * In order to avoid recursive function call, use
 * {@link #insertStyledText(int, String, AttributeSet) insertStyledText(int, String, AttributeSet)} and
 * {@link #removeStyledText(int, int) removeStyledText(int, int)} to modify text showing in the SyntaxTextArea
 * 
 * @see com.syntax.ui.SyntaxTextArea
 */
public class StyledTextBody extends TextBody {
    private static int cnt = 0;
    private StyledChangeListener callback;
    private int nowState;
    private ArrayList<ReversibleCommand> commands;
    /**
     * Construct empty StyledTextBody
     */
    public StyledTextBody() {
        this(null);
    }
    /**
     * Construct empty StyledTextBody and set {@link StyledChangeListener StyledChangeListener}
     * 
     * @param callback style change call back
     */
    public StyledTextBody(StyledChangeListener callback) {
        super();
        this.callback = callback;
        commands = new ArrayList<>();
        nowState = -1;
    }
    /**
     * Insert text to StyledTextBody. The content of StyledTextBody is synchronized with SyntaxTextArea
     * @param start the index where the text was inserted. If the location
     * is the head of paragraph, offset will should be 0. If the location is in the end
     * of paragraph, offset should be the length of paragraph
     * @param text the specified string which is instered to paragraph
     * @param attributeSet the attribute of text
     * 
     * @see com.syntax.ui.SyntaxTextArea
     */
    public synchronized void insertStyledText(int start, String text, AttributeSet attributeSet) {
        nextState(new StyledInsertCommand(start, text, attributeSet));
    }
    /**
     * Remove text on SyntaxTextArea. The content of StyledTextBody is synchronized with SyntaxTextArea
     * @param start the index where the text was removed. If the first character
     * of text is removed from the head of paragraph, offset should be 0
     * @param length the length of string which is removed from paragraph
     * 
     * @see com.syntax.ui.SyntaxTextArea
     */
    public synchronized void removeStyledText(int start, int length) {
        nextState(new StyledRemoveCommand(start, getText().substring(start, start + length)));
    }
    /**
     * Regist StyledChangeListener to StyledTextBody
     * 
     * @param callback specified callback
     * 
     * @see StyledChangeListener
     */
    public void setStyledChangeListener(StyledChangeListener callback) {
        this.callback = callback;
    }
    /**
     * Listen for style changing in StyledTextBody
     * 
     * @see StyledTextBody
     */
    public static interface StyledChangeListener {
        /**
         * Listeneing to text attribute changing in StyledTextBody
         * 
         * @param start the start position of specified text and point out the first
         * character of text
         * @param end the end position of specified text but it doesn't include the
         * last character of text
         * @param attributeSet the attribute of text
         */
        public void changeStyledText(int start, String text, AttributeSet attributeSet);
        public void removeStyledText(int start, String text);
    }
    /**
     * Update state with reversible command
     * 
     * @param rCommand the command which update this state
     */
    @Override
    protected synchronized void nextState(ReversibleCommand rCommand) {
        System.out.println("???" + cnt++);
        for(int i = commands.size() - 1; i > nowState; i--)
            commands.remove(i);
        nowState++;
        commands.add(rCommand);
        rCommand.execute();
    }
    /**
     * Reverse the last command
     * 
     * @return false if no command is reversed
     */
    @Override
    public boolean reverse() {
        if(nowState != -1) {
            nowState--;
            commands.get(nowState + 1).reverseExecution();
            return true;
        } else
            return false;
    }
    
    /**
     * To next state
     * 
     * @return false if there is not next state
     */
    @Override
    public boolean forward() {
        if(nowState + 1 < commands.size()) {
            nowState++;
            commands.get(nowState).execute();
            return true;
        } else
            return false;
    }
    protected class StyledInsertCommand extends InsertCommand {
        private int start;
        private String changeStr;
        private AttributeSet attributeSet;
        public StyledInsertCommand(int start,String changeStr, AttributeSet attributeSet) {
            super(start, changeStr);
            this.start = start;
            this.changeStr = changeStr;
            this.attributeSet = attributeSet;
            System.out.println("insert start " + start + " " + changeStr);
        }
        @Override
        public void execute() {
            super.execute();
            if(callback != null)
                callback.changeStyledText(start, changeStr, attributeSet);
        }
        @Override
        public void reverseExecution() {
            super.reverseExecution();
            if(callback != null)
                callback.removeStyledText(start, changeStr);
        }
    }
    protected class StyledRemoveCommand extends RemoveCommand {
        private int start;
        private String changeStr;
        public StyledRemoveCommand(int start,String changeStr) {
            super(start, changeStr);
            this.start = start;
            this.changeStr = changeStr;
            System.out.println("remove start " + start + " " + changeStr);
        }
        @Override
        public void execute() {
            super.execute();
            if(callback != null)
                callback.removeStyledText(start, changeStr);
        }
        @Override
        public void reverseExecution() {
            super.reverseExecution();
            System.out.println("here----");
            if(callback != null)
                callback.changeStyledText(start, changeStr, null);
        }
    }
}