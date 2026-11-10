#!/usr/bin/env bash
# Stops the Neo4j that this repository installed in .neo4j. Run it before you
# delete the repository folder, so that no Neo4j process is left running.
set -euo pipefail
source "$(dirname "$0")/env.sh"

if [ ! -d "$COURSE_NEO4J_HOME" ]; then
  echo "Neo4j is not installed in this repository. Nothing to stop."
  exit 0
fi

"$COURSE_NEO4J_HOME/bin/neo4j" stop || true
echo "Neo4j is stopped. You can delete this folder now."
