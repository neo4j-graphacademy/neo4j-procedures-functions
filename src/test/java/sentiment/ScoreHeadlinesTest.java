package sentiment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScoreHeadlinesTest {

    private Neo4j neo4j;
    private Driver driver;

    @BeforeAll
    void start() {
        neo4j = Neo4jBuilders.newInProcessBuilder()
            .withDisabledServer()
            .withProcedure(ScoreHeadlines.class)
            .build();
        driver = GraphDatabase.driver(neo4j.boltURI());
    }

    @AfterAll
    void stop() {
        driver.close();
        neo4j.close();
    }

    @Test
    void scoresOnlyHeadlinesWithoutAScore() {
        try (Session session = driver.session()) {
            session.run("""
                CREATE (:Headline {headline: 'Record profits, great news'}),
                       (:Headline {headline: 'Terrible loss and awful guidance'}),
                       (:Headline {headline: 'Already scored', compound: 0.0})
                """).consume();

            long scored = session
                .run("CALL sentiment.scoreHeadlines() YIELD scored RETURN scored")
                .single()
                .get("scored")
                .asLong();
            assertEquals(2, scored);

            double good = session
                .run("""
                    MATCH (h:Headline {headline: 'Record profits, great news'})
                    RETURN h.compound AS compound
                    """)
                .single()
                .get("compound")
                .asDouble();
            assertTrue(good > 0.0);

            double bad = session
                .run("""
                    MATCH (h:Headline {headline: 'Terrible loss and awful guidance'})
                    RETURN h.compound AS compound
                    """)
                .single()
                .get("compound")
                .asDouble();
            assertTrue(bad < 0.0);

            long secondCall = session
                .run("CALL sentiment.scoreHeadlines() YIELD scored RETURN scored")
                .single()
                .get("scored")
                .asLong();
            assertEquals(0, secondCall);
        }
    }
}
