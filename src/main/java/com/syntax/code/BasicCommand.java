package com.syntax.code;

/**
 * The command which can only execute
 */
public interface BasicCommand {
    /**
     * Execute this command in command module
     * 
     * @see CommandModule
     */
    public void execute();
}