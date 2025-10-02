package com.dfaminimizer.ui;

import com.dfaminimizer.model.DFA;
import com.dfaminimizer.model.State;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Pane for inputting DFA data
 */
public class DFAInputPane extends VBox {
    
    private TextField statesField;
    private TextField alphabetField;
    private TextArea transitionsArea;
    private TextField startStateField;
    private TextField finalStatesField;
    private Button loadExampleButton;
    
    public DFAInputPane() {
        initializeComponents();
        layoutComponents();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        statesField = new TextField();
        statesField.setPromptText("e.g., q0,q1,q2");
        
        alphabetField = new TextField();
        alphabetField.setPromptText("e.g., a,b");
        
        transitionsArea = new TextArea();
        transitionsArea.setPromptText("Format: fromState,symbol,toState (one per line)\ne.g.,\nq0,a,q1\nq0,b,q2\nq1,a,q1\nq1,b,q2\nq2,a,q2\nq2,b,q2");
        transitionsArea.setPrefRowCount(6);
        
        startStateField = new TextField();
        startStateField.setPromptText("e.g., q0");
        
        finalStatesField = new TextField();
        finalStatesField.setPromptText("e.g., q2");
        
        loadExampleButton = new Button("Load Example DFA");
    }
    
    private void layoutComponents() {
        setSpacing(10);
        setPadding(new Insets(10));
        
        // States input
        HBox statesBox = new HBox(10);
        statesBox.getChildren().addAll(
            new Label("States:"),
            statesField
        );
        statesBox.setAlignment(Pos.CENTER_LEFT);
        
        // Alphabet input
        HBox alphabetBox = new HBox(10);
        alphabetBox.getChildren().addAll(
            new Label("Alphabet:"),
            alphabetField
        );
        alphabetBox.setAlignment(Pos.CENTER_LEFT);
        
        // Transitions input
        VBox transitionsBox = new VBox(5);
        transitionsBox.getChildren().addAll(
            new Label("Transitions:"),
            transitionsArea
        );
        
        // Start state input
        HBox startStateBox = new HBox(10);
        startStateBox.getChildren().addAll(
            new Label("Start State:"),
            startStateField
        );
        startStateBox.setAlignment(Pos.CENTER_LEFT);
        
        // Final states input
        HBox finalStatesBox = new HBox(10);
        finalStatesBox.getChildren().addAll(
            new Label("Final States:"),
            finalStatesField
        );
        finalStatesBox.setAlignment(Pos.CENTER_LEFT);
        
        // Control buttons
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(loadExampleButton);
        buttonBox.setAlignment(Pos.CENTER);
        
        getChildren().addAll(
            statesBox,
            alphabetBox,
            transitionsBox,
            startStateBox,
            finalStatesBox,
            buttonBox
        );
    }
    
    private void setupEventHandlers() {
        loadExampleButton.setOnAction(e -> loadExampleDFA());
    }
    
    private void loadExampleDFA() {
        // Load a simple example DFA
        statesField.setText("q0,q1,q2");
        alphabetField.setText("a,b");
        transitionsArea.setText("q0,a,q1\nq0,b,q2\nq1,a,q1\nq1,b,q2\nq2,a,q2\nq2,b,q2");
        startStateField.setText("q0");
        finalStatesField.setText("q2");
    }
    
    public DFA getDFA() {
        try {
            DFA dfa = new DFA();
            
            // Parse states
            String[] stateNames = statesField.getText().trim().split(",");
            for (String stateName : stateNames) {
                stateName = stateName.trim();
                if (!stateName.isEmpty()) {
                    dfa.addState(new State(stateName));
                }
            }
            
            // Parse alphabet
            String[] alphabetChars = alphabetField.getText().trim().split(",");
            for (String charStr : alphabetChars) {
                charStr = charStr.trim();
                if (!charStr.isEmpty() && charStr.length() == 1) {
                    dfa.getAlphabet().add(charStr.charAt(0));
                }
            }
            
            // Parse transitions
            String[] transitionLines = transitionsArea.getText().trim().split("\n");
            for (String line : transitionLines) {
                line = line.trim();
                if (!line.isEmpty()) {
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
            }
            
            // Set start state
            String startStateName = startStateField.getText().trim();
            if (!startStateName.isEmpty()) {
                State startState = dfa.getStateByName(startStateName);
                if (startState != null) {
                    startState.setStart(true);
                }
            }
            
            // Set final states
            String[] finalStateNames = finalStatesField.getText().trim().split(",");
            for (String stateName : finalStateNames) {
                stateName = stateName.trim();
                if (!stateName.isEmpty()) {
                    State finalState = dfa.getStateByName(stateName);
                    if (finalState != null) {
                        finalState.setFinal(true);
                    }
                }
            }
            
            return dfa;
            
        } catch (Exception e) {
            throw new RuntimeException("Error parsing DFA: " + e.getMessage(), e);
        }
    }
}
