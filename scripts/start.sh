#!/bin/bash

echo "ENV VARS-----------------------------------------------"
# Create a .env file to store the environment variables
env_file=".env"
> "$env_file" # Clear any existing .env file conten
# Fetch all parameters under the path "/kmail/"
parameters=$(aws ssm describe-parameters --query "Parameters[?starts_with(Name, '/kmail/')].Name" --output text)
# Loop through each parameter name and fetch the value
for param in $parameters; do
  # Fetch the parameter value
  value=$(aws ssm get-parameter --name "$param" --with-decryption --query "Parameter.Value" --output text)

  # Remove the leading path ("/kmail/") to get a valid environment variable name
  env_var_name=$(echo "$param" | sed 's|/kmail/||')

  # Export as an environment variable
  export "$env_var_name"="$value"
  echo "$env_var_name=$value" >> "$env_file"

  # Optionally print for debugging (remove for security reasons)
  echo "Set environment variable: $env_var_name"
done

echo "GET PASS AND LOGIN-----------------------------------------------"
aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com
echo "docker pull ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:latest"
docker pull ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:latest
docker run --env-file "$env_file" -d --name my-container -p 8080:8080 ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:latest