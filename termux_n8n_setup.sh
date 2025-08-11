#!/data/data/com.termux/files/usr/bin/bash
set -euo pipefail

# Termux installation script for n8n (OSS) on Android
# Usage: bash termux_n8n_setup.sh

# Update packages
pkg update -y && pkg upgrade -y

# Install dependencies for building native Node modules
pkg install -y nodejs-lts python clang make pkg-config openssl libffi sqlite git

# Ensure python points to termux python
npm config set python python

# Optional: speed up npm
npm config set fund false
npm config set audit false

# Create n8n data directory
mkdir -p "$HOME/.config/n8n" "$HOME/n8n-data" "$HOME/bin"

# Install n8n globally (this may take a while)
npm install -g n8n

# Create start script
cat > "$HOME/bin/start-n8n" << 'EOF'
#!/data/data/com.termux/files/usr/bin/bash
set -euo pipefail

# Keep CPU awake while running
termux-wake-lock || true

export N8N_PORT=5678
export N8N_PROTOCOL=http
export N8N_HOST=127.0.0.1
export N8N_EDITOR_BASE_URL="http://127.0.0.1:5678/"
export N8N_DIAGNOSTICS_ENABLED=false
export GENERIC_TIMEZONE=UTC
export N8N_USER_FOLDER="$HOME/n8n-data"

# Uncomment to enable simple auth
# export N8N_BASIC_AUTH_ACTIVE=true
# export N8N_BASIC_AUTH_USER="admin"
# export N8N_BASIC_AUTH_PASSWORD="changeme"

# Start n8n
n8n start
EOF

chmod +x "$HOME/bin/start-n8n"

echo "\nDone. To start n8n: ~/bin/start-n8n\nThen open the Android app (WebView) to http://127.0.0.1:5678" 
