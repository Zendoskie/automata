package com.dfaminimizer;

import com.dfaminimizer.model.DFA;
import com.dfaminimizer.model.State;
import com.dfaminimizer.algorithm.DFAMinimizer;

/**
 * Test class to verify DFA minimization functionality
 */
public class DFAMinimizerTest {
    
    public static void main(String[] args) {
        System.out.println("Testing DFA Minimization Tool");
        System.out.println("=============================");
        
        // Create a test DFA
        DFA dfa = createTestDFA();
        
        // Test minimization
        DFAMinimizer minimizer = new DFAMinimizer();
        DFA minimizedDFA = minimizer.minimize(dfa);
        
        System.out.println("\nTest completed successfully!");
    }
    
    private static DFA createTestDFA() {
        DFA dfa = new DFA();
        
        // Add states
        State q0 = new State("q0", true, false);  // start state
        State q1 = new State("q1", false, false);
        State q2 = new State("q2", false, true);  // final state
        State q3 = new State("q3", false, true);  // final state
        
        dfa.addState(q0);
        dfa.addState(q1);
        dfa.addState(q2);
        dfa.addState(q3);
        
        // Add transitions
        dfa.addTransition(q0, q1, 'a');
        dfa.addTransition(q0, q2, 'b');
        dfa.addTransition(q1, q1, 'a');
        dfa.addTransition(q1, q2, 'b');
        dfa.addTransition(q2, q2, 'a');
        dfa.addTransition(q2, q2, 'b');
        dfa.addTransition(q3, q3, 'a');
        dfa.addTransition(q3, q3, 'b');
        
        return dfa;
    }
}
