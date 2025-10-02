# DFA Minimization Tool

A JavaFX application for visualizing and minimizing Deterministic Finite Automata (DFA) using the partition refinement algorithm.

## Features

- **DFA Input**: Enter DFA states, alphabet, transitions, start state, and final states
- **Visualization**: Interactive graph visualization showing states as circles and transitions as arrows
- **Minimization**: Implements the partition refinement algorithm to minimize DFAs
- **Side-by-side Comparison**: View original and minimized DFA graphs simultaneously
- **Console Output**: Detailed logging of the minimization process

## Requirements

- Java 17 or higher
- JavaFX 17 or higher
- Maven 3.6 or higher

## Building and Running

### Using Maven

1. Clone or download the project
2. Navigate to the project directory
3. Build the project:
   ```bash
   mvn clean compile
   ```

4. Run the application:
   ```bash
   mvn javafx:run
   ```

### Using IDE

1. Import the project as a Maven project
2. Ensure JavaFX is properly configured in your IDE
3. Run the `DFAMinimizerApp` class

## Usage

1. **Input DFA**: Use the input form to define your DFA:
   - **States**: Comma-separated list (e.g., `q0,q1,q2`)
   - **Alphabet**: Comma-separated list (e.g., `a,b`)
   - **Transitions**: One per line in format `fromState,symbol,toState`
   - **Start State**: Single state name
   - **Final States**: Comma-separated list

2. **Load Example**: Click "Load Example DFA" to load a sample DFA

3. **Minimize**: Click "Minimize DFA" to run the minimization algorithm

4. **View Results**: The original and minimized DFAs will be displayed side by side

## Example DFA

The application includes a sample DFA with:
- States: q0, q1, q2
- Alphabet: a, b
- Transitions:
  - q0 --a--> q1
  - q0 --b--> q2
  - q1 --a--> q1
  - q1 --b--> q2
  - q2 --a--> q2
  - q2 --b--> q2
- Start State: q0
- Final States: q2

## Algorithm

The minimization uses the partition refinement algorithm:

1. **Initial Partition**: Separate final and non-final states
2. **Refinement**: For each group, check if states have different transition signatures
3. **Split Groups**: If states in a group lead to different groups for any symbol, split them
4. **Repeat**: Continue until no more splits are possible
5. **Build Minimized DFA**: Create new DFA with merged equivalent states

## Project Structure

```
src/main/java/com/dfaminimizer/
├── model/           # DFA data structures
│   ├── State.java
│   ├── Transition.java
│   └── DFA.java
├── algorithm/       # Minimization algorithm
│   └── DFAMinimizer.java
└── ui/             # JavaFX user interface
    ├── DFAMinimizerApp.java
    ├── DFAInputPane.java
    └── DFAVisualizationPane.java
```

## License

This project is open source and available under the MIT License.