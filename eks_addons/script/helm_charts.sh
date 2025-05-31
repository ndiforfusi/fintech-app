#!/bin/bash

# Exit on any error
set -e

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Ensure Helm is installed
if ! command_exists helm; then
    echo "Helm is not installed. Please install Helm before running this script."
    exit 1
fi

# Ensure kubectl is installed
if ! command_exists kubectl; then
    echo "kubectl is not installed. Please install kubectl before running this script."
    exit 1
fi

##############################
# Add Grafana Helm Repository
##############################

echo "Adding Grafana Helm repository..."
if helm repo list | grep -q "^grafana"; then
    echo "Grafana repository already exists, skipping."
else
    helm repo add grafana https://grafana.github.io/helm-charts
fi
helm repo update

##############################
# Create Namespace 'monitoring' if it doesn't exist
##############################

echo "Creating namespace 'monitoring' if not exists..."
kubectl create namespace monitoring --dry-run=client -o yaml | kubectl apply -f -

##############################
# Install Grafana via Helm
##############################

echo "Installing Grafana via Helm..."
helm install grafana grafana/grafana \
  --namespace monitoring \
  --set adminPassword='admin123' \
  --set service.type=ClusterIP

echo "Waiting for Grafana pod to be ready..."
kubectl rollout status deployment/grafana -n monitoring

echo "Grafana installation completed!"
kubectl get svc -n monitoring -l app.kubernetes.io/name=grafana
