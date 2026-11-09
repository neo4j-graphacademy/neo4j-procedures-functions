#!/usr/bin/env bash
# Downloads Neo4j Community Edition into .neo4j/, configures it for this
# course and starts it. Safe to run more than once.
set -euo pipefail
source "$(dirname "$0")/env.sh"

if [ ! -d "$COURSE_NEO4J_HOME" ]; then
  mkdir -p "$REPO_ROOT/.neo4j"
  archive="$REPO_ROOT/.neo4j/neo4j-community-$COURSE_NEO4J_VERSION-unix.tar.gz"
  echo "Downloading Neo4j Community $COURSE_NEO4J_VERSION..."
  curl -fSL "https://dist.neo4j.org/neo4j-community-$COURSE_NEO4J_VERSION-unix.tar.gz" -o "$archive"
  tar -xzf "$archive" -C "$REPO_ROOT/.neo4j"
  rm "$archive"

  "$COURSE_NEO4J_HOME/bin/neo4j-admin" dbms set-initial-password "$COURSE_NEO4J_PASSWORD"
fi

conf="$COURSE_NEO4J_HOME/conf/neo4j.conf"

set_conf() {
  local key="$1" value="$2"
  grep -v "^${key}=" "$conf" > "$conf.tmp" || true
  mv "$conf.tmp" "$conf"
  echo "${key}=${value}" >> "$conf"
}

set_conf "server.bolt.listen_address" ":$COURSE_BOLT_PORT"
set_conf "server.http.listen_address" ":$COURSE_HTTP_PORT"

start_neo4j
"$REPO_ROOT/scripts/wait-for-neo4j.sh"
echo "Neo4j is running."
