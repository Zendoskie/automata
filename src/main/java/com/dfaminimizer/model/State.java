package com.dfaminimizer.model;

import java.util.Objects;

/**
 * Represents a state in a DFA
 */
public class State {
    private final String name;
    private boolean isStart;
    private boolean isFinal;
    
    public State(String name) {
        this.name = name;
        this.isStart = false;
        this.isFinal = false;
    }
    
    public State(String name, boolean isStart, boolean isFinal) {
        this.name = name;
        this.isStart = isStart;
        this.isFinal = isFinal;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isStart() {
        return isStart;
    }
    
    public void setStart(boolean start) {
        isStart = start;
    }
    
    public boolean isFinal() {
        return isFinal;
    }
    
    public void setFinal(boolean fin) {
        isFinal = fin;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(name, state.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    @Override
    public String toString() {
        return name;
    }
}
