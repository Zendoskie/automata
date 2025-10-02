/**
 * DFA Minimization Algorithm using Partition Refinement
 */

class DFAMinimizer {
    constructor() {
        this.consoleOutput = [];
    }

    minimize(dfa) {
        this.consoleOutput = [];
        this.log('Starting DFA minimization...');
        this.log('Original DFA:');
        this.log(dfa.printDFA());
        
        // Step 1: Initial partition - separate final and non-final states
        let partition = this.createInitialPartition(dfa);
        this.log('Initial partition: ' + this.partitionToString(partition));
        
        // Step 2: Refine partition until no more changes
        let changed = true;
        let iteration = 0;
        
        while (changed) {
            iteration++;
            this.log(`Iteration ${iteration}:`);
            
            const newPartition = new Set();
            changed = false;
            
            for (const group of partition) {
                if (group.size === 1) {
                    // Single state groups remain unchanged
                    newPartition.add(new Set(group));
                    continue;
                }
                
                // Try to split the group
                const splitGroups = new Map();
                
                for (const state of group) {
                    const signature = this.getStateSignature(state, partition, dfa);
                    if (!splitGroups.has(signature)) {
                        splitGroups.set(signature, new Set());
                    }
                    splitGroups.get(signature).add(state);
                }
                
                if (splitGroups.size > 1) {
                    // Group was split
                    changed = true;
                    for (const splitGroup of splitGroups.values()) {
                        newPartition.add(splitGroup);
                    }
                    this.log(`  Split group [${Array.from(group).map(s => s.name).join(', ')}] into ${splitGroups.size} groups`);
                } else {
                    // Group remains unchanged
                    newPartition.add(new Set(group));
                }
            }
            
            partition = newPartition;
            this.log('  New partition: ' + this.partitionToString(partition));
        }
        
        this.log(`Minimization completed after ${iteration} iterations`);
        this.log('Final partition: ' + this.partitionToString(partition));
        
        // Step 3: Create minimized DFA
        const minimizedDFA = this.createMinimizedDFA(dfa, partition);
        this.log('Minimized DFA:');
        this.log(minimizedDFA.printDFA());
        
        return minimizedDFA;
    }

    createInitialPartition(dfa) {
        const partition = new Set();
        
        const finalStates = new Set();
        const nonFinalStates = new Set();
        
        for (const state of dfa.states) {
            if (state.isFinal) {
                finalStates.add(state);
            } else {
                nonFinalStates.add(state);
            }
        }
        
        if (finalStates.size > 0) {
            partition.add(finalStates);
        }
        if (nonFinalStates.size > 0) {
            partition.add(nonFinalStates);
        }
        
        return partition;
    }

    getStateSignature(state, partition, dfa) {
        const signature = [];
        
        for (const symbol of dfa.alphabet) {
            const nextState = dfa.getNextState(state, symbol);
            if (!nextState) {
                signature.push('null');
            } else {
                // Find which group the next state belongs to
                const groupName = this.findGroupName(nextState, partition);
                signature.push(groupName);
            }
        }
        
        return signature.join(',');
    }

    findGroupName(state, partition) {
        for (const group of partition) {
            if (group.has(state)) {
                // Create a group name based on the states in the group
                return '[' + Array.from(group).map(s => s.name).join(',') + ']';
            }
        }
        return 'unknown';
    }

    createMinimizedDFA(originalDFA, finalPartition) {
        const minimizedDFA = new DFA();
        
        // Create mapping from original states to new state names
        const groupToName = new Map();
        const stateToGroupName = new Map();
        
        let groupIndex = 0;
        for (const group of finalPartition) {
            const groupName = 'q' + groupIndex;
            groupToName.set(group, groupName);
            
            for (const state of group) {
                stateToGroupName.set(state, groupName);
            }
            groupIndex++;
        }
        
        // Create new states
        for (const group of finalPartition) {
            const groupName = groupToName.get(group);
            
            // Check if any state in the group is final
            const isFinal = Array.from(group).some(state => state.isFinal);
            
            // Check if any state in the group is start
            const isStart = Array.from(group).some(state => state.isStart);
            
            const newState = new State(groupName, isStart, isFinal);
            minimizedDFA.addState(newState);
        }
        
        // Create transitions
        for (const group of finalPartition) {
            const groupName = groupToName.get(group);
            const fromState = minimizedDFA.getStateByName(groupName);
            
            // Use any state from the group to determine transitions
            const representativeState = Array.from(group)[0];
            
            for (const symbol of originalDFA.alphabet) {
                const nextState = originalDFA.getNextState(representativeState, symbol);
                if (nextState) {
                    const nextGroupName = stateToGroupName.get(nextState);
                    const toState = minimizedDFA.getStateByName(nextGroupName);
                    minimizedDFA.addTransition(fromState, toState, symbol);
                }
            }
        }
        
        return minimizedDFA;
    }

    partitionToString(partition) {
        const groups = Array.from(partition).map(group => 
            '[' + Array.from(group).map(s => s.name).join(',') + ']'
        );
        return '[' + groups.join(', ') + ']';
    }

    log(message) {
        this.consoleOutput.push(message);
        console.log(message);
    }

    getConsoleOutput() {
        return this.consoleOutput.join('\n');
    }
}
