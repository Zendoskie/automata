/**
 * DFA Model Classes for Web Application
 */

class State {
    constructor(name, isStart = false, isFinal = false) {
        this.name = name;
        this.isStart = isStart;
        this.isFinal = isFinal;
    }

    equals(other) {
        return other instanceof State && this.name === other.name;
    }

    toString() {
        return this.name;
    }
}

class Transition {
    constructor(from, to, symbol) {
        this.from = from;
        this.to = to;
        this.symbol = symbol;
    }

    equals(other) {
        return other instanceof Transition &&
               this.from.equals(other.from) &&
               this.to.equals(other.to) &&
               this.symbol === other.symbol;
    }

    toString() {
        return `${this.from.name} --${this.symbol}--> ${this.to.name}`;
    }
}

class DFA {
    constructor() {
        this.states = new Set();
        this.alphabet = new Set();
        this.transitionFunction = new Map();
        this.startState = null;
        this.finalStates = new Set();
    }

    addState(state) {
        this.states.add(state);
        this.transitionFunction.set(state.name, new Map());
        
        if (state.isStart) {
            this.startState = state;
        }
        if (state.isFinal) {
            this.finalStates.add(state);
        }
    }

    addTransition(from, to, symbol) {
        if (!this.states.has(from) || !this.states.has(to)) {
            throw new Error('States must be added to DFA before adding transitions');
        }

        this.alphabet.add(symbol);
        this.transitionFunction.get(from.name).set(symbol, to);
    }

    addTransitionByName(fromName, toName, symbol) {
        const from = this.getStateByName(fromName);
        const to = this.getStateByName(toName);
        
        if (!from || !to) {
            throw new Error(`State not found: ${fromName} or ${toName}`);
        }
        
        this.addTransition(from, to, symbol);
    }

    getStateByName(name) {
        for (const state of this.states) {
            if (state.name === name) {
                return state;
            }
        }
        return null;
    }

    getNextState(currentState, symbol) {
        const transitions = this.transitionFunction.get(currentState.name);
        return transitions ? transitions.get(symbol) : null;
    }

    accepts(input) {
        let currentState = this.startState;
        
        for (const symbol of input) {
            currentState = this.getNextState(currentState, symbol);
            if (!currentState) {
                return false; // No transition defined for this symbol
            }
        }
        
        return this.finalStates.has(currentState);
    }

    getTransitions() {
        const transitions = [];
        for (const from of this.states) {
            const stateTransitions = this.transitionFunction.get(from.name);
            for (const [symbol, to] of stateTransitions) {
                transitions.push(new Transition(from, to, symbol));
            }
        }
        return transitions;
    }

    printDFA() {
        const output = [];
        output.push('DFA States: [' + Array.from(this.states).map(s => s.name).join(', ') + ']');
        output.push('Alphabet: [' + Array.from(this.alphabet).join(', ') + ']');
        output.push('Start State: ' + (this.startState ? this.startState.name : 'null'));
        output.push('Final States: [' + Array.from(this.finalStates).map(s => s.name).join(', ') + ']');
        output.push('Transitions:');
        
        for (const transition of this.getTransitions()) {
            output.push('  ' + transition.toString());
        }
        
        return output.join('\n');
    }

    toString() {
        return this.printDFA();
    }

    // Static method to create DFA from input data
    static fromInputData(data) {
        const dfa = new DFA();
        
        // Parse states
        const stateNames = data.states.split(',').map(s => s.trim()).filter(s => s);
        for (const stateName of stateNames) {
            dfa.addState(new State(stateName));
        }
        
        // Parse alphabet
        const alphabetChars = data.alphabet.split(',').map(s => s.trim()).filter(s => s);
        for (const char of alphabetChars) {
            if (char.length === 1) {
                dfa.alphabet.add(char);
            }
        }
        
        // Parse transitions
        const transitionLines = data.transitions.split('\n').map(s => s.trim()).filter(s => s);
        for (const line of transitionLines) {
            const parts = line.split(',');
            if (parts.length === 3) {
                const fromState = parts[0].trim();
                const symbol = parts[1].trim();
                const toState = parts[2].trim();
                
                if (symbol.length === 1) {
                    dfa.addTransitionByName(fromState, toState, symbol);
                }
            }
        }
        
        // Set start state
        const startStateName = data.startState.trim();
        if (startStateName) {
            const startState = dfa.getStateByName(startStateName);
            if (startState) {
                startState.isStart = true;
                dfa.startState = startState;
            }
        }
        
        // Set final states
        const finalStateNames = data.finalStates.split(',').map(s => s.trim()).filter(s => s);
        for (const stateName of finalStateNames) {
            const finalState = dfa.getStateByName(stateName);
            if (finalState) {
                finalState.isFinal = true;
                dfa.finalStates.add(finalState);
            }
        }
        
        return dfa;
    }
}
