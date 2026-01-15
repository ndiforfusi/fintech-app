#!/bin/bash

set -euo pipefail

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Update package index
echo "Updating package index..."
sudo apt-get update -y

# Install curl if not installed
if ! command_exists curl; then
    echo "Installing curl..."
    sudo apt-get install -y curl
fi

# Install Helm
if ! command_exists helm; then
    echo "Installing Helm..."
    curl -fsSL https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash
else
    echo "Helm already installed. Skipping."
fi

# Verify Helm installation
echo "Verifying Helm installation..."
helm version

# Create a namespace for Helm (optional)
echo "Creating a namespace for Helm charts (optional)..."
kubectl create namespace helm-charts --dry-run=client -o yaml | kubectl apply -f -

# Add the stable Helm repo
echo "Adding the stable Helm repository..."
if ! helm repo list | grep -q "^stable"; then
    helm repo add stable https://charts.helm.sh/stable
else
    echo "Stable repo already configured. Skipping."
fi

# Update Helm repositories
echo "Updating Helm repositories..."
helm repo update

echo "Helm installation completed!"
