#!/usr/bin/env python3
"""
Simple HTTP server for the DFA Minimization Tool web application
"""

import http.server
import socketserver
import webbrowser
import os
import sys
from pathlib import Path

PORT = 8000

class CustomHTTPRequestHandler(http.server.SimpleHTTPRequestHandler):
    def end_headers(self):
        # Add CORS headers for local development
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type')
        super().end_headers()

def main():
    # Change to the directory containing the web files
    web_dir = Path(__file__).parent
    os.chdir(web_dir)
    
    # Check if index.html exists
    if not (web_dir / 'index.html').exists():
        print("Error: index.html not found in current directory")
        sys.exit(1)
    
    # Start the server
    with socketserver.TCPServer(("", PORT), CustomHTTPRequestHandler) as httpd:
        print(f"DFA Minimization Tool Web Server")
        print(f"Server running at: http://localhost:{PORT}")
        print(f"Serving files from: {web_dir}")
        print(f"Press Ctrl+C to stop the server")
        print()
        
        # Try to open the browser automatically
        try:
            webbrowser.open(f'http://localhost:{PORT}')
            print("Browser opened automatically")
        except:
            print("Could not open browser automatically. Please open http://localhost:8000 manually")
        
        print()
        
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\nServer stopped by user")
            sys.exit(0)

if __name__ == "__main__":
    main()
