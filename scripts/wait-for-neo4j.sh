#!/usr/bin/env bash
# Waits until Neo4j accepts a query, or gives up after about two minutes.
set -euo pipefail
source "$(dirname "$0")/env.sh"

for _ in $(seq 1 60); do
  if "$COURSE_NEO4J_HOME/bin/cypher-shell" -a "neo4j://localhost:$COURSE_BOLT_PORT" \
      -u "$COURSE_NEO4J_USERNAME" -p "$COURSE_NEO4J_PASSWORD" "RETURN 1" > /dev/null 2>&1; then
    exit 0
  fi
  sleep 2
done

echo "Neo4j did not start. Check $COURSE_NEO4J_HOME/logs/neo4j.log"
exit 1
