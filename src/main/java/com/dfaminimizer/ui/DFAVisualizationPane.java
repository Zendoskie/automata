package com.dfaminimizer.ui;

import com.dfaminimizer.model.DFA;
import com.dfaminimizer.model.State;
import com.dfaminimizer.model.Transition;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.HashMap;
import java.util.Map;

/**
 * Pane for visualizing DFA graphs
 */
public class DFAVisualizationPane extends ScrollPane {
    
    private Pane graphPane;
    private Map<State, Circle> stateCircles;
    private Map<State, Text> stateLabels;
    private Map<Transition, Line> transitionLines;
    private Map<Transition, Text> transitionLabels;
    
    public DFAVisualizationPane() {
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        graphPane = new Pane();
        stateCircles = new HashMap<>();
        stateLabels = new HashMap<>();
        transitionLines = new HashMap<>();
        transitionLabels = new HashMap<>();
        
        setContent(graphPane);
        setPrefSize(400, 300);
        setStyle("-fx-background-color: white;");
    }
    
    private void setupLayout() {
        // Set up scroll pane properties
        setFitToWidth(true);
        setFitToHeight(true);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }
    
    public void visualizeDFA(DFA dfa) {
        clearVisualization();
        
        if (dfa == null || dfa.getStates().isEmpty()) {
            return;
        }
        
        // Calculate positions for states in a circular layout
        Map<State, Point2D> statePositions = calculateStatePositions(dfa);
        
        // Draw states
        for (State state : dfa.getStates()) {
            Point2D position = statePositions.get(state);
            drawState(state, position);
        }
        
        // Draw transitions
        for (Transition transition : dfa.getTransitions()) {
            Point2D fromPos = statePositions.get(transition.getFrom());
            Point2D toPos = statePositions.get(transition.getTo());
            drawTransition(transition, fromPos, toPos);
        }
        
        // Draw start arrow
        if (dfa.getStartState() != null) {
            drawStartArrow(dfa.getStartState(), statePositions.get(dfa.getStartState()));
        }
    }
    
    private Map<State, Point2D> calculateStatePositions(DFA dfa) {
        Map<State, Point2D> positions = new HashMap<>();
        
        int stateCount = dfa.getStates().size();
        if (stateCount == 0) {
            return positions;
        }
        
        double centerX = 200;
        double centerY = 150;
        double radius = Math.min(120, 100);
        
        int index = 0;
        for (State state : dfa.getStates()) {
            double angle = 2 * Math.PI * index / stateCount;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            positions.put(state, new Point2D(x, y));
            index++;
        }
        
        return positions;
    }
    
    private void drawState(State state, Point2D position) {
        // Create state circle
        Circle circle = new Circle(position.getX(), position.getY(), 25);
        
        // Set circle style based on state type
        if (state.isFinal()) {
            // Final state - double circle
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(3);
            circle.setFill(Color.LIGHTGRAY);
            
            // Inner circle for final state
            Circle innerCircle = new Circle(position.getX(), position.getY(), 20);
            innerCircle.setStroke(Color.BLACK);
            innerCircle.setStrokeWidth(2);
            innerCircle.setFill(Color.TRANSPARENT);
            graphPane.getChildren().add(innerCircle);
        } else {
            // Regular state
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(2);
            circle.setFill(Color.WHITE);
        }
        
        // Create state label
        Text label = new Text(state.getName());
        label.setX(position.getX() - label.getBoundsInLocal().getWidth() / 2);
        label.setY(position.getY() + label.getBoundsInLocal().getHeight() / 4);
        label.setTextAlignment(TextAlignment.CENTER);
        
        graphPane.getChildren().addAll(circle, label);
        stateCircles.put(state, circle);
        stateLabels.put(state, label);
    }
    
    private void drawTransition(Transition transition, Point2D fromPos, Point2D toPos) {
        // Calculate arrow position
        double dx = toPos.getX() - fromPos.getX();
        double dy = toPos.getY() - fromPos.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // Normalize direction vector
        double unitX = dx / distance;
        double unitY = dy / distance;
        
        // Calculate start and end points (on circle edges)
        double startX = fromPos.getX() + unitX * 25;
        double startY = fromPos.getY() + unitY * 25;
        double endX = toPos.getX() - unitX * 25;
        double endY = toPos.getY() - unitY * 25;
        
        // Create transition line
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(2);
        
        // Create transition label
        Text label = new Text(String.valueOf(transition.getSymbol()));
        double labelX = (startX + endX) / 2 - label.getBoundsInLocal().getWidth() / 2;
        double labelY = (startY + endY) / 2 - 5;
        label.setX(labelX);
        label.setY(labelY);
        
        // Draw arrowhead
        drawArrowhead(endX, endY, unitX, unitY);
        
        graphPane.getChildren().addAll(line, label);
        transitionLines.put(transition, line);
        transitionLabels.put(transition, label);
    }
    
    private void drawArrowhead(double x, double y, double unitX, double unitY) {
        double arrowLength = 10;
        double arrowAngle = Math.PI / 6; // 30 degrees
        
        // Calculate arrowhead points
        double angle1 = Math.atan2(unitY, unitX) + arrowAngle;
        double angle2 = Math.atan2(unitY, unitX) - arrowAngle;
        
        double x1 = x - arrowLength * Math.cos(angle1);
        double y1 = y - arrowLength * Math.sin(angle1);
        double x2 = x - arrowLength * Math.cos(angle2);
        double y2 = y - arrowLength * Math.sin(angle2);
        
        // Draw arrowhead lines
        Line arrow1 = new Line(x, y, x1, y1);
        Line arrow2 = new Line(x, y, x2, y2);
        arrow1.setStroke(Color.BLACK);
        arrow1.setStrokeWidth(2);
        arrow2.setStroke(Color.BLACK);
        arrow2.setStrokeWidth(2);
        
        graphPane.getChildren().addAll(arrow1, arrow2);
    }
    
    private void drawStartArrow(State startState, Point2D position) {
        // Draw start arrow pointing to start state
        double arrowX = position.getX() - 40;
        double arrowY = position.getY();
        
        Line startArrow = new Line(arrowX, arrowY, position.getX() - 25, position.getY());
        startArrow.setStroke(Color.BLACK);
        startArrow.setStrokeWidth(3);
        
        // Draw arrowhead
        drawArrowhead(position.getX() - 25, position.getY(), 1, 0);
        
        graphPane.getChildren().add(startArrow);
    }
    
    private void clearVisualization() {
        graphPane.getChildren().clear();
        stateCircles.clear();
        stateLabels.clear();
        transitionLines.clear();
        transitionLabels.clear();
    }
}
