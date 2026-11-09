#!/usr/bin/env bash
# Clears and reloads the fictitious Stock and Headline nodes.
set -euo pipefail
cd "$(dirname "$0")/.."

./scripts/cypher.sh -f data/headlines.cypher
./scripts/cypher.sh "MATCH (s:Stock) WITH count(s) AS stocks MATCH (h:Headline) RETURN stocks, count(h) AS headlines"
