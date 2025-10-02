package com.dfaminimizer;

import com.dfaminimizer.model.DFA;
import com.dfaminimizer.model.State;
import com.dfaminimizer.algorithm.DFAMinimizer;

import java.util.Scanner;

/**
 * Console-based DFA Minimization Tool (no JavaFX required)
 */
public class ConsoleDFAMinimizer {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("DFA Minimization Tool - Console Version");
        System.out.println("=======================================");
        
        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Load example DFA");
            System.out.println("2. Enter custom DFA");
            System.out.println("3. Exit");
            System.out.print("Choose an option (1-3): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    loadAndMinimizeExample();
                    break;
                case 2:
                    loadAndMinimizeCustom(scanner);
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private static void loadAndMinimizeExample() {
        System.out.println("\nLoading example DFA...");
        DFA dfa = createExampleDFA();
        
        System.out.println("\nOriginal DFA:");
        dfa.printDFA();
        
        System.out.println("\nMinimizing DFA...");
        DFAMinimizer minimizer = new DFAMinimizer();
        DFA minimizedDFA = minimizer.minimize(dfa);
        
        System.out.println("\nMinimization completed!");
    }
    
    private static void loadAndMinimizeCustom(Scanner scanner) {
        try {
            System.out.println("\nEnter DFA details:");
            
            // Get states
            System.out.print("Enter states (comma-separated, e.g., q0,q1,q2): ");
            String statesInput = scanner.nextLine();
            String[] stateNames = statesInput.split(",");
            
            // Get alphabet
            System.out.print("Enter alphabet (comma-separated, e.g., a,b): ");
            String alphabetInput = scanner.nextLine();
            String[] alphabetChars = alphabetInput.split(",");
            
            // Get start state
            System.out.print("Enter start state: ");
            String startState = scanner.nextLine().trim();
            
            // Get final states
            System.out.print("Enter final states (comma-separated): ");
            String finalStatesInput = scanner.nextLine();
            String[] finalStateNames = finalStatesInput.split(",");
            
            // Get transitions
            System.out.println("Enter transitions (one per line, format: fromState,symbol,toState):");
            System.out.println("Enter 'done' when finished:");
            
            DFA dfa = new DFA();
            
            // Add states
            for (String stateName : stateNames) {
                stateName = stateName.trim();
                if (!stateName.isEmpty()) {
                    dfa.addState(new State(stateName));
                }
            }
            
            // Set start state
            State start = dfa.getStateByName(startState);
            if (start != null) {
                start.setStart(true);
            }
            
            // Set final states
            for (String finalStateName : finalStateNames) {
                finalStateName = finalStateName.trim();
                if (!finalStateName.isEmpty()) {
                    State finalState = dfa.getStateByName(finalStateName);
                    if (finalState != null) {
                        finalState.setFinal(true);
                    }
                }
            }
            
            // Add transitions
            String line;
            while (!(line = scanner.nextLine()).equals("done")) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String fromState = parts[0].trim();
                    String symbol = parts[1].trim();
                    String toState = parts[2].trim();
                    
                    if (symbol.length() == 1) {
                        dfa.addTransition(fromState, toState, symbol.charAt(0));
                    }
                }
            }
            
            System.out.println("\nOriginal DFA:");
            dfa.printDFA();
            
            System.out.println("\nMinimizing DFA...");
            DFAMinimizer minimizer = new DFAMinimizer();
            DFA minimizedDFA = minimizer.minimize(dfa);
            
            System.out.println("\nMinimization completed!");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static DFA createExampleDFA() {
        DFA dfa = new DFA();
        
        // Add states
        State q0 = new State("q0", true, false);  // start state
        State q1 = new State("q1", false, false);
        State q2 = new State("q2", false, true);  // final state
        
        dfa.addState(q0);
        dfa.addState(q1);
        dfa.addState(q2);
        
        // Add transitions
        dfa.addTransition(q0, q1, 'a');
        dfa.addTransition(q0, q2, 'b');
        dfa.addTransition(q1, q1, 'a');
        dfa.addTransition(q1, q2, 'b');
        dfa.addTransition(q2, q2, 'a');
        dfa.addTransition(q2, q2, 'b');
        
        return dfa;
    }
}
