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
public class SentimentLabelTest {

    private Neo4j neo4j;
    private Driver driver;

    @BeforeAll
    void start() {
        neo4j = Neo4jBuilders.newInProcessBuilder()
            .withDisabledServer()
            .withFunction(SentimentLabel.class)
            .build();
        driver = GraphDatabase.driver(neo4j.boltURI());
    }

    @AfterAll
    void stop() {
        driver.close();
        neo4j.close();
    }

    private String label(String argument) {
        try (Session session = driver.session()) {
            return session
                .run("RETURN sentiment.label(" + argument + ") AS label")
                .single()
                .get("label")
                .asString(null);
        }
    }

    @Test
    void positiveScoreIsLabelledPositive() {
        assertEquals("positive", label("0.62"));
        assertEquals("positive", label("0.05"));
    }

    @Test
    void negativeScoreIsLabelledNegative() {
        assertEquals("negative", label("-0.4"));
        assertEquals("negative", label("-0.05"));
    }

    @Test
    void scoreBetweenTheThresholdsIsNeutral() {
        assertEquals("neutral", label("0.01"));
        assertEquals("neutral", label("0.0"));
        assertEquals("neutral", label("-0.01"));
    }

    @Test
    void nullInputReturnsNull() {
        assertTrue(label("null") == null);
    }
}
