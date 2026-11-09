# Building custom Functions and Procedures

Companion repository for the GraphAcademy course
[Building custom Functions and Procedures](https://graphacademy.neo4j.com/courses/neo4j-procedures-functions/).

You will write a Neo4j plugin in Java: a user-defined function, a procedure and
an aggregation function. You will test each one with an in-process Neo4j
instance, package the plugin as a jar, deploy it and call it from Cypher.

The project structure follows the
[neo4j-procedure-template](https://github.com/neo4j-examples/neo4j-procedure-template)
(Apache License 2.0).

## What is in the repository

| Path | Purpose |
|---|---|
| `src/main/java/sentiment/` | Starter classes. You complete these in the challenges. |
| `src/test/java/sentiment/` | Tests that tell you when a challenge is complete. |
| `solutions/` | A finished version of each class. |
| `scripts/` | Commands to set up Neo4j, deploy the jar and run Cypher. |
| `data/headlines.cypher` | Four fictitious stocks and twenty headlines. |

## Requirements

- Java 21
- Neo4j Community Edition 2026.10.2, installed into `.neo4j/` by
  `./scripts/setup-neo4j.sh`

Open the repository in a GitHub Codespace and the setup runs for you.

## Commands

```bash
./scripts/check.sh                     # check Java, Maven and Neo4j
./mvnw test -Dtest=ScoreTest           # run the tests for one class
./mvnw clean package -DskipTests       # build the shaded jar
./scripts/deploy.sh                    # copy the jar, set the allowlist, restart Neo4j
./scripts/cypher.sh "RETURN 1"         # run a Cypher statement
./scripts/load-data.sh                 # reload the stocks and headlines
./scripts/solve.sh Score               # copy a finished solution into src/
```

The scripts only use the Neo4j in `.neo4j/`. They ignore `NEO4J_*` environment
variables, so they never touch another Neo4j on your machine. If port 7687 or
7474 is in use, set `COURSE_BOLT_PORT` and `COURSE_HTTP_PORT` before you run
`./scripts/setup-neo4j.sh`.

The ports are read each time a script runs, so set the same variables in every
terminal you use.

The local Neo4j user is `neo4j` and the password is `password`.
