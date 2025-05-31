#!/bin/bash

set -e

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Ensure Helm is installed
if ! command_exists helm; then
    echo "❌ Helm is not installed. Please install Helm before running this script."
    exit 1
fi

# Ensure kubectl is installed
if ! command_exists kubectl; then
    echo "❌ kubectl is not installed. Please install kubectl before running this script."
    exit 1
fi

# Add Grafana Helm repository if not present
echo "📦 Adding Grafana Helm repository..."
if helm repo list | grep -q "^grafana"; then
    echo "✅ Grafana repository already exists, skipping."
else
    helm repo add grafana https://grafana.github.io/helm-charts
fi
helm repo update

# Create monitoring namespace if not present
echo "📁 Creating namespace 'monitoring' if not exists..."
kubectl create namespace monitoring --dry-run=client -o yaml | kubectl apply -f -

# Check if Grafana is already installed
if helm list -n monitoring | grep -q "^grafana"; then
    echo "✅ Grafana is already installed in the 'monitoring' namespace. Skipping installation."
else
    echo "🚀 Installing Grafana via Helm..."
    helm install grafana grafana/grafana \
      --namespace monitoring \
      --set adminPassword='admin123' \
      --set service.type=ClusterIP

    echo "⏳ Waiting for Grafana deployment to become ready..."
    kubectl rollout status deployment/grafana -n monitoring
fi

# Output service info
echo "📊 Grafana deployment complete!"
kubectl get svc -n monitoring -l app.kubernetes.io/name=grafana
