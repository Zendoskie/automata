package com.dfaminimizer.algorithm;

import com.dfaminimizer.model.DFA;
import com.dfaminimizer.model.State;

import java.util.*;

/**
 * Implements DFA minimization using partition refinement algorithm
 */
public class DFAMinimizer {
    
    /**
     * Minimizes a DFA using partition refinement algorithm
     * @param dfa The DFA to minimize
     * @return The minimized DFA
     */
    public DFA minimize(DFA dfa) {
        System.out.println("Starting DFA minimization...");
        System.out.println("Original DFA:");
        dfa.printDFA();
        
        // Step 1: Initial partition - separate final and non-final states
        Set<Set<State>> partition = createInitialPartition(dfa);
        System.out.println("Initial partition: " + partition);
        
        // Step 2: Refine partition until no more changes
        boolean changed = true;
        int iteration = 0;
        
        while (changed) {
            iteration++;
            System.out.println("Iteration " + iteration + ":");
            
            Set<Set<State>> newPartition = new HashSet<>();
            changed = false;
            
            for (Set<State> group : partition) {
                if (group.size() == 1) {
                    // Single state groups remain unchanged
                    newPartition.add(new HashSet<>(group));
                    continue;
                }
                
                // Try to split the group
                Map<String, Set<State>> splitGroups = new HashMap<>();
                
                for (State state : group) {
                    String signature = getStateSignature(state, partition, dfa);
                    splitGroups.computeIfAbsent(signature, k -> new HashSet<>()).add(state);
                }
                
                if (splitGroups.size() > 1) {
                    // Group was split
                    changed = true;
                    newPartition.addAll(splitGroups.values());
                    System.out.println("  Split group " + group + " into " + splitGroups.values());
                } else {
                    // Group remains unchanged
                    newPartition.add(new HashSet<>(group));
                }
            }
            
            partition = newPartition;
            System.out.println("  New partition: " + partition);
        }
        
        System.out.println("Minimization completed after " + iteration + " iterations");
        System.out.println("Final partition: " + partition);
        
        // Step 3: Create minimized DFA
        return createMinimizedDFA(dfa, partition);
    }
    
    /**
     * Creates initial partition separating final and non-final states
     */
    private Set<Set<State>> createInitialPartition(DFA dfa) {
        Set<Set<State>> partition = new HashSet<>();
        
        Set<State> finalStates = new HashSet<>();
        Set<State> nonFinalStates = new HashSet<>();
        
        for (State state : dfa.getStates()) {
            if (state.isFinal()) {
                finalStates.add(state);
            } else {
                nonFinalStates.add(state);
            }
        }
        
        if (!finalStates.isEmpty()) {
            partition.add(finalStates);
        }
        if (!nonFinalStates.isEmpty()) {
            partition.add(nonFinalStates);
        }
        
        return partition;
    }
    
    /**
     * Gets the signature of a state for partition refinement
     * The signature represents which group each symbol leads to
     */
    private String getStateSignature(State state, Set<Set<State>> partition, DFA dfa) {
        StringBuilder signature = new StringBuilder();
        
        for (char symbol : dfa.getAlphabet()) {
            State nextState = dfa.getNextState(state, symbol);
            if (nextState == null) {
                signature.append("null,");
            } else {
                // Find which group the next state belongs to
                String groupName = findGroupName(nextState, partition);
                signature.append(groupName).append(",");
            }
        }
        
        return signature.toString();
    }
    
    /**
     * Finds the name of the group containing the given state
     */
    private String findGroupName(State state, Set<Set<State>> partition) {
        for (Set<State> group : partition) {
            if (group.contains(state)) {
                // Create a group name based on the states in the group
                return group.toString();
            }
        }
        return "unknown";
    }
    
    /**
     * Creates the minimized DFA from the final partition
     */
    private DFA createMinimizedDFA(DFA originalDFA, Set<Set<State>> finalPartition) {
        DFA minimizedDFA = new DFA();
        
        // Create mapping from original states to new state names
        Map<Set<State>, String> groupToName = new HashMap<>();
        Map<State, String> stateToGroupName = new HashMap<>();
        
        int groupIndex = 0;
        for (Set<State> group : finalPartition) {
            String groupName = "q" + groupIndex;
            groupToName.put(group, groupName);
            
            for (State state : group) {
                stateToGroupName.put(state, groupName);
            }
            groupIndex++;
        }
        
        // Create new states
        for (Set<State> group : finalPartition) {
            String groupName = groupToName.get(group);
            
            // Check if any state in the group is final
            boolean isFinal = group.stream().anyMatch(State::isFinal);
            
            // Check if any state in the group is start
            boolean isStart = group.stream().anyMatch(State::isStart);
            
            State newState = new State(groupName, isStart, isFinal);
            minimizedDFA.addState(newState);
        }
        
        // Create transitions
        for (Set<State> group : finalPartition) {
            String groupName = groupToName.get(group);
            State fromState = minimizedDFA.getStateByName(groupName);
            
            // Use any state from the group to determine transitions
            State representativeState = group.iterator().next();
            
            for (char symbol : originalDFA.getAlphabet()) {
                State nextState = originalDFA.getNextState(representativeState, symbol);
                if (nextState != null) {
                    String nextGroupName = stateToGroupName.get(nextState);
                    State toState = minimizedDFA.getStateByName(nextGroupName);
                    minimizedDFA.addTransition(fromState, toState, symbol);
                }
            }
        }
        
        System.out.println("Minimized DFA:");
        minimizedDFA.printDFA();
        
        return minimizedDFA;
    }
}
