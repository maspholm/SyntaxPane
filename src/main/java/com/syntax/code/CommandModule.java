package com.syntax.code;

/**
 * This module describe the behavior of command-based state
 */
public abstract class CommandModule {
    /**
     * Add a command to module and apply {@link Command#execute() execute()}
     * to change the module
     * 
     * @param rCommand the command which apply to the module
     */
    protected abstract void nextState(Command rCommand);
    /**
     * Reverse the last command
     * 
     * @return true if state change successfully
     */
    public abstract boolean reverse();
    /**
     * If there is any command was reversed, apply it again to change the state 
     * 
     * @return true if state change successfully
     */
    public abstract boolean forward();
}