package com.dfaminimizer.model;

import java.util.Objects;

/**
 * Represents a transition in a DFA
 */
public class Transition {
    private final State from;
    private final State to;
    private final char symbol;
    
    public Transition(State from, State to, char symbol) {
        this.from = from;
        this.to = to;
        this.symbol = symbol;
    }
    
    public State getFrom() {
        return from;
    }
    
    public State getTo() {
        return to;
    }
    
    public char getSymbol() {
        return symbol;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return symbol == that.symbol &&
               Objects.equals(from, that.from) &&
               Objects.equals(to, that.to);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(from, to, symbol);
    }
    
    @Override
    public String toString() {
        return from.getName() + " --" + symbol + "--> " + to.getName();
    }
}
