#!/usr/bin/env bash
# Runs one Cypher statement, or a file with -f, against the local Neo4j.
# Usage: ./scripts/cypher.sh "RETURN 1"
#        ./scripts/cypher.sh -f data/headlines.cypher
set -euo pipefail
source "$(dirname "$0")/env.sh"

exec "$COURSE_NEO4J_HOME/bin/cypher-shell" \
  -a "neo4j://localhost:$COURSE_BOLT_PORT" \
  -u "$COURSE_NEO4J_USERNAME" -p "$COURSE_NEO4J_PASSWORD" \
  --format plain "$@"
