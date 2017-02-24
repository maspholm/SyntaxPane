package com.syntax.code;

/**
 * The command which can execute and reverse execution
 */
public interface ReversibleCommand extends BasicCommand {
    /**
     * Reverse the {@link BasicCommand#execute() execute}.
     * Make the state seeme like previous state which had not be influenced by
     * the execution
     */
    public void reverseExecution();
}