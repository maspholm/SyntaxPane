package com.syntax.code;

public abstract class CommandModule {
    protected abstract void nextState(ReversibleCommand rCommand);
    public abstract boolean reverse();
    public abstract boolean forward();
}