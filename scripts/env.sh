#!/usr/bin/env bash
# Shared settings for the scripts. Source this file, do not run it.
#
# These scripts only ever use the Neo4j installed in this repository's
# .neo4j folder. They deliberately ignore NEO4J_HOME, NEO4J_URI and other
# NEO4J_* variables, so they cannot touch another Neo4j on your machine.

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

# cypher-shell and neo4j-admin read these as defaults, so clear them.
unset NEO4J_URI NEO4J_ADDRESS NEO4J_USERNAME NEO4J_PASSWORD NEO4J_DATABASE NEO4J_HOME

COURSE_NEO4J_VERSION="2026.10.2"
COURSE_NEO4J_HOME="$REPO_ROOT/.neo4j/neo4j-community-$COURSE_NEO4J_VERSION"
COURSE_NEO4J_USERNAME="neo4j"
COURSE_NEO4J_PASSWORD="password"

# Set COURSE_BOLT_PORT and COURSE_HTTP_PORT if another Neo4j already uses
# the default ports.
COURSE_BOLT_PORT="${COURSE_BOLT_PORT:-7687}"
COURSE_HTTP_PORT="${COURSE_HTTP_PORT:-7474}"

PLUGIN_NAMESPACE="sentiment.*"

# Starts Neo4j in its own session where possible, so that it keeps running
# after the terminal or the Codespace lifecycle command that started it exits.
start_neo4j() {
  if command -v setsid > /dev/null 2>&1; then
    setsid "$COURSE_NEO4J_HOME/bin/neo4j" start < /dev/null > /dev/null 2>&1 || true
  else
    "$COURSE_NEO4J_HOME/bin/neo4j" start > /dev/null 2>&1 || true
  fi
}
