#!/usr/bin/env bash
# Builds the shaded jar and deploys it. Run this after you change the code.
set -euo pipefail
cd "$(dirname "$0")/.."

./mvnw -B -q clean package -DskipTests
./scripts/deploy.sh
