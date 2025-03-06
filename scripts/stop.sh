#!/bin/bash

echo "Stopping old container..."
docker stop my-container || true
docker rm my-container || true
