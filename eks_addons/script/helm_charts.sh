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
# Add EKS Helm Repository if not already added
##############################

echo "Adding EKS repository for AWS Load Balancer Controller..."
if helm repo list | grep -q "^eks"; then
    echo "eks repository already exists, skipping."
else
    helm repo add eks https://aws.github.io/eks-charts
fi
helm repo update

##############################
# Set Required Variables - update these for your environment
##############################

CLUSTER_NAME="prod-eks-cluster"      # Replace with your EKS cluster name
VPC_ID="vpc-051e0c3ccd6fab596"       # Replace with your VPC ID
REGION="us-east-1"                   # Replace with your AWS region
SERVICE_ACCOUNT_NAME="aws-load-balancer-controller"
IMAGE_TAG="v2.11.0"

##############################
# Install AWS Load Balancer Controller using Helm
##############################

echo "Checking if AWS Load Balancer Controller release exists..."
if helm status aws-load-balancer-controller -n kube-system >/dev/null 2>&1; then
    echo "AWS Load Balancer Controller release already exists. Skipping installation."
else
    echo "Installing AWS Load Balancer Controller via Helm..."
    helm install aws-load-balancer-controller eks/aws-load-balancer-controller \
      --namespace kube-system \
      --set clusterName="$CLUSTER_NAME" \
      --set serviceAccount.create=false \
      --set enableValidatingWebhook=false \
      --set serviceAccount.name="$SERVICE_ACCOUNT_NAME" \
      --set vpcId="$VPC_ID" \
      --set region="$REGION" \
      --set image.tag="$IMAGE_TAG"
fi

##############################
# Verification
##############################

echo "Verifying AWS Load Balancer Controller installation..."
kubectl get pods -n kube-system -l "app.kubernetes.io/name=aws-load-balancer-controller"

echo "AWS Load Balancer Controller installation completed!"

##############################
# Install Grafana via Helm
##############################

echo "Adding Grafana Helm repository..."
if helm repo list | grep -q "^grafana"; then
    echo "Grafana repository already exists, skipping."
else
    helm repo add grafana https://grafana.github.io/helm-charts
fi
helm repo update

echo "Creating namespace 'monitoring' if not exists..."
kubectl create namespace monitoring --dry-run=client -o yaml | kubectl apply -f -

echo "Installing Grafana via Helm..."
helm install grafana grafana/grafana \
  --namespace monitoring \
  --set adminPassword='admin123' \
  --set service.type=LoadBalancer

echo "Waiting for Grafana pod to be ready..."
kubectl rollout status deployment/grafana -n monitoring

echo "Grafana installation completed!"
kubectl get svc -n monitoring -l app.kubernetes.io/name=grafana
