/**
 * Main Application Logic
 */

class DFAMinimizerApp {
    constructor() {
        this.originalVisualizer = new DFAVisualizer('originalDFA');
        this.minimizedVisualizer = new DFAVisualizer('minimizedDFA');
        this.console = document.getElementById('console');
        
        this.initializeEventListeners();
        this.loadExample();
    }

    initializeEventListeners() {
        document.getElementById('minimizeBtn').addEventListener('click', () => this.minimizeDFA());
        document.getElementById('loadExample').addEventListener('click', () => this.loadExample());
        document.getElementById('clearBtn').addEventListener('click', () => this.clearAll());
    }

    loadExample() {
        document.getElementById('states').value = 'q0,q1,q2';
        document.getElementById('alphabet').value = 'a,b';
        document.getElementById('transitions').value = 'q0,a,q1\nq0,b,q2\nq1,a,q1\nq1,b,q2\nq2,a,q2\nq2,b,q2';
        document.getElementById('startState').value = 'q0';
        document.getElementById('finalStates').value = 'q2';
        
        this.log('Example DFA loaded');
    }

    clearAll() {
        document.getElementById('states').value = '';
        document.getElementById('alphabet').value = '';
        document.getElementById('transitions').value = '';
        document.getElementById('startState').value = '';
        document.getElementById('finalStates').value = '';
        
        this.originalVisualizer.showMessage('No DFA to visualize');
        this.minimizedVisualizer.showMessage('No DFA to visualize');
        this.clearConsole();
    }

    minimizeDFA() {
        try {
            this.clearConsole();
            
            // Get input data
            const inputData = this.getInputData();
            
            // Validate input
            this.validateInput(inputData);
            
            // Create DFA from input
            const dfa = DFA.fromInputData(inputData);
            
            // Visualize original DFA
            this.originalVisualizer.visualizeDFA(dfa);
            this.log('Original DFA loaded and visualized');
            
            // Minimize DFA
            this.log('Starting minimization process...');
            const minimizer = new DFAMinimizer();
            const minimizedDFA = minimizer.minimize(dfa);
            
            // Visualize minimized DFA
            this.minimizedVisualizer.visualizeDFA(minimizedDFA);
            
            // Show console output
            this.log(minimizer.getConsoleOutput());
            
            this.showSuccess('DFA has been minimized successfully!');
            
        } catch (error) {
            this.showError('Error: ' + error.message);
            console.error('Minimization error:', error);
        }
    }

    getInputData() {
        return {
            states: document.getElementById('states').value.trim(),
            alphabet: document.getElementById('alphabet').value.trim(),
            transitions: document.getElementById('transitions').value.trim(),
            startState: document.getElementById('startState').value.trim(),
            finalStates: document.getElementById('finalStates').value.trim()
        };
    }

    validateInput(data) {
        if (!data.states) {
            throw new Error('States are required');
        }
        
        if (!data.alphabet) {
            throw new Error('Alphabet is required');
        }
        
        if (!data.transitions) {
            throw new Error('Transitions are required');
        }
        
        if (!data.startState) {
            throw new Error('Start state is required');
        }
        
        if (!data.finalStates) {
            throw new Error('Final states are required');
        }
        
        // Validate state names
        const stateNames = data.states.split(',').map(s => s.trim()).filter(s => s);
        if (stateNames.length === 0) {
            throw new Error('At least one state is required');
        }
        
        // Validate start state exists
        if (!stateNames.includes(data.startState)) {
            throw new Error('Start state must be one of the defined states');
        }
        
        // Validate final states exist
        const finalStateNames = data.finalStates.split(',').map(s => s.trim()).filter(s => s);
        for (const finalState of finalStateNames) {
            if (!stateNames.includes(finalState)) {
                throw new Error(`Final state '${finalState}' must be one of the defined states`);
            }
        }
        
        // Validate transitions
        const transitionLines = data.transitions.split('\n').map(s => s.trim()).filter(s => s);
        for (const line of transitionLines) {
            const parts = line.split(',');
            if (parts.length !== 3) {
                throw new Error(`Invalid transition format: '${line}'. Expected: fromState,symbol,toState`);
            }
            
            const [fromState, symbol, toState] = parts.map(s => s.trim());
            
            if (!stateNames.includes(fromState)) {
                throw new Error(`Transition '${line}' references undefined state '${fromState}'`);
            }
            
            if (!stateNames.includes(toState)) {
                throw new Error(`Transition '${line}' references undefined state '${toState}'`);
            }
            
            if (symbol.length !== 1) {
                throw new Error(`Transition '${line}' has invalid symbol '${symbol}'. Symbol must be a single character`);
            }
        }
    }

    log(message) {
        const timestamp = new Date().toLocaleTimeString();
        this.console.textContent += `[${timestamp}] ${message}\n`;
        this.console.scrollTop = this.console.scrollHeight;
    }

    clearConsole() {
        this.console.textContent = '';
    }

    showError(message) {
        this.log(`ERROR: ${message}`);
        this.showNotification(message, 'error');
    }

    showSuccess(message) {
        this.log(`SUCCESS: ${message}`);
        this.showNotification(message, 'success');
    }

    showNotification(message, type) {
        // Remove existing notifications
        const existing = document.querySelector('.notification');
        if (existing) {
            existing.remove();
        }
        
        const notification = document.createElement('div');
        notification.className = `notification ${type}`;
        notification.textContent = message;
        notification.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 12px 20px;
            border-radius: 8px;
            color: white;
            font-weight: 500;
            z-index: 1000;
            animation: slideIn 0.3s ease;
            max-width: 300px;
        `;
        
        if (type === 'error') {
            notification.style.background = '#e53e3e';
        } else if (type === 'success') {
            notification.style.background = '#38a169';
        }
        
        document.body.appendChild(notification);
        
        // Auto remove after 3 seconds
        setTimeout(() => {
            if (notification.parentNode) {
                notification.style.animation = 'slideOut 0.3s ease';
                setTimeout(() => notification.remove(), 300);
            }
        }, 3000);
    }
}

// Add CSS animations
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

// Initialize the application when the page loads
document.addEventListener('DOMContentLoaded', () => {
    new DFAMinimizerApp();
});
