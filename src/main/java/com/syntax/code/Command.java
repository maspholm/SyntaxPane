package com.syntax.code;

/**
 * A Fully functionall command. The main goal is supporting managing the inserstion and removment
 * of {@link StyledTextBody StyledTextBody}
 */
public interface Command extends ReversibleCommand {
    /**
     * Combine this command and previous command.
     * "Previous" means the order of adding command to command module
     * 
     * @param command previous command
     * @return null if two command can't combine together. Otherwise, return the result of combination
     */
    public Command combine(Command command);
}