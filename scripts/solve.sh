#!/usr/bin/env bash
# Copies the solution for one or more challenges into src/main/java.
# Usage: ./scripts/solve.sh SentimentLabel Score
set -euo pipefail
cd "$(dirname "$0")/.."

if [ "$#" -eq 0 ]; then
  echo "Usage: ./scripts/solve.sh <ClassName> [<ClassName> ...]"
  echo "Available: $(ls solutions | sed 's/\.java$//' | tr '\n' ' ')"
  exit 1
fi

for name in "$@"; do
  if [ ! -f "solutions/${name}.java" ]; then
    echo "No solution named ${name}."
    exit 1
  fi
  cp "solutions/${name}.java" "src/main/java/sentiment/${name}.java"
  echo "Copied solutions/${name}.java"
done
