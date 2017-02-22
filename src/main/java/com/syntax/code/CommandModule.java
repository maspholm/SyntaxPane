package com.syntax.code;

public abstract class CommandModule {
    protected abstract void nextState(Command rCommand);
    public abstract boolean reverse();
    public abstract boolean forward();
}