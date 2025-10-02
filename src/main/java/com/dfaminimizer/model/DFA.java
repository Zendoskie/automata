package com.dfaminimizer.model;

import java.util.*;

/**
 * Represents a Deterministic Finite Automaton
 */
public class DFA {
    private final Set<State> states;
    private final Set<Character> alphabet;
    private final Map<String, Map<Character, State>> transitionFunction;
    private State startState;
    private final Set<State> finalStates;
    
    public DFA() {
        this.states = new HashSet<>();
        this.alphabet = new HashSet<>();
        this.transitionFunction = new HashMap<>();
        this.finalStates = new HashSet<>();
    }
    
    public void addState(State state) {
        states.add(state);
        transitionFunction.put(state.getName(), new HashMap<>());
        if (state.isStart()) {
            startState = state;
        }
        if (state.isFinal()) {
            finalStates.add(state);
        }
    }
    
    public void addTransition(State from, State to, char symbol) {
        if (!states.contains(from) || !states.contains(to)) {
            throw new IllegalArgumentException("States must be added to DFA before adding transitions");
        }
        
        alphabet.add(symbol);
        transitionFunction.get(from.getName()).put(symbol, to);
    }
    
    public void addTransition(String fromName, String toName, char symbol) {
        State from = getStateByName(fromName);
        State to = getStateByName(toName);
        if (from == null || to == null) {
            throw new IllegalArgumentException("State not found: " + fromName + " or " + toName);
        }
        addTransition(from, to, symbol);
    }
    
    public State getStateByName(String name) {
        return states.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    
    public State getNextState(State currentState, char symbol) {
        Map<Character, State> transitions = transitionFunction.get(currentState.getName());
        return transitions.get(symbol);
    }
    
    public boolean accepts(String input) {
        State currentState = startState;
        for (char symbol : input.toCharArray()) {
            currentState = getNextState(currentState, symbol);
            if (currentState == null) {
                return false; // No transition defined for this symbol
            }
        }
        return finalStates.contains(currentState);
    }
    
    public Set<State> getStates() {
        return new HashSet<>(states);
    }
    
    public Set<Character> getAlphabet() {
        return new HashSet<>(alphabet);
    }
    
    public State getStartState() {
        return startState;
    }
    
    public Set<State> getFinalStates() {
        return new HashSet<>(finalStates);
    }
    
    public Set<Transition> getTransitions() {
        Set<Transition> transitions = new HashSet<>();
        for (State from : states) {
            Map<Character, State> stateTransitions = transitionFunction.get(from.getName());
            for (Map.Entry<Character, State> entry : stateTransitions.entrySet()) {
                transitions.add(new Transition(from, entry.getValue(), entry.getKey()));
            }
        }
        return transitions;
    }
    
    public void printDFA() {
        System.out.println("DFA States: " + states);
        System.out.println("Alphabet: " + alphabet);
        System.out.println("Start State: " + startState);
        System.out.println("Final States: " + finalStates);
        System.out.println("Transitions:");
        for (Transition t : getTransitions()) {
            System.out.println("  " + t);
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DFA{\n");
        sb.append("  States: ").append(states).append("\n");
        sb.append("  Alphabet: ").append(alphabet).append("\n");
        sb.append("  Start State: ").append(startState).append("\n");
        sb.append("  Final States: ").append(finalStates).append("\n");
        sb.append("  Transitions:\n");
        for (Transition t : getTransitions()) {
            sb.append("    ").append(t).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
