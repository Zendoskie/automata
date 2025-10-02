package com.dfaminimizer.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Main JavaFX application for DFA Minimization Tool
 */
public class DFAMinimizerApp extends Application {
    
    private DFAInputPane inputPane;
    private DFAVisualizationPane originalVisualization;
    private DFAVisualizationPane minimizedVisualization;
    private Button minimizeButton;
    private TextArea consoleOutput;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("DFA Minimization Tool");
        
        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));
        
        // Create input section
        inputPane = new DFAInputPane();
        VBox inputSection = new VBox(10);
        inputSection.getChildren().addAll(
            new Label("DFA Input"),
            inputPane
        );
        
        // Create visualization section
        HBox visualizationSection = new HBox(20);
        visualizationSection.setAlignment(Pos.CENTER);
        
        VBox originalSection = new VBox(10);
        originalSection.getChildren().addAll(
            new Label("Original DFA"),
            originalVisualization = new DFAVisualizationPane()
        );
        
        VBox minimizedSection = new VBox(10);
        minimizedSection.getChildren().addAll(
            new Label("Minimized DFA"),
            minimizedVisualization = new DFAVisualizationPane()
        );
        
        visualizationSection.getChildren().addAll(originalSection, minimizedSection);
        
        // Create control section
        minimizeButton = new Button("Minimize DFA");
        minimizeButton.setOnAction(e -> minimizeDFA());
        minimizeButton.setPrefWidth(150);
        
        HBox controlSection = new HBox(10);
        controlSection.setAlignment(Pos.CENTER);
        controlSection.getChildren().add(minimizeButton);
        
        // Create console output
        consoleOutput = new TextArea();
        consoleOutput.setPrefRowCount(8);
        consoleOutput.setEditable(false);
        consoleOutput.setStyle("-fx-font-family: monospace;");
        
        VBox consoleSection = new VBox(5);
        consoleSection.getChildren().addAll(
            new Label("Console Output"),
            consoleOutput
        );
        
        // Layout the main components
        mainLayout.setTop(inputSection);
        mainLayout.setCenter(visualizationSection);
        mainLayout.setBottom(new VBox(10, controlSection, consoleSection));
        
        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Set up console output redirection
        setupConsoleOutput();
    }
    
    private void minimizeDFA() {
        try {
            // Get DFA from input pane
            var dfa = inputPane.getDFA();
            if (dfa == null) {
                showAlert("Error", "Please enter a valid DFA first.");
                return;
            }
            
            // Clear console
            consoleOutput.clear();
            
            // Visualize original DFA
            originalVisualization.visualizeDFA(dfa);
            
            // Minimize DFA
            com.dfaminimizer.algorithm.DFAMinimizer minimizer = new com.dfaminimizer.algorithm.DFAMinimizer();
            var minimizedDFA = minimizer.minimize(dfa);
            
            // Visualize minimized DFA
            minimizedVisualization.visualizeDFA(minimizedDFA);
            
            // Show success message
            showAlert("Success", "DFA has been minimized successfully!");
            
        } catch (Exception e) {
            showAlert("Error", "Error minimizing DFA: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setupConsoleOutput() {
        // Redirect System.out to console output
        System.setOut(new java.io.PrintStream(new java.io.OutputStream() {
            @Override
            public void write(int b) {
                consoleOutput.appendText(String.valueOf((char) b));
            }
        }));
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
