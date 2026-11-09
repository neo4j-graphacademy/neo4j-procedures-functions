#!/usr/bin/env bash
# Checks that Java, Maven and the local Neo4j are ready.
cd "$(dirname "$0")/.."
source scripts/env.sh

failed=0

printf "Java 21... "
if java -version 2>&1 | head -n 1 | grep -q '"21'; then
  echo "OK"
else
  echo "FAILED"
  echo "  Install Java 21 and make sure 'java -version' reports it."
  failed=1
fi

printf "Maven... "
if ./mvnw -v > /dev/null 2>&1; then
  echo "OK"
else
  echo "FAILED"
  echo "  Run ./mvnw -v to see the error."
  failed=1
fi

printf "Neo4j... "
neo4j_ok=0
if [ -x "$COURSE_NEO4J_HOME/bin/cypher-shell" ]; then
  # A Codespace that was just resumed may still be starting Neo4j, so wait
  # for up to a minute when the server process is running.
  for _ in $(seq 1 30); do
    if ./scripts/cypher.sh "RETURN 1" > /dev/null 2>&1; then
      neo4j_ok=1
      break
    fi
    if ! "$COURSE_NEO4J_HOME/bin/neo4j" status > /dev/null 2>&1; then
      break
    fi
    sleep 2
  done
fi
if [ "$neo4j_ok" -eq 1 ]; then
  echo "OK"
else
  echo "FAILED"
  echo "  Run ./scripts/setup-neo4j.sh to install and start Neo4j."
  failed=1
fi

if [ "$failed" -eq 0 ]; then
  echo "All checks passed!"
else
  exit 1
fi
