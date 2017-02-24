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
    private StyledChangeListener callback;
    private int nowState;
    private ArrayList<Command> commands;
    private boolean forcedMerge;
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
        forcedMerge = false;
    }
    /**
     * Make the following editions containing {@link #removeStyledText(int,int) removement}
     * and {@link #insertStyledText(int,String,AttributeSet) insertion} merge into an edition.
     * The difference between this edition with others is that this action can be reversed
     * by a CTRL + Z shortcut but the others can't. Use {@link #finishForcedMerge() finishForcedMerge()}
     * to make the following editions be normal or be a separator between two merged editions
     */
    public synchronized void startForcedMerge() {
        forcedMerge = true;
    }
    /**
     * Stop the forced merge mode. This can be a separator between two forced merged editions or just
     * change to mode.
     * 
     * @see #startForcedMerge()
     */
    public synchronized void finishForcedMerge() {
        forcedMerge = false;
        if(commands.size() != 0) {
            Command command = commands.get(commands.size() - 1);
            if(command instanceof CommandCollection)
                ((CommandCollection)command).setActive(false);
        }
    }
    /**
     * Insert text to StyledTextBody. The content of StyledTextBody is synchronized with SyntaxTextArea
     * 
     * @param start the index where the text was inserted. If the location
     * is the head of paragraph, offset will should be 0. If the location is in the end
     * of paragraph, offset should be the length of paragraph
     * @param text the specified string which is instered to paragraph
     * @param attributeSet the attribute of text
     * 
     * @see com.syntax.ui.SyntaxTextArea
     */
    public synchronized void insertStyledText(int start, String text, AttributeSet attributeSet) {
        StyledInsertCommand command = new StyledInsertCommand(start, text, attributeSet);
        if(text.length() > 1)
            command.finish();

        if(forcedMerge) {
            CommandCollection commandCollection = new CommandCollection(command);
            nextState(commandCollection);
        } else
            nextState(command);
    }
    /**
     * Remove text on SyntaxTextArea. The content of StyledTextBody is synchronized with SyntaxTextArea
     * 
     * @param start the index where the text was removed. If the first character
     * of text is removed from the head of paragraph, offset should be 0
     * @param length the length of string which is removed from paragraph
     * 
     * @see com.syntax.ui.SyntaxTextArea
     */
    public synchronized void removeStyledText(int start, int length) {
        StyledRemoveCommand command = new StyledRemoveCommand(start, getText().substring(start, start + length));
        if(length > 1)
            command.finish();
        if(forcedMerge) {
            CommandCollection commandCollection = new CommandCollection(command);
            nextState(commandCollection);
        } else
            nextState(command);
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
     * Implement {@link CommandModule#nextState(Command) nextState(Command)}.
     * Update state by a {@link Command Command}.
     * If the number of commands in this command module is not zero before this
     * command is added to, this command will try to {@link Command#combine(Command) combine}
     * the last command which have worked. If this command module have gone through some
     * {@link #reverse() reverse} operation, some commands in command module may be reversed
     * to un-worked state. Any command is added to command module can cause that commands in
     * un-worked state be deleted from command list
     * 
     * @param command the command which update this state
     */
    @Override
    protected synchronized void nextState(Command command) {
        for(int i = commands.size() - 1; i > nowState; i--)
            commands.remove(i);
        // Combine command
        if(commands.size() >= 1) {
            Command combinedCommand = command.combine(commands.get(commands.size() - 1));
            if(combinedCommand != null) {
                commands.remove(commands.size() - 1);
                commands.add(combinedCommand);
            } else {
                nowState++;
                commands.add(command);
            }
        } else {
            nowState++;
            commands.add(command);
        }
        // execute
        command.execute();
    }
    /**
     * Implement {@link CommandModule#reverse() reverse()}.
     * Move the current state to previous state by command in command list which have be added in module.
     * If there isn't previous command, the state will not be changed
     * 
     * @return false if there is not previous state
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
     * Implement {@link CommandModule#forward() forawrd()}.
     * Move the current state to next state on command in command list which have be added in module.
     * If there isn't next command, the state will not be changed
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
    private class StyledInsertCommand extends InsertCommand {
        private int start;
        private String changeStr;
        private AttributeSet attributeSet;
        public StyledInsertCommand(int start,String changeStr, AttributeSet attributeSet) {
            super(start, changeStr);
            this.start = start;
            this.changeStr = changeStr;
            this.attributeSet = attributeSet;
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
        @Override
        public Command combine(Command command) {
            if(isCombineable()) {
                if(!getChangeStr().equals("\n"))
                    if(command instanceof StyledInsertCommand) {
                        StyledInsertCommand sLastCommand = (StyledInsertCommand)command;
                        if(sLastCommand.isCombineable())
                            if(getStart() == sLastCommand.getStart() + sLastCommand.getChangeStr().length()) {
                                StyledInsertCommand newCommand = new StyledInsertCommand(
                                                                        sLastCommand.getStart(),
                                                                        sLastCommand.getChangeStr() + getChangeStr(),
                                                                        null);
                                return newCommand;
                            }
                    }
            }
            return null;
        }
    }
    private class StyledRemoveCommand extends RemoveCommand {
        private int start;
        private String changeStr;
        public StyledRemoveCommand(int start,String changeStr) {
            super(start, changeStr);
            this.start = start;
            this.changeStr = changeStr;
        }
        @Override
        public void execute() {
            super.execute();
            if(callback != null)
                callback.removeStyledText(start , changeStr);
        }
        @Override
        public void reverseExecution() {
            super.reverseExecution();
            if(callback != null)
                callback.changeStyledText(start, changeStr, null);
        }
        @Override
        public Command combine(Command command) {
            if(isCombineable())
                if(command instanceof StyledRemoveCommand) {
                    StyledRemoveCommand sLastCommand = (StyledRemoveCommand)command;
                    if(sLastCommand.isCombineable())
                        if(getStart() + getChangeStr().length() == sLastCommand.getStart()) {
                            StyledRemoveCommand newCommand = new StyledRemoveCommand(
                                                                    getStart(),
                                                                    getChangeStr() + sLastCommand.getChangeStr());
                            if(getChangeStr().equals("\n"))
                                newCommand.finish();
                            return newCommand;
                        }
                }
            return null;
        }
    }
    private class CommandCollection implements Command {
        private ArrayList<Command> commands;
        private boolean active;
        public CommandCollection() {
            commands = new ArrayList<>();
            active = true;
        }
        public CommandCollection(Command command) {
            commands = new ArrayList<>();
            commands.add(command);
            active = true;
        }
        public void add(Command command) {
            commands.add(command);
        }
        public boolean isActive() {
            return active;
        }
        public void setActive(boolean active) {
            this.active = active;
        }
        @Override
        public void execute() {
            for(Command command: commands)
                command.execute();
        }
        @Override
        public void reverseExecution() {
            for(int i = commands.size() - 1; i >= 0; i--)
                commands.get(i).reverseExecution();
        }
        public ArrayList<Command> getCommands() {
            return commands;
        }
        @Override
        public Command combine(Command command) {
            CommandCollection newCommand = new CommandCollection();
            if(command instanceof CommandCollection) {
                CommandCollection commandCollection = (CommandCollection)command;
                if(commandCollection.isActive()) {
                    for(Command cmd: commandCollection.getCommands())
                        newCommand.add(cmd);
                    for(Command cmd: getCommands())
                        newCommand.add(cmd);
                    return newCommand;
                } else
                    return null;
            } else
                return null;
        }
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
         * @param text the text which is inserted to the text body
         * @param attributeSet the attribute of text
         */
        public void changeStyledText(int start, String text, AttributeSet attributeSet);
        /**
         * Listener to request of changing attribute of text when removement happen on
         * text body
         * 
         * @param start the start position of specified text and point out the first
         * character of text
         * @param text the text which is inserted to the text body
         */
        public void removeStyledText(int start, String text);
    }
}
