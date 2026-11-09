#!/usr/bin/env bash
# Copies the shaded jar to the plugins directory, allowlists the sentiment
# namespace and restarts Neo4j. Build first with: ./mvnw clean package -DskipTests
set -euo pipefail
source "$(dirname "$0")/env.sh"

cd "$REPO_ROOT"

jar="$(ls target/*.jar 2>/dev/null | grep -v '/original-' | head -n 1 || true)"
if [ -z "$jar" ]; then
  echo "No jar found in target/. Run: ./mvnw clean package -DskipTests"
  exit 1
fi

if [ ! -d "$COURSE_NEO4J_HOME" ]; then
  echo "Neo4j is not installed. Run: ./scripts/setup-neo4j.sh"
  exit 1
fi

"$COURSE_NEO4J_HOME/bin/neo4j" stop > /dev/null 2>&1 || true

mkdir -p "$COURSE_NEO4J_HOME/plugins"
rm -f "$COURSE_NEO4J_HOME"/plugins/sentiment-plugin-*.jar
cp "$jar" "$COURSE_NEO4J_HOME/plugins/"
echo "Copied $jar to plugins/"

conf="$COURSE_NEO4J_HOME/conf/neo4j.conf"
grep -v "^dbms.security.procedures.allowlist=" "$conf" > "$conf.tmp" || true
mv "$conf.tmp" "$conf"
echo "dbms.security.procedures.allowlist=$PLUGIN_NAMESPACE" >> "$conf"
echo "Allowlisted $PLUGIN_NAMESPACE"

"$COURSE_NEO4J_HOME/bin/neo4j" start
"$REPO_ROOT/scripts/wait-for-neo4j.sh"
echo "Neo4j restarted with the new plugin."
