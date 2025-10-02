/**
 * DFA Visualization using SVG
 */

class DFAVisualizer {
    constructor(containerId) {
        this.container = document.getElementById(containerId);
        this.svg = null;
        this.statePositions = new Map();
        this.stateRadius = 25;
        this.arrowSize = 8;
    }

    visualizeDFA(dfa) {
        this.clear();
        
        if (!dfa || dfa.states.size === 0) {
            this.showMessage('No DFA to visualize');
            return;
        }

        this.createSVG();
        this.calculateStatePositions(dfa);
        this.drawStates(dfa);
        this.drawTransitions(dfa);
        this.drawStartArrow(dfa);
    }

    clear() {
        this.container.innerHTML = '';
        this.statePositions.clear();
    }

    createSVG() {
        const rect = this.container.getBoundingClientRect();
        const width = Math.max(400, rect.width);
        const height = Math.max(350, rect.height);
        
        this.svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
        this.svg.setAttribute('width', width);
        this.svg.setAttribute('height', height);
        this.svg.setAttribute('viewBox', `0 0 ${width} ${height}`);
        this.svg.style.background = '#f8fafc';
        
        // Add arrow markers
        this.addArrowMarkers();
        
        this.container.appendChild(this.svg);
    }

    addArrowMarkers() {
        const defs = document.createElementNS('http://www.w3.org/2000/svg', 'defs');
        
        // Regular arrow
        const marker = document.createElementNS('http://www.w3.org/2000/svg', 'marker');
        marker.setAttribute('id', 'arrowhead');
        marker.setAttribute('markerWidth', '10');
        marker.setAttribute('markerHeight', '7');
        marker.setAttribute('refX', '9');
        marker.setAttribute('refY', '3.5');
        marker.setAttribute('orient', 'auto');
        
        const polygon = document.createElementNS('http://www.w3.org/2000/svg', 'polygon');
        polygon.setAttribute('points', '0 0, 10 3.5, 0 7');
        polygon.setAttribute('fill', '#4a5568');
        
        marker.appendChild(polygon);
        defs.appendChild(marker);
        
        // Start arrow
        const startMarker = document.createElementNS('http://www.w3.org/2000/svg', 'marker');
        startMarker.setAttribute('id', 'arrowhead-start');
        startMarker.setAttribute('markerWidth', '10');
        startMarker.setAttribute('markerHeight', '7');
        startMarker.setAttribute('refX', '9');
        startMarker.setAttribute('refY', '3.5');
        startMarker.setAttribute('orient', 'auto');
        
        const startPolygon = document.createElementNS('http://www.w3.org/2000/svg', 'polygon');
        startPolygon.setAttribute('points', '0 0, 10 3.5, 0 7');
        startPolygon.setAttribute('fill', '#38a169');
        
        startMarker.appendChild(startPolygon);
        defs.appendChild(startMarker);
        
        this.svg.appendChild(defs);
    }

    calculateStatePositions(dfa) {
        const states = Array.from(dfa.states);
        const centerX = 200;
        const centerY = 175;
        const radius = Math.min(120, 100);
        
        for (let i = 0; i < states.length; i++) {
            const angle = (2 * Math.PI * i) / states.length;
            const x = centerX + radius * Math.cos(angle);
            const y = centerY + radius * Math.sin(angle);
            this.statePositions.set(states[i], { x, y });
        }
    }

    drawStates(dfa) {
        for (const state of dfa.states) {
            const pos = this.statePositions.get(state);
            this.drawState(state, pos.x, pos.y);
        }
    }

    drawState(state, x, y) {
        // Create state circle
        const circle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
        circle.setAttribute('cx', x);
        circle.setAttribute('cy', y);
        circle.setAttribute('r', this.stateRadius);
        circle.setAttribute('class', 'state-circle');
        
        // Style based on state type
        if (state.isFinal) {
            circle.classList.add('final');
            // Add inner circle for final state
            const innerCircle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
            innerCircle.setAttribute('cx', x);
            innerCircle.setAttribute('cy', y);
            innerCircle.setAttribute('r', this.stateRadius - 5);
            innerCircle.setAttribute('class', 'state-circle');
            innerCircle.style.fill = 'none';
            innerCircle.style.stroke = '#e53e3e';
            innerCircle.style.strokeWidth = '2';
            this.svg.appendChild(innerCircle);
        }
        
        if (state.isStart) {
            circle.classList.add('start');
        }
        
        this.svg.appendChild(circle);
        
        // Create state label
        const label = document.createElementNS('http://www.w3.org/2000/svg', 'text');
        label.setAttribute('x', x);
        label.setAttribute('y', y);
        label.setAttribute('class', 'state-label');
        label.setAttribute('text-anchor', 'middle');
        label.setAttribute('dominant-baseline', 'central');
        label.textContent = state.name;
        
        this.svg.appendChild(label);
    }

    drawTransitions(dfa) {
        for (const transition of dfa.getTransitions()) {
            const fromPos = this.statePositions.get(transition.from);
            const toPos = this.statePositions.get(transition.to);
            this.drawTransition(transition, fromPos, toPos);
        }
    }

    drawTransition(transition, fromPos, toPos) {
        // Calculate arrow position
        const dx = toPos.x - fromPos.x;
        const dy = toPos.y - fromPos.y;
        const distance = Math.sqrt(dx * dx + dy * dy);
        
        // Normalize direction vector
        const unitX = dx / distance;
        const unitY = dy / distance;
        
        // Calculate start and end points (on circle edges)
        const startX = fromPos.x + unitX * this.stateRadius;
        const startY = fromPos.y + unitY * this.stateRadius;
        const endX = toPos.x - unitX * this.stateRadius;
        const endY = toPos.y - unitY * this.stateRadius;
        
        // Create transition line
        const line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
        line.setAttribute('x1', startX);
        line.setAttribute('y1', startY);
        line.setAttribute('x2', endX);
        line.setAttribute('y2', endY);
        line.setAttribute('class', 'transition-line');
        line.setAttribute('marker-end', 'url(#arrowhead)');
        
        this.svg.appendChild(line);
        
        // Create transition label
        const label = document.createElementNS('http://www.w3.org/2000/svg', 'text');
        const labelX = (startX + endX) / 2;
        const labelY = (startY + endY) / 2 - 5;
        label.setAttribute('x', labelX);
        label.setAttribute('y', labelY);
        label.setAttribute('class', 'transition-label');
        label.setAttribute('text-anchor', 'middle');
        label.setAttribute('dominant-baseline', 'central');
        label.textContent = transition.symbol;
        
        this.svg.appendChild(label);
    }

    drawStartArrow(dfa) {
        if (!dfa.startState) return;
        
        const pos = this.statePositions.get(dfa.startState);
        const arrowX = pos.x - 40;
        const arrowY = pos.y;
        
        const line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
        line.setAttribute('x1', arrowX);
        line.setAttribute('y1', arrowY);
        line.setAttribute('x2', pos.x - this.stateRadius);
        line.setAttribute('y2', pos.y);
        line.setAttribute('class', 'start-arrow');
        line.setAttribute('marker-end', 'url(#arrowhead-start)');
        
        this.svg.appendChild(line);
    }

    showMessage(message) {
        this.container.innerHTML = `<div style="display: flex; align-items: center; justify-content: center; height: 100%; color: #718096; font-style: italic;">${message}</div>`;
    }
}
