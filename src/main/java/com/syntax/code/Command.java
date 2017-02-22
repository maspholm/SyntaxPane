package com.syntax.code;

public interface Command extends ReversibleCommand {
    public Command combine(Command command);
}