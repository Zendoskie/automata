# DFA Minimization Tool - Web Version

A modern web-based application for visualizing and minimizing Deterministic Finite Automata (DFA) using the partition refinement algorithm.

## 🌐 Live Demo

Simply open `index.html` in your web browser, or use the provided server for the best experience.

## 🚀 Quick Start

### Option 1: Direct Browser Access
1. Open `index.html` in any modern web browser
2. The application will work immediately with all features

### Option 2: Local Server (Recommended)
1. **Windows**: Double-click `start-server.bat`
2. **Mac/Linux**: Run `./start-server.sh` or `python3 server.py`
3. Your browser will open automatically to `http://localhost:8000`

## ✨ Features

- **🎨 Modern UI**: Beautiful, responsive design with smooth animations
- **📊 Interactive Visualization**: SVG-based DFA graphs with hover effects
- **⚡ Real-time Processing**: Instant minimization with live console output
- **📱 Mobile Friendly**: Works on desktop, tablet, and mobile devices
- **🔍 Input Validation**: Comprehensive error checking and helpful messages
- **📋 Example DFA**: Built-in sample for quick testing
- **💾 No Installation**: Pure HTML/CSS/JavaScript - no dependencies required

## 🎯 How to Use

1. **Input DFA Data**:
   - **States**: Comma-separated list (e.g., `q0,q1,q2`)
   - **Alphabet**: Comma-separated list (e.g., `a,b`)
   - **Transitions**: One per line in format `fromState,symbol,toState`
   - **Start State**: Single state name
   - **Final States**: Comma-separated list

2. **Load Example**: Click "Load Example" to see a sample DFA

3. **Minimize**: Click "Minimize DFA" to run the algorithm

4. **View Results**: See original and minimized DFAs side by side

## 🔧 Technical Details

### Architecture
- **Frontend**: Pure HTML5, CSS3, JavaScript (ES6+)
- **Visualization**: SVG with custom rendering
- **Algorithm**: Partition refinement implementation
- **No Dependencies**: Works offline, no external libraries

### Browser Support
- Chrome 60+
- Firefox 55+
- Safari 12+
- Edge 79+

### File Structure
```
├── index.html              # Main HTML file
├── styles.css              # CSS styling
├── dfa-model.js           # DFA data structures
├── dfa-minimizer.js       # Minimization algorithm
├── dfa-visualizer.js      # SVG visualization
├── app.js                 # Main application logic
├── server.py              # Python HTTP server
├── start-server.bat       # Windows launcher
└── start-server.sh        # Unix launcher
```

## 🧮 Algorithm

The minimization uses the **partition refinement algorithm**:

1. **Initial Partition**: Separate final and non-final states
2. **Refinement**: For each group, check transition signatures
3. **Split Groups**: Split groups with different signatures
4. **Repeat**: Continue until no more splits possible
5. **Build Minimized DFA**: Create new DFA with merged states

## 📱 Mobile Usage

The application is fully responsive and works great on mobile devices:
- Touch-friendly interface
- Responsive layout that adapts to screen size
- Optimized for both portrait and landscape orientations

## 🎨 Customization

The application uses CSS custom properties for easy theming:
- Modify `styles.css` to change colors, fonts, and layout
- All visual elements are customizable
- Dark mode support can be easily added

## 🔍 Troubleshooting

### Common Issues

1. **"Cannot load DFA"**: Check that all states are defined and transitions reference valid states
2. **Visualization not showing**: Ensure your browser supports SVG
3. **Server won't start**: Make sure Python is installed and port 8000 is available

### Browser Console
Open browser developer tools (F12) to see detailed error messages and console output.

## 🌟 Example DFA

The built-in example demonstrates:
- **States**: q0, q1, q2
- **Alphabet**: a, b
- **Transitions**:
  - q0 --a--> q1
  - q0 --b--> q2
  - q1 --a--> q1
  - q1 --b--> q2
  - q2 --a--> q2
  - q2 --b--> q2
- **Start State**: q0
- **Final States**: q2

This DFA accepts strings ending with 'b' and can be minimized to 2 states.

## 📄 License

This project is open source and available under the MIT License.

---

**Enjoy exploring DFA minimization! 🎉**
