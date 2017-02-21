package com.syntax.code;

import java.util.ArrayList;

public class TextBody extends CommandModule {
    private StringBuilder text;
    private ArrayList<ReversibleCommand> commands;
    private int nowState;
    private TextChangeListener mTextChangeListener;
    /**
     * Construct empty {@link TextBody}.
     */
    public TextBody() {
        this("");
    }
    /**
     * Construct TextBody with initial content
     * 
     * @param initialStr initial content
     */
    public TextBody(String initialStr) {
        text = new StringBuilder(initialStr);
        commands = new ArrayList<>();
        nowState = -1;
    }
    /**
     * Set the text change listener
     * 
     * @param mTextChangeListener text change listener
     */
    public void setTextChangeListener(TextChangeListener mTextChangeListener) {
        this.mTextChangeListener = mTextChangeListener;
    }
    /**
     * Get entire paragraph
     * 
     * @return entire paragraph
     */
    public synchronized String getText() {
        return text.toString();
    }
    /**
     * Get the length of paragraph
     * 
     * @return length of paragraph
     */
    public synchronized int length() {
        return text.length();
    }
    /**
     * Insert text to specified position in TextBody
     * 
     * @param start the index where the text was inserted. If the location
     * is at the head of paragraph, offset will should be 0. If the location
     * is at the tail of paragraph, offset should be the length of paragraph
     * @param changeStr the specified string which is instered to paragraph
     */
    public synchronized void insertText(int start, String changeStr) {
        nextState(new InsertCommand(start, changeStr));
    }
    /**
     * Remove text from TextBody
     * 
     * @param start the index where the text was removed. If the first character
     * of text is removed at the head of paragraph, offset should be 0
     * @param length the length of string which is removed from TextBody
     */
    public synchronized void removeText(int start, int length) {
        nextState(new RemoveCommand(start, text.substring(start, start + length)));
    }
    /**
     * Same as getText()
     * 
     * @return entire paragraph
     */
    @Override
    public String toString() {
        return text.toString();
    }
    /**
     * Update state with reversible command
     * 
     * @param rCommand the command which update this state
     */
    @Override
    protected synchronized void nextState(ReversibleCommand rCommand) {
        for(int i = commands.size() - 1; i > nowState; i--)
            commands.remove(i);
        nowState++;
        rCommand.execute();
        commands.add(rCommand);
    }
    /**
     * Reverse the last command
     * 
     * @return false if no command is reversed
     */
    @Override
    public boolean reverse() {
        if(nowState != -1) {
            commands.get(nowState).reverseExecution();
            nowState--;
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
    protected class InsertCommand implements ReversibleCommand {
        private int start;
        private String changeStr;
        public InsertCommand(int start,String changeStr) {
            this.start = start;
            this.changeStr = new String(changeStr);
        }
        @Override
        public void execute() {
            text.insert(start, changeStr);
            if(mTextChangeListener != null)
                mTextChangeListener.insertChange(start, changeStr);
        }
        @Override
        public void reverseExecution() {
            text.delete(start, start + changeStr.length());
            if(mTextChangeListener != null)
                mTextChangeListener.removeChange(start, changeStr.length());
        }
    }
    protected class RemoveCommand implements ReversibleCommand {
        private int start;
        private String changeStr;
        public RemoveCommand(int start,String changeStr) {
            this.start = start;
            this.changeStr = new String(changeStr);
        }
        @Override
        public void execute() {
            text.delete(start, start + changeStr.length());
            if(mTextChangeListener != null)
                mTextChangeListener.removeChange(start, changeStr.length());
        }
        @Override
        public void reverseExecution() {
            text.insert(start, changeStr);
            if(mTextChangeListener != null)
                mTextChangeListener.insertChange(start, changeStr);
        }
    }
    /**
     * Listen to change on {@link TextBody TextBody}
     */
    public static interface TextChangeListener {
        /**
         * Listen to insert text update
         * 
         * @param start the index where the text was inserted. If the location
         * is the head of paragraph, offset will should be 0. If the location is in the end
         * of paragraph, offset should be the length of paragraph
         * @param changeStr the change of text
         */
        public void insertChange(int start, String changeStr);
        /**
         * Listen to remove text update
         * 
         * @param start the index where the text was inserted. If the location
         * is the head of paragraph, offset will should be 0. If the location is in the end
         * of paragraph, offset should be the length of paragraph
         * @param length the length of text which is removed
         */
        public void removeChange(int start, int length);
    }
}